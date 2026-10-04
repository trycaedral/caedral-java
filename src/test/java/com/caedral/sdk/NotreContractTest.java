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
        // V1 base shape: no economy fields.
        assertNull(completion.getNotre().getInputSaved());
        assertNull(completion.getNotre().getResult());
    }

    @Test
    void responseTelemetryMetadataV2() throws Exception {
        String raw = Files.readString(FIXTURES.resolve("notre-response-telemetry-v2.json"));
        ChatCompletion completion = MAPPER.readValue(raw, ChatCompletion.class);
        assertNotNull(completion.getNotre());
        assertTrue(completion.getNotre().isEnabled());
        assertTrue(completion.getNotre().isIntervened());
        assertFalse(completion.getNotre().isFallbackUsed());
        assertEquals(Integer.valueOf(1200), completion.getNotre().getInputBefore());
        assertEquals(Integer.valueOf(310), completion.getNotre().getInputSent());
        assertEquals(Integer.valueOf(890), completion.getNotre().getInputSaved());
        assertEquals("optimized", completion.getNotre().getResult());
        assertNotNull(completion.getNotre().getValueUsd());
        assertTrue(completion.getNotre().getValueUsd() > 0.0026
                && completion.getNotre().getValueUsd() < 0.0028);
    }

    @Test
    void responseTelemetryMetadataV3() throws Exception {
        String raw = Files.readString(FIXTURES.resolve("notre-response-telemetry-v3.json"));
        ChatCompletion completion = MAPPER.readValue(raw, ChatCompletion.class);
        assertNotNull(completion.getNotre());
        assertEquals("chat", completion.getNotre().getShape());
        assertEquals(Integer.valueOf(3), completion.getNotre().getContractVersion());
        assertNotNull(completion.getNotre().getSavedBreakdown());
        assertEquals(640, ((Number) completion.getNotre().getSavedBreakdown().get("cache_hit_tokens")).intValue());
        assertEquals(20, ((Number) completion.getNotre().getSavedBreakdown().get("dedup_tokens")).intValue());
        assertEquals(0, ((Number) completion.getNotre().getSavedBreakdown().get("prefilter_tokens")).intValue());
        assertEquals(Integer.valueOf(660), completion.getNotre().getInputSaved());
        assertEquals(Integer.valueOf(900), completion.getNotre().getInputBefore());
        assertEquals(Integer.valueOf(240), completion.getNotre().getInputSent());
    }
}
