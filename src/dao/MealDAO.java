package dao;

import model.Meal;
import model.FoodItem;
import util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;

/**
 * MealDAO - Data Access Object for Meal CRUD operations.
 * Handles the many-to-many relationship between meals and food items.
 * 
 * Syllabus: JDBC (Unit V) - Complex queries, foreign keys, junction tables
 *           Collections (Unit IV) - ArrayList
 */
public class MealDAO {

    private FoodItemDAO foodItemDAO = new FoodItemDAO();

    /**
     * CREATE - Add a new meal to the database.
     * Also links the meal's food items via the junction table.
     */
    public int addMeal(Meal meal) {
        String sql = "INSERT INTO meals (user_id, meal_name) VALUES (?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, meal.getUserId());
            stmt.setString(2, meal.getMealName());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    int mealId = keys.getInt(1);
                    meal.setId(mealId);
                    
                    // Add each food item to the junction table
                    for (FoodItem food : meal.getFoodList()) {
                        addFoodToMeal(mealId, food.getId(), 1);
                    }
                    
                    System.out.println("Meal created! ID: " + mealId + " - " + meal.getMealName());
                    return mealId;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error adding meal: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Link a food item to a meal in the junction table.
     * 
     * @param mealId     The meal ID
     * @param foodItemId The food item ID
     * @param quantity   How many servings
     */
    public boolean addFoodToMeal(int mealId, int foodItemId, int quantity) {
        String sql = "INSERT INTO meal_food_items (meal_id, food_item_id, quantity) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, mealId);
            stmt.setInt(2, foodItemId);
            stmt.setInt(3, quantity);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error linking food to meal: " + e.getMessage());
        }
        return false;
    }

    /**
     * READ - Get all meals for a specific user, including their food items.
     * Uses JOIN to combine data from meals, meal_food_items, and food_items tables.
     */
    public ArrayList<Meal> getMealsByUserId(int userId) {
        ArrayList<Meal> meals = new ArrayList<>();
        String sql = "SELECT * FROM meals WHERE user_id = ? ORDER BY meal_date DESC";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Meal meal = new Meal(
                    rs.getInt("id"),
                    rs.getString("meal_name"),
                    rs.getInt("user_id"),
                    rs.getString("meal_date")
                );
                
                // Load the food items for this meal
                loadFoodItemsForMeal(meal);
                meals.add(meal);
            }
        } catch (SQLException e) {
            System.out.println("Error getting meals: " + e.getMessage());
        }
        return meals;
    }

    /**
     * Load food items for a specific meal from the junction table.
     * This demonstrates a JOIN query.
     */
    private void loadFoodItemsForMeal(Meal meal) {
        String sql = "SELECT f.*, mf.quantity FROM food_items f " +
                     "JOIN meal_food_items mf ON f.id = mf.food_item_id " +
                     "WHERE mf.meal_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, meal.getId());
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                FoodItem food = new FoodItem(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("calories"),
                    rs.getDouble("protein"),
                    rs.getDouble("carbs"),
                    rs.getDouble("fats")
                );
                // Add food item (quantity times) to the meal
                int quantity = rs.getInt("quantity");
                for (int i = 0; i < quantity; i++) {
                    meal.addFood(food);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error loading food items for meal: " + e.getMessage());
        }
    }

    /**
     * DELETE - Remove a meal and its food item links.
     * CASCADE in the schema handles deleting from junction table automatically.
     */
    public boolean deleteMeal(int mealId) {
        String sql = "DELETE FROM meals WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, mealId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Meal deleted! ID: " + mealId);
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error deleting meal: " + e.getMessage());
        }
        return false;
    }
}
