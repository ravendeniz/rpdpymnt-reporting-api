package com.rpdpymnt.reporting;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.formula.functions.T;

import java.io.IOException;
import java.io.InputStream;

public class JsonResourceObjectMapper {

    private Class<T> model;

    public JsonResourceObjectMapper(Class<T> model) {
        this.model = model;
    }

    public T loadTestJson(String fileName) throws IOException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        InputStream inputStream= classLoader.getResourceAsStream(fileName);
        return new ObjectMapper().readValue(inputStream, this.model);
    }
}
