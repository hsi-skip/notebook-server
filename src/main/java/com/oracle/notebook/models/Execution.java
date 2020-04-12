package com.oracle.notebook.models;

import lombok.Getter;
import lombok.Setter;
import org.graalvm.polyglot.Context;

import java.io.ByteArrayOutputStream;

@Setter
@Getter
public class Execution {

    private Context context;
    private ByteArrayOutputStream outputStream;

    public Execution() {
    	
    	this.context = Context.newBuilder("python").out(this.outputStream).err(this.outputStream).build();
    	this.outputStream =  new ByteArrayOutputStream();

    }
}
