CREATE TABLE posts
(
    id           BIGSERIAL PRIMARY KEY,
    title        VARCHAR(255) NOT NULL,
    content      TEXT,
    author_id    BIGINT       NOT NULL,
    community_id BIGINT       NOT NULL,
    score        INT          NOT NULL DEFAULT 0,
    created_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP,

    CONSTRAINT fk_posts_author
        FOREIGN KEY (author_id) REFERENCES users (id),

    CONSTRAINT fk_posts_community
        FOREIGN KEY (community_id) REFERENCES communities (id)
);
