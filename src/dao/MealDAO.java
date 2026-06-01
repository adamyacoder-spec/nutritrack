package dao;

import model.Meal;
import model.FoodItem;
import util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;

public class MealDAO {

    public int addMeal(Meal meal) {
        String sql = "INSERT INTO meals (user_id, meal_name, meal_date) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, meal.getUserId());
            stmt.setString(2, meal.getMealName());
            stmt.setString(3, meal.getMealDate());

            if (stmt.executeUpdate() > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    int mealId = keys.getInt(1);
                    meal.setId(mealId);

                    for (Meal.MealItem item : meal.getMealItems()) {
                        addFoodToMeal(mealId, item);
                    }
                    return mealId;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding meal: " + e.getMessage());
        }
        return -1;
    }

    public boolean addFoodToMeal(int mealId, Meal.MealItem item) {
        String sql = "INSERT INTO meal_food_items (meal_id, food_item_id, amount, unit, calculated_calories, calculated_protein, calculated_carbs, calculated_fats) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, mealId);
            stmt.setInt(2, item.getFoodItem().getId());
            stmt.setDouble(3, item.getAmount());
            stmt.setString(4, item.getUnit());
            stmt.setInt(5, item.getCalculatedCalories());
            stmt.setDouble(6, item.getCalculatedProtein());
            stmt.setDouble(7, item.getCalculatedCarbs());
            stmt.setDouble(8, item.getCalculatedFats());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error linking food to meal: " + e.getMessage());
        }
        return false;
    }

    public ArrayList<Meal> getMealsByUserId(int userId) {
        ArrayList<Meal> meals = new ArrayList<>();
        String sql = "SELECT * FROM meals WHERE user_id = ? ORDER BY meal_date DESC";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Meal meal = new Meal(
                        rs.getInt("id"),
                        rs.getString("meal_name"),
                        rs.getInt("user_id"),
                        rs.getString("meal_date")
                    );
                    loadFoodItems(meal);
                    meals.add(meal);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting meals: " + e.getMessage());
        }
        return meals;
    }

    public ArrayList<Meal> getMealsByUserIdAndDate(int userId, String date) {
        ArrayList<Meal> meals = new ArrayList<>();
        String sql = "SELECT * FROM meals WHERE user_id = ? AND meal_date = ? ORDER BY id";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setString(2, date);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Meal meal = new Meal(
                        rs.getInt("id"),
                        rs.getString("meal_name"),
                        rs.getInt("user_id"),
                        rs.getString("meal_date")
                    );
                    loadFoodItems(meal);
                    meals.add(meal);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting meals by date: " + e.getMessage());
        }
        return meals;
    }

    private void loadFoodItems(Meal meal) {
        String sql = "SELECT f.*, mf.amount, mf.unit, mf.calculated_calories, mf.calculated_protein, mf.calculated_carbs, mf.calculated_fats " +
                     "FROM food_items f " +
                     "JOIN meal_food_items mf ON f.id = mf.food_item_id " +
                     "WHERE mf.meal_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, meal.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    FoodItem food = new FoodItem(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("calories_per_100g"),
                        rs.getDouble("protein_per_100g"),
                        rs.getDouble("carbs_per_100g"),
                        rs.getDouble("fats_per_100g"),
                        rs.getDouble("weight_per_cup"),
                        rs.getDouble("weight_per_unit")
                    );

                    Meal.MealItem item = new Meal.MealItem(
                        food,
                        rs.getDouble("amount"),
                        rs.getString("unit"),
                        rs.getInt("calculated_calories"),
                        rs.getDouble("calculated_protein"),
                        rs.getDouble("calculated_carbs"),
                        rs.getDouble("calculated_fats")
                    );
                    meal.addMealItem(item);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading food items for meal: " + e.getMessage());
        }
    }

    public boolean deleteMeal(int mealId) {
        String sql = "DELETE FROM meals WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, mealId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting meal: " + e.getMessage());
        }
        return false;
    }
}
