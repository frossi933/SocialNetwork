INSERT INTO users(id, email, name, password, active) VALUES
    (1, 'diego.maradona@example.com','Diego Maradona', 'argentina123',true),
    (2, 'lionel.messi@example.com', 'Lionel Messi', 'copaamerica2021', true),
    (3, 'marcelo.gallardo@example.com', 'Marcelo Gallardo', 'riverplate123', true);

ALTER SEQUENCE users_id_seq RESTART WITH 4;

INSERT INTO posts(id, authorId, text, image) VALUES
    (1, 3, 'Well played, another victory! thank you players of River Plate for your effort!', decode('013d7d16d7ad4fefb61bd95b765c8ceb', 'hex'));

ALTER SEQUENCE posts_id_seq RESTART WITH 2;

INSERT INTO comments(id, postId, authorId, text) VALUES
    (1, 1, 1, 'You are the best!'),
    (2, 1, 2, 'Amazing!!'),
    (3, 1, 3, 'Thanks :)');

ALTER SEQUENCE comments_id_seq RESTART WITH 4;
