package com.oracle.notebook.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InterpreterRequest {

    /**
     * The script.
     */
    private String code;

    /**
     * The session.
     */
    private String sessionId;

}
