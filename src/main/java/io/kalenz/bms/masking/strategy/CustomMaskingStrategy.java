package io.kalenz.bms.masking.strategy;

import io.kalenz.bms.masking.MaskingStrategy;
import org.springframework.stereotype.Component;

@Component
public class CustomMaskingStrategy implements MaskingStrategy {

    @Override
    public String mask(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }
        return value;
    }

    public String mask(String value, int keepFirst, int keepLast) {
        if (value == null || value.isBlank()) {
            return "";
        }
        int length = value.length();
        if (keepFirst < 0) keepFirst = 0;
        if (keepLast < 0) keepLast = 0;
        if (keepFirst >= length) {
            keepFirst = length;
        }
        if (keepLast > length - keepFirst) {
            keepLast = Math.max(0, length - keepFirst);
        }
        String first = value.substring(0, keepFirst);
        String last = value.substring(length - keepLast);
        int maskedLength = length - keepFirst - keepLast;
        String masked = maskedLength > 0 ? "*".repeat(maskedLength) : "";
        return first + masked + last;
    }
}
