import model.User;
import model.FoodItem;
import model.Meal;
import dao.UserDAO;
import dao.FoodItemDAO;
import dao.MealDAO;
import util.DatabaseUtil;
import util.CalorieCalculator;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("     NutriTrack - Nutrition Console");
        System.out.println("========================================\n");

        if (!DatabaseUtil.testConnection()) {
            System.out.println("Database connection failed. Exiting.");
            return;
        }

        UserDAO userDAO = new UserDAO();
        FoodItemDAO foodDAO = new FoodItemDAO();
        MealDAO mealDAO = new MealDAO();

        // 1. Create a user
        System.out.println("--- Creating User ---");
        User newUser = new User("Rahul Sharma", 22, "Male", 70.0, 175.0, "Gain Muscle (Lean Bulk)", "Lightly Active");
        int userId = userDAO.addUser(newUser);
        System.out.println("Saved user with ID: " + userId);

        // 2. Read user & recommend calories
        User user = userDAO.getUserById(userId);
        if (user != null) {
            double cal = CalorieCalculator.calculateCalories(
                user.getWeight(), user.getHeight(), user.getAge(),
                user.getGender(), user.getGoal(), user.getActivityLevel()
            );
            System.out.println("Recommended target: " + Math.round(cal) + " kcal/day\n");
        }

        // 3. Search and display foods
        System.out.println("--- Food List ---");
        ArrayList<FoodItem> foods = foodDAO.getAllFoodItems();
        for (FoodItem f : foods) {
            System.out.println("  " + f.getName() + " (" + f.getCaloriesPer100g() + " kcal/100g)");
        }
        System.out.println();

        // 4. Create a meal with customized gram/unit amounts
        System.out.println("--- Creating a custom meal ---");
        Meal breakfast = new Meal("Breakfast", userId);

        if (!foods.isEmpty()) {
            // Add 1.5 units of Banana
            FoodItem banana = foods.get(0);
            double bananaAmount = 1.5;
            double bananaGrams = bananaAmount * banana.getWeightPerUnit();
            double factor1 = bananaGrams / 100.0;
            Meal.MealItem item1 = new Meal.MealItem(
                banana, bananaAmount, "unit",
                (int) Math.round(banana.getCaloriesPer100g() * factor1),
                banana.getProteinPer100g() * factor1,
                banana.getCarbsPer100g() * factor1,
                banana.getFatsPer100g() * factor1
            );
            breakfast.addMealItem(item1);

            // Add 150 grams of Chicken Breast
            FoodItem chicken = foods.get(1);
            double chickenAmount = 150.0; // grams
            double factor2 = chickenAmount / 100.0;
            Meal.MealItem item2 = new Meal.MealItem(
                chicken, chickenAmount, "g",
                (int) Math.round(chicken.getCaloriesPer100g() * factor2),
                chicken.getProteinPer100g() * factor2,
                chicken.getCarbsPer100g() * factor2,
                chicken.getFatsPer100g() * factor2
            );
            breakfast.addMealItem(item2);
        }

        mealDAO.addMeal(breakfast);

        // 5. Fetch and print user meals
        System.out.println("--- Retrieving Meal History ---");
        ArrayList<Meal> userMeals = mealDAO.getMealsByUserId(userId);
        for (Meal m : userMeals) {
            System.out.println(m.getMealName() + " (Date: " + m.getMealDate() + ")");
            System.out.println("  Total calories: " + m.getTotalCalories() + " kcal");
            System.out.println("  Total protein:  " + String.format("%.1f", m.getTotalProtein()) + "g");
            for (Meal.MealItem item : m.getMealItems()) {
                System.out.println("    - " + item.getFoodItem().getName() + ": " + item.getAmount() + " " + item.getUnit() + " (" + item.getCalculatedCalories() + " kcal)");
            }
        }
        System.out.println("\nConsole demo completed successfully.");
    }
}
