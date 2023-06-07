CREATE TABLE redeems
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    points_to_redeem INT NOT NULL,
    redeem_name VARCHAR(256) NOT NULL,
    is_available BOOLEAN NOT NULL
);

CREATE TABLE redeems_seq
(
    next_val INT NOT NULL AUTO_INCREMENT PRIMARY KEY
) AUTO_INCREMENT = 50;

INSERT INTO redeems_seq VALUES (1);

CREATE TABLE redeem_histories
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    guest_phone_number VARCHAR(256) NOT NULL,
    redeem_id BIGINT NOT NULL,
    pointsForRedeem INT NOT NULL,
    FOREIGN KEY (redeem_id) REFERENCES redeems(id),
    FOREIGN KEY (guest_phone_number) REFERENCES guests(phone_number)
);
CREATE TABLE redeem_histories_seq
(
    next_val INT NOT NULL AUTO_INCREMENT PRIMARY KEY
) AUTO_INCREMENT = 50;

INSERT INTO redeem_histories_seq VALUES (1);