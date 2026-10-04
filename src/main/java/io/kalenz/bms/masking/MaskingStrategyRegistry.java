package io.kalenz.bms.masking;

import io.kalenz.bms.masking.strategy.CustomMaskingStrategy;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class MaskingStrategyRegistry {

    private final List<MaskingStrategy> strategies;
    private final CustomMaskingStrategy customStrategy;
    private final Map<MaskingStrategyType, MaskingStrategy> registry = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        for (MaskingStrategy strategy : strategies) {
            if (strategy instanceof CustomMaskingStrategy) {
                continue;
            }
            MaskingStrategyType type = resolveType(strategy);
            if (type != null) {
                registry.put(type, strategy);
            }
        }
    }

    public String mask(String value, MaskingStrategyType type, int keepFirst, int keepLast) {
        if (type == null) {
            return value != null ? value : "";
        }
        MaskingStrategy strategy = registry.get(type);
        if (strategy != null) {
            return strategy.mask(value);
        }
        if (type == MaskingStrategyType.CUSTOM) {
            return customStrategy.mask(value, keepFirst, keepLast);
        }
        return value != null ? value : "";
    }

    public void register(MaskingStrategyType type, MaskingStrategy strategy) {
        registry.put(type, strategy);
    }

    private MaskingStrategyType resolveType(MaskingStrategy strategy) {
        String className = strategy.getClass().getSimpleName();
        for (MaskingStrategyType type : MaskingStrategyType.values()) {
            if (className.equalsIgnoreCase(type.name() + "MaskingStrategy")) {
                return type;
            }
        }
        return null;
    }
}
