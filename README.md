# How to run

The system is deployed via docker-compose through images in `/docker-compose`

## SSL

*Based on [this article](https://codewithhugo.com/docker-compose-local-https/#with-caddy-and-a-caddyfile), requires `mkcert` and `Caddy`.*

To deploy with SSL, first, the certificates must be generated with `mkcert foo.test` (replace `foo.test` with any domain mapped to `127.0.0.1` in the `hosts` file).
The certificates must then be put in `caddy/certs`.

Then, run `docker compose -f docker-compose-ssl.yml up -d`.

