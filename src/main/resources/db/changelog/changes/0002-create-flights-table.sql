--liquibase formatted sql

--changeset vairis:2

CREATE TABLE flights (
                         flight_id SERIAL PRIMARY KEY,
                         airport_from_id VARCHAR(255) NOT NULL,
                         airport_to_id VARCHAR(255) NOT NULL,
                         carrier VARCHAR(255) NOT NULL,
                         departure_Time TIMESTAMP NOT NULL,
                         arrival_Time TIMESTAMP NOT NULL,
                         FOREIGN KEY (airport_from_id) REFERENCES airports (airport_id),
                         FOREIGN KEY (airport_to_id) REFERENCES airports (airport_id)
);