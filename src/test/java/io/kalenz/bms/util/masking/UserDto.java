package io.kalenz.bms.util.masking;

import io.kalenz.bms.masking.Masking;
import io.kalenz.bms.masking.MaskingStrategyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String name;
    @Masking(strategy = MaskingStrategyType.EMAIL)
    private String email;
    @Masking(strategy = MaskingStrategyType.USERNAME)
    private String username;
    @Masking(strategy = MaskingStrategyType.PHONE)
    private String phone;
    @Masking(strategy = MaskingStrategyType.CUSTOM, keepFirst = 2, keepLast = 2)
    private String ssn;
    @Masking(strategy = MaskingStrategyType.CUSTOM, keepFirst = 4, keepLast = 0)
    private String accountNumber;
}
