CREATE TABLE roles
(
    id SERIAL PRIMARY KEY,
    name CHARACTER VARYING(60) NOT NULL UNIQUE
);