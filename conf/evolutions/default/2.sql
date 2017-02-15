# --- !Ups

CREATE TABLE pictures
(
  id SERIAL PRIMARY KEY,
  name TEXT NOT NULL,
  url TEXT,
  created_at TIMESTAMP WITHOUT TIME ZONE,
  votable BOOLEAN
);

CREATE TABLE votes
(
  id SERIAL PRIMARY KEY,
  user_id INTEGER NOT NULL REFERENCES users (id),
  picture_id INTEGER NOT NULL REFERENCES pictures (id),
  cosplay_score DECIMAL,
  other_score DECIMAL
);

# --- !Downs

DROP TABLE pictures;
DROP TABLE votes;