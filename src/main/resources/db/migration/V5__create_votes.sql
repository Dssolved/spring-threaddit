CREATE TABLE votes
(
    id      BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    post_id BIGINT NOT NULL,
    value   INT    NOT NULL CHECK (value IN (-1, 1)),

    CONSTRAINT fk_votes_user
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,

    CONSTRAINT fk_votes_post
        FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE,

    CONSTRAINT uk_votes_user_post UNIQUE (user_id, post_id)
);
