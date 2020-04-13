package com.oracle.notebook.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Invalid Request Format")
public class InvalidRequestFormatException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param message
	 */
	public InvalidRequestFormatException(String message) {
        super(message);
    }
}
