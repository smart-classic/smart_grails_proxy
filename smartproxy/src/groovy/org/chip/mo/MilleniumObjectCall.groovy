package org.chip.mo

import groovy.xml.MarkupBuilder;
import groovyx.net.http.*
abstract class MilleniumObjectCall {

	def writer
	def builder
	def transaction
	def targetServlet
	
	def init(){
		writer = new StringWriter()
		builder = new MarkupBuilder(writer)
	}
	
	def makeCall(recordId, moURL){
		def requestXML = createRequest(recordId)
		
		def resp = makeRestCall(requestXML, moURL)
		
		readResponse(resp)
	}
	
	def createRequest(recordId){
		builder.RequestMessage(){
			TransactionName(transaction)
			Payload(){
				generatePayload(recordId)
			}
		}
		return writer.toString()
	}
	
	def abstract generatePayload(recordId)
	
	def makeRestCall(requestXML, moURL){
		def restClient = new RESTClient(moURL+targetServlet)
		restClient.setContentType(ContentType.XML)
		def resp=restClient.post(body:requestXML, requestContentType : ContentType.XML)
		return resp
	}

	def abstract readResponse(resp)
}
