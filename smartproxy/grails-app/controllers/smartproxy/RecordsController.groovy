package smartproxy

class RecordsController {

    def index = { }
	
	def milleniumService
	def rdfService
	
	def medications = {
		def rdfOut = '<rdf:RDF xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"  xmlns:sp="http://smartplatforms.org/terms#"  xmlns:dcterms="http://purl.org/dc/terms/">   <sp:Medication>      <sp:drugName>        <sp:CodedValue>          <sp:code rdf:resource="http://link.informatics.stonybrook.edu/rxnorm/RXCUI/856845"/>          <dcterms:title>AMITRIPTYLINE HCL 50 MG TAB</dcterms:title>        </sp:CodedValue>      </sp:drugName>      <sp:strength>1400</sp:strength>      <sp:strengthUnit>mg</sp:strengthUnit>      <sp:dose>2</sp:dose>      <sp:doseUnit>{tbl}</sp:doseUnit>      <sp:startDate>2007-03-14</sp:startDate>      <sp:endDate>2007-08-14</sp:endDate>      <sp:instructions>Take two tablets twice daily as needed for pain</sp:instructions>   </sp:Medication></rdf:RDF>'
		response.outputStream << rdfOut
	}
	
	def demographics = {
		
		//read in the cerner person id
		def recordId=params['record_id']
		
		def moResponse = milleniumService.makeCall('demographics', recordId)
		def rdfResponse=rdfService.convert('demographics', moResponse)
		
		response.setContentType ('application/xml')
		response.outputStream << rdfResponse
	}
}
