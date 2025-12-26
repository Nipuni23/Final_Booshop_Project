DROP DATABASE IF EXISTS bookshop;
CREATE DATABASE bookshop;
USE bookshop;

-- Customer table with auto increment ID
CREATE TABLE customer (
                          cust_id INT AUTO_INCREMENT PRIMARY KEY,
                          cus_name VARCHAR(50) NOT NULL,
                          cus_phone CHAR(15) UNIQUE,
                          cus_email VARCHAR(100) UNIQUE NOT NULL,
                          cus_address VARCHAR(100) NOT NULL
);

-- Employee table with auto increment ID
CREATE TABLE employee (
                          emp_id INT AUTO_INCREMENT PRIMARY KEY,
                          emp_name VARCHAR(50) NOT NULL,
                          password VARCHAR(50) NOT NULL,
                          role VARCHAR(50) NOT NULL,
                          contact_number VARCHAR(10) UNIQUE NOT NULL
);

-- Item table with auto increment ID
CREATE TABLE item (
                      item_id INT AUTO_INCREMENT PRIMARY KEY,
                      item_name VARCHAR(50) NOT NULL,
                      category VARCHAR(50) NOT NULL,
                      unit_price DECIMAL(10,2) NOT NULL,
                      quantity_in_stock INT NOT NULL
);

-- Supplier table with auto increment ID
CREATE TABLE supplier (
                          sup_id INT AUTO_INCREMENT PRIMARY KEY,
                          sup_name VARCHAR(50) NOT NULL,
                          sup_email VARCHAR(100) UNIQUE NOT NULL,
                          contact_number VARCHAR(10) UNIQUE NOT NULL
);

-- Item Supplier junction table
CREATE TABLE item_supplier (
                               item_id INT NOT NULL,
                               sup_id INT NOT NULL,
                               supply_price DECIMAL(10,2) NOT NULL,
                               supply_date DATETIME NOT NULL,
                               qty INT NOT NULL,

                               PRIMARY KEY (item_id, sup_id),
                               FOREIGN KEY (item_id) REFERENCES item(item_id) ON DELETE CASCADE,
                               FOREIGN KEY (sup_id) REFERENCES supplier(sup_id) ON DELETE CASCADE
);

-- Orders table with auto increment ID
CREATE TABLE orders (
                        order_id INT AUTO_INCREMENT PRIMARY KEY,
                        order_date DATETIME NOT NULL,
                        total_amount DECIMAL(10,2) NOT NULL,
                        status ENUM('Pending','Completed','Cancelled') DEFAULT 'Pending'
);

-- Order Item junction table
CREATE TABLE order_item (
                            order_id INT NOT NULL,
                            item_id INT NOT NULL,
                            qty INT NOT NULL,
                            unit_price DECIMAL(10,2) NOT NULL,

                            PRIMARY KEY (order_id, item_id),
                            FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
                            FOREIGN KEY (item_id) REFERENCES item(item_id) ON DELETE CASCADE
);

-- Users table with auto increment ID
CREATE TABLE users (
                       user_id INT AUTO_INCREMENT PRIMARY KEY,
                       user_name VARCHAR(50) NOT NULL,
                       user_password VARCHAR(50) NOT NULL,
                       user_status ENUM('active','inactive') DEFAULT 'active'
);

-- Insert default admin user
INSERT INTO users (user_name, user_password, user_status)
VALUES ('admin','admin','active');

-- Payment table with auto increment ID
CREATE TABLE payment (
                         payment_id INT AUTO_INCREMENT PRIMARY KEY,
                         order_id INT NOT NULL,
                         payment_method ENUM('Cash','Card','Online') NOT NULL,
                         amount DECIMAL(10,2) NOT NULL,
                         payment_date DATETIME DEFAULT CURRENT_TIMESTAMP,

                         FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE
);