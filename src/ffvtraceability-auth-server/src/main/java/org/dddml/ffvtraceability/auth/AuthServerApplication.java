package org.dddml.ffvtraceability.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/*
 @EnableWebSecurity引入了以下配置类
 @Import({ WebSecurityConfiguration.class, SpringWebMvcImportSelector.class, OAuth2ImportSelector.class,
		HttpSecurityConfiguration.class })
 @EnableGlobalAuthentication
 其中 httpSecurity 在 HttpSecurityConfiguration 作为一个bean被初始化并注入到容器中
 其上的 @Scope("prototype") 注解使得每次请求都会创建一个新的实例
 */
@EnableWebSecurity(debug = true)
@SpringBootApplication
@EnableAsync
public class AuthServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthServerApplication.class, args);
    }
}
