package org.chip.rdf

import groovy.xml.StreamingMarkupBuilder
import org.chip.rdf.vitals.*

class Vitals extends Record {

	def toRDF(){
		//return rdfOut
		def builder = new StreamingMarkupBuilder()
		builder.encoding="UTF-8"
		def rdfBuilder = {
			mkp.xmlDeclaration()
			mkp.declareNamespace(rdf:'http://www.w3.org/1999/02/22-rdf-syntax-ns#')
			mkp.declareNamespace('sp':'http://smartplatforms.org/terms#')
			mkp.declareNamespace('foaf':'http://xmlns.com/foaf/0.1/')
			mkp.declareNamespace('dc':'http://purl.org/dc/elements/1.1/')
			mkp.declareNamespace('dcterms':'http://purl.org/dc/terms/')
			mkp.declareNamespace('v':'http://www.w3.org/2006/vcard/ns#')
			'rdf:RDF'(){
				'sp:VitalSigns'(){
				/*'dc:date'('2006-07-31T07:14:14.429965')
					createEncounter('2006-07-31T07:14:14.429965', '2006-07-31T07:14:14.429965', 
						'http://smartplatforms.org/terms/code/encounterType#ambulatory', 'Ambulatory Encounter')
					createVital('height', 'Height (measured)', '1.80', 'http://loinc.org/codes/8302-2', 'm')
					createVital('weight', 'Body weight (measured)', '70.8', 'http://loinc.org/codes/3141-9', 'kg' )
					createVital('bodyMassIndex', 'Body mass index', '21.8', 'http://loinc.org/codes/39156-5', '{BMI}')
					createVital('respiratoryRate', 'Respiration rate', '16', 'http://loinc.org/codes/9279-1', '{breaths}')
					createVital('heartRate', 'Heart Rate', '70', 'http://loinc.org/codes/8867-4', '{beats}/min')
					createVital('oxygenSaturation', 'Oxygen saturation', '99', 'http://loinc.org/codes/2710-2', '%{HemoglobinSaturation}')
					createVital('temperature', 'Body temperature', '37', 'http://loinc.org/codes/8310-5', 'Cel')
					'sp:bloodPressure'(){
						'sp:BloodPressure'(){
							createVital('systolic', 'Systolic blood pressure', '90.4221588376', 'http://loinc.org/codes/8480-6', 'mm[Hg]')
							createVital('diastolic', 'Diastolic blood pressure', '43.2299834277', 'http://loinc.org/codes/8462-4', 'mm[Hg]')
							createVital('bodyPosition', 'Sitting', null, 'http://www.ihtsdo.org/snomed-ct/concepts/33586001', null)
							createVital('bodySite', 'Left thigh', null, 'http://www.ihtsdo.org/snomed-ct/concepts/61396006', null)
							createVital('method', 'Auscultation', null, 'http://smartplatforms.org/terms/code/bloodPressureMethod#auscultation', null)
						}
					}*/
					
					'dc:date'('2006-07-31T07:14:14.429965')
					createEncounter('2006-07-31T07:14:14.429965', '2006-07-31T07:14:14.429965',
						'http://smartplatforms.org/terms/code/encounterType#ambulatory', 'Ambulatory Encounter')
					createVital('height', 'Height (measured)', '1.80', 'http://loinc.org/codes/8302-2', 'm')
					createVital('weight', 'Body weight (measured)', '70.8', 'http://loinc.org/codes/3141-9', 'kg' )
					createVital('bodyMassIndex', 'Body mass index', '21.8', 'http://loinc.org/codes/39156-5', '{BMI}')
					createVital('respiratoryRate', 'Respiration rate', '16', 'http://loinc.org/codes/9279-1', '{breaths}')
					createVital('heartRate', 'Heart Rate', '70', 'http://loinc.org/codes/8867-4', '{beats}/min')
					createVital('oxygenSaturation', 'Oxygen saturation', '99', 'http://loinc.org/codes/2710-2', '%{HemoglobinSaturation}')
					createVital('temperature', 'Body temperature', '37', 'http://loinc.org/codes/8310-5', 'Cel')
					'sp:bloodPressure'(){
						'sp:BloodPressure'(){
							createVital('systolic', 'Systolic blood pressure', '90.4221588376', 'http://loinc.org/codes/8480-6', 'mm[Hg]')
							createVital('diastolic', 'Diastolic blood pressure', '43.2299834277', 'http://loinc.org/codes/8462-4', 'mm[Hg]')
							createVital('bodyPosition', 'Sitting', null, 'http://www.ihtsdo.org/snomed-ct/concepts/33586001', null)
							createVital('bodySite', 'Left thigh', null, 'http://www.ihtsdo.org/snomed-ct/concepts/61396006', null)
							createVital('method', 'Auscultation', null, 'http://smartplatforms.org/terms/code/bloodPressureMethod#auscultation', null)
						}
					}
				}
			}
		}
					
		def writer = new StringWriter()
		writer<<builder.bind(rdfBuilder)
		writer.toString()
	}
				  
	def createVital(type, title, value, resource, unit){
	 'sp:${type}'(){
	  'sp:VitalSign'(){
		  'sp:vitalName'(){
			  'sp:CodedValue'(){
				  'sp:code'('rdf:resource':resource)
				  'dcterms:title'(title)
			  }
		  }
		  'sp:value'(value)
	  'sp:unit'(unit)
	  }
	 }
	}
	
	def createEncounter(startDate, endDate, encounterResourceCode, encounterTitle){
		'sp:encounter'(){
			'sp:Encounter'(){
				'sp:startDate'(startDate)
				'sp:endDate'(endDate)
				'sp:encounterType'(){
					'sp:CodedValue'(){
						'sp:code'('rdf:resource':encounterResourceCode)
						'dcterms:title'(encounterTitle)
					}
				}
			}
		}
	}
}
