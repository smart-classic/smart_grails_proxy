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
		def rdfBuilder = {
			mkp.xmlDeclaration()
			mkp.declareNamespace(rdf:'http://www.w3.org/1999/02/22-rdf-syntax-ns#')
			mkp.declareNamespace('sp':'http://smartplatforms.org/terms#')
			mkp.declareNamespace('foaf':'http://xmlns.com/foaf/0.1/')
			mkp.declareNamespace('dc':'http://purl.org/dc/elements/1.1/')
			mkp.declareNamespace('dcterms':'http://purl.org/dc/terms/')
			mkp.declareNamespace('v':'http://www.w3.org/2006/vcard/ns#')
			'rdf:RDF'(){
				vitalSignsSet.each{ vitalSigns ->
						'sp:VitalSigns'(){
							'dcterms:date'(vitalSigns.getDate())
							createEncounter(vitalSigns.getEncounter())
							createVitalSign(vitalSigns.height, 'height')
							createVitalSign(vitalSigns.weight, 'weight')
							createVitalSign(vitalSigns.bodyMassIndex, 'bodyMassIndex')
							createVitalSign(vitalSigns.respiratoryRate, 'respiratoryRate')
							createVitalSign(vitalSigns.heartRate, 'heartRate')
							createVitalSign(vitalSigns.oxygenSaturation, 'oxygenSaturation')
							createVitalSign(vitalSigns.temperature, 'temperature')
							createBloodPressure(vitalSigns.bloodPressure)
						}
				}
			}
		}
					
		//long l2 = new Date().getTime()
		//println("creating rdf took : "+(l2-l1)/1000)
		
		def writer = new StringWriter()
		writer<<builder.bind(rdfBuilder)
		writer.toString()
	}

	def createBloodPressure(bloodPressure){
		createVitalSign(bloodPressure.diastolic, 'diastolic')
		createVitalSign(bloodPressure.systolic, 'systolic')
		createCodedValue(bloodPressure.bodyPosition, 'bodyPosition')
		createCodedValue(bloodPressure.bodySite, 'bodySite')
		createCodedValue(bloodPressure.method, 'method')
	}
					  
	def createVitalSign(VitalSign vitalSign, String type){
	 'sp:${type}'(){
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
	
	def createCodedValue(CodedValue codedValue, String type){
		"sp:${type}"(){
			'sp:CodedValue'(){
				'sp:code'('rdf:resource':codedValue.code)
				'dcterms:title'(codedValue.title)
			}
		}
	}
	
	def createEncounter(Encounter encounter){
		/*if(encounterElementCount==0){
			encounterElementCount++
		*/
			'sp:encounter'(){
					/*'sp:Encounter'('rdf:nodeID':encounter.getId()){*/
					'sp:Encounter'(){
						'sp:startDate'(encounter.getStartDate())
						'sp:endDate'(encounter.getEndDate())
						if(encounter.encounterType.code()!=null && encounter.encounterType.code!=""){
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