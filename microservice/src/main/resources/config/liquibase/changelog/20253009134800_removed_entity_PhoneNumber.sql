--liquibase formatted sql
--changeset "João Felisberto":2025093010000-1
ALTER TABLE client ADD phone_number BIGINT;
ALTER TABLE client ADD phone_country_code VARCHAR(255);

--liquibase formatted sql
--changeset "João Felisberto":2025093010000-2
UPDATE client
SET
    phone_country_code = (SELECT country_code FROM phone_number WHERE phone_number.id = client.phone_number_id),
    phone_number = (SELECT number FROM phone_number WHERE phone_number.id = client.phone_number_id)
WHERE
    client.phone_number_id IS NOT NULL;

--liquibase formatted sql
--changeset "João Felisberto":2025093010000-3
ALTER TABLE client ADD CHECK(phone_country_code IS NOT NULL);
ALTER TABLE client ADD CHECK(phone_number IS NOT NULL);
 -- Why is this not valid????
 -- https://docs.liquibase.com/reference-guide/change-types/addnotnullconstraint
 -- https://h2database.com/html/commands.html#alter_table_add_constraint
 --ALTER TABLE client MODIFY phone_country_code VARCHAR(255) NOT NULL;
 --ALTER TABLE client MODIFY phone_number BIGINT NOT NULL;

--liquibase formatted sql
--changeset "João Felisberto":2025093010000-4
ALTER TABLE client DROP CONSTRAINT IF EXISTS FK_CLIENT__PHONE_NUMBER_ID;
DROP TABLE phone_number;
 -- Same issue as above
 -- https://docs.liquibase.com/reference-guide/change-types/dropforeignkeyconstraint
 -- https://h2database.com/html/commands.html#alter_table_drop_constraint
