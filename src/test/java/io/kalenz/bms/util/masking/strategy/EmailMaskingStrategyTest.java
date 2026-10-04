package io.kalenz.bms.util.masking.strategy;

import io.kalenz.bms.masking.MaskingStrategy;
import io.kalenz.bms.masking.strategy.EmailMaskingStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailMaskingStrategyTest {

    private final MaskingStrategy strategy = new EmailMaskingStrategy();

    @Test
    void mask_shouldMaskMiddleOfEmail() {
        assertEquals("j***@gmail.com", strategy.mask("john@gmail.com"));
        assertEquals("a***@example.org", strategy.mask("alice@example.org"));
    }

    @Test
    void mask_shouldMaskSingleCharLocalPart() {
        assertEquals("a***@test.com", strategy.mask("a@test.com"));
    }

    @Test
    void mask_shouldReturnEmptyString_whenNoAtSymbol() {
        assertEquals("", strategy.mask("notanemail"));
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
