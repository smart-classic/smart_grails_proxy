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
	static final Map bodyPositionCodeMap
	static final Map bodyPositionTitleMap
	
	static{
		encounterResourceMap = new HashMap()
		encounterResourceMap.put("Outpatient","http://smartplatforms.org/terms/code/encounterType#ambulatory")
		encounterResourceMap.put("Emergency","http://smartplatforms.org/terms/code/encounterType#emergency")
		encounterResourceMap.put("Field","http://smartplatforms.org/terms/code/encounterType#field")
		encounterResourceMap.put("Home","http://smartplatforms.org/terms/code/encounterType#home")
		encounterResourceMap.put("Inpatient","http://smartplatforms.org/terms/code/encounterType#inpatient")
		encounterResourceMap.put("Virtual","http://smartplatforms.org/terms/code/encounterType#virtual")
		
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
		vitalEventCodesSet.add(EVENTCODELOCATION)
		vitalEventCodesSet.add(EVENTCODEPOSITION)
		
		bpEventCodesSet = new HashSet()
		bpEventCodesSet.add(EVENTCODESYS)
		bpEventCodesSet.add(EVENTCODEDIA)
		bpEventCodesSet.add(EVENTCODELOCATION)
		bpEventCodesSet.add(EVENTCODEPOSITION)
		
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
			builder.EventCount('999')
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
	*Reads in the MO response and converts it to a Vitals object
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
				encounter.setResource(encounterResourceMap.get(it.EncounterType.Display.text()))
				encounter.setTitle(encounterTitleMap.get(it.EncounterType.Display.text()))
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
						(bpEventCodesSet.contains(currentEventCode))?vitalSign.setIsBPField(true):vitalSign.setIsBPField(false)
						
						if(vitalSignsMap.get(currentEncounterId).vitalSignMap.keySet().contains(currentParentEventId)){
							vitalSignsMap.get(currentEncounterId).vitalSignMap.get(currentParentEventId).add(vitalSign)
						}else{
							List<VitalSign> newVitalSignList = new ArrayList()
							newVitalSignList.add(vitalSign)
							vitalSignsMap.get(currentEncounterId).vitalSignMap.put(currentParentEventId, newVitalSignList)
							
						}
						
					}
			}	
			payload.Results.ClinicalEvents.CodedResult.each{ currentCodedResult->
					//i++
					def currentEncounterId = currentCodedResult.EncounterId.text()
					def currentEventCode = currentCodedResult.EventCode.Value.text()
					def currentEventTag = currentCodedResult.EventTag.text()
					def currentEventId = currentCodedResult.EventId.text()
					def currentParentEventId = currentCodedResult.ParentEventId.text()
					
					
					if((currentEventCode!=null) && (vitalEventCodesSet.contains(currentEventCode)) && valueIsValid(currentEventTag)){
						VitalSign vitalSign = new VitalSign()
						vitalSign.setEventId(currentEventId)
						vitalSign.setParentEventId(currentParentEventId)
						vitalSign.setValue(currentEventTag)
						vitalSign.setCode(currentEventCode)
						vitalSign.setType(vitalTypeMap.get(currentEventCode))
						vitalSign.setTitle(vitalTitleMap.get(currentEventTag))
						vitalSign.setResource(vitalResourceMap.get(currentEventTag))
						(bpEventCodesSet.contains(currentEventCode))?vitalSign.setIsBPField(true):vitalSign.setIsBPField(false)
						vitalSign.setIsCodedField(true)
						
						if(vitalSignsMap.get(currentEncounterId).vitalSignMap.keySet().contains(currentParentEventId)){
							vitalSignsMap.get(currentEncounterId).vitalSignMap.get(currentParentEventId).add(vitalSign)
						}else{
							List<VitalSign> newVitalSignList = new ArrayList()
							newVitalSignList.add(vitalSign)
							vitalSignsMap.get(currentEncounterId).vitalSignMap.put(currentParentEventId, newVitalSignList)
							
						}
					}
			}
			//println("number of results returned : " + i)
			//long l2 = new Date().getTime()
			//println("vitals reading moresponse took: "+(l2-l1)/1000)
		}
		return new Vitals(vitalSignsMap)
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
