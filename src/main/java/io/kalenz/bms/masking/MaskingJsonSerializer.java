package io.kalenz.bms.masking;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.BeanProperty;
import tools.jackson.databind.ValueSerializer;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.jsontype.TypeSerializer;

public class MaskingJsonSerializer extends ValueSerializer<Object> {

    private static volatile MaskingStrategyRegistry registry;

    private final Masking masking;

    public MaskingJsonSerializer() {
        this.masking = null;
    }

    public MaskingJsonSerializer(Masking masking) {
        this.masking = masking;
    }

    public static void setRegistry(MaskingStrategyRegistry reg) {
        registry = reg;
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializationContext ctx)
            throws JacksonException {
        String strValue = value != null ? value.toString() : "";
        String masked = registry.mask(strValue, masking.strategy(), masking.keepFirst(), masking.keepLast());
        gen.writeString(masked);
    }

    @Override
    public void serializeWithType(Object value, JsonGenerator gen, SerializationContext ctx, TypeSerializer typeSerializer)
            throws JacksonException {
        serialize(value, gen, ctx);
    }

    @Override
    public ValueSerializer<?> createContextual(SerializationContext ctx, BeanProperty property) {
        if (property == null) {
            return this;
        }
        Masking ann = property.getAnnotation(Masking.class);
        if (ann == null) {
            return null;
        }
        return new MaskingJsonSerializer(ann);
    }
}
