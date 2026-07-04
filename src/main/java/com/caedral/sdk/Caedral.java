package com.caedral.sdk;

import com.caedral.sdk.http.CaedralHttpClient;
import com.caedral.sdk.service.AudioService;
import com.caedral.sdk.service.ChatCompletionsService;
import com.caedral.sdk.service.ChatService;
import com.caedral.sdk.service.EmbeddingsService;
import com.caedral.sdk.service.ImagesService;
import com.caedral.sdk.service.ModelsService;
import com.caedral.sdk.service.RerankService;
import com.caedral.sdk.service.UsageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;

import java.time.Duration;
import java.util.Objects;

/**
 * Official Java client for the Caedral API.
 */
public final class Caedral {

    private final ChatService chat;
    private final ModelsService models;
    private final UsageService usage;
    private final EmbeddingsService embeddings;
    private final ImagesService images;
    private final AudioService audio;
    private final RerankService rerank;

    private Caedral(CaedralHttpClient http) {
        ChatCompletionsService completions = new ChatCompletionsService(http);
        this.chat = new ChatService(completions);
        this.models = new ModelsService(http);
        this.usage = new UsageService(http);
        this.embeddings = new EmbeddingsService(http);
        this.images = new ImagesService(http);
        this.audio = new AudioService(http);
        this.rerank = new RerankService(http);
    }

    public static Builder builder() {
        return new Builder();
    }

    public ChatService chat() {
        return chat;
    }

    public ModelsService models() {
        return models;
    }

    public UsageService usage() {
        return usage;
    }

    public EmbeddingsService embeddings() {
        return embeddings;
    }

    public ImagesService images() {
        return images;
    }

    public AudioService audio() {
        return audio;
    }

    public RerankService rerank() {
        return rerank;
    }

    public static final class Builder {

        private String apiKey;
        private String baseUrl = CaedralHttpClient.DEFAULT_BASE_URL;
        private Duration timeout = CaedralHttpClient.DEFAULT_TIMEOUT;
        private int maxRetries = CaedralHttpClient.DEFAULT_MAX_RETRIES;
        private OkHttpClient httpClient;
        private ObjectMapper objectMapper;

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder maxRetries(int maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public Builder httpClient(OkHttpClient httpClient) {
            this.httpClient = httpClient;
            return this;
        }

        public Builder objectMapper(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
            return this;
        }

        public Caedral build() {
            if (apiKey == null || apiKey.isBlank()) {
                throw new IllegalArgumentException("apiKey is required");
            }

            ObjectMapper mapper = objectMapper != null
                    ? objectMapper
                    : CaedralHttpClient.defaultObjectMapper();
            OkHttpClient client = httpClient != null
                    ? httpClient
                    : CaedralHttpClient.defaultHttpClient(timeout);

            CaedralHttpClient http = new CaedralHttpClient(
                    apiKey.trim(),
                    Objects.requireNonNullElse(baseUrl, CaedralHttpClient.DEFAULT_BASE_URL),
                    client,
                    maxRetries,
                    mapper
            );
            return new Caedral(http);
        }
    }
}
