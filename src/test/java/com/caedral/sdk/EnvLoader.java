package com.caedral.sdk;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

final class EnvLoader {

    private EnvLoader() {
    }

    static Map<String, String> loadRootEnv() {
        Map<String, String> values = new HashMap<>();
        Path envPath = Path.of("..", ".env");
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

    static String resolve(String key, Map<String, String> fileEnv) {
        String fromProcess = System.getenv(key);
        if (fromProcess != null && !fromProcess.isBlank()) {
            return fromProcess;
        }
        return fileEnv.get(key);
    }
}
