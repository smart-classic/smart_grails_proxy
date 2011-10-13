package org.chip.rdf

import groovy.xml.StreamingMarkupBuilder
import org.chip.rdf.vitals.*

class Vitals extends Record {
	
	public Vitals(vitalSignsMapIn){
		vitalSignsMap=vitalSignsMapIn
	}
	
	/**
	 * Maps each Encounter Id to an instance of the VitalSigns Object.
	 * VitalSigns contains 
	 * 	- An Encounter Object which stores details about a particular Encounter.
	 * 	- A vitalSignMap which maps each Parent Event Id to a list of VitalSign Objects.
	 * VitalSign contains details about a particular Vital measure (e.g. diastolic bp)
	 */
	Map<String, VitalSigns> vitalSignsMap;

	/**
	 * iterate through the list of encounter ids (key set for vitalSignsMap)
	 * for each vitalSignMap retrieved (belonging to current encounter id)
	 * *iterate through the list of parent event ids (key set for vitalSignMap)
	 * 	• for each vitalsign list retrieved (belonging to current parent id)
	 * 		∘ go through all non bp fields and create vitalsign elements
	 *  	∘ go through all bp fields and
	 *	 		‣ if coded field then generate appropriate rdf
	 *	 		‣ if not coded field then generate appropriate rdf
	 */
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
				Set vitalSignsKeySet = vitalSignsMap.keySet()
				vitalSignsKeySet.each{ vitalSignsKey ->
					def encounterElementCount =0
					
					VitalSigns vitalSigns = vitalSignsMap.get(vitalSignsKey)
					Map<String, VitalSign> vitalSignMap = vitalSigns.getVitalSignMap()
					Set parentEventIdSet=vitalSignMap.keySet()
					parentEventIdSet.each{parentEventId->
						//'sp:VitalSigns'(parentEventId:parentEventId){
						'sp:VitalSigns'(){
							Encounter encounter = vitalSigns.getEncounter()
							'dcterms:date'(encounter.getStartDate())
							//createEncounter(encounter.getStartDate(), encounter.getEndDate(), encounter.getResource(), encounter.getTitle())
							encounterElementCount++
							if(encounterElementCount==1){
							'sp:encounter'(){
								'sp:Encounter'('rdf:nodeID':encounter.getId()){
								//'sp:Encounter'(){
									'sp:startDate'(encounter.getStartDate())
									'sp:endDate'(encounter.getEndDate())
									if(encounter.getResource()!=null && encounter.getResource()!=""){
									'sp:encounterType'(){
										'sp:CodedValue'(){
											'sp:code'('rdf:resource':encounter.getResource())
											'dcterms:title'(encounter.getTitle())
										}
									}
									}
								}
								}
							}else{
							'sp:encounter'('rdf:nodeID':encounter.getId())
							}
							
							boolean hasBPFields = false
							List<VitalSign> vitalSignList = vitalSigns.getVitalSignMap().get(parentEventId)
							vitalSignList.each { vitalSign-> 
								if (!vitalSign.isBPField){
									def type = vitalSign.getType()
									//createVital(vitalSign.getType(), vitalSign.getTitle(), vitalSign.getValue(), vitalSign.getResource(), vitalSign.getUnit())
									"sp:${type}"(){
										'sp:VitalSign'(){
											'sp:vitalName'(){
												'sp:CodedValue'(){
													'sp:code'('rdf:resource':vitalSign.getResource())
													'dcterms:title'(vitalSign.getTitle())
												}
											}
											'sp:value'(vitalSign.getValue())
										'sp:unit'(vitalSign.getUnit())
										}
									}
								}else{
									hasBPFields = true
								}
							}
	
							if(hasBPFields){
								'sp:bloodPressure'(){
								'sp:BloodPressure'(){
									vitalSignList.each { vitalSign->
										if (vitalSign.isBPField){
											//createVital(vitalSign.getType(), vitalSign.getTitle(), vitalSign.getValue(), vitalSign.getResource(), vitalSign.getValue())
											def type = vitalSign.getType()
											"sp:${type}"(){
												if(vitalSign.isCodedField){
													'sp:CodedValue'(){
														'sp:code'('rdf:resource':vitalSign.getResource())
														'dcterms:title'(vitalSign.getTitle())
													}
												}else{
													'sp:VitalSign'(){
														'sp:vitalName'(){
															'sp:CodedValue'(){
																'sp:code'('rdf:resource':vitalSign.getResource())
																'dcterms:title'(vitalSign.getTitle())
															}
														}
														'sp:value'(vitalSign.getValue())
													'sp:unit'(vitalSign.getUnit())
													}
												}
											}
										}
									}
								}
								}
							}
						}
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
