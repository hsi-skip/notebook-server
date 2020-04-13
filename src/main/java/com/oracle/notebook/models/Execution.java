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

    /**
     * Constructor
     */
    public Execution(String language) {
        this.outputStream =  new ByteArrayOutputStream();
        this.context = Context.newBuilder(language).out(this.outputStream).err(this.outputStream).build();
        //this.context = Context.newBuilder(language).build();


    }
}
