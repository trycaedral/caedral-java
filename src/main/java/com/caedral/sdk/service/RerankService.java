package com.caedral.sdk.service;

import com.caedral.sdk.http.CaedralHttpClient;
import com.caedral.sdk.model.RerankCreateRequest;
import com.caedral.sdk.model.RerankCreateResponse;

/**
 * Document rerank service backing {@code POST /v1/rerank}.
 */
public final class RerankService {

    private final CaedralHttpClient http;

    public RerankService(CaedralHttpClient http) {
        this.http = http;
    }

    /**
     * Reorder a list of documents by semantic relevance to a query.
     *
     * @param request rerank request containing the query, candidate
     *                documents, optional model, and optional
     *                {@code top_n}
     * @return relevance-scored results ordered from most to least
     *         relevant
     * @throws com.caedral.sdk.exception.CaedralAPIException if the API
     *         returns a non-2xx response
     */
    public RerankCreateResponse create(RerankCreateRequest request) {
        return http.postJson("/v1/rerank", request, RerankCreateResponse.class);
    }
}
