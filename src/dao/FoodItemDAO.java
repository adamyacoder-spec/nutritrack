package dao;

import model.FoodItem;
import util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;

public class FoodItemDAO {

    public int addFoodItem(FoodItem food) {
        String sql = "INSERT INTO food_items (name, calories_per_100g, protein_per_100g, carbs_per_100g, fats_per_100g, weight_per_cup, weight_per_unit) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, food.getName());
            stmt.setInt(2, food.getCaloriesPer100g());
            stmt.setDouble(3, food.getProteinPer100g());
            stmt.setDouble(4, food.getCarbsPer100g());
            stmt.setDouble(5, food.getFatsPer100g());
            stmt.setDouble(6, food.getWeightPerCup());
            stmt.setDouble(7, food.getWeightPerUnit());

            if (stmt.executeUpdate() > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    int id = keys.getInt(1);
                    food.setId(id);
                    return id;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding food item: " + e.getMessage());
        }
        return -1;
    }

    public FoodItem getFoodItemById(int id) {
        String sql = "SELECT * FROM food_items WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new FoodItem(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("calories_per_100g"),
                        rs.getDouble("protein_per_100g"),
                        rs.getDouble("carbs_per_100g"),
                        rs.getDouble("fats_per_100g"),
                        rs.getDouble("weight_per_cup"),
                        rs.getDouble("weight_per_unit")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting food item: " + e.getMessage());
        }
        return null;
    }

    public ArrayList<FoodItem> getAllFoodItems() {
        ArrayList<FoodItem> items = new ArrayList<>();
        String sql = "SELECT * FROM food_items ORDER BY name";

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                items.add(new FoodItem(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("calories_per_100g"),
                    rs.getDouble("protein_per_100g"),
                    rs.getDouble("carbs_per_100g"),
                    rs.getDouble("fats_per_100g"),
                    rs.getDouble("weight_per_cup"),
                    rs.getDouble("weight_per_unit")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error getting food items: " + e.getMessage());
        }
        return items;
    }

    public ArrayList<FoodItem> searchFoodByName(String name) {
        ArrayList<FoodItem> items = new ArrayList<>();
        String sql = "SELECT * FROM food_items WHERE name LIKE ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + name + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    items.add(new FoodItem(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("calories_per_100g"),
                        rs.getDouble("protein_per_100g"),
                        rs.getDouble("carbs_per_100g"),
                        rs.getDouble("fats_per_100g"),
                        rs.getDouble("weight_per_cup"),
                        rs.getDouble("weight_per_unit")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching food items: " + e.getMessage());
        }
        return items;
    }

    public boolean deleteFoodItem(int id) {
        String sql = "DELETE FROM food_items WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting food item: " + e.getMessage());
        }
        return false;
    }
}
