package com.oracle.notebook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.oracle.notebook.models.Execution;
import com.oracle.notebook.models.InterpreterRequest;
import com.oracle.notebook.models.InterpreterResponse;
import com.oracle.notebook.service.InterpreterService;

import javax.validation.Valid;
import java.util.Random;

@RestController
public class InterpreterController {

	@Autowired
	private InterpreterService interpreterService;

	
	/**
	 * 
	 * @param request
	 * @return
	 */
	@PostMapping("/execute")
	public InterpreterResponse execute(@Valid @RequestBody InterpreterRequest request) {

		InterpreterResponse response = new InterpreterResponse();

		Random rand = new Random();

		if (request.getSessionId() == null) {
			request.setSessionId(String.valueOf(rand.nextLong()));
			response.setSessionId(request.getSessionId());
		}
		
		try {
			Execution execution = interpreterService.execute(request);
			response.setResult(execution.getOutputStream().toString());
		} catch (RuntimeException e) {
			response.setResult(e.getMessage());
		}finally {
			response.setSessionId(request.getSessionId());
		}

		return response;
	}

}
