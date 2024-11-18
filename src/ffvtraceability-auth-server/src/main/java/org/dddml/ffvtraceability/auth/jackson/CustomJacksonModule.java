package org.dddml.ffvtraceability.auth.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import org.dddml.ffvtraceability.auth.security.CustomUserDetails;

public class CustomJacksonModule extends SimpleModule {

    public CustomJacksonModule() {
        super(CustomJacksonModule.class.getName());
    }

    @Override
    public void setupModule(SetupContext context) {
        super.setupModule(context);

        // 注册 CustomUserDetails 的序列化器和反序列化器
        context.setMixInAnnotations(CustomUserDetails.class, CustomUserDetailsMixin.class);
    }
}