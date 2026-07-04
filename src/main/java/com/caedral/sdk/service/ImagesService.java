package com.caedral.sdk.service;

import com.caedral.sdk.http.CaedralHttpClient;
import com.caedral.sdk.model.ImageGenerateRequest;
import com.caedral.sdk.model.ImageGenerateResponse;

/**
 * Image generation service backing {@code POST /v1/images/generations}.
 */
public final class ImagesService {

    private final CaedralHttpClient http;

    public ImagesService(CaedralHttpClient http) {
        this.http = http;
    }

    /**
     * Generate one or more images from a text prompt.
     *
     * @param request image generation request (prompt, optional model,
     *                {@code n}, and {@code size})
     * @return the generated images as URLs or base64-encoded data
     * @throws com.caedral.sdk.exception.CaedralAPIException if the API
     *         returns a non-2xx response
     */
    public ImageGenerateResponse generate(ImageGenerateRequest request) {
        return http.postJson("/v1/images/generations", request, ImageGenerateResponse.class);
    }
}
