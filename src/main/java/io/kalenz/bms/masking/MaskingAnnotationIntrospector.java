package io.kalenz.bms.masking;

import tools.jackson.databind.introspect.JacksonAnnotationIntrospector;

public class MaskingAnnotationIntrospector extends JacksonAnnotationIntrospector {

    @Override
    public Object findSerializer(tools.jackson.databind.cfg.MapperConfig<?> config,
                                 tools.jackson.databind.introspect.Annotated annotated) {
        Masking masking = annotated.getAnnotation(Masking.class);
        if (masking != null) {
            return new MaskingJsonSerializer(masking);
        }
        return super.findSerializer(config, annotated);
    }
}
