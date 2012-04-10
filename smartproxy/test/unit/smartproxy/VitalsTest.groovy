package smartproxy

import java.util.Map;

import grails.test.*

import org.chip.rdf.Vitals;
import org.chip.readers.EventsReader;
import org.chip.managers.VitalSignsManager;
import org.chip.mo.EncountersCall;
import org.chip.mo.model.Event;
import org.chip.rdf.vitals.VitalSigns

import org.codehaus.groovy.grails.commons.ConfigurationHolder;

class VitalsTest extends GrailsUnitTestCase {
	private def config
	
	protected void setUp() {
		super.setUp()
		//mock the config
		mockConfig('''
			cerner{
					mo{
						eventCode{
							EVENTCODEHEIGHT="2700653"
							EVENTCODEWEIGHT="2700654"
							EVENTCODERRATE="703540"
							EVENTCODEHEARTRATE="7935038"
							EVENTCODEOSAT="8238766"
							EVENTCODETEMP="8713424"
							EVENTCODESYS="703501"
							EVENTCODEDIA="703516"
							EVENTCODELOCATION="4099993"
							EVENTCODEPOSITION="13488852"
							EVENTCODEBPMETHOD="4100005"
							EVENTCODESYSSUPINE="1164536"
							EVENTCODESYSSITTING="1164545"
							EVENTCODESYSSTANDING="1164548"
							EVENTCODEDIASUPINE="1164539"
							EVENTCODEDIASITTING="1164542"
							EVENTCODEDIASTANDING="1164551"
						}
						encounterResource{
							Outpatient= "http://smartplatforms.org/terms/codes/EncounterType#ambulatory"
							Emergency= "http://smartplatforms.org/terms/codes/EncounterType#emergency"
							Field= "http://smartplatforms.org/terms/codes/EncounterType#field"
							Home= "http://smartplatforms.org/terms/codes/EncounterType#home"
							Inpatient= "http://smartplatforms.org/terms/codes/EncounterType#inpatient"
							Virtual="http://smartplatforms.org/terms/codes/EncounterType#virtual"
						}
						encounterTitle{
							Outpatient="Ambulatory encounter"
							Emergency= "Emergency encounter"
							Field= "Field encounter"
							Home= "Home encounter"
							Inpatient= "Inpatient encounter"
							Virtual= "Virtual encounter"
						}
						vitalsType{
							EVENTCODEHEIGHT= "height"
							EVENTCODEWEIGHT= "weight"
							EVENTCODERRATE= "respiratoryRate"
							EVENTCODEHEARTRATE= "heartRate"
							EVENTCODEOSAT= "oxygenSaturation"
							EVENTCODETEMP= "temperature"
							EVENTCODESYS= "systolic"
							EVENTCODEDIA= "diastolic"
							EVENTCODEBPMETHOD= "method"
							EVENTCODELOCATION= "bodySite"
							EVENTCODEPOSITION= "bodyPosition"
						}
						vitalsTitle{		
							EVENTCODEHEIGHT= "Height (measured)"
							EVENTCODEWEIGHT= "Body weight (measured)"
							EVENTCODERRATE= "Respiration rate"
							EVENTCODEHEARTRATE= "Heart Rate"
							EVENTCODEOSAT= "Oxygen saturation"
							EVENTCODETEMP= "Body temperature"
							EVENTCODESYS= "Systolic blood pressure"
							EVENTCODEDIA= "Diastolic blood pressure"
						}
						
						vitalsTitleTagMap{
							Auscultation= "Auscultation"
							Palpation= "Palpation"
							Automated= "Machine"
							Invasive= "Invasive"
							Sitting= "Sitting"
							Standing= "Standing"
							Supine= "Supine"
							Left_upper= "Left arm"
							Right_upper="Right arm"
							Left_lower= "Left thigh"
							Right_lower= "Right thigh"
						}
						
						vitalResource{
							EVENTCODEHEIGHT= "http://loinc.org/codes/8302-2"
							EVENTCODEWEIGHT= "http://loinc.org/codes/3141-9"
							EVENTCODERRATE= "http://loinc.org/codes/9279-1"
							EVENTCODEHEARTRATE= "http://loinc.org/codes/8867-4"
							EVENTCODEOSAT= "http://loinc.org/codes/2710-2"
							EVENTCODETEMP= "http://loinc.org/codes/8310-5"
							EVENTCODESYS= "http://loinc.org/codes/8480-6"
							EVENTCODEDIA= "http://loinc.org/codes/8462-4"
						}
						
						vitalResourceTagMap{
							Auscultation= "http://smartplatforms.org/terms/codes/BloodPressureMethod#auscultation"
							Palpation= "http://smartplatforms.org/terms/codes/BloodPressureMethod#palpation"
							Automated= "http://smartplatforms.org/terms/codes/BloodPressureMethod#machine"
							Invasive= "http://smartplatforms.org/terms/codes/BloodPressureMethod#invasive"
							Sitting= "http://www.ihtsdo.org/snomed-ct/concepts/33586001" 
							Standing= "http://www.ihtsdo.org/snomed-ct/concepts/10904000"
							Supine= "http://www.ihtsdo.org/snomed-ct/concepts/40199007"
							Left_upper="http://www.ihtsdo.org/snomed-ct/concepts/368208006"
							Right_upper="http://www.ihtsdo.org/snomed-ct/concepts/368209003"
							Left_lower="http://www.ihtsdo.org/snomed-ct/concepts/61396006"
							Right_lower="http://www.ihtsdo.org/snomed-ct/concepts/11207009"
						}
						
						vitalUnits{
							EVENTCODEHEIGHT= "m"
							EVENTCODEWEIGHT= "kg"
							EVENTCODERRATE= "{breaths}"
							EVENTCODEHEARTRATE= "{beats}/min"
							EVENTCODEOSAT= "%{HemoglobinSaturation}"
							EVENTCODETEMP= "Cel"
							EVENTCODESYS= "mm[Hg]"
							EVENTCODEDIA= "mm[Hg]"
						}
						//values are of no importance to us in the bpEvents map. We are only interested in the keyset.
						//The keyset contains all the events which are a part of blood pressure
						bpEvents{
							EVENTCODESYS=""
							EVENTCODEDIA=""
							EVENTCODELOCATION=""
							EVENTCODEPOSITION=""
							EVENTCODEBPMETHOD=""
						}
					}
				}
		''')
		
		config = ConfigurationHolder.config
	}

	protected void tearDown() {
		super.tearDown()
	}
	
	void testVitals(){
		def ecm = config.cerner.mo.eventCode
		def	resultsTestData="C:\\repository\\smart\\Results.xml"
		def resultsByDateTestData="C:\\repository\\smart\\Results-Date.xml"
		def encountersTestData="C:\\repository\\smart\\Encounters.xml"
		
		def encountersById = processEncounters(encountersTestData)
		
		def eventsByParentEventId = processResults(resultsTestData)
		def vitals = createVitals(encountersById, eventsByParentEventId)	
		examineVitals(vitals)
		
		eventsByParentEventId = processResults(resultsByDateTestData)
		vitals = createVitals(encountersById, eventsByParentEventId)
		examineVitalsByDates(vitals)	
		
		//Tests to verify the dates for vitalsigns objects
		resultsTestData="C:\\repository\\smart\\ResultsTest.xml"
		eventsByParentEventId = processResults(resultsTestData)
		vitals = createVitals(encountersById, eventsByParentEventId)
		examineVitalSignsDates(vitals)
	}
	
	def processEncounters(encountersTestData){
		def encountersPayload=parseDataFile(encountersTestData)
		EncountersCall encountersCall= new EncountersCall()
		encountersCall.init()
		Map encountersById = encountersCall.processPayload(encountersPayload)
		return encountersById
	}
	
	def processResults(resultsTestData){
		def resultsPayload = parseDataFile(resultsTestData)
		EventsReader eventsReader = new EventsReader()
		eventsReader.processPayload(resultsPayload)
		Map eventsByParentEventId = eventsReader.getEvents()
		return eventsByParentEventId
	}
	
	def parseDataFile(dataFile){
		def xmlSlurper = new XmlSlurper()
		return xmlSlurper.parse(dataFile)
	}
	
	def createVitals(encountersById, eventsByParentEventId){
		VitalSignsManager vitalSignsManager = new VitalSignsManager()
		vitalSignsManager.recordEncounters(encountersById)
		vitalSignsManager.recordEvents(eventsByParentEventId)

		vitalSignsManager.processEvents()

		Vitals vitals = vitalSignsManager.getVitals()
		
		return vitals
	}
	
	void examineVitals(vitals){
		//Start examining the returned vitals
		assert vitals!=null

		assert vitals.vitalSignsSet.size()==1
		
		vitals.vitalSignsSet.each{vitalSigns->
			vitalSigns=(VitalSigns)vitalSigns
			assert vitalSigns.encounter.encounterType.code=="http://smartplatforms.org/terms/codes/EncounterType#ambulatory"
			assert vitalSigns.encounter.encounterType.title=="Ambulatory encounter"
			assert vitalSigns.encounter.startDate=="2011-03-30T19:54:00.000+01:00"
			assert vitalSigns.encounter.endDate=="2011-03-31T04:59:00.000+01:00"
			
			assert vitalSigns.bloodPressure.bodySite.title=="Right arm"
			assert vitalSigns.bloodPressure.bodySite.code=="http://www.ihtsdo.org/snomed-ct/concepts/368209003"
			
			assert vitalSigns.bloodPressure.bodyPosition.title=="Standing"
			assert vitalSigns.bloodPressure.bodyPosition.code=="http://www.ihtsdo.org/snomed-ct/concepts/10904000"
			
			assert vitalSigns.bloodPressure.method.title=="Palpation"
			assert vitalSigns.bloodPressure.method.code=="http://smartplatforms.org/terms/codes/BloodPressureMethod#palpation"
			
			assert vitalSigns.bloodPressure.systolic.value=="88"
			assert vitalSigns.bloodPressure.systolic.unit=="mm[Hg]"
			assert vitalSigns.bloodPressure.systolic.vitalName.title=="Systolic blood pressure"
			assert vitalSigns.bloodPressure.systolic.vitalName.code=="http://loinc.org/codes/8480-6"
			
			assert vitalSigns.bloodPressure.diastolic.value=="123"
			assert vitalSigns.bloodPressure.diastolic.unit=="mm[Hg]"
			assert vitalSigns.bloodPressure.diastolic.vitalName.title=="Diastolic blood pressure"
			assert vitalSigns.bloodPressure.diastolic.vitalName.code=="http://loinc.org/codes/8462-4"
			
			assert vitalSigns.heartRate.value=="88"
			assert vitalSigns.heartRate.unit=="{beats}/min"
			assert vitalSigns.heartRate.vitalName.title=="Heart Rate"
			assert vitalSigns.heartRate.vitalName.code=="http://loinc.org/codes/8867-4"
			
			assert vitalSigns.respiratoryRate.value=="88"
			assert vitalSigns.respiratoryRate.unit=="{breaths}"
			assert vitalSigns.respiratoryRate.vitalName.title=="Respiration rate"
			assert vitalSigns.respiratoryRate.vitalName.code=="http://loinc.org/codes/9279-1"
			
			assert vitalSigns.oxygenSaturation.value=="88"
			assert vitalSigns.oxygenSaturation.unit=="%{HemoglobinSaturation}"
			assert vitalSigns.oxygenSaturation.vitalName.title=="Oxygen saturation"
			assert vitalSigns.oxygenSaturation.vitalName.code=="http://loinc.org/codes/2710-2"
			
			assert vitalSigns.temperature.value=="29"
			assert vitalSigns.temperature.unit=="Cel"
			assert vitalSigns.temperature.vitalName.title=="Body temperature"
			assert vitalSigns.temperature.vitalName.code=="http://loinc.org/codes/8310-5"
			
			assert vitalSigns.height==null
			assert vitalSigns.weight==null
		}
	}
	
	def examineVitalsByDates(vitals){
		assert vitals!=null
		
		assert vitals.vitalSignsSet.size()==1
		
		vitals.vitalSignsSet.each{vitalSigns->
			vitalSigns=(VitalSigns)vitalSigns
			
			assert vitalSigns.encounter.encounterType.code=="http://smartplatforms.org/terms/codes/EncounterType#ambulatory"
			assert vitalSigns.encounter.encounterType.title=="Ambulatory encounter"
			assert vitalSigns.encounter.startDate=="2011-05-18T20:03:00.000+01:00"
			assert vitalSigns.encounter.endDate=="2011-05-19T04:59:00.000+01:00"
			
			assert vitalSigns.bloodPressure.bodySite.title=="Left arm"
			assert vitalSigns.bloodPressure.bodySite.code=="http://www.ihtsdo.org/snomed-ct/concepts/368208006"
			
			assert vitalSigns.bloodPressure.method.title=="Machine"
			assert vitalSigns.bloodPressure.method.code=="http://smartplatforms.org/terms/codes/BloodPressureMethod#machine"
			
			assert vitalSigns.bloodPressure.systolic.value=="168"
			assert vitalSigns.bloodPressure.systolic.unit=="mm[Hg]"
			assert vitalSigns.bloodPressure.systolic.vitalName.title=="Systolic blood pressure"
			assert vitalSigns.bloodPressure.systolic.vitalName.code=="http://loinc.org/codes/8480-6"
			
			assert vitalSigns.bloodPressure.diastolic.value=="100"
			assert vitalSigns.bloodPressure.diastolic.unit=="mm[Hg]"
			assert vitalSigns.bloodPressure.diastolic.vitalName.title=="Diastolic blood pressure"
			assert vitalSigns.bloodPressure.diastolic.vitalName.code=="http://loinc.org/codes/8462-4"
			
			assert vitalSigns.heartRate.value=="80"
			assert vitalSigns.heartRate.unit=="{beats}/min"
			assert vitalSigns.heartRate.vitalName.title=="Heart Rate"
			assert vitalSigns.heartRate.vitalName.code=="http://loinc.org/codes/8867-4"
			
			assert vitalSigns.respiratoryRate.value=="18"
			assert vitalSigns.respiratoryRate.unit=="{breaths}"
			assert vitalSigns.respiratoryRate.vitalName.title=="Respiration rate"
			assert vitalSigns.respiratoryRate.vitalName.code=="http://loinc.org/codes/9279-1"
			
			assert vitalSigns.oxygenSaturation.value=="100"
			assert vitalSigns.oxygenSaturation.unit=="%{HemoglobinSaturation}"
			assert vitalSigns.oxygenSaturation.vitalName.title=="Oxygen saturation"
			assert vitalSigns.oxygenSaturation.vitalName.code=="http://loinc.org/codes/2710-2"
			
			assert vitalSigns.temperature.value=="37.6"
			assert vitalSigns.temperature.unit=="Cel"
			assert vitalSigns.temperature.vitalName.title=="Body temperature"
			assert vitalSigns.temperature.vitalName.code=="http://loinc.org/codes/8310-5"
			
			
			assert vitalSigns.height==null
			assert vitalSigns.weight==null
			assert vitalSigns.bloodPressure.bodyPosition==null
		}
	}
	
	def examineVitalSignsDates(vitals){
		assert vitals!=null
		//assert vitals.vitalSignsSet.size()==1
		
		Set datesSet = new HashSet()
		vitals.vitalSignsSet.each{vitalSigns->
			vitalSigns=(VitalSigns)vitalSigns
			datesSet.add(vitalSigns.date)
		}
		
		assert datesSet.size()==9
		assert datesSet.contains("2011-05-25T12:14:00.000-04:00")
		assert datesSet.contains("2011-08-02T14:04:00.000-04:00")
		assert datesSet.contains("2011-08-08T13:33:00.000-04:00")
		assert datesSet.contains("2011-08-08T13:40:00.000-04:00")
		assert datesSet.contains("2011-08-08T13:37:00.000-04:00")
		assert datesSet.contains("2011-08-08T13:30:00.000-04:00")
		assert datesSet.contains("2011-08-08T13:54:00.000-04:00")
		assert datesSet.contains("2011-08-08T13:42:00.000-04:00")
		assert datesSet.contains("2011-05-25T12:25:00.000-04:00")
	}
}
