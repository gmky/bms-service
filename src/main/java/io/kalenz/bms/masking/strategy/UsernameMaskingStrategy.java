package io.kalenz.bms.masking.strategy;

import io.kalenz.bms.masking.MaskingStrategy;
import org.springframework.stereotype.Component;

@Component
public class UsernameMaskingStrategy implements MaskingStrategy {

    @Override
    public String mask(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }
        int length = value.length();
        if (length <= 1) {
            return value;
        }
        if (length == 2) {
            return value.charAt(0) + "*";
        }
        char first = value.charAt(0);
        char last = value.charAt(length - 1);
        String masked = "*".repeat(Math.max(0, length - 2));
        return first + masked + last;
    }
}
