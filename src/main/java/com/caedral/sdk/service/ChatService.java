package com.caedral.sdk.service;

/**
 * Namespace grouping chat-related endpoints.
 *
 * <p>Currently exposes {@link #completions()} for
 * {@code POST /v1/chat/completions}.
 */
public final class ChatService {

    private final ChatCompletionsService completions;

    public ChatService(ChatCompletionsService completions) {
        this.completions = completions;
    }

    /**
     * @return the chat completions sub-service.
     */
    public ChatCompletionsService completions() {
        return completions;
    }
}
