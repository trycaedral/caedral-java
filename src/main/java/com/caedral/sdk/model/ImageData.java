package com.caedral.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ImageData {

    private String url;

    @JsonProperty("b64_json")
    private String b64Json;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getB64Json() {
        return b64Json;
    }

    public void setB64Json(String b64Json) {
        this.b64Json = b64Json;
    }
}
