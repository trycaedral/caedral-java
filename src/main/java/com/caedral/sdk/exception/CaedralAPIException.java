package com.caedral.sdk.exception;

/**
 * Raised when the Caedral API returns an error response.
 */
public class CaedralAPIException extends RuntimeException {

    private final int statusCode;
    private final String type;
    private final Object rawBody;

    public CaedralAPIException(String message, int statusCode, String type, Object rawBody) {
        super(message);
        this.statusCode = statusCode;
        this.type = type;
        this.rawBody = rawBody;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getType() {
        return type;
    }

    public Object getRawBody() {
        return rawBody;
    }

    @SuppressWarnings("unchecked")
    public static CaedralAPIException fromResponse(int statusCode, Object body) {
        if (body instanceof java.util.Map<?, ?> map) {
            Object errorObj = map.get("error");
            if (errorObj instanceof java.util.Map<?, ?> error) {
                String message = stringValue(error.get("message"));
                if (message == null || message.isBlank()) {
                    message = "Request failed with status " + statusCode;
                }
                int code = statusCode;
                Object codeValue = error.get("code");
                if (codeValue instanceof Number number && number.intValue() != 0) {
                    code = number.intValue();
                }
                String type = stringValue(error.get("type"));
                if (type == null || type.isBlank()) {
                    type = "unknown";
                }
                return new CaedralAPIException(message, code, type, body);
            }
            String topLevelMessage = stringValue(map.get("message"));
            if (topLevelMessage != null && !topLevelMessage.isBlank()) {
                return new CaedralAPIException(topLevelMessage, statusCode, "unknown", body);
            }
        }

        if (body instanceof String text && !text.isBlank()) {
            return new CaedralAPIException(text, statusCode, "unknown", body);
        }

        return new CaedralAPIException(
                "Request failed with status " + statusCode,
                statusCode,
                "unknown",
                body
        );
    }

    private static String stringValue(Object value) {
        return value == null ? null : String.valueOf(value);
    }
}
