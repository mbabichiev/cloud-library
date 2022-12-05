CREATE TABLE users
(
    id          INT PRIMARY KEY,
    login       CHARACTER VARYING(60) NOT NULL UNIQUE,
    password    CHARACTER VARYING(255) NOT NULL UNIQUE,
    email       CHARACTER VARYING(60) NOT NULL,
    role_id     INT constraint users_roles_id_fk references roles
);

create unique index users_login_index on users (login);