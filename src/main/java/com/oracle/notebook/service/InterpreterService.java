package com.oracle.notebook.service;


import com.oracle.notebook.models.Execution;
import com.oracle.notebook.models.InterpreterRequest;

public interface InterpreterService {

	/**
	 * 
	 * @param request
	 * @return
	 * @throws RuntimeException
	 */
    Execution execute(InterpreterRequest request) throws RuntimeException;
    
}
