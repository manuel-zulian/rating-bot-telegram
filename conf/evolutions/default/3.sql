# --- !Ups

ALTER TABLE pictures ADD COLUMN votable boolean default false;

# --- !Downs

ALTER TABLE pictures DROP COLUMN votable;