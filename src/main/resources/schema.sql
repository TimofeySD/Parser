CREATE TABLE IF NOT EXISTS historical_events (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    event_year INTEGER NOT NULL,
    title TEXT NOT NULL,
    description TEXT,
    source_url TEXT NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS scientists (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name TEXT NOT NULL,
    birth_year INTEGER NOT NULL,
    death_year INTEGER,
    biography TEXT,
    source_url TEXT NOT NULL,
    UNIQUE (source_url, name)
);

ALTER TABLE scientists
    ADD COLUMN IF NOT EXISTS biography TEXT;