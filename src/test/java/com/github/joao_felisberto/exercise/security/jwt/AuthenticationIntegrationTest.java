package com.github.joao_felisberto.exercise.security.jwt;

import com.github.joao_felisberto.exercise.config.SecurityConfiguration;
import com.github.joao_felisberto.exercise.config.SecurityJwtConfiguration;
import com.github.joao_felisberto.exercise.config.WebConfigurer;
import com.github.joao_felisberto.exercise.management.SecurityMetersService;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;
import tech.jhipster.config.JHipsterProperties;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(
    classes = {
        JHipsterProperties.class,
        WebConfigurer.class,
        SecurityConfiguration.class,
        SecurityJwtConfiguration.class,
        SecurityMetersService.class,
        JwtAuthenticationTestUtils.class,
    }
)
public @interface AuthenticationIntegrationTest {
}
