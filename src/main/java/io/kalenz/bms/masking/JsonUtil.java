package io.kalenz.bms.masking;

import tools.jackson.databind.json.JsonMapper;

public class JsonUtil {

    private static volatile JsonMapper MAPPER;

    private JsonUtil() {
    }

    public static synchronized void init(JsonMapper mapper) {
        MAPPER = mapper;
    }

    public static String toString(Object value) {
        if (MAPPER == null) {
            throw new IllegalStateException("JsonUtil has not been initialized. Call init() first.");
        }
        try {
            return MAPPER.writeValueAsString(value);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize object to JSON", e);
        }
    }

    public static <T> T fromString(String json, Class<T> clazz) {
        if (MAPPER == null) {
            throw new IllegalStateException("JsonUtil has not been initialized. Call init() first.");
        }
        try {
            return MAPPER.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize JSON to object", e);
        }
    }
}
