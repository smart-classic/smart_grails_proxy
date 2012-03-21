package org.chip.managers


import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

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
			if(bpEventsIndices!=null){
				bpEventsIndices.reverseEach{index->
					eventList.remove(index)
				}
			}
			//Add the bpSet to events list.
			if(isBPSetValid(bpSet)){
				eventList.add(bpSet)
			}
		}
	}
	
	/**
	 * Checks if the only event in the bpSet is one of the three optional vitals: location, position, method
	 * @param bpSet
	 * @return
	 */
	def isBPSetValid(bpSet){
		if(bpSet.size()==0){
			return false
		}
		
		try{
			assert bpSet.any{bpEvent->
				bpEvent.eventCode in [ecm.get("EVENTCODESYS"), ecm.get("EVENTCODEDIA")]
			}
		}catch(AssertionError ae){
			log.error("Found Invalid BP Set. Cannot find corresponding systolic and diastolic values for the following optional bp Events:")
			bpSet.each {bpEvent->
				log.error(bpEvent.toString())
			}
			return false
		}
		
		return true
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
					bloodPressure = new BloodPressure()
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
