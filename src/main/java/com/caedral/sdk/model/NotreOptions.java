package com.caedral.sdk.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotreOptions {

    private String mode;
    private Boolean telemetry;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Boolean getTelemetry() {
        return telemetry;
    }

    public void setTelemetry(Boolean telemetry) {
        this.telemetry = telemetry;
    }
}
