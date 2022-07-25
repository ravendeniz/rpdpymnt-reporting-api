CREATE TABLE USER_PROFILE (
  ID                    int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  EMAIL                 VARCHAR(50),
  NAME                  VARCHAR(50),
  PASSWORD              VARCHAR(255),
  ROLES                 INT,
  CREATED_AT            timestamp,
  UPDATED_AT            timestamp
);

INSERT INTO USER_PROFILE (ID, EMAIL, NAME, PASSWORD, ROLES, CREATED_AT, UPDATED_AT)
VALUES (1, 'cs.denizkarakaya@gmail.com', 'Deniz Karakaya', '$2a$10$9mNkNNgUsoYryShxfqG4suiO5VWA/S4Ek2dBR9QLfodSuNmakg99.', 1, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
