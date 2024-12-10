DROP TABLE IF EXISTS public.site_statistics;
DROP TABLE IF EXISTS public.sites;
DROP TABLE IF EXISTS public.words;

CREATE TABLE IF NOT EXISTS public.sites
(
    name character varying,
    id SERIAL PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS public.words
(
    value character varying,
    id SERIAL PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS public.site_statistics
(
    site_id integer not null REFERENCES sites(id),
    word_occurrences integer not null,
    word_id integer not null REFERENCES words(id),
    UNIQUE (site_id, word_id)
);

