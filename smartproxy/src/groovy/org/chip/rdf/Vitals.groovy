package org.chip.rdf

import org.chip.rdf.vitals.CodedValue;
import org.chip.rdf.vitals.VitalSign;
import groovy.xml.StreamingMarkupBuilder
import org.chip.rdf.vitals.*
import org.chip.utils.RandomIdGenerator;
import org.codehaus.groovy.grails.commons.ConfigurationHolder;

class Vitals extends Record {

	public final static String CODE_RDF_TYPE='http://smartplatforms.org/terms#Code'
	
	Set vitalSignsSet;
	Map nodeIdsByEncounter;
	
	public Vitals(vitalSignsSetIn){
		vitalSignsSet=vitalSignsSetIn
		nodeIdsByEncounter=new HashMap()
	}
	
	def toRDF(){
		//long l1 = new Date().getTime()
		//return rdfOut
		def config = ConfigurationHolder.config
		def belongsToUrl = config.oauth.smartEmr.apiBase
		belongsToUrl += '/records/'
		
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
			mkp.declareNamespace('spcode':'http://smartplatforms.org/terms/codes/')
			'rdf:RDF'(){
				vitalSignsSet.each{ vitalSigns ->
						'sp:VitalSigns'(){
							'sp:belongsTo'('rdf:resource':belongsToUrl+vitalSigns.getBelongsTo())
							'dcterms:date'(vitalSigns.getDate())
							createEncounter(belongsToUrl, rdfBuilder, vitalSigns.getEncounter())
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
					
					'sp:bodyPosition'(){
						createCodedValue(rdfBuilder, bloodPressure.bodyPosition)
					}
					
					'sp:bodySite'(){
						createCodedValue(rdfBuilder, bloodPressure.bodySite)
					}
					
					'sp:method'(){
						createCodedValue(rdfBuilder, bloodPressure.method)
					}
				}
			}
		}
	}
					  
	def createVitalSign(rdfBuilder, VitalSign vitalSign, String type){
		if(vitalSign!=null){
			 rdfBuilder."sp:${type}"(){
			  'sp:VitalSign'(){
				  'sp:vitalName'(){
					  createCodedValue(rdfBuilder, vitalSign.vitalName)
				  }
				  'sp:value'(vitalSign.value)
			  'sp:unit'(vitalSign.unit)
			  }
			}
		}
	}

	def createCodedValue(rdfBuilder, CodedValue codedValue){
		if(codedValue!=null){
			rdfBuilder.'sp:CodedValue'(){
				'dcterms:title'(codedValue.title)
				createCode(rdfBuilder, codedValue.getCode())
				//'sp:code'('rdf:resource':codedValue.code)
			}
		}
	}
	
	def createCode(rdfBuilder, Code code){
		rdfBuilder.'sp:code'(){
			"spcode:${code.type}"('rdf:about':code.system+code.identifier){
				'rdf:type'('rdf:resource':CODE_RDF_TYPE)
				'dcterms:title'(code.title)
				'sp:system'(code.system)
				'dcterms:identifier'(code.identifier)
			}
		}
	}
	
	def createEncounter(belongsToUrl, rdfBuilder, Encounter encounter){
		if(nodeIdsByEncounter.keySet().contains(encounter)){
			def rdfNodeId=nodeIdsByEncounter.get(encounter)
			rdfBuilder.'sp:encounter'('rdf:nodeID':rdfNodeId)
		}else{
			def rdfNodeId=RandomIdGenerator.generateString(9)
			nodeIdsByEncounter.put(encounter, rdfNodeId)
			rdfBuilder.'sp:encounter'(){
				'sp:Encounter'('rdf:nodeID':rdfNodeId){
					'sp:belongsTo'('rdf:resource':belongsToUrl+encounter.getBelongsTo())
					'sp:startDate'(encounter.getStartDate())
					'sp:endDate'(encounter.getEndDate())
					if(encounter.encounterType.code!=null && encounter.encounterType.code!=""){
						'sp:encounterType'(){
							createCodedValue(rdfBuilder, encounter.encounterType)
						}
					}
				}
			}
		}
	}
}