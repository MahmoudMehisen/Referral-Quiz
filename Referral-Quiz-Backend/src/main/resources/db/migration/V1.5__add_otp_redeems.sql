CREATE TABLE otp_redeems
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    otp_token VARCHAR(255),
    guest_phone_number VARCHAR(256) NOT NULL,
    redeem_id BIGINT NOT NULL,
    expiry_date TIMESTAMP,
    FOREIGN KEY (redeem_id) REFERENCES redeems(id),
    FOREIGN KEY (guest_phone_number) REFERENCES guests(phone_number)
);
CREATE TABLE otp_redeems_seq
(
    next_val INT NOT NULL AUTO_INCREMENT PRIMARY KEY
) AUTO_INCREMENT = 50;

INSERT INTO otp_redeems_seq VALUES (1);