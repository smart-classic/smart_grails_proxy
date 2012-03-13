package org.chip.rdf

import org.chip.rdf.vitals.CodedValue;
import org.chip.rdf.vitals.VitalSign;
import groovy.xml.StreamingMarkupBuilder
import org.chip.rdf.vitals.*

class Vitals extends Record {
	
	public Vitals(vitalSignsSetIn){
		vitalSignsSet=vitalSignsSetIn
	}
	
	Set vitalSignsSet;

	def toRDF(){
		//long l1 = new Date().getTime()
		//return rdfOut
		def builder = new StreamingMarkupBuilder()
		builder.encoding="UTF-8"
		def writer = new StringWriter()
		writer<<builder.bind{rdfBuilder->
			mkp.xmlDeclaration()
			mkp.declareNamespace('rdf':'http://www.w3.org/1999/02/22-rdf-syntax-ns#')
			mkp.declareNamespace('sp':'http://smartplatforms.org/terms#')
			mkp.declareNamespace('foaf':'http://xmlns.com/foaf/0.1/')
			mkp.declareNamespace('dc':'http://purl.org/dc/elements/1.1/')
			mkp.declareNamespace('dcterms':'http://purl.org/dc/terms/')
			mkp.declareNamespace('v':'http://www.w3.org/2006/vcard/ns#')
			'rdf:RDF'(){
				vitalSignsSet.each{ vitalSigns ->
						'sp:VitalSigns'(){
							'dcterms:date'(vitalSigns.getDate())
							createEncounter(rdfBuilder, vitalSigns.getEncounter())
							createVitalSign(rdfBuilder, vitalSigns.height, 'height')
							createVitalSign(rdfBuilder, vitalSigns.weight, 'weight')
							createVitalSign(rdfBuilder, vitalSigns.bodyMassIndex, 'bodyMassIndex')
							createVitalSign(rdfBuilder, vitalSigns.respiratoryRate, 'respiratoryRate')
							createVitalSign(rdfBuilder, vitalSigns.heartRate, 'heartRate')
							createVitalSign(rdfBuilder, vitalSigns.oxygenSaturation, 'oxygenSaturation')
							createVitalSign(rdfBuilder, vitalSigns.temperature, 'temperature')
							createBloodPressure(rdfBuilder, vitalSigns.bloodPressure)
						}
				}
			}
		}
					
		//long l2 = new Date().getTime()
		//println("creating rdf took : "+(l2-l1)/1000)
		

		writer.toString()
	}

	def createBloodPressure(rdfBuilder, bloodPressure){
		if(bloodPressure!=null){
			rdfBuilder.'sp:bloodPressure'(){
				'sp:BloodPressure'(){
					createVitalSign(rdfBuilder, bloodPressure.diastolic, 'diastolic')
					createVitalSign(rdfBuilder, bloodPressure.systolic, 'systolic')
					createCodedValue(rdfBuilder, bloodPressure.bodyPosition, 'bodyPosition')
					createCodedValue(rdfBuilder, bloodPressure.bodySite, 'bodySite')
					createCodedValue(rdfBuilder, bloodPressure.method, 'method')
				}
			}
		}
	}
					  
	def createVitalSign(rdfBuilder, VitalSign vitalSign, String type){
		if(vitalSign!=null){
			 rdfBuilder."sp:${type}"(){
			  'sp:VitalSign'(){
				  'sp:vitalName'(){
					  'sp:CodedValue'(){
						  'sp:code'('rdf:resource':vitalSign.vitalName.code)
						  'dcterms:title'(vitalSign.vitalName.title)
					  }
				  }
				  'sp:value'(vitalSign.value)
			  'sp:unit'(vitalSign.unit)
			  }
			 }
		}
	}
	
	def createCodedValue(rdfBuilder, CodedValue codedValue, String type){
		if(codedValue!=null){
			rdfBuilder."sp:${type}"(){
				'sp:CodedValue'(){
					'sp:code'('rdf:resource':codedValue.code)
					'dcterms:title'(codedValue.title)
				}
			}
		}
	}
	
	def createEncounter(rdfBuilder, Encounter encounter){
		/*if(encounterElementCount==0){
			encounterElementCount++
		*/
			rdfBuilder.'sp:encounter'(){
					/*'sp:Encounter'('rdf:nodeID':encounter.getId()){*/
					'sp:Encounter'(){
						'sp:startDate'(encounter.getStartDate())
						'sp:endDate'(encounter.getEndDate())
						if(encounter.encounterType.code!=null && encounter.encounterType.code!=""){
							'sp:encounterType'(){
								'sp:CodedValue'(){
									'sp:code'('rdf:resource':encounter.encounterType.code)
									'dcterms:title'(encounter.encounterType.title)
								}
							}
						}
					}
				}
		/*}
		else{
			'sp:encounter'('rdf:nodeID':encounter.getId())
		}*/
	}
}