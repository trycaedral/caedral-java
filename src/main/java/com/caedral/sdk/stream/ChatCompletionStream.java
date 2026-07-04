package com.caedral.sdk.stream;

import com.caedral.sdk.exception.CaedralAPIException;
import com.caedral.sdk.model.ChatCompletionChunk;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Blocking iterator over SSE chat completion chunks.
 * Implements {@link Iterator} + {@link AutoCloseable} — the idiomatic choice for
 * a synchronous SDK (no reactive dependency required).
 */
public final class ChatCompletionStream implements Iterator<ChatCompletionChunk>, AutoCloseable {

    private final BufferedReader reader;
    private final ObjectMapper mapper;
    private final Response response;
    private ChatCompletionChunk nextChunk;
    private boolean finished;
    private RuntimeException pendingError;

    public ChatCompletionStream(Response response, ObjectMapper mapper) {
        this.response = response;
        this.mapper = mapper;
        ResponseBody body = response.body();
        if (body == null) {
            throw new CaedralAPIException("Empty stream response", response.code(), "unknown", null);
        }
        this.reader = new BufferedReader(new InputStreamReader(body.byteStream(), StandardCharsets.UTF_8));
        advance();
    }

    @Override
    public boolean hasNext() {
        if (pendingError != null) {
            throw pendingError;
        }
        return nextChunk != null;
    }

    @Override
    public ChatCompletionChunk next() {
        if (pendingError != null) {
            throw pendingError;
        }
        if (nextChunk == null) {
            throw new NoSuchElementException();
        }
        ChatCompletionChunk current = nextChunk;
        advance();
        return current;
    }

    @Override
    public void close() {
        try {
            reader.close();
        } catch (IOException ignored) {
            // ignore
        }
        response.close();
    }

    private void advance() {
        if (finished) {
            nextChunk = null;
            return;
        }

        try {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || !line.startsWith("data:")) {
                    continue;
                }
                String data = line.substring("data:".length()).trim();
                if (data.isEmpty() || "[DONE]".equals(data)) {
                    continue;
                }
                nextChunk = mapper.readValue(data, ChatCompletionChunk.class);
                return;
            }
            finished = true;
            nextChunk = null;
        } catch (IOException ex) {
            pendingError = new CaedralAPIException("Stream read failed: " + ex.getMessage(), 0, "stream_error", null);
            nextChunk = null;
        }
    }
}
