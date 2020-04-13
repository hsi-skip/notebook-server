package com.oracle.notebook.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Request Long Time Execute")
public class TimeOutException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param message
	 */
	public TimeOutException(String message) {
        super(message);
    }
}
