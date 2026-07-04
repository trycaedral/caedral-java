package com.caedral.sdk.service;

import com.caedral.sdk.http.CaedralHttpClient;
import com.caedral.sdk.model.ModelListResponse;

/**
 * Model catalog service backing {@code GET /v1/models}.
 */
public final class ModelsService {

    private final CaedralHttpClient http;

    public ModelsService(CaedralHttpClient http) {
        this.http = http;
    }

    /**
     * List every model available to the authenticated account, along
     * with metadata such as context window and pricing tier.
     *
     * @return the model list response
     * @throws com.caedral.sdk.exception.CaedralAPIException if the API
     *         returns a non-2xx response
     */
    public ModelListResponse list() {
        return http.get("/v1/models", ModelListResponse.class);
    }
}
