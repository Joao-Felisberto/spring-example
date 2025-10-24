# JHipster generated Docker-Compose configuration

`docker-compose.yml` is the final project with every component containerized without SSL
`docker-compose-ssl.yml` is the final project with every component containerized WITH SSL
`docker-compose-ssl-no-microservice.yml` is the final project with every component containerized EXCEPT microservice (that should run locally with `prod` profile) WITH SSL

## Usage

Build microservices with `mvn package -Pprod verify jib:dockerBuild`

Launch all your infrastructure by running: `docker compose up -d`.

### SSL

For the `docker-compose-ssl.yml` and `docker-compose-ssl-no-microservice.yml`, SSL setup is needed.

Instructions adapted from [this blogpost](https://codewithhugo.com/docker-compose-local-https/).

1. Install [mkcert](https://github.com/FiloSottile/mkcert#installation)
2. Append `127.0.0.1 foo.test` to `C:\Windows\System32\drivers\etc\hosts`
3. In `caddy/certs` run `mkcert -install` so `foo.test.pem` and `foo.test-key.pem` are in that directory

## Configured Docker services

### Service registry and configuration server:

- [Consul](http://localhost:8500)

### Applications and dependencies:

- gateway (gateway application)
- gateway's postgresql database
- microservice (microservice application)
- microservice's postgresql database

### Additional Services:
