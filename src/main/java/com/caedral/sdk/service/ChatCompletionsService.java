package com.caedral.sdk.service;

import com.caedral.sdk.http.CaedralHttpClient;
import com.caedral.sdk.model.ChatCompletion;
import com.caedral.sdk.model.ChatCompletionRequest;
import com.caedral.sdk.stream.ChatCompletionStream;
import com.caedral.sdk.stream.StreamHandler;
import okhttp3.Response;

/**
 * Chat completions service backing {@code POST /v1/chat/completions}.
 *
 * <p>Supports buffered responses via {@link #create(ChatCompletionRequest)}
 * and Server-Sent Events streaming via
 * {@link #createStream(ChatCompletionRequest)} or the callback-based
 * {@link #createStream(ChatCompletionRequest, StreamHandler)} overload.
 */
public final class ChatCompletionsService {

    private final CaedralHttpClient http;

    public ChatCompletionsService(CaedralHttpClient http) {
        this.http = http;
    }

    /**
     * Create a buffered (non-streaming) chat completion.
     *
     * <p>Forces {@code request.stream} to {@code false} before sending.
     *
     * @param request chat completion request (model, messages, sampling
     *                options, etc.)
     * @return the full generated {@link ChatCompletion}
     * @throws IllegalArgumentException if {@code request.stream} is
     *         {@code true}; use
     *         {@link #createStream(ChatCompletionRequest)} instead
     * @throws com.caedral.sdk.exception.CaedralAPIException if the
     *         API returns a non-2xx response
     */
    public ChatCompletion create(ChatCompletionRequest request) {
        if (Boolean.TRUE.equals(request.getStream())) {
            throw new IllegalArgumentException("Use createStream() when request.stream is true");
        }
        request.setStream(false);
        return http.postJson("/v1/chat/completions", request, ChatCompletion.class);
    }

    /**
     * Open a streaming chat completion and return the raw stream.
     *
     * <p>Forces {@code request.stream} to {@code true}. The caller is
     * responsible for iterating the returned {@link ChatCompletionStream}
     * and closing it when done (it is {@link AutoCloseable}).
     *
     * @param request chat completion request
     * @return a {@link ChatCompletionStream} yielding incremental chunks
     * @throws com.caedral.sdk.exception.CaedralAPIException if the API
     *         returns a non-2xx response
     */
    public ChatCompletionStream createStream(ChatCompletionRequest request) {
        request.setStream(true);
        Response response = http.postStream("/v1/chat/completions", request);
        return new ChatCompletionStream(response, http.getMapper());
    }

    /**
     * Open a streaming chat completion and dispatch chunks to a
     * {@link StreamHandler} callback.
     *
     * <p>{@link StreamHandler#onChunk} is invoked for every incoming
     * chunk, followed by {@link StreamHandler#onComplete} on success or
     * {@link StreamHandler#onError} if a runtime exception is raised.
     * The underlying stream is always closed on exit.
     *
     * @param request chat completion request
     * @param handler callback that receives streamed chunks and
     *                completion/error signals
     * @throws com.caedral.sdk.exception.CaedralAPIException if the API
     *         returns a non-2xx response
     */
    public void createStream(ChatCompletionRequest request, StreamHandler handler) {
        try (ChatCompletionStream stream = createStream(request)) {
            while (stream.hasNext()) {
                handler.onChunk(stream.next());
            }
            handler.onComplete();
        } catch (RuntimeException ex) {
            handler.onError(ex);
            throw ex;
        }
    }
}
