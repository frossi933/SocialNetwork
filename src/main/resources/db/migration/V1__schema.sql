DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS posts;
DROP TABLE IF EXISTS comments;

CREATE TABLE users(
    id SERIAL,
    email VARCHAR UNIQUE NOT NULL,
    name VARCHAR NOT NULL,
    password VARCHAR NOT NULL,
    active BOOLEAN NOT NULL
);
ALTER TABLE users ADD CONSTRAINT users_id PRIMARY KEY(id);

CREATE TABLE posts(
    id SERIAL,
    author_id INTEGER NOT NULL,
    text VARCHAR,
    image BYTEA NOT NULL,
    timestamp TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE posts ADD CONSTRAINT posts_id PRIMARY KEY(id);
ALTER TABLE posts ADD CONSTRAINT posts_author_id FOREIGN KEY(author_id) REFERENCES users(id) ON DELETE NO ACTION ON UPDATE CASCADE;

CREATE TABLE comments(
    id SERIAL,
    post_id INTEGER NOT NULL,
    author_id INTEGER NOT NULL,
    text VARCHAR NOT NULL,
    timestamp TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE comments ADD CONSTRAINT comments_id PRIMARY KEY(id);
ALTER TABLE comments ADD CONSTRAINT comments_post_id FOREIGN KEY(post_id) REFERENCES posts(id) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE comments ADD CONSTRAINT comments_author_id FOREIGN KEY(author_id) REFERENCES users(id) ON DELETE NO ACTION ON UPDATE CASCADE;
