package dao;

import model.FoodItem;
import util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;

/**
 * FoodItemDAO - Data Access Object for FoodItem CRUD operations.
 * 
 * Syllabus: JDBC (Unit V) - CRUD operations with PreparedStatement
 *           Collections (Unit IV) - ArrayList
 */
public class FoodItemDAO {

    /**
     * CREATE - Add a new food item to the database.
     */
    public int addFoodItem(FoodItem food) {
        String sql = "INSERT INTO food_items (name, calories, protein, carbs, fats) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, food.getName());
            stmt.setInt(2, food.getCalories());
            stmt.setDouble(3, food.getProtein());
            stmt.setDouble(4, food.getCarbs());
            stmt.setDouble(5, food.getFats());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    int id = keys.getInt(1);
                    food.setId(id);
                    System.out.println("Food item added! ID: " + id + " - " + food.getName());
                    return id;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error adding food item: " + e.getMessage());
        }
        return -1;
    }

    /**
     * READ - Get a food item by ID.
     */
    public FoodItem getFoodItemById(int id) {
        String sql = "SELECT * FROM food_items WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new FoodItem(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("calories"),
                    rs.getDouble("protein"),
                    rs.getDouble("carbs"),
                    rs.getDouble("fats")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error getting food item: " + e.getMessage());
        }
        return null;
    }

    /**
     * READ ALL - Get all food items from the database.
     */
    public ArrayList<FoodItem> getAllFoodItems() {
        ArrayList<FoodItem> foodItems = new ArrayList<>();
        String sql = "SELECT * FROM food_items ORDER BY name";
        
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                FoodItem food = new FoodItem(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("calories"),
                    rs.getDouble("protein"),
                    rs.getDouble("carbs"),
                    rs.getDouble("fats")
                );
                foodItems.add(food);
            }
        } catch (SQLException e) {
            System.out.println("Error getting food items: " + e.getMessage());
        }
        return foodItems;
    }

    /**
     * SEARCH - Search food items by name (partial match).
     */
    public ArrayList<FoodItem> searchFoodByName(String name) {
        ArrayList<FoodItem> foodItems = new ArrayList<>();
        String sql = "SELECT * FROM food_items WHERE name LIKE ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + name + "%");  // % for partial matching
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
                foodItems.add(food);
            }
        } catch (SQLException e) {
            System.out.println("Error searching food items: " + e.getMessage());
        }
        return foodItems;
    }

    /**
     * DELETE - Remove a food item from the database.
     */
    public boolean deleteFoodItem(int id) {
        String sql = "DELETE FROM food_items WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Food item deleted! ID: " + id);
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error deleting food item: " + e.getMessage());
        }
        return false;
    }
}
