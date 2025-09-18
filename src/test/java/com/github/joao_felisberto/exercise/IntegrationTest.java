package com.github.joao_felisberto.exercise;

import com.github.joao_felisberto.exercise.config.AsyncSyncConfiguration;
import com.github.joao_felisberto.exercise.config.EmbeddedSQL;
import com.github.joao_felisberto.exercise.config.JacksonConfiguration;
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
@SpringBootTest(classes = { ExerciseApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedSQL
public @interface IntegrationTest {
}
