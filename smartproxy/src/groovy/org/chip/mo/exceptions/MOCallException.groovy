package org.chip.mo.exceptions

/**
* MOCallException.groovy
* Purpose: Generic exception thrown when the Millennium Object server returns an unexpected response.
* Since the exception is thrown by the application in response to a response parsing error or invalid response status the exceptionMessage, statusCode and rootCause should be provided explicitely.
* The RecordsController catches the exception and uses it to render an appropriate response.
* @author mkapoor
* @version Jun 19, 2012 12:53:03 PM
*/
class MOCallException extends Exception {
	
	/**
	 * The exception message to be displayed to the end user.
	 */
	private String exceptionMessage
	/**
	 * The status code the SMART container looks at to determine the status of the request it made to the app.
	 * e.g. The app returns a 404 when no data is found for a recordId even though the Millennium Object server returns a 200 status code.  
	 */
	private int statusCode
	/**
	 * The underlying cause of the problem as communicated by the Millennium Object server, or the exception that occured when parsing the MO resonse.r
	 */
	private String rootCause
	
	public MOCallException(exceptionMessage, statusCode, rootCause){
		this.exceptionMessage = exceptionMessage
		this.statusCode = statusCode
		this.rootCause = rootCause
	}
	
	public String toString(){
		return message
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getRootCause() {
		return rootCause;
	}

	public void setRootCause(String rootCause) {
		this.rootCause = rootCause;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}

}
