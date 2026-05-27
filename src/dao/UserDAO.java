package dao;

import model.User;
import util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;

/**
 * UserDAO - Data Access Object for User CRUD operations.
 * 
 * Syllabus: JDBC (Unit V) - Querying, Inserting, Updating, Deleting records
 *           Exception Handling (Unit III) - try-catch with SQLException
 *           Collections (Unit IV) - ArrayList for storing results
 * 
 * CRUD = Create, Read, Update, Delete
 */
public class UserDAO {

    /**
     * CREATE - Insert a new user into the database.
     * Uses PreparedStatement to prevent SQL injection.
     * 
     * @param user The User object to insert
     * @return The generated ID of the new user, or -1 if failed
     */
    public int addUser(User user) {
        String sql = "INSERT INTO users (name, age, gender, weight, height, goal) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            // Set values for each ? placeholder
            stmt.setString(1, user.getName());
            stmt.setInt(2, user.getAge());
            stmt.setString(3, user.getGender());
            stmt.setDouble(4, user.getWeight());
            stmt.setDouble(5, user.getHeight());
            stmt.setString(6, user.getGoal());
            
            // Execute the INSERT statement
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                // Get the auto-generated ID
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    int id = keys.getInt(1);
                    user.setId(id);
                    System.out.println("User added successfully! ID: " + id);
                    return id;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error adding user: " + e.getMessage());
        }
        return -1;
    }

    /**
     * READ - Get a single user by their ID.
     * Uses ResultSet to read data from the query result.
     * 
     * @param id The user ID to search for
     * @return User object if found, null if not found
     */
    public User getUserById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            // Execute the SELECT query
            ResultSet rs = stmt.executeQuery();
            
            // If a row is found, create a User object from it
            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("age"),
                    rs.getString("gender"),
                    rs.getDouble("weight"),
                    rs.getDouble("height"),
                    rs.getString("goal")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error getting user: " + e.getMessage());
        }
        return null;
    }

    /**
     * READ ALL - Get all users from the database.
     * Returns an ArrayList (Unit IV - Collections).
     * 
     * @return ArrayList of all User objects
     */
    public ArrayList<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            // Loop through all rows in the result
            while (rs.next()) {
                User user = new User(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("age"),
                    rs.getString("gender"),
                    rs.getDouble("weight"),
                    rs.getDouble("height"),
                    rs.getString("goal")
                );
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Error getting all users: " + e.getMessage());
        }
        return users;
    }

    /**
     * UPDATE - Update an existing user's data.
     * 
     * @param user The User object with updated data (must have valid ID)
     * @return true if update was successful
     */
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET name=?, age=?, gender=?, weight=?, height=?, goal=? WHERE id=?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, user.getName());
            stmt.setInt(2, user.getAge());
            stmt.setString(3, user.getGender());
            stmt.setDouble(4, user.getWeight());
            stmt.setDouble(5, user.getHeight());
            stmt.setString(6, user.getGoal());
            stmt.setInt(7, user.getId());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User updated successfully!");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error updating user: " + e.getMessage());
        }
        return false;
    }

    /**
     * DELETE - Remove a user from the database.
     * 
     * @param id The ID of the user to delete
     * @return true if deletion was successful
     */
    public boolean deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User deleted successfully! ID: " + id);
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error deleting user: " + e.getMessage());
        }
        return false;
    }
}
