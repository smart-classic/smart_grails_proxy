package org.chip.rdf

import groovy.xml.StreamingMarkupBuilder

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
					'dc:date'('2006-07-31T07:14:14.429965')
					'sp:encounter'(){
					'sp:Encounter'(){
						'sp:startDate'('2006-07-31T07:14:14.429965')
						'sp:endDate'('2006-07-31T07:14:14.429965')
						'sp:encounterType'(){
							'sp:CodedValue'(){
								'sp:code'('rdf:resource':'http://smartplatforms.org/terms/code/encounterType#ambulatory')
								'dcterms:title'('Ambulatory Encounter')
							}
						}
					}
					}
					'sp:height'(){
						'sp:VitalSign'(){
							'sp:vitalName'(){
								'sp:CodedValue'(){
									'sp:code'('rdf:resource':'http://loinc.org/codes/8302-2')
									'dcterms:title'('Height (measured)')
								}
							}
							'sp:value'('1.80')
							'sp:unit'('m')
						}
					}
					'sp:bloodPressure'(){
						'sp:BloodPressure'(){
							   'sp:systolic'(){
								 'sp:VitalSign'(){
								  'sp:vitalName'(){
								   'sp:CodedValue'(){
									 'sp:code'('rdf:resource':"http://loinc.org/codes/8480-6")
									 'dcterms:title'('Systolic blood pressure')
								   }
								  }
								 'sp:value'('90.4221588376')
								 'sp:unit'('mm[Hg]')
								 }
							   }
							   'sp:diastolic'(){
								 'sp:VitalSign'(){
								  'sp:vitalName'(){
								   'sp:CodedValue'(){
									 'sp:code'('rdf:resource':'http://loinc.org/codes/8462-4')
									 'dcterms:title'('Diastolic blood pressure')
								   }
								  }
								 'sp:value'('43.2299834277')
								 'sp:unit'('mm[Hg]')
								 }
							   }
							  'sp:bodyPosition'(){
								 'sp:CodedValue'(){
								   'sp:code'('rdf:resource':'http://www.ihtsdo.org/snomed-ct/concepts/33586001')
								   'dcterms:title'('Sitting')
								   }
							  }
							   'sp:bodySite'(){
								 'sp:CodedValue'(){
								   'sp:code'('rdf:resource':'http://www.ihtsdo.org/snomed-ct/concepts/61396006')
								   'dcterms:title'('Left thigh')
								 }
							   }
							   'sp:method'(){
								 'sp:CodedValue'(){
								   'sp:code'('rdf:resource':'http://smartplatforms.org/terms/code/bloodPressureMethod#auscultation')
								   'dcterms:title'('Auscultation')
								 }
							   }
						}
					}
				}
			}
		}
		
		def writer = new StringWriter()
		writer<<builder.bind(rdfBuilder)
		writer.toString()
	}
}
