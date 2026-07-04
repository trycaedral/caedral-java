package com.caedral.sdk.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmbeddingCreateRequest {

    private String model;
    private Object input;

    public EmbeddingCreateRequest() {
    }

    public EmbeddingCreateRequest(String model, Object input) {
        this.model = model;
        this.input = input;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Object getInput() {
        return input;
    }

    public void setInput(Object input) {
        this.input = input;
    }
}
