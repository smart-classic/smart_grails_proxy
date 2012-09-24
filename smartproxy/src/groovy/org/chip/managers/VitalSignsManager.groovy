package org.chip.managers


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import org.chip.mo.MilleniumObjectCall;
import org.chip.mo.ResultsCall;
import org.chip.mo.exceptions.MOCallException;
import org.chip.mo.mappers.SmartMapper;
import org.chip.mo.model.Event;
import org.chip.rdf.Vitals;
import org.chip.rdf.vitals.BloodPressure;
import org.chip.rdf.vitals.Code;
import org.chip.rdf.vitals.CodedValue;
import org.chip.rdf.vitals.Encounter;
import org.chip.rdf.vitals.VitalSign;
import org.chip.rdf.vitals.VitalSigns;
import org.codehaus.groovy.grails.commons.ConfigurationHolder;

/**
* VitalSignsManager.groovy
* Purpose:Processes the parsed MO response containing encounter and results information. 
* @author mkapoor
* @version Jun 19, 2012 12:53:03 PM
*/
class VitalSignsManager {
	
	private static final Log log = LogFactory.getLog(this)
	
	Map<String, List> eventLists = new HashMap()
	Map<String, Encounter> encountersById
	
	Set<VitalSigns> vitalSignsSet = new HashSet()
	
	/**
	 * eventCodesMap
	 */
	static final Map ecm
		
	static final Set bpEventCodesSet

	
	static{
		def config = ConfigurationHolder.config
		
		ecm = config.cerner.mo.eventCode
		
		bpEventCodesSet = readBPEventCodes(config.cerner.mo.bpEvents.keySet())
	}
	
	static def readBPEventCodes(bpEvents){
		def hashSet = new HashSet()
		bpEvents.each {bpEvent->
			hashSet.add(ecm.get(bpEvent))
		}
		return hashSet
	}
	
	/**
	 * Records the incoming encountersById map containing all Encounters mapped to their id in.
	 * This will be used by the manager to assign an encounter to each VitalSigns object it creates
	 * @param encountersById
	 */
	def recordEncounters(Map<String,Encounter> encountersById){
		this.encountersById = encountersById
	}
	
	/**
	 * Records the incoming events mapped by their event ids.
	 * The events can have either numerical or coded results
	 * @param eventsByParentEventId
	 * @return
	 */
	def recordEvents(Map<String, List<Event>> eventsByParentEventId){
		this.eventLists = eventsByParentEventId
	}
	
	def processEvents(){
		
		//Step 1.
		groupBPEvents()
		
		//Step 2.
		persistVitalSigns()
		
	}
	
	/**
	 * Group bp events together
	 */
	def groupBPEvents(){
		this.eventLists.each {key, eventList ->
			//For each event list, group together the bpevents in to a bpSet.
			Set bpSet=new HashSet()
			List bpEventsIndices = new ArrayList()
			eventList.each {event ->
				if (bpEventCodesSet.contains(event.eventCode)){
					bpSet.add(event)
					
					//Remove the event from the events list.
					bpEventsIndices.add(eventList.indexOf(event))
				}
			}
			
			//Remove the individual bp events from the eventslist.
			if(bpEventsIndices.size() > 0){
				bpEventsIndices.reverseEach{index->
					eventList.remove(index)
				}
				
				//Add the bpSet to events list.
				if (validBPSet(bpSet)) {
					eventList.add(bpSet)
				}
			}
			
		}
	}
	
	/**
	 * Checks if the bpSet contains only optional bp events
	 * 
	 * @param bpSet
	 * @return
	 */
	def validBPSet(bpSet){

		assert bpSet.size() > 0, "Empty blood pressure set is invalid: " + bpset;
		
		if (!bpSet.any{ bpEvent-> bpEvent.eventCode == ecm.get("EVENTCODESYS") }) {
			return false
		}

		if (!bpSet.any{ bpEvent-> bpEvent.eventCode == ecm.get("EVENTCODEDIA") }) {
			return false
		}

		return true;
	}
	
	/**
	 * Iterate over the list of events 
	 * Create vitalsign, bloodpressure and vitalSigns objects, grouping the related objects into a single VitalSigns object.
	 * Assign the appropriate encounter object to the vitalSigns object.
	 * Save the vitalSigns to db  
	 */
	def persistVitalSigns(){
		
		this.eventLists.each {key, eventList ->
			if(eventList.size()>0){
				//For each eventlist - create a set containing VitalSign and BloodPressure objects
				VitalSigns vitalSigns=new VitalSigns()
				def encounterId
				def recordId
				String vitalSignsDate
				BloodPressure bloodPressure
				
				eventList.each {event ->
					if(event instanceof Event){
						//an event object. Create a corresponding vitalsign object 
						def vitalSign = createVitalSign(event)
						def vitalType = SmartMapper.map(event.eventCode, 'Type')
						vitalSigns.setProperty(vitalType, vitalSign)
	
						//read the end date for this event.
						vitalSignsDate=event.eventEndDateTime
						//read the encounterId for this event
						encounterId = event.getEncounterId()
						//read the recordId for this event
						recordId = event.getRecordId()
					}else if(event instanceof HashSet){
						//a hashset object with bloodpressure information.
						bloodPressure = new BloodPressure()
						//Iterate over all elements in the set and create a bloodpressure object containing individual properties.
						//Each bloodPressure property corresponds to one element in the set.
						event.each{
							def bloodPressureProperty
							if (it.eventValue==null){
								//if the event property has a blank value, we have to create a coded value. e.g. bodyPosition
								bloodPressureProperty= createCodedValue(it.eventTag)
							}else{
								//the event property has a value. Create a vital sign object. e.g. systolic
								bloodPressureProperty = createVitalSign(it)
							}
	
							def propertyType = SmartMapper.map(it.eventCode, 'Type')
							bloodPressure.setProperty(propertyType, bloodPressureProperty)
							
							//read the encounterId for this event
							encounterId = it.getEncounterId()
							//read the recordId for this event
							recordId = it.getRecordId()
							//read the end date for this event.
							vitalSignsDate=it.eventEndDateTime
						}
						bloodPressure.save()
						vitalSigns.setProperty('bloodPressure', bloodPressure)
					} else {
						throw new Exception ("unrecognized event type: " + event);
					}
				}
				
				//All events in this list have the same encounter id. Read it from the first event.
				//Add the encounter to the vitalSigns object.
				assert encounterId != null, "No encounter ID found for " + eventList
				assert encountersById.get(encounterId) != null, "Encounter ID unkonwn: " + encounterId
			
				vitalSigns.setProperty('encounter', encountersById.get(encounterId))
				vitalSigns.setProperty('patientId', recordId)
				vitalSigns.setProperty('date', convertToDate(vitalSignsDate))
				
				//Run any required unit conversions
				convertValues(vitalSigns)
				//Save the vitalSigns object to the db.
				vitalSigns.save(flush:true)
				//Add the vitalSigns object to the vitalSignsSet.
				vitalSignsSet.add(vitalSigns)
			}
		}
	}
	
	def convertToDate(String vitalSignsDate){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
		String timeZoneString = "GMT"+vitalSignsDate.substring(23)
		df.setTimeZone(TimeZone.getTimeZone(timeZoneString))
		Date date = df.parse(vitalSignsDate.substring(0, 23))
		return date
	}
	
	def createVitalSign(Event event){
		VitalSign vitalSign = new VitalSign()
		vitalSign.setUnit(SmartMapper.map(event.eventCode, 'Unit'))
		vitalSign.setValue(event.eventValue)
		vitalSign.setVitalName(createCodedValue(event.eventCode))
		vitalSign.save()
		return vitalSign
	}
	
	def createCodedValue(String eventCode){
		String title=SmartMapper.map(eventCode, 'Title')
		CodedValue codedValue = CodedValue.get(title)
		if(null==codedValue){
			codedValue = new CodedValue()
			codedValue.setCode(
				createCode(eventCode)
			)
			codedValue.setTitle(SmartMapper.map(eventCode, 'Title'))
			codedValue.save()
		}
		return codedValue
	}

	def createCode(eventCode){
		String title=SmartMapper.map(eventCode, 'Title')
		Code code = Code.get(title)
		if(null==code){
			code = new Code()
			code.setType(SmartMapper.map(eventCode, 'codeType'))
			code.setTitle(SmartMapper.map(eventCode, 'Title'))
			code.setSystem(SmartMapper.map(eventCode, 'codingSystem'))
			code.setIdentifier(SmartMapper.map(eventCode, 'Resource'))
			code.save()
		}
		return code
	}
	/**
	 * Run any required unit conversions
	 * - height values need to be converted from m to cm.
	 * @param currentValue
	 * @param currentEventCode
	 * @return
	 */
	def convertValues(vitalSigns){
		if(vitalSigns.height!=null){
			double height = Double.parseDouble(vitalSigns.height.value)
			height = height/100
			vitalSigns.height.value = Double.toString(height)
		}
	}
	
	def getVitalsFromResponse(){
		return new Vitals(vitalSignsSet)
	}
	
	def getVitalsFromCache(Map requestParams){
		Vitals vitals
		
		String limitParam = requestParams.get(ResultsCall.LIMITPARAM)
		int limit
		if(null!=limitParam){
			limit = Integer.parseInt(limitParam)
		}
		
		String offsetParam = requestParams.get(ResultsCall.OFFSETPARAM)
		int offset
		if(null!=offsetParam){
			offset= Integer.parseInt(offsetParam)
		}
		
		String patientId = requestParams.get(MilleniumObjectCall.RECORDIDPARAM)
		
		List vitalSigns = VitalSigns.findAllByPatientId(patientId, [max:limit, offset:offset, sort:"date", order:"desc"])
		Set<VitalSigns> vitalSignsSet = new HashSet()
		vitalSignsSet.addAll(vitalSigns)
		vitals = new Vitals(vitalSignsSet)
		return vitals
	}
}
