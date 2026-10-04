package io.kalenz.bms.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private CacheProperties cache;
    private SecurityProperties security;

    @Data
    public static class CacheProperties {
        private int defaultTTL = 30;
    }

    @Data
    public static class SecurityProperties {
        public List<String> publicPaths;
    }
}
