import model.User;
import model.FoodItem;
import model.Meal;
import dao.UserDAO;
import dao.FoodItemDAO;
import dao.MealDAO;
import util.DatabaseUtil;
import util.CalorieCalculator;

import java.util.ArrayList;

/**
 * Main - Entry point for NutriTrack application.
 * Demonstrates complete JDBC CRUD lifecycle with MySQL.
 * 
 * ╔══════════════════════════════════════════════════════════════╗
 * ║  BEFORE RUNNING THIS PROGRAM:                               ║
 * ║  1. Make sure MySQL is running                               ║
 * ║  2. Run sql/schema.sql in MySQL to create the database       ║
 * ║  3. Update password in util/DatabaseUtil.java if needed      ║
 * ║  4. Compile with: javac -cp ".;../lib/*" *.java              ║
 * ║  5. Run with: java -cp ".;../lib/*" Main                    ║
 * ╚══════════════════════════════════════════════════════════════╝
 */
public class Main {
    public static void main(String[] args) {

        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║       NutriTrack - Nutrition Tracker     ║");
        System.out.println("║        Database Connection Demo          ║");
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.println();

        // ========================================
        // STEP 1: Test Database Connection
        // ========================================
        System.out.println("--- Step 1: Testing Database Connection ---");
        if (!DatabaseUtil.testConnection()) {
            System.out.println("\nCannot continue without database. Exiting.");
            return;
        }
        System.out.println();

        // Initialize DAOs
        UserDAO userDAO = new UserDAO();
        FoodItemDAO foodDAO = new FoodItemDAO();
        MealDAO mealDAO = new MealDAO();

        // ========================================
        // STEP 2: CREATE - Add a new user
        // ========================================
        System.out.println("--- Step 2: Creating a New User (INSERT) ---");
        User newUser = new User("Rahul Sharma", 22, "Male", 70.0, 175.0, "maintain");
        int userId = userDAO.addUser(newUser);
        System.out.println("Created: " + newUser);
        System.out.println();

        // ========================================
        // STEP 3: READ - Retrieve the user back
        // ========================================
        System.out.println("--- Step 3: Reading User from Database (SELECT) ---");
        User fetchedUser = userDAO.getUserById(userId);
        if (fetchedUser != null) {
            System.out.println("Fetched: " + fetchedUser);
        }
        System.out.println();

        // ========================================
        // STEP 4: Calculate recommended calories
        // ========================================
        System.out.println("--- Step 4: Calculating Recommended Calories ---");
        double recommendedCal = CalorieCalculator.calculateCalories(
            fetchedUser.getWeight(),
            fetchedUser.getHeight(),
            fetchedUser.getAge(),
            fetchedUser.getGender(),
            fetchedUser.getGoal()
        );
        System.out.println("Recommended daily calories for " + fetchedUser.getName() + ": " + recommendedCal + " kcal");
        System.out.println();

        // ========================================
        // STEP 5: READ - Get all food items (from sample data)
        // ========================================
        System.out.println("--- Step 5: Available Food Items (SELECT ALL) ---");
        ArrayList<FoodItem> allFoods = foodDAO.getAllFoodItems();
        System.out.println("Found " + allFoods.size() + " food items in database:");
        for (FoodItem food : allFoods) {
            System.out.println("  " + food);
        }
        System.out.println();

        // ========================================
        // STEP 6: CREATE - Add a custom food item
        // ========================================
        System.out.println("--- Step 6: Adding Custom Food Item (INSERT) ---");
        FoodItem customFood = new FoodItem("Protein Shake", 200, 30.0, 10.0, 3.0);
        foodDAO.addFoodItem(customFood);
        System.out.println("Added: " + customFood);
        System.out.println();

        // ========================================
        // STEP 7: SEARCH - Search food by name
        // ========================================
        System.out.println("--- Step 7: Searching Food Items (SELECT with LIKE) ---");
        ArrayList<FoodItem> searchResults = foodDAO.searchFoodByName("Chicken");
        System.out.println("Search results for 'Chicken':");
        for (FoodItem food : searchResults) {
            System.out.println("  " + food);
        }
        System.out.println();

        // ========================================
        // STEP 8: CREATE - Create a meal with food items
        // ========================================
        System.out.println("--- Step 8: Creating a Meal with Food Items ---");
        Meal breakfast = new Meal("Breakfast", userId);

        // Add some food items to the meal (using items from database)
        if (allFoods.size() >= 4) {
            breakfast.addFood(allFoods.get(3)); // Egg
            breakfast.addFood(allFoods.get(0)); // Banana or first item
            breakfast.addFood(allFoods.get(4)); // Milk or 5th item
        }
        
        int mealId = mealDAO.addMeal(breakfast);
        System.out.println("Created: " + breakfast);
        System.out.println("  Total Calories: " + breakfast.getTotalCalories() + " kcal");
        System.out.println("  Total Protein:  " + breakfast.getTotalProtein() + " g");
        System.out.println("  Total Carbs:    " + breakfast.getTotalCarbs() + " g");
        System.out.println("  Total Fats:     " + breakfast.getTotalFats() + " g");
        System.out.println();

        // ========================================
        // STEP 9: READ - Get all meals for the user
        // ========================================
        System.out.println("--- Step 9: Getting User's Meals (SELECT with JOIN) ---");
        ArrayList<Meal> userMeals = mealDAO.getMealsByUserId(userId);
        System.out.println("Meals for " + fetchedUser.getName() + ":");
        for (Meal meal : userMeals) {
            System.out.println("  " + meal);
            for (FoodItem food : meal.getFoodList()) {
                System.out.println("    -> " + food.getName() + " (" + food.getCalories() + " cal)");
            }
        }
        System.out.println();

        // ========================================
        // STEP 10: UPDATE - Update user's goal
        // ========================================
        System.out.println("--- Step 10: Updating User Goal (UPDATE) ---");
        fetchedUser.setGoal("gain");
        fetchedUser.setWeight(72.0);
        userDAO.updateUser(fetchedUser);
        
        // Verify the update
        User updatedUser = userDAO.getUserById(userId);
        System.out.println("Updated user: " + updatedUser);
        System.out.println();

        // ========================================
        // STEP 11: Show all users
        // ========================================
        System.out.println("--- Step 11: All Users in Database ---");
        ArrayList<User> allUsers = userDAO.getAllUsers();
        for (User u : allUsers) {
            System.out.println("  " + u);
        }
        System.out.println();

        // ========================================
        // DONE!
        // ========================================
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║   All JDBC operations completed!         ║");
        System.out.println("║                                          ║");
        System.out.println("║   Operations demonstrated:               ║");
        System.out.println("║   ✓ Connection (DriverManager)          ║");
        System.out.println("║   ✓ INSERT (PreparedStatement)          ║");
        System.out.println("║   ✓ SELECT (ResultSet)                  ║");
        System.out.println("║   ✓ SELECT with LIKE (Search)           ║");
        System.out.println("║   ✓ SELECT with JOIN (Meals+Foods)      ║");
        System.out.println("║   ✓ UPDATE                              ║");
        System.out.println("║   ✓ ArrayList (Collections)             ║");
        System.out.println("║   ✓ try-catch (Exception Handling)      ║");
        System.out.println("╚══════════════════════════════════════════╝");
    }
}
