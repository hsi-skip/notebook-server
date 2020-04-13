package com.oracle.notebook;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.notebook.models.InterpreterRequest;

@SpringBootTest
@AutoConfigureMockMvc
class NotebookServerApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper mapper;

	@Test
	/**
	 * Test with valid request
	 * 
	 * @throws Exception
	 */
	public void testWithRequestValid() throws Exception {
		InterpreterRequest request = new InterpreterRequest();
		request.setCode("%python print('Hello World !')");

		this.mockMvc
				.perform(post("/execute").content(mapper.writeValueAsString(request))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.result", is("Hello World !")));
	}

	@Test
	public void testWithRequestInvalid() throws Exception {
		InterpreterRequest request = new InterpreterRequest();
		request.setCode("");

		this.mockMvc
				.perform(post("/execute").content(mapper.writeValueAsString(request))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.result",
						is("Invalid request format, use format like '%<interpreter-name><whitespace><code>'")));

	}

	@Test
	public void testWithLanguageUnsupported() throws Exception {
		InterpreterRequest request = new InterpreterRequest();
		request.setCode("%test Invalid language");

		this.mockMvc
				.perform(post("/execute").content(mapper.writeValueAsString(request))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.result",
						is("This language is unsupported, the only language suported is \" + Constants.LAGUAGE")));

	}

	@Test
	/**
	 * Test with too long execute
	 * 
	 * @throws Exception
	 */
	public void testWithTooLongExecute() throws Exception {
		InterpreterRequest request = new InterpreterRequest();
		request.setCode("%python while (true): print('Infinite loop !') ");

		this.mockMvc
				.perform(post("/execute").content(mapper.writeValueAsString(request))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.result", is("Request taking too long to execute")));

	}

	@Test
	/**
	 * Test with wrong code
	 * 
	 * @throws Exception
	 */
	public void testWithWrongCode() throws Exception {
		InterpreterRequest request = new InterpreterRequest();
		request.setCode("%python print(a)");

		this.mockMvc
				.perform(post("/execute").content(mapper.writeValueAsString(request))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.result", is("NameError: name 'a' is not defined")));

	}
}
