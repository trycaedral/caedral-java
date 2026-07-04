package com.caedral.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class ChatCompletionChunkChoice {

    private int index;
    private Map<String, Object> delta;

    @JsonProperty("finish_reason")
    private String finishReason;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Map<String, Object> getDelta() {
        return delta;
    }

    public void setDelta(Map<String, Object> delta) {
        this.delta = delta;
    }

    public String getFinishReason() {
        return finishReason;
    }

    public void setFinishReason(String finishReason) {
        this.finishReason = finishReason;
    }
}
