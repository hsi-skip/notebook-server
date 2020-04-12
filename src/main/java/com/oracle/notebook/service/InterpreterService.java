package com.oracle.notebook.service;


import com.oracle.notebook.models.Execution;
import com.oracle.notebook.models.InterpreterRequest;

public interface InterpreterService {

    Execution execute(InterpreterRequest request) throws RuntimeException;
    
}
