package org.chip.mo

import groovy.xml.MarkupBuilder;
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil
import groovyx.net.http.*

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chip.mo.exceptions.MOCallException
import org.chip.mo.exceptions.InvalidRequestException;

abstract class MilleniumObjectCall {
	
	private static final Log log = LogFactory.getLog(this)

	def writer
	def builder
	def transaction
	def targetServlet
	
	protected Map<String,Object> requestParams = new HashMap()
	
	protected static final String RECORDIDPARAM = "RECORDIDPARAM"
	protected static final String MO_RESPONSE_PARAM = "MO_RESPONSE"
	
	private static final String MO_RESP_STATUS_NODATA="NoData"
	private static final String MO_RESP_STATUS_ERROR="Error"
	private static final String MO_RESP_STATUS_SUCCESS="Success"
	
	private static Map responseErrorMessageMap
	private static Map responseErrorStatusCodeMap
	
	static{
		responseErrorMessageMap = new HashMap<String, String>()
		responseErrorMessageMap.put(MO_RESP_STATUS_ERROR, "MO Server returned Error for Record ID: ")
		
		responseErrorStatusCodeMap = new HashMap<String, Integer>()
		responseErrorStatusCodeMap.put(MO_RESP_STATUS_ERROR, 500)
		responseErrorStatusCodeMap.put(MO_RESP_STATUS_NODATA, 404)
	}
	
	def init(){
		writer = new StringWriter()
		builder = new MarkupBuilder(writer)
	}
	
	def makeCall(recordId, moUrl, moResponse){
		requestParams.put(MO_RESPONSE_PARAM, moResponse);
		makeCall(recordId, moUrl)
	}
	
	/**
	 * The Workhorse method.
	 * -Creates the outgoing MO request.
	 * -calls makeRestCall to make the actual MO call.
	 * -calls readResponse to create an RDF Model from the MO response.
	 * @param recordId
	 * @param moURL
	 * @return
	 */
	def makeCall(recordId, moURL) throws MOCallException{
		requestParams.put(RECORDIDPARAM, recordId)
		def resp
		try{
			def requestXML = createRequest()
		
			resp = makeRestCall(requestXML, moURL)
			
			handleExceptions(resp, recordId)
		} catch (InvalidRequestException ire){
			log.error(ire.exceptionMessage +" for "+ recordId +" because " + ire.rootCause)
		} 
		readResponse(resp)
	}
	
	def handleExceptions(resp, recordId)throws MOCallException{
		def replyMessage = resp.getData()
		def status= replyMessage.Status.text()
		def isResponseError = false
		if( status != MO_RESP_STATUS_SUCCESS && 
			status != MO_RESP_STATUS_NODATA ){
			def errorMessage = responseErrorMessageMap.get(status)
			def statusCode = responseErrorStatusCodeMap.get(status)
			if(errorMessage==null){
				errorMessage = "Unexpected response from MO "
				statusCode = 500
			}
			throw new MOCallException(errorMessage+ recordId, statusCode, "MO Response returned status of "+status)
		}
	}
	
	def createRequest(){
		builder.RequestMessage(){
			TransactionName(transaction)
			Payload(){
				generatePayload()
			}
		}
		return writer.toString()
	}
	
	/**
	 * Overriden by the extending class to provide transaction specific functionality
	 * @param requestParams
	 * @return
	 */
	def abstract generatePayload()
	
	def makeRestCall(requestXML, moURL)throws MOCallException{
		def resp
		def restClient = new RESTClient(moURL+targetServlet)
		restClient.setContentType(ContentType.XML)
		resp=restClient.post(body:requestXML, requestContentType : ContentType.XML)
		return resp
	}

	/**
	 * Overriden by the extending class to provide transaction specific functionality
	 * @param resp
	 * @return
	 */
	def abstract readResponse(resp) throws MOCallException
}
