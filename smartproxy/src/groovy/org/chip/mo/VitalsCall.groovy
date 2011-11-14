package org.chip.mo;

import org.chip.rdf.Vitals;
import org.chip.rdf.vitals.*;
import groovy.xml.MarkupBuilder;

class VitalsCall extends MilleniumObjectCall{
	
	private static final String ENCOUNTERIDSPARAM = "ENCOUNTERIDSPARAM"
	
	Map vitalSignsMap = new HashMap()
	
	private static final String EVENTCODEHEIGHT="2700653"
	private static final String EVENTCODEWEIGHT="2700654"
	private static final String EVENTCODERRATE="703540"
	private static final String EVENTCODEHEARTRATE="7935038"
	private static final String EVENTCODEOSAT="8238766"
	private static final String EVENTCODETEMP="8713424"
	private static final String EVENTCODESYS="703501"
	private static final String EVENTCODEDIA="703516"
	private static final String EVENTCODELOCATION="4099993"
	private static final String EVENTCODEPOSITION="13488852"
	private static final String EVENTCODEBPMETHOD="4100005"
	private static final String EVENTCODESYSSUPINE="1164536"
	private static final String EVENTCODESYSSITTING="1164545"
	private static final String EVENTCODESYSSTANDING="1164548"
	private static final String EVENTCODEDIASUPINE="1164539"
	private static final String EVENTCODEDIASITTING="1164542"
	private static final String EVENTCODEDIASTANDING="1164551"	
	
	static final Map encounterResourceMap
	static final Map encounterTitleMap
	static final Map vitalTypeMap
	static final Map vitalTitleMap
	static final Map vitalResourceMap
	static final Map vitalUnitMap
	static final Set vitalEventCodesSet
	static final Set bpEventCodesSet
	static final Set complexBPEventCodesSet
	
	static{
		encounterResourceMap = new HashMap()
		encounterResourceMap.put("Outpatient","http://smartplatforms.org/terms/codes/EncounterType#ambulatory")
		encounterResourceMap.put("Emergency","http://smartplatforms.org/terms/codes/EncounterType#emergency")
		encounterResourceMap.put("Field","http://smartplatforms.org/terms/codes/EncounterType#field")
		encounterResourceMap.put("Home","http://smartplatforms.org/terms/codes/EncounterType#home")
		encounterResourceMap.put("Inpatient","http://smartplatforms.org/terms/codes/EncounterType#inpatient")
		encounterResourceMap.put("Virtual","http://smartplatforms.org/terms/codes/EncounterType#virtual")
		
		encounterTitleMap = new HashMap()
		encounterTitleMap.put("Outpatient","Ambulatory encounter")
		encounterTitleMap.put("Emergency", "Emergency encounter")
		encounterTitleMap.put("Field", "Field encounter")
		encounterTitleMap.put("Home", "Home encounter")
		encounterTitleMap.put("Inpatient", "Inpatient encounter")
		encounterTitleMap.put("Virtual", "Virtual encounter")
		
		vitalTypeMap = new HashMap()
		vitalTypeMap.put(EVENTCODEHEIGHT, "height")
		vitalTypeMap.put(EVENTCODEWEIGHT, "weight")
		//vitalTypeMap.put("", "bodyMassIndex")
		vitalTypeMap.put(EVENTCODERRATE, "respiratoryRate")
		vitalTypeMap.put(EVENTCODEHEARTRATE, "heartRate")
		vitalTypeMap.put(EVENTCODEOSAT, "oxygenSaturation")
		vitalTypeMap.put(EVENTCODETEMP, "temperature")
		vitalTypeMap.put(EVENTCODESYS, "systolic")
		vitalTypeMap.put(EVENTCODEDIA, "diastolic")
		vitalTypeMap.put(EVENTCODEBPMETHOD, "method")
		vitalTypeMap.put(EVENTCODELOCATION, "bodySite")
		vitalTypeMap.put(EVENTCODEPOSITION, "bodyPosition")
		
		vitalTitleMap = new HashMap()
		vitalTitleMap.put(EVENTCODEHEIGHT, "Height (measured)")
		vitalTitleMap.put(EVENTCODEWEIGHT, "Body weight (measured)")
		//vitalTitleMap.put("", "Body mass index")
		vitalTitleMap.put(EVENTCODERRATE, "Respiration rate")
		vitalTitleMap.put(EVENTCODEHEARTRATE, "Heart Rate")
		vitalTitleMap.put(EVENTCODEOSAT, "Oxygen saturation")
		vitalTitleMap.put(EVENTCODETEMP, "Body temperature")
		vitalTitleMap.put(EVENTCODESYS, "Systolic blood pressure")
		vitalTitleMap.put(EVENTCODEDIA, "Diastolic blood pressure")
		vitalTitleMap.put("Auscultation", "Auscultation")
		vitalTitleMap.put("Palpation", "Palpation")
		vitalTitleMap.put("Automated", "Machine")
		vitalTitleMap.put("", "Invasive")
		vitalTitleMap.put("Sitting", "Sitting")
		vitalTitleMap.put("Standing", "Standing")
		vitalTitleMap.put("Supine", "Supine")
		vitalTitleMap.put("Left upper","Left arm")
		vitalTitleMap.put("Right upper","Right arm")
		vitalTitleMap.put("Left lower","Left thigh")
		vitalTitleMap.put("Right lower","Right thigh")
		
		vitalResourceMap = new HashMap()
		vitalResourceMap.put(EVENTCODEHEIGHT, "http://loinc.org/codes/8302-2")
		vitalResourceMap.put(EVENTCODEWEIGHT, "http://loinc.org/codes/3141-9")
		//vitalResourceMap.put("", "http://loinc.org/codes/39156-5")
		vitalResourceMap.put(EVENTCODERRATE, "http://loinc.org/codes/9279-1")
		vitalResourceMap.put(EVENTCODEHEARTRATE, "http://loinc.org/codes/8867-4")
		vitalResourceMap.put(EVENTCODEOSAT, "http://loinc.org/codes/2710-2")
		vitalResourceMap.put(EVENTCODETEMP, "http://loinc.org/codes/8310-5")
		vitalResourceMap.put(EVENTCODESYS, "http://loinc.org/codes/8480-6")
		vitalResourceMap.put(EVENTCODEDIA, "http://loinc.org/codes/8462-4")
		vitalResourceMap.put("Auscultation", "http://smartplatforms.org/terms/codes/BloodPressureMethod#auscultation")
		vitalResourceMap.put("Palpation", "http://smartplatforms.org/terms/codes/BloodPressureMethod#palpation")
		vitalResourceMap.put("Automated", "http://smartplatforms.org/terms/codes/BloodPressureMethod#machine")
		vitalResourceMap.put("", "http://smartplatforms.org/terms/codes/BloodPressureMethod#invasive")
		vitalResourceMap.put("Sitting", "http://www.ihtsdo.org/snomed-ct/concepts/33586001" )
		vitalResourceMap.put("Standing", "http://www.ihtsdo.org/snomed-ct/concepts/10904000" )
		vitalResourceMap.put("Supine", "http://www.ihtsdo.org/snomed-ct/concepts/40199007" )
		vitalResourceMap.put("Left upper","http://www.ihtsdo.org/snomed-ct/concepts/368208006")
		vitalResourceMap.put("Right upper","http://www.ihtsdo.org/snomed-ct/concepts/368209003")
		vitalResourceMap.put("Left lower","http://www.ihtsdo.org/snomed-ct/concepts/61396006")
		vitalResourceMap.put("Right lower","http://www.ihtsdo.org/snomed-ct/concepts/11207009")
		
		vitalUnitMap = new HashMap()
		vitalUnitMap.put(EVENTCODEHEIGHT, "m")
		vitalUnitMap.put(EVENTCODEWEIGHT, "kg")
		//vitalUnitMap.put("", "{BMI}")
		vitalUnitMap.put(EVENTCODERRATE, "{breaths}")
		vitalUnitMap.put(EVENTCODEHEARTRATE, "{beats}/min")
		vitalUnitMap.put(EVENTCODEOSAT, "%{HemoglobinSaturation}")
		vitalUnitMap.put(EVENTCODETEMP, "Cel")
		vitalUnitMap.put(EVENTCODESYS, "mm[Hg]")
		vitalUnitMap.put(EVENTCODEDIA, "mm[Hg]")
		
		vitalEventCodesSet = new HashSet()
		vitalEventCodesSet.add(EVENTCODEHEIGHT)
		vitalEventCodesSet.add(EVENTCODEWEIGHT)
		//vitalEventCodesSet.add("")
		vitalEventCodesSet.add(EVENTCODERRATE)
		vitalEventCodesSet.add(EVENTCODEHEARTRATE)
		vitalEventCodesSet.add(EVENTCODEOSAT)
		vitalEventCodesSet.add(EVENTCODETEMP)
		vitalEventCodesSet.add(EVENTCODESYS)
		vitalEventCodesSet.add(EVENTCODEDIA)
		vitalEventCodesSet.add(EVENTCODEBPMETHOD)
		vitalEventCodesSet.add(EVENTCODELOCATION)
		vitalEventCodesSet.add(EVENTCODEPOSITION)
		vitalEventCodesSet.add(EVENTCODESYSSUPINE)
		vitalEventCodesSet.add(EVENTCODESYSSITTING)
		vitalEventCodesSet.add(EVENTCODESYSSTANDING)
		vitalEventCodesSet.add(EVENTCODEDIASUPINE)
		vitalEventCodesSet.add(EVENTCODEDIASITTING)
		vitalEventCodesSet.add(EVENTCODEDIASTANDING)
		
		bpEventCodesSet = new HashSet()
		bpEventCodesSet.add(EVENTCODESYS)
		bpEventCodesSet.add(EVENTCODEDIA)
		bpEventCodesSet.add(EVENTCODELOCATION)
		bpEventCodesSet.add(EVENTCODEPOSITION)
		bpEventCodesSet.add(EVENTCODEBPMETHOD)
		
		complexBPEventCodesSet = new HashSet()
		complexBPEventCodesSet.add(EVENTCODESYSSUPINE)
		complexBPEventCodesSet.add(EVENTCODESYSSITTING)
		complexBPEventCodesSet.add(EVENTCODESYSSTANDING)
		complexBPEventCodesSet.add(EVENTCODEDIASUPINE)
		complexBPEventCodesSet.add(EVENTCODEDIASITTING)
		complexBPEventCodesSet.add(EVENTCODEDIASTANDING)
		
	}
	
	def makeCall(recordId, moURL){
		
		transaction = 'ReadEncountersByFilters'
		targetServlet = 'com.cerner.encounter.EncounterServlet'
		
		Map<String,Object> requestParams = new HashMap()
		requestParams.put(RECORDIDPARAM, recordId)
		
		def requestXML = createRequest(requestParams)
		//long l1 = new Date().getTime()
		def resp = makeRestCall(requestXML, moURL)
		//long l2 = new Date().getTime()
		//println("encounter mo call took : "+(l2-l1)/1000)
		readResponse(resp)
		//println("no of encounters: "+vitalSignsMap.size())
		
		//refresh the writer and builder objects
		writer = new StringWriter()
		builder = new MarkupBuilder(writer)
		
		transaction = 'ReadResultsByCount'
		targetServlet = 'com.cerner.results.ResultsServlet'
		
		requestParams.put(ENCOUNTERIDSPARAM, vitalSignsMap.keySet())
		requestXML = createRequest(requestParams)
		//l1 = new Date().getTime()
		resp=makeRestCall(requestXML, moURL)
		//l2 = new Date().getTime()
		//println("vitals mo call took : "+(l2-l1)/1000)

		readResponse(resp)
	}
	
	/**
	* Generates MO requests to 
	* - Get Encounters for a given patient ID
	* - Get Vitals for a list of Encounters and a given patient id
	* @param recordId
	* @return
	*/
	def generatePayload(requestParams){
		def recordId = (String)requestParams.get(RECORDIDPARAM)
		if (transaction.equals("ReadEncountersByFilters")){
			builder.PersonId(recordId)
			builder.BypassOrganizationSecurityIndicator('true')
		}else{
			builder.PersonId(recordId)
			builder.EventCount('99999')
			builder.EventSet(){
				Name('CLINICAL INFORMATION')
			}
			
			Set encounterIdSet = (Set)requestParams.get(ENCOUNTERIDSPARAM)
			builder.EncounterIds(){
				encounterIdSet.each{encounterId->
					EncounterId(encounterId)
				}
			}
		}
	}
	
	/**
	* Reading the MO response for Vitals:
	* Iterate through all NumericResults
	* 	• if the event code is in the vitalEventCodesSet and value is valid
	* 		∘ start creating the vitals object
	* 		∘ specify if the added vital is a bpevent
	* 		∘ Add the vital to a list. This list will be added to a map which has parent event id as the key. 
	* 			So all vitals with the same parent event id are grouped together.
	* 		∘ If can't match my parent event id, match by timestamps.
	* 
	* Iterate through all CodedResults	
	* 	• if the event code is in the vitalEventCodesSet and value is valid
	* 		∘ start creating the vitals object
	* 		∘ specify if the added vital is a bpevent
	* 		∘ specify that the vital is codedfield
	* 		∘ Add the vital to a list. This list will be added to a map which has parent event id as the key. So all vitals with the same parent event id are grouped together.
	* 		∘ If can't match my parent event id, match by timestamps.
	* @param moResponse
	* @return
	*/
	def readResponse(moResponse){
		
		def replyMessage = moResponse.getData()
		def payload= replyMessage.Payload
		if (transaction.equals("ReadEncountersByFilters")){
			//long l1 = new Date().getTime()
			payload.Encounters.Encounter.each{
				Encounter encounter = new Encounter()
				encounter.setId(it.EncounterId.text())
				encounter.setStartDate(it.RegistrationDateTime.text())
				encounter.setEndDate(it.DischargeDateTime.text())
				encounter.setResource(encounterResourceMap.get(it.EncounterTypeClass.Display.text()))
				encounter.setTitle(encounterTitleMap.get(it.EncounterTypeClass.Display.text()))
				VitalSigns vitalSigns = new VitalSigns()
				vitalSigns.setEncounter(encounter)
				vitalSignsMap.put(it.EncounterId.text(), vitalSigns)
			}
			//long l2 = new Date().getTime()
			//println("encounter reading moresponse took: "+(l2-l1)/1000)
		}else{
			//int i = 0
			//long l1 = new Date().getTime()
			payload.Results.ClinicalEvents.NumericResult.each{ currentNumericResult->
					//i++
					def currentEncounterId = currentNumericResult.EncounterId.text()
					def currentEventCode = currentNumericResult.EventCode.Value.text()
					def currentValue = currentNumericResult.Value.text()
					def currentEventId = currentNumericResult.EventId.text()
					def currentParentEventId = currentNumericResult.ParentEventId.text()
					def currentEventEndDateTime = currentNumericResult.EventEndDateTime.text()
					def currentUpdateDateTime = currentNumericResult.UpdateDateTime.text()
					
					
					currentValue=convertValue(currentValue, currentEventCode)
					
					if((currentEventCode!=null) && (vitalEventCodesSet.contains(currentEventCode)) && valueIsValid(currentValue)){
						VitalSign vitalSign = new VitalSign()
						vitalSign.setEventId(currentEventId)
						vitalSign.setParentEventId(currentParentEventId)
						vitalSign.setValue(currentValue)
						vitalSign.setCode(currentEventCode)
						vitalSign.setType(vitalTypeMap.get(currentEventCode))
						vitalSign.setTitle(vitalTitleMap.get(currentEventCode))
						vitalSign.setResource(vitalResourceMap.get(currentEventCode))
						vitalSign.setUnit(vitalUnitMap.get(currentEventCode))
						vitalSign.setEventEndDateTime(currentEventEndDateTime)
						vitalSign.setUpdateDateTime(currentUpdateDateTime)
						(bpEventCodesSet.contains(currentEventCode))?vitalSign.setIsBPField(true):vitalSign.setIsBPField(false)
						
						addVitalSignToVitalSignsMap(vitalSign, currentEncounterId)
						
					}
			}	
			payload.Results.ClinicalEvents.CodedResult.each{ currentCodedResult->
					//i++
					def currentEncounterId = currentCodedResult.EncounterId.text()
					def currentEventCode = currentCodedResult.EventCode.Value.text()
					def currentEventTag = currentCodedResult.EventTag.text()
					def currentEventId = currentCodedResult.EventId.text()
					def currentParentEventId = currentCodedResult.ParentEventId.text()
					def currentEventEndDateTime = currentCodedResult.EventEndDateTime.text()
					def currentUpdateDateTime = currentCodedResult.UpdateDateTime.text()
					
					
					if((currentEventCode!=null) && (vitalEventCodesSet.contains(currentEventCode)) && valueIsValid(currentEventTag)){
						VitalSign vitalSign = new VitalSign()
						vitalSign.setEventId(currentEventId)
						vitalSign.setParentEventId(currentParentEventId)
						vitalSign.setValue(currentEventTag)
						vitalSign.setCode(currentEventCode)
						vitalSign.setType(vitalTypeMap.get(currentEventCode))
						vitalSign.setTitle(vitalTitleMap.get(currentEventTag))
						vitalSign.setResource(vitalResourceMap.get(currentEventTag))
						vitalSign.setEventEndDateTime(currentEventEndDateTime)
						vitalSign.setUpdateDateTime(currentUpdateDateTime)
						(bpEventCodesSet.contains(currentEventCode))?vitalSign.setIsBPField(true):vitalSign.setIsBPField(false)
						vitalSign.setIsCodedField(true)
						
						addVitalSignToVitalSignsMap(vitalSign, currentEncounterId)
					}
			}
			
			postProcessVitalSignsMap()
			//println("number of results returned : " + i)
			//long l2 = new Date().getTime()
			//println("vitals reading moresponse took: "+(l2-l1)/1000)
		}
		return new Vitals(vitalSignsMap)
	}
	
	def addVitalSignToVitalSignsMap(VitalSign vitalSign, String currentEncounterId){
		if(vitalSignsMap.get(currentEncounterId).vitalSignMap.keySet().contains(vitalSign.getParentEventId())){
			vitalSignsMap.get(currentEncounterId).vitalSignMap.get(vitalSign.getParentEventId()).add(vitalSign)
		}else{
			//Unable to match on parentEventId
			//Try and match on timestamp first
			boolean matchFound = false
			//Get the set of all parentEventIds for this encounter
			Set vitalSignMapKeySet = vitalSignsMap.get(currentEncounterId).vitalSignMap.keySet()
			//Iterate through each parentEventId
			vitalSignMapKeySet.each{ parentEventId ->
				//Get the vitalSignList attached to this parentEventId
				List vitalSignList = vitalSignsMap.get(currentEncounterId).vitalSignMap.get(parentEventId)
				
				//compare the timestamps from the first element in the list with the current event's timestamps.
				String listEventEndDateTime = null
				String listUpdateDateTime = null
				if(vitalSignList.size()>0){
					listEventEndDateTime = ((VitalSign)vitalSignList.get(0)).getEventEndDateTime()
					listUpdateDateTime = ((VitalSign)vitalSignList.get(0)).getUpdateDateTime()
				}
				if(vitalSign.getEventEndDateTime()==listEventEndDateTime && vitalSign.getUpdateDateTime()==listUpdateDateTime){
					//timestamps match. Add the vital to this list.
					vitalSignsMap.get(currentEncounterId).vitalSignMap.get(parentEventId).add(vitalSign)
					matchFound = true
				}
			}
			
			//Unable to match on timestamp. Create a new list for this parentEventId
			if(!matchFound){
				List<VitalSign> newVitalSignList = new ArrayList()
				newVitalSignList.add(vitalSign)
				vitalSignsMap.get(currentEncounterId).vitalSignMap.put(vitalSign.getParentEventId(), newVitalSignList)
			}
			
		}
	}
	
	/**
	 * Iterate through all encounters
	 * ->Iterate through all parent event ids for a given encounter
	 * -->Get the vitals list for the current parent event id
	 * -->if the first vitals corresponds to a complex bp event, assume all are and proceed to process them.
	 * -->Split the list of complex events into seperate lists based on body position
	 * -->Add the new lists into the vitalSignsMap
	 * -->Remove the original list of complex events from the vitalSignsMap
	 * @return
	 */
	def postProcessVitalSignsMap(){
		Map atomicVitalSignsMap = new HashMap()
		Map encounterComplexParentIdsMap = new HashMap()
		
		Set vitalSignsMapKeySet = vitalSignsMap.keySet()
		vitalSignsMapKeySet.each{encounterId->
			
			atomicVitalSignsMap.put(encounterId, new VitalSigns())
			encounterComplexParentIdsMap.put(encounterId, new ArrayList())
			
			Set parentEventIds = vitalSignsMap.get(encounterId).vitalSignMap.keySet()
			parentEventIds.each{parentEventId->
				List vitalSignList = vitalSignsMap.get(encounterId).vitalSignMap.get(parentEventId)
				if(vitalSignList.size()>0){
					if (complexBPEventCodesSet.contains(((VitalSign)vitalSignList.get(0)).getCode())){
						List supineBPVitalsList = new ArrayList()
						List standingBPVitalsList = new ArrayList()
						List sittingBPVitalsList = new ArrayList()
						vitalSignList.each{complexVitalSign->
							if(complexVitalSign.getCode()==EVENTCODESYSSUPINE){
								supineBPVitalsList.add(createVitalSignFromNumericResult(complexVitalSign.getEventId(), complexVitalSign.getParentEventId(),
									 complexVitalSign.getValue(), EVENTCODESYS,
									 complexVitalSign.getEventEndDateTime(), complexVitalSign.getUpdateDateTime()))
							}else if(complexVitalSign.getCode()==EVENTCODEDIASUPINE){
								supineBPVitalsList.add(createVitalSignFromNumericResult(complexVitalSign.getEventId(), complexVitalSign.getParentEventId(),
									complexVitalSign.getValue(), EVENTCODEDIA,
									complexVitalSign.getEventEndDateTime(), complexVitalSign.getUpdateDateTime()))
							}else if(complexVitalSign.getCode()==EVENTCODESYSSTANDING){
								standingBPVitalsList.add(createVitalSignFromNumericResult(complexVitalSign.getEventId(), complexVitalSign.getParentEventId(),
									complexVitalSign.getValue(), EVENTCODESYS,
									complexVitalSign.getEventEndDateTime(), complexVitalSign.getUpdateDateTime()))
							}else if(complexVitalSign.getCode()==EVENTCODEDIASTANDING){
								standingBPVitalsList.add(createVitalSignFromNumericResult(complexVitalSign.getEventId(), complexVitalSign.getParentEventId(),
									complexVitalSign.getValue(), EVENTCODEDIA,
									complexVitalSign.getEventEndDateTime(), complexVitalSign.getUpdateDateTime()))
							}else if(complexVitalSign.getCode()==EVENTCODESYSSITTING){
								sittingBPVitalsList.add(createVitalSignFromNumericResult(complexVitalSign.getEventId(), complexVitalSign.getParentEventId(),
									complexVitalSign.getValue(), EVENTCODESYS,
									complexVitalSign.getEventEndDateTime(), complexVitalSign.getUpdateDateTime()))
							}else if(complexVitalSign.getCode()==EVENTCODEDIASITTING){
								sittingBPVitalsList.add(createVitalSignFromNumericResult(complexVitalSign.getEventId(), complexVitalSign.getParentEventId(),
									complexVitalSign.getValue(), EVENTCODEDIA,
									complexVitalSign.getEventEndDateTime(), complexVitalSign.getUpdateDateTime()))
							}
						}
						if(supineBPVitalsList.size()>0){
							supineBPVitalsList.add(
								createVitalSignFromCodedResult(supineBPVitalsList.get(0).getEventId(),
									supineBPVitalsList.get(0).getParentEventId(),
									'Supine',
									EVENTCODEPOSITION,
									supineBPVitalsList.get(0).getEventEndDateTime(),
									supineBPVitalsList.get(0).getUpdateDateTime()))
							
							atomicVitalSignsMap.get(encounterId).vitalSignMap.put(supineBPVitalsList.get(0).getEventId(), supineBPVitalsList)
						}
						if(standingBPVitalsList.size()>0){
							standingBPVitalsList.add(
								createVitalSignFromCodedResult(standingBPVitalsList.get(0).getEventId(),
									standingBPVitalsList.get(0).getParentEventId(),
									'Standing',
									EVENTCODEPOSITION,
									standingBPVitalsList.get(0).getEventEndDateTime(),
									standingBPVitalsList.get(0).getUpdateDateTime()))
							
							atomicVitalSignsMap.get(encounterId).vitalSignMap.put(standingBPVitalsList.get(0).getEventId(), standingBPVitalsList)
						}
						if(sittingBPVitalsList.size()>0){
							sittingBPVitalsList.add(
								createVitalSignFromCodedResult(sittingBPVitalsList.get(0).getEventId(),
									sittingBPVitalsList.get(0).getParentEventId(),
									'Sitting',
									EVENTCODEPOSITION,
									sittingBPVitalsList.get(0).getEventEndDateTime(),
									sittingBPVitalsList.get(0).getUpdateDateTime()))
							
							atomicVitalSignsMap.get(encounterId).vitalSignMap.put(sittingBPVitalsList.get(0).getEventId(), sittingBPVitalsList)
						}
						
						encounterComplexParentIdsMap.get(encounterId).add(parentEventId)
					}
				}
			}
		} 
		
		Set atomicVitalSignsMapKeySet = atomicVitalSignsMap.keySet()
		atomicVitalSignsMapKeySet.each { encounterId->
			Set parentEventIdSet = atomicVitalSignsMap.get(encounterId).vitalSignMap.keySet()
			parentEventIdSet.each{ parentEventId->
				vitalSignsMap.get(encounterId).vitalSignMap.put(parentEventId, atomicVitalSignsMap.get(encounterId).vitalSignMap.get(parentEventId))
			}
		}
		
		Set encounterComplexParentIdsMapKeySet = encounterComplexParentIdsMap.keySet()
		encounterComplexParentIdsMapKeySet.each{encounterId->
			List complexParentIds = encounterComplexParentIdsMap.get(encounterId)
			complexParentIds.each{complexParentId->
				vitalSignsMap.get(encounterId).vitalSignMap.remove(complexParentId)
			}
		}
	}
	
	def createVitalSignFromNumericResult(currentEventId, currentParentEventId, currentValue, currentEventCode, currentEventEndDateTime, currentUpdateDateTime){
		VitalSign vitalSign = new VitalSign()
		vitalSign.setEventId(currentEventId)
		vitalSign.setParentEventId(currentParentEventId)
		vitalSign.setValue(currentValue)
		vitalSign.setCode(currentEventCode)
		vitalSign.setType(vitalTypeMap.get(currentEventCode))
		vitalSign.setTitle(vitalTitleMap.get(currentEventCode))
		vitalSign.setResource(vitalResourceMap.get(currentEventCode))
		vitalSign.setUnit(vitalUnitMap.get(currentEventCode))
		vitalSign.setEventEndDateTime(currentEventEndDateTime)
		vitalSign.setUpdateDateTime(currentUpdateDateTime)
		(bpEventCodesSet.contains(currentEventCode))?vitalSign.setIsBPField(true):vitalSign.setIsBPField(false)
		return vitalSign
	}
	
	def createVitalSignFromCodedResult(currentEventId, currentParentEventId, currentEventTag, currentEventCode, currentEventEndDateTime, currentUpdateDateTime){
		VitalSign vitalSign = new VitalSign()
		vitalSign.setEventId(currentEventId)
		vitalSign.setParentEventId(currentParentEventId)
		vitalSign.setValue(currentEventTag)
		vitalSign.setCode(currentEventCode)
		vitalSign.setType(vitalTypeMap.get(currentEventCode))
		vitalSign.setTitle(vitalTitleMap.get(currentEventTag))
		vitalSign.setResource(vitalResourceMap.get(currentEventTag))
		vitalSign.setEventEndDateTime(currentEventEndDateTime)
		vitalSign.setUpdateDateTime(currentUpdateDateTime)
		(bpEventCodesSet.contains(currentEventCode))?vitalSign.setIsBPField(true):vitalSign.setIsBPField(false)
		vitalSign.setIsCodedField(true)
		return vitalSign
	}
	
	def convertValue(currentValue, currentEventCode){
		if (currentEventCode.equals(EVENTCODEHEIGHT)){
			double height = Double.parseDouble(currentValue)
			height = height/100
			currentValue = Double.toString(height)
		}
		return currentValue
	}
	
	def valueIsValid(currentValue){
		if (currentValue==null) return false
		if (currentValue.equals("")) return false
		if (currentValue.equals("0")) return false
		if (currentValue.equals("0.0")) return false
		
		return true
	}
}
