package com.caedral.sdk;

import com.caedral.sdk.model.ChatCompletion;
import com.caedral.sdk.model.ChatCompletionRequest;
import com.caedral.sdk.model.NotreOptions;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class NotreContractTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Path FIXTURES = Path.of("tests/fixtures/notre-contract");

    @Test
    void notreAutoTelemetryRequestParity() throws Exception {
        String raw = Files.readString(FIXTURES.resolve("notre-auto-telemetry.json"));
        ChatCompletionRequest req = MAPPER.readValue(raw, ChatCompletionRequest.class);
        assertNotNull(req.getNotre());
        assertEquals("auto", req.getNotre().getMode());
        assertEquals(Boolean.TRUE, req.getNotre().getTelemetry());
        assertEquals(MAPPER.readTree(raw), MAPPER.readTree(MAPPER.writeValueAsString(req)));
    }

    @Test
    void notreOmittedBackwardCompatible() throws Exception {
        String raw = Files.readString(FIXTURES.resolve("notre-omitted.json"));
        ChatCompletionRequest req = MAPPER.readValue(raw, ChatCompletionRequest.class);
        assertNull(req.getNotre());
    }

    @Test
    void responseTelemetryMetadataV1() throws Exception {
        String raw = Files.readString(FIXTURES.resolve("notre-response-telemetry.json"));
        ChatCompletion completion = MAPPER.readValue(raw, ChatCompletion.class);
        assertNotNull(completion.getNotre());
        assertTrue(completion.getNotre().isEnabled());
        assertEquals("auto", completion.getNotre().getMode());
        assertFalse(completion.getNotre().isIntervened());
        assertFalse(completion.getNotre().isFallbackUsed());
    }
}
