-- ============================================
-- NutriTrack Database Schema
-- Database: nutritrack
-- ============================================

-- Create the database
CREATE DATABASE IF NOT EXISTS nutritrack;
USE nutritrack;

-- ============================================
-- 1. USERS TABLE
-- Stores user profile information
-- ============================================
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    gender VARCHAR(10),
    weight DOUBLE,
    height DOUBLE,
    goal VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- 2. FOOD ITEMS TABLE
-- Stores nutritional data for food items
-- ============================================
CREATE TABLE IF NOT EXISTS food_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    calories INT NOT NULL,
    protein DOUBLE DEFAULT 0,
    carbs DOUBLE DEFAULT 0,
    fats DOUBLE DEFAULT 0
);

-- ============================================
-- 3. MEALS TABLE
-- Stores meals created by users
-- ============================================
CREATE TABLE IF NOT EXISTS meals (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    meal_name VARCHAR(100) NOT NULL,
    meal_date DATE DEFAULT (CURRENT_DATE),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- ============================================
-- 4. MEAL_FOOD_ITEMS (Junction Table)
-- Links meals to food items (many-to-many)
-- ============================================
CREATE TABLE IF NOT EXISTS meal_food_items (
    meal_id INT NOT NULL,
    food_item_id INT NOT NULL,
    quantity INT DEFAULT 1,
    PRIMARY KEY (meal_id, food_item_id),
    FOREIGN KEY (meal_id) REFERENCES meals(id) ON DELETE CASCADE,
    FOREIGN KEY (food_item_id) REFERENCES food_items(id) ON DELETE CASCADE
);

-- ============================================
-- Sample Data (for testing)
-- ============================================
INSERT INTO food_items (name, calories, protein, carbs, fats) VALUES
    ('Banana', 105, 1.3, 27.0, 0.4),
    ('Chicken Breast', 165, 31.0, 0.0, 3.6),
    ('Brown Rice (1 cup)', 216, 5.0, 45.0, 1.8),
    ('Egg (Boiled)', 78, 6.3, 0.6, 5.3),
    ('Milk (1 glass)', 149, 8.0, 12.0, 8.0),
    ('Apple', 95, 0.5, 25.0, 0.3),
    ('Paneer (100g)', 265, 18.3, 1.2, 20.8),
    ('Chapati', 104, 3.0, 18.0, 3.7),
    ('Dal (1 bowl)', 180, 12.0, 30.0, 1.5),
    ('Oats (1 bowl)', 154, 5.0, 27.0, 2.6);
