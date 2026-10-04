package io.kalenz.bms.util.masking.strategy;

import io.kalenz.bms.masking.MaskingStrategy;
import io.kalenz.bms.masking.strategy.UsernameMaskingStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsernameMaskingStrategyTest {

    private final MaskingStrategy strategy = new UsernameMaskingStrategy();

    @Test
    void mask_shouldMaskMiddleKeepingFirstAndLast() {
        assertEquals("j********3", strategy.mask("johndoe123"));
    }

    @Test
    void mask_shouldMaskTwoCharUsername() {
        assertEquals("a*", strategy.mask("ab"));
    }

    @Test
    void mask_shouldReturnSingleCharAsIs() {
        assertEquals("j", strategy.mask("j"));
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
