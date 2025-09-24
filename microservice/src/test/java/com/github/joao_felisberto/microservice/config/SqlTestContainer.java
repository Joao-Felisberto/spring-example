package com.github.joao_felisberto.microservice.config;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.testcontainers.containers.JdbcDatabaseContainer;

public interface SqlTestContainer extends InitializingBean, DisposableBean {
    JdbcDatabaseContainer<?> getTestContainer();
}
