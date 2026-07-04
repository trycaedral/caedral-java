package com.caedral.sdk;

import com.caedral.sdk.exception.CaedralAPIException;
import com.caedral.sdk.model.ChatCompletion;
import com.caedral.sdk.model.ChatCompletionRequest;
import com.caedral.sdk.model.ChatMessage;
import com.caedral.sdk.model.ModelListResponse;
import com.caedral.sdk.model.UsageSummary;
import com.caedral.sdk.stream.ChatCompletionStream;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IntegrationTest {

    private static final Map<String, String> FILE_ENV = EnvLoader.loadRootEnv();
    private static final String BASE_URL = EnvLoader.resolve("CAEDRAL_BASE_URL", FILE_ENV);

    private static Caedral client;
    private static TestKeyHelper.TestKeyFixture fixture;

    @BeforeAll
    static void setUp() throws SQLException {
        String apiKey = EnvLoader.resolve("CAEDRAL_TEST_API_KEY", FILE_ENV);
        if (apiKey == null || apiKey.isBlank()) {
            fixture = TestKeyHelper.createTestApiKey(EnvLoader.resolve("DATABASE_URL", FILE_ENV));
            apiKey = fixture.rawKey();
        }

        client = Caedral.builder()
                .apiKey(apiKey)
                .baseUrl(BASE_URL != null ? BASE_URL : "http://localhost:5001")
                .build();
    }

    @AfterAll
    static void tearDown() {
        if (fixture != null) {
            fixture.cleanup();
        }
    }

    @Test
    void listModels() {
        ModelListResponse models = client.models().list();

        assertEquals("list", models.getObject());
        assertTrue(models.getData().size() >= 4);

        Set<String> ids = models.getData().stream().map(model -> model.getId()).collect(Collectors.toSet());
        assertTrue(ids.containsAll(Set.of(
                "caedral-base",
                "caedral-titan",
                "caedral-olympus",
                "caedral-primordial"
        )));

        assertEquals("caedral", models.getData().get(0).getOwnedBy());
    }

    @Test
    void chatCompletion() {
        ChatCompletion completion = retryUpstream(() -> client.chat().completions().create(
                new ChatCompletionRequest(
                        "caedral-base",
                        List.of(new ChatMessage("user", "Reply with exactly: JAVA SDK OK"))
                )
        ));

        assertEquals("chat.completion", completion.getObject());
        assertEquals("caedral-base", completion.getModel());
        assertNotNull(completion.getChoices().get(0).getMessage().get("content"));
    }

    @Test
    void chatStreamingIterator() {
        StringBuilder text = new StringBuilder();
        retryUpstream(() -> {
            try (ChatCompletionStream stream = client.chat().completions().createStream(
                    new ChatCompletionRequest(
                            "caedral-base",
                            List.of(new ChatMessage("user", "Count to 3."))
                    )
            )) {
                while (stream.hasNext()) {
                    var chunk = stream.next();
                    assertEquals("chat.completion.chunk", chunk.getObject());
                    if (!chunk.getChoices().isEmpty()) {
                        Object delta = chunk.getChoices().get(0).getDelta().get("content");
                        if (delta instanceof String content) {
                            text.append(content);
                        }
                    }
                }
            }
            return null;
        });

        assertFalse(text.isEmpty());
    }

    @Test
    void chatStreamingCallback() {
        StringBuilder text = new StringBuilder();
        retryUpstream(() -> {
            client.chat().completions().createStream(
                    new ChatCompletionRequest(
                            "caedral-base",
                            List.of(new ChatMessage("user", "Say hi."))
                    ),
                    chunk -> {
                        if (!chunk.getChoices().isEmpty()) {
                            Object delta = chunk.getChoices().get(0).getDelta().get("content");
                            if (delta instanceof String content) {
                                text.append(content);
                            }
                        }
                    }
            );
            return null;
        });

        assertFalse(text.isEmpty());
    }

    @Test
    void usageGet() {
        UsageSummary usage = client.usage().get();

        assertNotNull(usage.getAccountStatus());
        assertNotNull(usage.getPlan());
        assertNotNull(usage.getPlanStatus());
        assertTrue(usage.getWeeklyPool().getLimit() >= 0);
    }

    @Test
    void invalidApiKey() {
        Caedral badClient = Caedral.builder()
                .apiKey("cd_live_invalid_integration_test_key")
                .baseUrl(BASE_URL != null ? BASE_URL : "http://localhost:5001")
                .build();

        CaedralAPIException chatError = assertThrows(CaedralAPIException.class, () ->
                badClient.chat().completions().create(
                        new ChatCompletionRequest(
                                "caedral-base",
                                List.of(new ChatMessage("user", "Hello"))
                        )
                )
        );
        assertEquals("invalid_api_key", chatError.getType());
        assertEquals(401, chatError.getStatusCode());

        CaedralAPIException usageError = assertThrows(CaedralAPIException.class, badClient.usage()::get);
        assertEquals("invalid_api_key", usageError.getType());
    }

    @FunctionalInterface
    private interface ThrowingSupplier<T> {
        T get() throws Exception;
    }

    private static <T> T retryUpstream(ThrowingSupplier<T> supplier) {
        RuntimeException lastError = null;
        for (int attempt = 0; attempt < 3; attempt++) {
            if (attempt > 0) {
                try {
                    Thread.sleep(attempt * 2000L);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(ex);
                }
            }
            try {
                return supplier.get();
            } catch (RuntimeException ex) {
                lastError = ex;
                if (!isRetryableUpstream(ex)) {
                    throw ex;
                }
            } catch (Exception ex) {
                lastError = new RuntimeException(ex);
                if (!isRetryableUpstream(lastError)) {
                    throw lastError;
                }
            }
        }
        throw lastError;
    }

    private static boolean isRetryableUpstream(Throwable error) {
        Throwable current = error;
        while (current != null) {
            if (current instanceof CaedralAPIException apiError) {
                String message = apiError.getMessage();
                if (message != null && message.contains("Upstream error")) {
                    return true;
                }
                if (apiError.getStatusCode() == 502 || apiError.getStatusCode() == 503) {
                    return true;
                }
            }
            current = current.getCause();
        }
        return false;
    }
}
