package io.kalenz.bms.util.masking.strategy;

import io.kalenz.bms.masking.MaskingStrategy;
import io.kalenz.bms.masking.strategy.PhoneMaskingStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PhoneMaskingStrategyTest {

    private final MaskingStrategy strategy = new PhoneMaskingStrategy();

    @Test
    void mask_shouldMaskKeepingLastFourDigits() {
        assertEquals("******1234", strategy.mask("1234561234"));
    }

    @Test
    void mask_shouldMaskPhoneWithFormatting() {
        assertEquals("*******1234", strategy.mask("+1 (234) 561-1234"));
    }

    @Test
    void mask_shouldMaskAllDigitsWhenFourOrLess() {
        assertEquals("****", strategy.mask("1234"));
    }

    @Test
    void mask_shouldReturnEmptyString_whenBlank() {
        assertEquals("", strategy.mask(""));
        assertEquals("", strategy.mask("   "));
    }

    @Test
    void mask_shouldReturnEmptyString_whenNull() {
        assertEquals("", strategy.mask(null));
    }
}
