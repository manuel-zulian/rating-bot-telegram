# --- !Ups

CREATE TABLE pictures
(
  id INTEGER NOT NULL PRIMARY KEY,
  name TEXT NOT NULL,
  url TEXT,
  created_at TIMESTAMP WITHOUT TIME ZONE
);

CREATE TABLE votes
(
  id INTEGER NOT NULL PRIMARY KEY,
  user_id INTEGER NOT NULL REFERENCES users (id),
  cosplay_score DECIMAL,
  other_score DECIMAL
);

# --- !Downs

DROP TABLE pictures;
DROP TABLE votes;