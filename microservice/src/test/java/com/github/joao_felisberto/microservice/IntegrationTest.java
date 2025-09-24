package com.github.joao_felisberto.microservice;

import com.github.joao_felisberto.microservice.config.AsyncSyncConfiguration;
import com.github.joao_felisberto.microservice.config.EmbeddedSQL;
import com.github.joao_felisberto.microservice.config.JacksonConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { MicroserviceApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedSQL
public @interface IntegrationTest {
}
