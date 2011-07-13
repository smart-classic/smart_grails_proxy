package org.chip.mo;

import org.chip.rdf.Vitals;
import org.chip.rdf.vitals.*;
import groovy.xml.MarkupBuilder;

class VitalsCall extends MilleniumObjectCall{
	
	Map vitalSignsMap = new HashMap()
	
	static final Map encounterResourceMap
	static final Map encounterTitleMap
	static final Map vitalTypeMap
	static final Map vitalTitleMap
	static final Map vitalResourceMap
	static final Map vitalUnitMap
	static final Set vitalEventCodesSet
	static final Set bpEventCodesSet
	
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
		vitalTypeMap.put("15612799", "height")
		vitalTypeMap.put("3777472", "weight")
		vitalTypeMap.put("", "bodyMassIndex")
		vitalTypeMap.put("703540", "respiratoryRate")
		vitalTypeMap.put("7935038", "heartRate")
		vitalTypeMap.put("8238766", "oxygenSaturation")
		vitalTypeMap.put("8713424", "temperature")
		vitalTypeMap.put("703501", "systolic")
		vitalTypeMap.put("703516", "diastolic")
		vitalTypeMap.put("", "bodyPosition")
		vitalTypeMap.put("4099993", "bodySite")
		vitalTypeMap.put("4100005", "method")
		
		vitalTitleMap = new HashMap()
		vitalTitleMap.put("15612799", "Height (measured)")
		vitalTitleMap.put("3777472", "Body weight (measured)")
		vitalTitleMap.put("", "Body mass index")
		vitalTitleMap.put("703540", "Respiration rate")
		vitalTitleMap.put("7935038", "Heart Rate")
		vitalTitleMap.put("8238766", "Oxygen saturation")
		vitalTitleMap.put("8713424", "Body temperature")
		vitalTitleMap.put("703501", "Systolic blood pressure")
		vitalTitleMap.put("703516", "Diastolic blood pressure")
		
		vitalResourceMap = new HashMap()
		vitalResourceMap.put("15612799", "http://loinc.org/codes/8302-2")
		vitalResourceMap.put("3777472", "http://loinc.org/codes/3141-9")
		vitalResourceMap.put("", "http://loinc.org/codes/39156-5")
		vitalResourceMap.put("703540", "http://loinc.org/codes/9279-1")
		vitalResourceMap.put("7935038", "http://loinc.org/codes/8867-4")
		vitalResourceMap.put("8238766", "http://loinc.org/codes/2710-2")
		vitalResourceMap.put("8713424", "http://loinc.org/codes/8310-5")
		vitalResourceMap.put("703501", "http://loinc.org/codes/8480-6")
		vitalResourceMap.put("703516", "http://loinc.org/codes/8462-4")
		
		vitalUnitMap = new HashMap()
		vitalUnitMap.put("15612799", "m")
		vitalUnitMap.put("3777472", "kg")
		vitalUnitMap.put("", "{BMI}")
		vitalUnitMap.put("703540", "{breaths}")
		vitalUnitMap.put("7935038", "{beats}/min")
		vitalUnitMap.put("8238766", "%{HemoglobinSaturation}")
		vitalUnitMap.put("8713424", "Cel")
		vitalUnitMap.put("703501", "mm[Hg]")
		vitalUnitMap.put("703516", "mm[Hg]")
		
		vitalEventCodesSet = new HashSet()
		vitalEventCodesSet.add("15612799")
		vitalEventCodesSet.add("3777472")
		//vitalEventCodesSet.add("")
		vitalEventCodesSet.add("703540")
		vitalEventCodesSet.add("7935038")
		vitalEventCodesSet.add("8238766")
		vitalEventCodesSet.add("8713424")
		vitalEventCodesSet.add("703501")
		vitalEventCodesSet.add("703516")
		//vitalEventCodesSet.add("")
		vitalEventCodesSet.add("4099993")
		vitalEventCodesSet.add("4100005")
		
		bpEventCodesSet = new HashSet()
		bpEventCodesSet.add("703501")
		bpEventCodesSet.add("703516")
		//bpEventCodesSet.add("")
		bpEventCodesSet.add("4099993")
		bpEventCodesSet.add("4100005")
		
		
	}
	
	def makeCall(recordId, moURL){
		
		transaction = 'ReadEncountersByFilters'
		targetServlet = 'com.cerner.encounter.EncounterServlet'
		def requestXML = createRequest(recordId)
		def resp = makeRestCall(requestXML, moURL)
		readResponse(resp)
		
		//refresh the writer and builder objects
		writer = new StringWriter()
		builder = new MarkupBuilder(writer)
		
		transaction = 'ReadResultsMatrix'
		targetServlet = 'com.cerner.results.ResultsServlet'
		requestXML = createRequest(recordId)
		resp=makeRestCall(requestXML, moURL)
		readResponse(resp)
	}
	
	def readResponse(moResponse){
		def replyMessage = moResponse.getData()
		def payload= replyMessage.Payload
		if (transaction.equals("ReadEncountersByFilters")){
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
		}else{
			payload.ResultsMatrix.Row.each{ currentRow->
				currentRow.Cell.each{ currentCell ->
					def currentEncounterId = currentCell.ClinicalEvents.NumericResult.EncounterId.text()
					def currentEventCode = currentCell.ClinicalEvents.NumericResult.EventCode.Value.text()
					def currentValue = currentCell.ClinicalEvents.NumericResult.Value.text()
					def currentEventTag = currentCell.ClinicalEvents.NumericResult.EventTag.text()
					
					currentValue=convertValue(currentValue, currentEventCode)
					
					if((currentEventCode!=null) && (vitalEventCodesSet.contains(currentEventCode)) && valueIsValid(currentValue)){					
						VitalSign vitalSign = new VitalSign()
						vitalSign.setValue(currentValue)
						vitalSign.setCode(currentEventCode)
						vitalSign.setType(vitalTypeMap.get(currentEventCode))
						vitalSign.setTitle(vitalTitleMap.get(currentEventCode))
						vitalSign.setResource(vitalResourceMap.get(currentEventCode))
						vitalSign.setUnit(vitalUnitMap.get(currentEventCode))
						(bpEventCodesSet.contains(currentEventCode))?vitalSign.setIsBPField(true):vitalSign.setIsBPField(false)
						
						vitalSignsMap.get(currentEncounterId).vitalSignList.add(vitalSign)
					}
				}
			}	
		}
		return new Vitals(vitalSignsMap)
	}
	
	def generatePayload(recordId){
		if (transaction.equals("ReadEncountersByFilters")){
			builder.PersonId(recordId)
			builder.BypassOrganizationSecurityIndicator('true')
		}else{
			builder.ResultsMatrixProperties(){
				Structure('Table')
				TimeGrouping('Actual')
				TimeSort('Chronological')
			}
			builder.ResultsSearchCriteria(){
				CountSearch(){
					PersonId(recordId)
					EventCount('999')
				}
			}
		}
	}
	
	def convertValue(currentValue, currentEventCode){
		if (currentEventCode.equals("15612799")){
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
