--liquibase formatted sql
--changeset "João Felisberto":20251015170400-1
ALTER TABLE client
ADD CONSTRAINT unique_nif
UNIQUE nif;

--liquibase formatted sql
--changeset "João Felisberto":20251015170400-2
ALTER TABLE address
ADD CONSTRAINT unique_address_all_fields
UNIQUE (
  city, country, postcode, state_or_province, street_one, street_two, email_address
);
