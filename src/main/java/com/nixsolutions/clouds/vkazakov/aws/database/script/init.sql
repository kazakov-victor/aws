DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS role CASCADE;

CREATE TABLE role
(
    id   BIGSERIAL NOT NULL PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE users
(
    id            BIGSERIAL NOT NULL PRIMARY KEY,
    birthdate DATE,
    email         VARCHAR(255)
        CONSTRAINT uniq_email UNIQUE,
    first_name    VARCHAR(255),
    last_name     VARCHAR(255),
    username         VARCHAR(255)
        CONSTRAINT uniq_username UNIQUE,
    password      VARCHAR(255),
    role_id       BIGINT    NOT NULL,
    phone_number  VARCHAR(255),
    photo_link    VARCHAR(255),
    CONSTRAINT FK_user_role FOREIGN KEY (role_id) REFERENCES role (id)
);


