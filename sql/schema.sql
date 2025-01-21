DROP TABLE IF EXISTS site_statistics;
DROP TABLE IF EXISTS sites;
DROP TABLE IF EXISTS words;

CREATE TABLE IF NOT EXISTS sites
(
    name character varying UNIQUE,
    id SERIAL PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS words
(
    value character varying,
    id SERIAL PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS site_statistics
(
    site_id integer not null REFERENCES sites(id),
    word_occurrences integer not null,
    word_id integer not null REFERENCES words(id),
    UNIQUE (site_id, word_id)
);

CREATE TABLE IF NOT EXISTS site_statistics
(
    site_id integer not null REFERENCES sites(id),
    word_occurrences integer not null,
    word_id integer not null REFERENCES words(id),
    UNIQUE (site_id, word_id)
);

CREATE TABLE IF NOT EXISTS jobs
(
    job_id integer not null,
    working_time integer not null,
    initial_depth integer not null,
    current_depth integer not null,
    site_id integer not null REFERENCES sites(id),
    UNIQUE (site_id)
);


