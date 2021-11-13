INSERT INTO users(id, email, name, active) VALUES
    (1, 'diego.maradona@example.com','Diego Maradona', true),
    (2, 'lionel.messi@example.com', 'Lionel Messi', true),
    (3, 'marcelo.gallardo@example.com', 'Marcelo Gallardo', true);

ALTER SEQUENCE users_id_seq RESTART WITH 4;

INSERT INTO posts(id, authorId, text, image, timestamp) VALUES
    (1, 3, 'Well played, another victory! thank you players of River Plate for your effort!', null, '2021-01-01 01:00:00');

ALTER SEQUENCE posts_id_seq RESTART WITH 2;

INSERT INTO comments(id, postId, authorId, text, timestamp) VALUES
    (1, 1, 1, 'You are the best!', '2021-01-01 08:00:00'),
    (2, 1, 2, 'Amazing!!', '2021-01-02 01:00:00'),
    (3, 1, 3, 'Thanks :)', '2021-01-03 07:00:00');

ALTER SEQUENCE comments_id_seq RESTART WITH 4;
