package io.kalenz.bms.util.masking.strategy;

import io.kalenz.bms.masking.strategy.CustomMaskingStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomMaskingStrategyTest {

    private final CustomMaskingStrategy strategy = new CustomMaskingStrategy();

    @Test
    void mask_shouldKeepFirstCharacters() {
        assertEquals("john***", strategy.mask("johndoe", 4, 0));
    }

    @Test
    void mask_shouldKeepLastCharacters() {
        assertEquals("****doe", strategy.mask("johndoe", 0, 3));
    }

    @Test
    void mask_shouldKeepBothFirstAndLast() {
        assertEquals("j*****e", strategy.mask("johndoe", 1, 1));
    }

    @Test
    void mask_shouldMaskAllWhenKeepFirstPlusKeepLastExceedsLength() {
        assertEquals("johndoe", strategy.mask("johndoe", 10, 10));
    }

    @Test
    void mask_shouldTreatNegativeKeepLastAsZero() {
        assertEquals("john***", strategy.mask("johndoe", 4, -1));
    }

    @Test
    void mask_shouldReturnEmptyString_whenBlank() {
        assertEquals("", strategy.mask("", 2, 2));
        assertEquals("", strategy.mask("   ", 1, 1));
    }

    @Test
    void mask_shouldReturnEmptyString_whenNull() {
        assertEquals("", strategy.mask(null, 2, 2));
    }
}
