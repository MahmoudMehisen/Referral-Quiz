CREATE TABLE users
(
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    email    VARCHAR(50)  NOT NULL UNIQUE,
    username VARCHAR(50)  NOT NULL UNIQUE,
    password VARCHAR(120) NOT NULL,
    role     VARCHAR(50) NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

CREATE TABLE questions
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    question_text VARCHAR(255) NOT NULL
);

CREATE TABLE questions_seq
(
    next_val INT NOT NULL AUTO_INCREMENT PRIMARY KEY
) AUTO_INCREMENT = 50;
INSERT INTO questions_seq
VALUES (1);

CREATE TABLE question_options
(
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    option_text       VARCHAR(255) NOT NULL,
    is_correct_answer BOOLEAN      NOT NULL,
    question_id       BIGINT       NOT NULL,
    FOREIGN KEY (question_id) REFERENCES questions (id)
);

CREATE TABLE question_options_seq
(
    next_val INT NOT NULL AUTO_INCREMENT PRIMARY KEY
) AUTO_INCREMENT = 50;
INSERT INTO question_options_seq
VALUES (1);



CREATE TABLE tokens
(
    token VARCHAR(500) NOT NULL PRIMARY KEY
);

CREATE TABLE guests
(
    phone_number VARCHAR(255) NOT NULL PRIMARY KEY,
    email        VARCHAR(255) UNIQUE,
    total_points INT,
    can_play     BOOLEAN
);

CREATE TABLE metadata
(
    id                       BIGINT PRIMARY KEY,
    points_per_question      INT,
    points_per_referral      INT,
    referral_expiration_time INT,
    can_user_do_referral     BOOLEAN
);

INSERT INTO users (email, username, password, role)
VALUES ('admin@example.com', 'admin', '$2a$10$4tWgG/dJaON/hZLqlLOMNuOgyoo6Ooyg.DUORM4rgGLxThtNBgT9e', 'ADMIN');
INSERT INTO metadata (id, points_per_question, points_per_referral, referral_expiration_time, can_user_do_referral)
VALUES (1, 20, 30, 21600000, true);
