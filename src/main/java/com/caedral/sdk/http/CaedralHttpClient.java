package com.caedral.sdk.http;

import com.caedral.sdk.exception.CaedralAPIException;
import com.caedral.sdk.exception.CaedralNetworkException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public final class CaedralHttpClient {

    public static final String DEFAULT_BASE_URL = "https://api.caedral.com";
    public static final int DEFAULT_MAX_RETRIES = 3;
    public static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(120);

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private final String apiKey;
    private final String baseUrl;
    private final OkHttpClient httpClient;
    private final int maxRetries;
    private final ObjectMapper mapper;

    public CaedralHttpClient(
            String apiKey,
            String baseUrl,
            OkHttpClient httpClient,
            int maxRetries,
            ObjectMapper mapper
    ) {
        this.apiKey = apiKey;
        this.baseUrl = trimTrailingSlash(baseUrl);
        this.httpClient = httpClient;
        this.maxRetries = maxRetries;
        this.mapper = mapper;
    }

    public static ObjectMapper defaultObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        return objectMapper;
    }

    public static OkHttpClient defaultHttpClient(Duration timeout) {
        return new OkHttpClient.Builder()
                .callTimeout(timeout)
                .connectTimeout(timeout)
                .readTimeout(timeout)
                .writeTimeout(timeout)
                .build();
    }

    public ObjectMapper getMapper() {
        return mapper;
    }

    public <T> T get(String path, Class<T> responseType) {
        Exception lastError = null;
        for (int attempt = 0; attempt <= maxRetries; attempt++) {
            try {
                return decodeResponse(execute(buildRequest("GET", path, null)), responseType);
            } catch (Exception ex) {
                lastError = ex;
                if (!shouldRetry(ex, attempt)) {
                    throw wrapException(ex);
                }
                sleepBackoff(attempt);
            }
        }
        throw wrapException(lastError);
    }

    public <T> T postJson(String path, Object body, Class<T> responseType) {
        try {
            return decodeResponse(execute(buildRequest("POST", path, body)), responseType);
        } catch (Exception ex) {
            throw wrapException(ex);
        }
    }

    public Response postStream(String path, Object body) {
        try {
            Response response = httpClient.newCall(buildRequest("POST", path, body)).execute();
            if (response.code() >= 400) {
                String raw = response.body() != null ? response.body().string() : "";
                response.close();
                throw parseError(response.code(), raw);
            }
            return response;
        } catch (IOException ex) {
            throw new CaedralNetworkException("Network request failed", ex);
        }
    }

    private Request buildRequest(String method, String path, Object body) throws JsonProcessingException {
        Request.Builder builder = new Request.Builder()
                .url(baseUrl + path)
                .header("Authorization", "Bearer " + apiKey);

        if ("POST".equals(method)) {
            String json = body == null ? "{}" : mapper.writeValueAsString(body);
            builder.post(RequestBody.create(json, JSON));
        } else {
            builder.get();
        }

        return builder.build();
    }

    private Response execute(Request request) throws IOException {
        try {
            return httpClient.newCall(request).execute();
        } catch (IOException ex) {
            throw new CaedralNetworkException("Network request failed", ex);
        }
    }

    private <T> T decodeResponse(Response response, Class<T> responseType) throws IOException {
        try (response) {
            String raw = response.body() != null ? response.body().string() : "";
            if (response.code() >= 400) {
                throw parseError(response.code(), raw);
            }
            if (responseType == Void.class || raw.isBlank()) {
                return null;
            }
            return mapper.readValue(raw, responseType);
        }
    }

    private CaedralAPIException parseError(int statusCode, String raw) {
        Object parsed = safeParse(raw);
        return CaedralAPIException.fromResponse(statusCode, parsed);
    }

    private Object safeParse(String raw) {
        if (raw == null || raw.isBlank()) {
            return raw;
        }
        try {
            return mapper.readValue(raw, Object.class);
        } catch (JsonProcessingException ex) {
            return raw;
        }
    }

    private boolean shouldRetry(Exception error, int attempt) {
        if (attempt >= maxRetries) {
            return false;
        }
        if (error instanceof CaedralNetworkException) {
            return true;
        }
        if (error instanceof CaedralAPIException apiError) {
            return apiError.getStatusCode() == 502 || apiError.getStatusCode() == 503;
        }
        return false;
    }

    private void sleepBackoff(int attempt) {
        try {
            TimeUnit.MILLISECONDS.sleep(100L * (1L << attempt));
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new CaedralNetworkException("Retry interrupted", ex);
        }
    }

    private RuntimeException wrapException(Exception error) {
        if (error instanceof RuntimeException runtime) {
            return runtime;
        }
        return new CaedralNetworkException(error.getMessage(), error);
    }

    private static String trimTrailingSlash(String value) {
        if (value == null || value.isBlank()) {
            return DEFAULT_BASE_URL;
        }
        String trimmed = value.trim();
        while (trimmed.endsWith("/")) {
            trimmed = trimmed.substring(0, trimmed.length() - 1);
        }
        return trimmed;
    }
}
