package com.caedral.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ChatCompletion {

    private String id;
    private String object;
    private long created;
    private String model;
    private List<ChatCompletionChoice> choices;
    private CompletionUsage usage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<ChatCompletionChoice> getChoices() {
        return choices;
    }

    public void setChoices(List<ChatCompletionChoice> choices) {
        this.choices = choices;
    }

    public CompletionUsage getUsage() {
        return usage;
    }

    public void setUsage(CompletionUsage usage) {
        this.usage = usage;
    }
}
