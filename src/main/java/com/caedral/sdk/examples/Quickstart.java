package com.caedral.sdk.examples;

import com.caedral.sdk.Caedral;
import com.caedral.sdk.model.ChatCompletion;
import com.caedral.sdk.model.ChatCompletionRequest;
import com.caedral.sdk.model.ChatMessage;
import com.caedral.sdk.model.ModelListResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Quickstart smoke test — run against local gateway.
 */
public final class Quickstart {

    public static void main(String[] args) {
        Map<String, String> fileEnv = loadRootEnv();

        String apiKey = firstNonBlank(
                System.getenv("CAEDRAL_API_KEY"),
                System.getenv("CAEDRAL_TEST_API_KEY"),
                fileEnv.get("CAEDRAL_API_KEY"),
                fileEnv.get("CAEDRAL_TEST_API_KEY")
        );
        if (apiKey == null) {
            System.err.println("Set CAEDRAL_API_KEY or CAEDRAL_TEST_API_KEY");
            System.exit(1);
        }

        String baseUrl = firstNonBlank(
                System.getenv("CAEDRAL_BASE_URL"),
                fileEnv.get("CAEDRAL_BASE_URL"),
                "http://localhost:5001"
        );

        Caedral caedral = Caedral.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .build();

        ModelListResponse models = caedral.models().list();
        List<String> ids = models.getData().stream().map(model -> model.getId()).toList();
        System.out.println("Models: " + ids);

        ChatCompletion completion = caedral.chat().completions().create(
                new ChatCompletionRequest(
                        "caedral-base",
                        List.of(new ChatMessage("user", "Say hello in one short sentence."))
                )
        );

        Object content = completion.getChoices().get(0).getMessage().get("content");
        System.out.println("Assistant: " + content);
    }

    private static Map<String, String> loadRootEnv() {
        Map<String, String> values = new HashMap<>();
        Path envPath = Path.of("..", "..", ".env");
        if (!Files.exists(envPath)) {
            return values;
        }
        try {
            for (String line : Files.readAllLines(envPath)) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#") || !trimmed.contains("=")) {
                    continue;
                }
                int idx = trimmed.indexOf('=');
                String key = trimmed.substring(0, idx).trim();
                String value = trimmed.substring(idx + 1).trim();
                if ((value.startsWith("\"") && value.endsWith("\""))
                        || (value.startsWith("'") && value.endsWith("'"))) {
                    value = value.substring(1, value.length() - 1);
                }
                values.put(key, value);
            }
        } catch (IOException ignored) {
            // ignore
        }
        return values;
    }

    private static String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return null;
    }
}
