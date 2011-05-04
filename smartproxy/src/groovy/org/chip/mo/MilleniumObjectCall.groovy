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
	def abstract readResponse(moResponse)
}
