package com.caedral.sdk.service;

import com.caedral.sdk.http.CaedralHttpClient;
import com.caedral.sdk.model.EmbeddingCreateRequest;
import com.caedral.sdk.model.EmbeddingCreateResponse;

/**
 * Text embeddings service backing {@code POST /v1/embeddings}.
 */
public final class EmbeddingsService {

    private final CaedralHttpClient http;

    public EmbeddingsService(CaedralHttpClient http) {
        this.http = http;
    }

    /**
     * Generate dense vector embeddings for one or more input strings.
     *
     * @param request embedding request specifying the model and
     *                input text(s)
     * @return the response containing embedding vectors in the same
     *         order as the inputs, plus token usage
     * @throws com.caedral.sdk.exception.CaedralAPIException if the API
     *         returns a non-2xx response
     */
    public EmbeddingCreateResponse create(EmbeddingCreateRequest request) {
        return http.postJson("/v1/embeddings", request, EmbeddingCreateResponse.class);
    }
}
