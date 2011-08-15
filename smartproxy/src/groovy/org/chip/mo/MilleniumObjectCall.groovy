package org.chip.mo

import groovy.xml.MarkupBuilder;
import groovyx.net.http.*
abstract class MilleniumObjectCall {

	def writer
	def builder
	def transaction
	def targetServlet
	
	protected static final String RECORDIDPARAM = "RECORDIDPARAM"
	
	def init(){
		writer = new StringWriter()
		builder = new MarkupBuilder(writer)
	}
	
	def makeCall(recordId, moURL){
		Map<String,Object> requestParams = new HashMap()
		requestParams.put(RECORDIDPARAM, recordId)
		def requestXML = createRequest(requestParams)
		
		def resp = makeRestCall(requestXML, moURL)
		
		readResponse(resp)
	}
	
	def createRequest(requestParams){
		builder.RequestMessage(){
			TransactionName(transaction)
			Payload(){
				generatePayload(requestParams)
			}
		}
		return writer.toString()
	}
	
	def abstract generatePayload(requestParams)
	
	def makeRestCall(requestXML, moURL){
		def restClient = new RESTClient(moURL+targetServlet)
		restClient.setContentType(ContentType.XML)
		def resp=restClient.post(body:requestXML, requestContentType : ContentType.XML)
		println(resp)
		return resp
	}

	def abstract readResponse(resp)
}
