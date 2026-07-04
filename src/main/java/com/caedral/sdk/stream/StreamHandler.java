package com.caedral.sdk.stream;

import com.caedral.sdk.model.ChatCompletionChunk;

/**
 * Callback interface for streaming chat completions.
 * Alternative to {@link ChatCompletionStream} when a push-based API is preferred.
 */
@FunctionalInterface
public interface StreamHandler {

    void onChunk(ChatCompletionChunk chunk);

    default void onError(Throwable error) {
        // no-op
    }

    default void onComplete() {
        // no-op
    }
}
