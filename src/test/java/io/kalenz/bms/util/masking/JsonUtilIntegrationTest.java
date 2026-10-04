package io.kalenz.bms.util.masking;

import io.kalenz.bms.masking.JsonUtil;
import io.kalenz.bms.masking.MaskingModule;
import io.kalenz.bms.masking.MaskingStrategyRegistry;
import io.kalenz.bms.masking.strategy.CustomMaskingStrategy;
import io.kalenz.bms.masking.strategy.EmailMaskingStrategy;
import io.kalenz.bms.masking.strategy.PhoneMaskingStrategy;
import io.kalenz.bms.masking.strategy.UsernameMaskingStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonUtilIntegrationTest {

    private MaskingStrategyRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new MaskingStrategyRegistry(
                List.of(new EmailMaskingStrategy(),
                        new UsernameMaskingStrategy(),
                        new PhoneMaskingStrategy(),
                        new CustomMaskingStrategy()),
                new CustomMaskingStrategy()
        );
        registry.init();

        MaskingModule maskingModule = new MaskingModule(registry);
        JsonMapper mapper = JsonMapper.builder().addModule(maskingModule).build();
        JsonUtil.init(mapper);
    }

    @Test
    void toString_shouldMaskFieldsAnnotatedWithMasking() {
        UserDto user = new UserDto("John Doe", "john.doe@gmail.com", "johndoe123",
                "+1 (555) 123-4567", "123-45-6789", "1234567890");

        String json = JsonUtil.toString(user);

        assertTrue(json.contains("\"name\":\"John Doe\""));
        assertTrue(json.contains("\"email\":\"j***@gmail.com\""));
        assertTrue(json.contains("\"username\":\"j********3\""));
        assertTrue(json.contains("\"phone\":\"*******4567\""));
        assertTrue(json.contains("\"ssn\":\"12*******89\""));
        assertTrue(json.contains("\"accountNumber\":\"1234******\""));
    }

    @Test
    void toString_shouldHandleNullFieldValues() {
        UserDto user = new UserDto("Jane", null, null, null, null, null);

        String json = JsonUtil.toString(user);

        assertTrue(json.contains("\"name\":\"Jane\""));
        assertTrue(json.contains("\"email\":null"));
        assertTrue(json.contains("\"username\":null"));
        assertTrue(json.contains("\"phone\":null"));
    }
}
