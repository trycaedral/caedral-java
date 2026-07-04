package com.caedral.sdk.model;

import java.util.List;

public class RerankCreateResponse {

    private String model;
    private List<RerankResult> results;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<RerankResult> getResults() {
        return results;
    }

    public void setResults(List<RerankResult> results) {
        this.results = results;
    }
}
