package io.kalenz.bms.masking;

import tools.jackson.databind.BeanDescription;
import tools.jackson.databind.SerializationConfig;
import tools.jackson.databind.module.SimpleModule;
import tools.jackson.databind.ser.BeanPropertyWriter;
import tools.jackson.databind.ser.ValueSerializerModifier;

import java.util.List;

public class MaskingModule extends SimpleModule {

    public MaskingModule(MaskingStrategyRegistry registry) {
        super("MaskingModule");
        MaskingJsonSerializer.setRegistry(registry);

        setSerializerModifier(new ValueSerializerModifier() {
            @Override
            public List<BeanPropertyWriter> changeProperties(SerializationConfig config,
                                                             BeanDescription.Supplier supplier,
                                                             List<BeanPropertyWriter> properties) {
                for (BeanPropertyWriter prop : properties) {
                    Masking masking = prop.getAnnotation(Masking.class);
                    if (masking != null) {
                        prop.assignSerializer(new MaskingJsonSerializer(masking));
                    }
                }
                return properties;
            }
        });
    }
}
