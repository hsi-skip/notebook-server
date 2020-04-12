package com.oracle.notebook.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Language Unsupported")

public class LanguageUnsupportedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public LanguageUnsupportedException(String message) {
        super(message);
    }

}
