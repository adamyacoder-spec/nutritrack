-- ============================================
-- NutriTrack Database Schema
-- Database: nutritrack
-- ============================================

DROP DATABASE IF EXISTS nutritrack;
CREATE DATABASE nutritrack;
USE nutritrack;

-- ============================================
-- 1. USERS TABLE
-- Stores user profile and goal details
-- ============================================
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    gender VARCHAR(10),
    weight DOUBLE NOT NULL,
    height DOUBLE NOT NULL,
    goal VARCHAR(50) NOT NULL,
    activity_level VARCHAR(50) DEFAULT 'Sedentary',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- 2. FOOD ITEMS TABLE
-- Stores nutritional details per 100 grams
-- along with standard unit and cup weights in grams
-- ============================================
CREATE TABLE food_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    calories_per_100g INT NOT NULL,
    protein_per_100g DOUBLE DEFAULT 0,
    carbs_per_100g DOUBLE DEFAULT 0,
    fats_per_100g DOUBLE DEFAULT 0,
    weight_per_cup DOUBLE DEFAULT 0,   -- Gram weight of 1 cup (0 if not applicable)
    weight_per_unit DOUBLE DEFAULT 0   -- Gram weight of 1 piece/unit (0 if not applicable)
);

-- ============================================
-- 3. MEALS TABLE
-- Stores meal headers
-- ============================================
CREATE TABLE meals (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    meal_name VARCHAR(100) NOT NULL,
    meal_date DATE DEFAULT (CURRENT_DATE),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- ============================================
-- 4. MEAL_FOOD_ITEMS (Junction Table)
-- Links meals to food items with specific quantity and pre-calculated macros
-- ============================================
CREATE TABLE meal_food_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    meal_id INT NOT NULL,
    food_item_id INT NOT NULL,
    amount DOUBLE NOT NULL,              -- e.g., 1.5 cups, 150 grams
    unit VARCHAR(20) NOT NULL,           -- 'g', 'cup', 'unit'
    calculated_calories INT NOT NULL,
    calculated_protein DOUBLE NOT NULL,
    calculated_carbs DOUBLE NOT NULL,
    calculated_fats DOUBLE NOT NULL,
    FOREIGN KEY (meal_id) REFERENCES meals(id) ON DELETE CASCADE,
    FOREIGN KEY (food_item_id) REFERENCES food_items(id) ON DELETE CASCADE
);

-- ============================================
-- Sample Data (Pre-seeded Food Items)
-- ============================================
INSERT INTO food_items (name, calories_per_100g, protein_per_100g, carbs_per_100g, fats_per_100g, weight_per_cup, weight_per_unit) VALUES
    ('Banana', 89, 1.1, 22.8, 0.3, 150.0, 120.0),        -- 1 piece ~ 120g
    ('Chicken Breast', 165, 31.0, 0.0, 3.6, 140.0, 150.0), -- 1 piece ~ 150g
    ('Brown Rice', 111, 2.6, 23.0, 0.9, 195.0, 0.0),      -- 1 cup ~ 195g
    ('Egg (Boiled)', 155, 12.6, 1.1, 10.6, 136.0, 50.0),   -- 1 large egg ~ 50g
    ('Milk (Whole)', 62, 3.2, 4.8, 3.3, 244.0, 0.0),      -- 1 cup ~ 244g
    ('Apple', 52, 0.3, 14.0, 0.2, 110.0, 182.0),          -- 1 medium apple ~ 182g
    ('Paneer', 265, 18.3, 1.2, 20.8, 120.0, 20.0),        -- 1 cube/slice ~ 20g
    ('Chapati', 275, 8.0, 55.0, 3.0, 0.0, 40.0),          -- 1 chapati ~ 40g
    ('Dal (Lentils)', 90, 6.0, 15.0, 0.8, 240.0, 0.0),    -- 1 cup ~ 240g
    ('Oats (Cooked)', 71, 2.5, 12.0, 1.5, 234.0, 0.0);    -- 1 cup ~ 234g
