CREATE TABLE expense (
   id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
   price DECIMAL(19,2) NOT NULL,
   expense_date DATE NOT NULL,
   category_id BIGINT NOT NULL,
   user_id BIGINT NOT NULL,
   FOREIGN KEY (category_id) REFERENCES category(id),
   FOREIGN KEY (user_id) REFERENCES users(id)
);