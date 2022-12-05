CREATE TABLE roles
(
    id         INT PRIMARY KEY constraint roles_pk,
    name       CHARACTER VARYING(60) NOT NULL UNIQUE
);