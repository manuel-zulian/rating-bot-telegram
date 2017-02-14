# --- !Ups

CREATE TABLE users
(
  id INTEGER NOT NULL PRIMARY KEY,
  first_name TEXT NOT NULL,
  last_name TEXT,
  username TEXT
);

# --- !Downs

DROP TABLE users;