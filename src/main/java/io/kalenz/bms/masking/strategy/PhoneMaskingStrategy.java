package io.kalenz.bms.masking.strategy;

import io.kalenz.bms.masking.MaskingStrategy;
import org.springframework.stereotype.Component;

@Component
public class PhoneMaskingStrategy implements MaskingStrategy {

    @Override
    public String mask(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }
        String digitsOnly = value.replaceAll("\\D", "");
        if (digitsOnly.length() <= 4) {
            return "*".repeat(digitsOnly.length());
        }
        int keepLast = 4;
        int maskLength = digitsOnly.length() - keepLast;
        return "*".repeat(maskLength) + digitsOnly.substring(maskLength);
    }
}
