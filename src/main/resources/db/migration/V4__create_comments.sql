CREATE TABLE comments
(
    id         BIGSERIAL PRIMARY KEY,
    content    TEXT      NOT NULL,
    author_id  BIGINT    NOT NULL,
    post_id    BIGINT    NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_comments_author
        FOREIGN KEY (author_id) REFERENCES users (id),

    CONSTRAINT fk_comments_post
        FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE
);

CREATE INDEX idx_comments_post ON comments (post_id);
