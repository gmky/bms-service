package io.kalenz.bms.masking;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

@Component
@RequiredArgsConstructor
public class JsonUtilInitializer {

    private final JsonMapper objectMapper;

    @PostConstruct
    public void init() {
        JsonUtil.init(objectMapper);
    }
}
