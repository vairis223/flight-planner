--liquibase formatted sql

--changeset vairis:1

CREATE TABLE airports (
                          airport_id VARCHAR(255) PRIMARY KEY,
                          country VARCHAR(255) NOT NULL,
                          city VARCHAR(255) NOT NULL

);

