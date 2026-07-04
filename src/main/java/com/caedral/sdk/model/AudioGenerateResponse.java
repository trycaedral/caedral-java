package com.caedral.sdk.model;

import java.util.List;
import java.util.Map;

public class AudioGenerateResponse {

    private String model;
    private List<Map<String, Object>> choices;
    private CompletionUsage usage;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<Map<String, Object>> getChoices() {
        return choices;
    }

    public void setChoices(List<Map<String, Object>> choices) {
        this.choices = choices;
    }

    public CompletionUsage getUsage() {
        return usage;
    }

    public void setUsage(CompletionUsage usage) {
        this.usage = usage;
    }
}
