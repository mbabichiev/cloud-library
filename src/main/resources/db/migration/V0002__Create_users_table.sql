CREATE TABLE users
(
    id          SERIAL PRIMARY KEY,
    login       CHARACTER VARYING(60) NOT NULL UNIQUE,
    password    CHARACTER VARYING(255) NOT NULL UNIQUE,
    email       CHARACTER VARYING(60) NOT NULL,
    role_id     INT references roles
);

create unique index users_login_index on users (login);