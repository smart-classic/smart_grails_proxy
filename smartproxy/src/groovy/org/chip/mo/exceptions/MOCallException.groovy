package org.chip.mo.exceptions

class MOCallException extends Exception {
	
	private String exceptionMessage
	private int statusCode
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
