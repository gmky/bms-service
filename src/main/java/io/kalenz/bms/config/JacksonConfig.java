package io.kalenz.bms.config;

import io.kalenz.bms.masking.MaskingModule;
import io.kalenz.bms.masking.MaskingStrategyRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.json.JsonMapper;

@Configuration
@RequiredArgsConstructor
public class JacksonConfig {

    @Bean
    public MaskingModule maskingModule(MaskingStrategyRegistry registry) {
        return new MaskingModule(registry);
    }

    @Bean
    @Primary
    public JsonMapper jsonMapper(MaskingModule maskingModule) {
        return JsonMapper.builder()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS)
                .enable(DateTimeFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
                .addModule(maskingModule)
                .build();
    }
}
