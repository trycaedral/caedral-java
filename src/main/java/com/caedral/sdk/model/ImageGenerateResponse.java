package com.caedral.sdk.model;

import java.util.List;

public class ImageGenerateResponse {

    private String model;
    private List<ImageData> data;
    private CompletionUsage usage;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<ImageData> getData() {
        return data;
    }

    public void setData(List<ImageData> data) {
        this.data = data;
    }

    public CompletionUsage getUsage() {
        return usage;
    }

    public void setUsage(CompletionUsage usage) {
        this.usage = usage;
    }
}
