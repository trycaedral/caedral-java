package com.caedral.sdk.service;

import com.caedral.sdk.http.CaedralHttpClient;
import com.caedral.sdk.model.AudioGenerateRequest;
import com.caedral.sdk.model.AudioGenerateResponse;

/**
 * Audio (text-to-speech) service backing {@code POST /v1/audio/speech}.
 */
public final class AudioService {

    private final CaedralHttpClient http;

    public AudioService(CaedralHttpClient http) {
        this.http = http;
    }

    /**
     * Synthesize speech audio from an input text string.
     *
     * @param request populated request containing the input text and
     *                optional model and voice overrides
     * @return the generated audio payload
     * @throws com.caedral.sdk.exception.CaedralAPIException if the
     *         API returns a non-2xx response
     */
    public AudioGenerateResponse generate(AudioGenerateRequest request) {
        return http.postJson("/v1/audio/speech", request, AudioGenerateResponse.class);
    }
}
