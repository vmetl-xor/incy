
CREATE SCHEMA TEST;

CREATE TABLE IF NOT EXISTS test.sites
(
    name character varying UNIQUE,
    id SERIAL PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS test.words
(
    value character varying,
    id SERIAL PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS test.site_statistics
(
    site_id integer not null REFERENCES sites(id),
    word_occurrences integer not null,
    word_id integer not null REFERENCES words(id),
    UNIQUE (site_id, word_id)
    );
