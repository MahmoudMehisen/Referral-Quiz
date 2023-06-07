CREATE TABLE question_groups(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE question_groups_seq
(
    next_val INT NOT NULL AUTO_INCREMENT PRIMARY KEY
) AUTO_INCREMENT = 50;

INSERT INTO question_groups_seq VALUES (1);

ALTER TABLE question_options ADD COLUMN selected_times INT NOT NULL DEFAULT 0;

ALTER TABLE questions ADD COLUMN group_id BIGINT NOT NULL, ADD FOREIGN KEY (group_id) REFERENCES question_groups (id);

ALTER TABLE metadata ADD COLUMN active_group_id INT;

ALTER TABLE tokens ADD COLUMN active BOOLEAN NOT NULL DEFAULT true;
