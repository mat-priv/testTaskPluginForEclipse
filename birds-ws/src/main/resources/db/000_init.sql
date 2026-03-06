drop table if exists birds;
drop table if exists bird_sightings;

CREATE TABLE birds
(
    id     BIGSERIAL PRIMARY KEY,
    name   TEXT          NOT NULL,
    color  TEXT          NOT NULL,
    weight NUMERIC(5, 2) NOT NULL,
    height NUMERIC(5, 2) NOT NULL

);

CREATE TABLE bird_sightings
(
    id       BIGSERIAL PRIMARY KEY,
    bird_id  BIGINT NOT NULL,
    location TEXT   NOT NULL,
    date     TIMESTAMP   NOT NULL,

    CONSTRAINT fk_sightings_bird FOREIGN KEY (bird_id) REFERENCES birds (id)
);