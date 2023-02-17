CREATE TABLE posts
(
    id          SERIAL PRIMARY KEY,
    user_id     INT NOT NULL references users,
    title       CHARACTER VARYING(60) NOT NULL,
    content     TEXT NOT NULL,
    publish_date INT NOT NULL
);

create unique index posts_title_index on posts (title);