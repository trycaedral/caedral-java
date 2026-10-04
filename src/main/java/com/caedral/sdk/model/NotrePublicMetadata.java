package com.caedral.sdk.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotrePublicMetadata {

    private boolean enabled;
    private String mode;
    private boolean intervened;

    @JsonProperty("fallback_used")
    private boolean fallbackUsed;

    // Contract V2 economy fields.
    @JsonProperty("input_before")
    private Integer inputBefore;

    @JsonProperty("input_sent")
    private Integer inputSent;

    @JsonProperty("input_saved")
    private Integer inputSaved;

    @JsonProperty("value_usd")
    private Double valueUsd;

    private String result;

    // Contract V3 shape fields (embeddings/rerank economy).
    private String shape;

    @JsonProperty("contract_version")
    private Integer contractVersion;

    @JsonProperty("saved_breakdown")
    private Map<String, Object> savedBreakdown;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public boolean isIntervened() {
        return intervened;
    }

    public void setIntervened(boolean intervened) {
        this.intervened = intervened;
    }

    public boolean isFallbackUsed() {
        return fallbackUsed;
    }

    public void setFallbackUsed(boolean fallbackUsed) {
        this.fallbackUsed = fallbackUsed;
    }

    public Integer getInputBefore() {
        return inputBefore;
    }

    public void setInputBefore(Integer inputBefore) {
        this.inputBefore = inputBefore;
    }

    public Integer getInputSent() {
        return inputSent;
    }

    public void setInputSent(Integer inputSent) {
        this.inputSent = inputSent;
    }

    public Integer getInputSaved() {
        return inputSaved;
    }

    public void setInputSaved(Integer inputSaved) {
        this.inputSaved = inputSaved;
    }

    public Double getValueUsd() {
        return valueUsd;
    }

    public void setValueUsd(Double valueUsd) {
        this.valueUsd = valueUsd;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public Integer getContractVersion() {
        return contractVersion;
    }

    public void setContractVersion(Integer contractVersion) {
        this.contractVersion = contractVersion;
    }

    public Map<String, Object> getSavedBreakdown() {
        return savedBreakdown;
    }

    public void setSavedBreakdown(Map<String, Object> savedBreakdown) {
        this.savedBreakdown = savedBreakdown;
    }
}
