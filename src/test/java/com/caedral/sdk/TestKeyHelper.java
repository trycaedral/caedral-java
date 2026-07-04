package com.caedral.sdk;

import org.mindrot.jbcrypt.BCrypt;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

final class TestKeyHelper {

    private static final String API_KEY_PREFIX = "cd_live_";

    private TestKeyHelper() {
    }

    static TestKeyFixture createTestApiKey(String databaseUrl) throws SQLException {
        if (databaseUrl == null || databaseUrl.isBlank()) {
            throw new IllegalStateException("DATABASE_URL is required to create a test API key");
        }

        String userId = UUID.randomUUID().toString();
        String apiKeyId = UUID.randomUUID().toString();
        String subId = UUID.randomUUID().toString();
        String rawKey = generateApiKeySecret();
        String keyPrefix = rawKey.substring(0, 16);
        String keyHash = BCrypt.hashpw(rawKey, BCrypt.gensalt(10));
        String email = "sdk-java-test-" + userId + "@example.com";

        Connection conn = openConnection(databaseUrl);
        conn.setAutoCommit(false);
        try {
            try (var userStmt = conn.prepareStatement("""
                    INSERT INTO "user" (id, name, email, email_verified, balance_cents, account_status)
                    VALUES (?, ?, ?, ?, ?, ?)
                    """)) {
                userStmt.setString(1, userId);
                userStmt.setString(2, "SDK Java Test");
                userStmt.setString(3, email);
                userStmt.setBoolean(4, true);
                userStmt.setInt(5, 0);
                userStmt.setString(6, "active");
                userStmt.executeUpdate();
            }

            try (var subStmt = conn.prepareStatement("""
                    INSERT INTO subscriptions (
                      id, user_id, plan, status, weekly_pool_limit, weekly_pool_used,
                      overage_enabled, overage_used_cents
                    )
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                    """)) {
                subStmt.setString(1, subId);
                subStmt.setString(2, userId);
                subStmt.setString(3, "pro");
                subStmt.setString(4, "active");
                subStmt.setInt(5, 1_000_000);
                subStmt.setInt(6, 0);
                subStmt.setBoolean(7, false);
                subStmt.setInt(8, 0);
                subStmt.executeUpdate();
            }

            try (var keyStmt = conn.prepareStatement("""
                    INSERT INTO api_keys (id, user_id, name, key_prefix, key_hash)
                    VALUES (?, ?, ?, ?, ?)
                    """)) {
                keyStmt.setString(1, apiKeyId);
                keyStmt.setString(2, userId);
                keyStmt.setString(3, "SDK Java test key");
                keyStmt.setString(4, keyPrefix);
                keyStmt.setString(5, keyHash);
                keyStmt.executeUpdate();
            }

            conn.commit();
        } catch (SQLException ex) {
            conn.rollback();
            conn.close();
            throw ex;
        }

        return new TestKeyFixture(userId, apiKeyId, rawKey, conn);
    }

    private static Connection openConnection(String databaseUrl) throws SQLException {
        if (databaseUrl.startsWith("jdbc:")) {
            return DriverManager.getConnection(databaseUrl);
        }

        URI uri = URI.create(databaseUrl.replaceFirst("^postgres(ql)?://", "http://"));
        int port = uri.getPort() > 0 ? uri.getPort() : 5432;
        String jdbcUrl = "jdbc:postgresql://" + uri.getHost() + ":" + port + uri.getRawPath();
        String userInfo = uri.getRawUserInfo();
        if (userInfo == null || userInfo.isBlank()) {
            return DriverManager.getConnection(jdbcUrl);
        }

        int split = userInfo.indexOf(':');
        String user = URLDecoder.decode(userInfo.substring(0, split), StandardCharsets.UTF_8);
        String password = URLDecoder.decode(userInfo.substring(split + 1), StandardCharsets.UTF_8);
        return DriverManager.getConnection(jdbcUrl, user, password);
    }

    private static String generateApiKeySecret() {
        byte[] bytes = new byte[24];
        new SecureRandom().nextBytes(bytes);
        return API_KEY_PREFIX + Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    record TestKeyFixture(String userId, String apiKeyId, String rawKey, Connection connection) {

        void cleanup() {
            try (connection) {
                try (var usage = connection.prepareStatement("DELETE FROM usage_logs WHERE user_id = ?")) {
                    usage.setString(1, userId);
                    usage.executeUpdate();
                }
                try (var keys = connection.prepareStatement("DELETE FROM api_keys WHERE id = ?")) {
                    keys.setString(1, apiKeyId);
                    keys.executeUpdate();
                }
                try (var subs = connection.prepareStatement("DELETE FROM subscriptions WHERE user_id = ?")) {
                    subs.setString(1, userId);
                    subs.executeUpdate();
                }
                try (var user = connection.prepareStatement("DELETE FROM \"user\" WHERE id = ?")) {
                    user.setString(1, userId);
                    user.executeUpdate();
                }
                connection.commit();
            } catch (SQLException ignored) {
                // ignore cleanup failures in tests
            }
        }
    }
}
