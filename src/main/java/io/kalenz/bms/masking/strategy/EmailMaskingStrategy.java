package io.kalenz.bms.masking.strategy;

import io.kalenz.bms.masking.MaskingStrategy;
import org.springframework.stereotype.Component;

@Component
public class EmailMaskingStrategy implements MaskingStrategy {

    @Override
    public String mask(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }
        int atIndex = value.indexOf('@');
        if (atIndex <= 0) {
            return "";
        }
        String localPart = value.substring(0, atIndex);
        String domain = value.substring(atIndex);
        if (localPart.length() == 1) {
            return localPart + "***" + domain;
        }
        return localPart.charAt(0) + "***" + domain;
    }
}
