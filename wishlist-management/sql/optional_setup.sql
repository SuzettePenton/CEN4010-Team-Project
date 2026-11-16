-- Optional: Spring creates these tables for you (ddl-auto=update).
-- Run this only if you want to see/create them by hand in Workbench.
CREATE DATABASE IF NOT EXISTS wishlist_db;
USE wishlist_db;

CREATE TABLE IF NOT EXISTS wish_lists (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  name VARCHAR(100) NOT NULL,
  UNIQUE KEY uq_user_name (user_id, name)
);

CREATE TABLE IF NOT EXISTS wish_list_items (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  wish_list_id BIGINT NOT NULL,
  book_id BIGINT NOT NULL,
  UNIQUE KEY uq_list_book (wish_list_id, book_id)
);
