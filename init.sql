-- Create database
CREATE DATABASE translationdb;

\c translationdb;

-- Locales table
CREATE TABLE locales (
  id SERIAL PRIMARY KEY,
  code VARCHAR(10) NOT NULL UNIQUE
);

-- Translations table
CREATE TABLE translations (
  id BIGSERIAL PRIMARY KEY,
  translation_key VARCHAR(255) NOT NULL,
  locale_id INT NOT NULL REFERENCES locales(id),
  content TEXT NOT NULL,
  created_on TIMESTAMP WITH TIME ZONE DEFAULT now(),
  updated_on TIMESTAMP WITH TIME ZONE DEFAULT now(),
  CONSTRAINT uq_key_locale UNIQUE (translation_key, locale_id)
);

-- Tags table
CREATE TABLE tags (
  id SERIAL PRIMARY KEY,
  name VARCHAR(100) NOT NULL UNIQUE
);

-- Many-to-many between translations and tags
CREATE TABLE translation_tags (
  translation_id BIGINT NOT NULL REFERENCES translations(id) ON DELETE CASCADE,
  tag_id INT NOT NULL REFERENCES tags(id) ON DELETE CASCADE,
  PRIMARY KEY (translation_id, tag_id)
);

-- Indexes
CREATE INDEX idx_trans_key_locale ON translations(translation_key, locale_id);
CREATE INDEX idx_trans_key ON translations(translation_key);
CREATE INDEX idx_trans_content_gin ON translations USING gin (to_tsvector('simple', content));
CREATE INDEX idx_trans_locale ON translations(locale_id);
CREATE INDEX idx_tag_name ON tags(name);
CREATE INDEX idx_translation_tags_tag ON translation_tags(tag_id);

-- Trigger to auto-update updated_on
CREATE OR REPLACE FUNCTION update_timestamp()
RETURNS TRIGGER AS $$
BEGIN
   NEW.updated_on = now();
   RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_update_timestamp
BEFORE UPDATE ON translations
FOR EACH ROW
EXECUTE FUNCTION update_timestamp();
