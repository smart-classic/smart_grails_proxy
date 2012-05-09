package org.chip.mo.exceptions

class InvalidRequestException extends Exception {
	private String exceptionMessage
	private int statusCode
	private String rootCause
	
	public InvalidRequestException(exceptionMessage, statusCode, rootCause){
		this.exceptionMessage = exceptionMessage
		this.statusCode = statusCode
		this.rootCause = rootCause
	}
}
