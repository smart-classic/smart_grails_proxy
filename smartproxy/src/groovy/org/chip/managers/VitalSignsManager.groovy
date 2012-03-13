package org.chip.managers


import java.util.Map;
import java.util.Set;

import org.chip.mo.exceptions.MOCallException;
import org.chip.mo.mappers.SmartMapper;
import org.chip.mo.model.Event;
import org.chip.rdf.Vitals;
import org.chip.rdf.vitals.BloodPressure;
import org.chip.rdf.vitals.CodedValue;
import org.chip.rdf.vitals.Encounter;
import org.chip.rdf.vitals.VitalSign;
import org.chip.rdf.vitals.VitalSigns;
import org.codehaus.groovy.grails.commons.ConfigurationHolder;


class VitalSignsManager {
	
	Map<String, List> eventLists = new HashMap()
	Map<String, Encounter> encountersById
	
	Set<VitalSigns> vitalSignsSet = new HashSet()
		
	private static final String EVENTCODEHEIGHT
	private static final String EVENTCODEWEIGHT
	private static final String EVENTCODERRATE
	private static final String EVENTCODEHEARTRATE
	private static final String EVENTCODEOSAT
	private static final String EVENTCODETEMP
	private static final String EVENTCODESYS
	private static final String EVENTCODEDIA
	private static final String EVENTCODELOCATION
	private static final String EVENTCODEPOSITION
	private static final String EVENTCODEBPMETHOD
	private static final String EVENTCODESYSSUPINE
	private static final String EVENTCODESYSSITTING
	private static final String EVENTCODESYSSTANDING
	private static final String EVENTCODEDIASUPINE
	private static final String EVENTCODEDIASITTING
	private static final String EVENTCODEDIASTANDING
	
	static final Set bpEventCodesSet

	
	static{
		
		def config = ConfigurationHolder.config
		
		EVENTCODEHEIGHT=config.cerner.mo.eventCode.EVENTCODEHEIGHT
		EVENTCODEWEIGHT=config.cerner.mo.eventCode.EVENTCODEWEIGHT
		EVENTCODERRATE=config.cerner.mo.eventCode.EVENTCODERRATE
		EVENTCODEHEARTRATE=config.cerner.mo.eventCode.EVENTCODEHEARTRATE
		EVENTCODEOSAT=config.cerner.mo.eventCode.EVENTCODEOSAT
		EVENTCODETEMP=config.cerner.mo.eventCode.EVENTCODETEMP
		EVENTCODESYS=config.cerner.mo.eventCode.EVENTCODESYS
		EVENTCODEDIA=config.cerner.mo.eventCode.EVENTCODEDIA
		EVENTCODELOCATION=config.cerner.mo.eventCode.EVENTCODELOCATION
		EVENTCODEPOSITION=config.cerner.mo.eventCode.EVENTCODEPOSITION
		EVENTCODEBPMETHOD=config.cerner.mo.eventCode.EVENTCODEBPMETHOD
		EVENTCODESYSSUPINE=config.cerner.mo.eventCode.EVENTCODESYSSUPINE
		EVENTCODESYSSITTING=config.cerner.mo.eventCode.EVENTCODESYSSITTING
		EVENTCODESYSSTANDING=config.cerner.mo.eventCode.EVENTCODESYSSTANDING
		EVENTCODEDIASUPINE=config.cerner.mo.eventCode.EVENTCODEDIASUPINE
		EVENTCODEDIASITTING=config.cerner.mo.eventCode.EVENTCODEDIASITTING
		EVENTCODEDIASTANDING=config.cerner.mo.eventCode.EVENTCODEDIASTANDING
		
		bpEventCodesSet = new HashSet()
		bpEventCodesSet.add(EVENTCODESYS)
		bpEventCodesSet.add(EVENTCODEDIA)
		bpEventCodesSet.add(EVENTCODELOCATION)
		bpEventCodesSet.add(EVENTCODEPOSITION)
		bpEventCodesSet.add(EVENTCODEBPMETHOD)
		
	}
	
	def recordEncounters(Map<String,Encounter> encountersById){
		this.encountersById = encountersById
	}
	
	def recordEvents(Map<String, List<Event>> eventsByParentEventId){
		this.eventLists = eventsByParentEventId
	}
	
	def processEvents(){
		
		//Step 1. Group bp events
		groupBPEvents()
		
		//Step 2. Create vitalsign, bloodpressure and vitalSigns objects. Also assign the appropriate encounter object to the vitalSigns object.
		createVitalSignsSet()
		
		//Step 3. Run any required unit conversions
		convertValues()
	}
	
	def groupBPEvents(){
		this.eventLists.each {key, eventList ->
			//For each event list, group together the bpevents in to a bpSet.
			Set bpSet
			List bpEventsIndices
			eventList.each {event ->
				if (bpEventCodesSet.contains(event.eventCode)){
					bpSet = (bpSet==null)?new HashSet():bpSet
					bpSet.add(event)
					
					//Remove the event from the events list.
					bpEventsIndices = (bpEventsIndices==null)?new ArrayList():bpEventsIndices
					bpEventsIndices.add(eventList.indexOf(event))
				}
			}
			
			//Remove the individual bp events from the eventslist.
			if(bpEventsIndices!=null){
				bpEventsIndices.reverseEach{index->
					eventList.remove(index)
				}
			}
			//Add the bpSet to events list.
			if(bpSet!=null){
				eventList.add(bpSet)
			}
		}
	}
	
	def createVitalSignsSet(){
		this.eventLists.each {key, eventList ->
			def encounterId
			//For each eventlist - create a set containing VitalSign and BloodPressure objects
			VitalSigns vitalSigns=new VitalSigns()
			BloodPressure bloodPressure
			eventList.each {event ->
				if(event instanceof Event){
					//an event object. Create a corresponding vitalsign object 
					def vitalSign = createVitalSign(event)
					def vitalType = SmartMapper.map(event.eventCode, 'Type')
					vitalSigns.setProperty(vitalType, vitalSign)
					
					//read the encounterId for this event
					encounterId = event.getEncounterId()
				}else if(event instanceof HashSet){
					//a hashset object with bloodpressure information.
					//Iterate over all elements in the set and create a bloodpressure object containing individual properties.
					//Each bloodPressure property corresponds to one element in the set.
					event.each{
						def bloodPressureProperty
						if (it.value==null){
							//if the event property has a blank value, we have to create a coded value. e.g. bodyPosition
							bloodPressureProperty= createCodedValue(it.eventTag)
						}else{
							//the event property has a value. Create a vital sign object. e.g. systolic
							bloodPressureProperty = createVitalSign(it)
						}

						def propertyType = SmartMapper.map(it.eventCode, 'Type')
						bloodPressure = (bloodPressure==null)?new BloodPressure():bloodPressure
						bloodPressure.setProperty(propertyType, bloodPressureProperty)
						
						//read the encounterId for this event
						encounterId = it.getEncounterId()
					}
					vitalSigns.setProperty('bloodPressure', bloodPressure)
				}
			}
			
			//All events in this list have the same encounter id. Read it from the first event.
			//Add the encounter to the vitalSigns object.
			
			vitalSigns.setProperty('encounter', encountersById.get(encounterId))
			vitalSigns.setProperty('date', encountersById.get(encounterId).getStartDate())
			
			//Add the vitalSigns object to the vitalSignsSet.
			vitalSignsSet.add(vitalSigns)
		}
	}
	
	def createVitalSign(Event event){
		VitalSign vitalSign = new VitalSign()
		vitalSign.setUnit(SmartMapper.map(event.eventCode, 'Unit'))
		vitalSign.setValue(event.value)
		vitalSign.setVitalName(createCodedValue(event.eventCode))
		return vitalSign
	}
	
	def createCodedValue(String eventCode){
		CodedValue codedValue = new CodedValue()
		codedValue.setCode(SmartMapper.map(eventCode, 'Resource'))
		codedValue.setTitle(SmartMapper.map(eventCode, 'Title'))
		return codedValue
	}

	/**
	 * Only the heights need to be converted from m to cm.
	 * This method takes care of that.
	 * @param currentValue
	 * @param currentEventCode
	 * @return
	 */
	def convertValues(){
		vitalSignsSet.each{vitalSigns->
			if(vitalSigns.height!=null){
				double height = Double.parseDouble(vitalSigns.height.value)
				height = height/100
				vitalSigns.height.value = Double.toString(height)
			}
		}
	}
	
	def getVitals(){
		return new Vitals(vitalSignsSet)
	}
}
