# JHipster generated Docker-Compose configuration

## Usage

Build microservices with `mvn package -Pprod verify jib:dockerBuild`

Launch all your infrastructure by running: `docker compose up -d`.

## Configured Docker services

### Service registry and configuration server:

- [Consul](http://localhost:8500)

### Applications and dependencies:

- gateway (gateway application)
- gateway's postgresql database
- microservice (microservice application)
- microservice's postgresql database

### Additional Services:
