package io.kalenz.bms.util.masking;

import io.kalenz.bms.masking.MaskingStrategy;
import io.kalenz.bms.masking.MaskingStrategyRegistry;
import io.kalenz.bms.masking.MaskingStrategyType;
import io.kalenz.bms.masking.strategy.CustomMaskingStrategy;
import io.kalenz.bms.masking.strategy.EmailMaskingStrategy;
import io.kalenz.bms.masking.strategy.PhoneMaskingStrategy;
import io.kalenz.bms.masking.strategy.UsernameMaskingStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MaskingStrategyRegistryTest {

    private MaskingStrategyRegistry registry;

    @BeforeEach
    void setUp() {
        List<MaskingStrategy> strategies = List.of(
                new EmailMaskingStrategy(),
                new UsernameMaskingStrategy(),
                new PhoneMaskingStrategy(),
                new CustomMaskingStrategy()
        );
        registry = new MaskingStrategyRegistry(strategies, new CustomMaskingStrategy());
        registry.init();
    }

    @Test
    void mask_shouldDelegateToEmailStrategy() {
        assertEquals("j***@gmail.com", registry.mask("john@gmail.com", MaskingStrategyType.EMAIL, 0, 0));
    }

    @Test
    void mask_shouldDelegateToUsernameStrategy() {
        assertEquals("j********3", registry.mask("johndoe123", MaskingStrategyType.USERNAME, 0, 0));
    }

    @Test
    void mask_shouldDelegateToPhoneStrategy() {
        assertEquals("*******1234", registry.mask("+1 (234) 561-1234", MaskingStrategyType.PHONE, 0, 0));
    }

    @Test
    void mask_shouldDelegateToCustomStrategy() {
        assertEquals("j*****e", registry.mask("johndoe", MaskingStrategyType.CUSTOM, 1, 1));
        assertEquals("john***", registry.mask("johndoe", MaskingStrategyType.CUSTOM, 4, 0));
    }

    @Test
    void mask_shouldReturnEmptyString_whenValueIsNull() {
        assertEquals("", registry.mask(null, MaskingStrategyType.EMAIL, 0, 0));
    }

    @Test
    void mask_shouldReturnValueAsIs_whenTypeIsNull() {
        assertEquals("john", registry.mask("john", null, 0, 0));
    }

    @Test
    void register_shouldAllowCustomStrategy() {
        registry.register(MaskingStrategyType.CUSTOM, value -> "CUSTOM_" + value);
        assertEquals("CUSTOM_john", registry.mask("john", MaskingStrategyType.CUSTOM, 11, 0));
    }
}
