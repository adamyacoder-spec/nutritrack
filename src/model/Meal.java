package model;

import java.util.ArrayList;

/**
 * Meal model class - represents a meal containing multiple food items.
 * Maps to the 'meals' table in the database.
 * 
 * Syllabus: OOP (Unit II) - Encapsulation
 *           Collections (Unit IV) - ArrayList
 */
public class Meal {
    private int id;
    private int userId;
    private String mealName;
    private String mealDate;
    private ArrayList<FoodItem> foodList;

    // Constructor for creating new meals
    public Meal(String mealName, int userId) {
        this.mealName = mealName;
        this.userId = userId;
        this.foodList = new ArrayList<>();
    }

    // Constructor with id (for reading from database)
    public Meal(int id, String mealName, int userId, String mealDate) {
        this.id = id;
        this.mealName = mealName;
        this.userId = userId;
        this.mealDate = mealDate;
        this.foodList = new ArrayList<>();
    }

    // Add food to meal
    public void addFood(FoodItem food) {
        foodList.add(food);
    }

    // Remove food from meal
    public void removeFood(FoodItem food) {
        foodList.remove(food);
    }

    // Get total calories in this meal
    public int getTotalCalories() {
        int total = 0;
        for (FoodItem food : foodList) {
            total += food.getCalories();
        }
        return total;
    }

    // Get total protein in this meal
    public double getTotalProtein() {
        double total = 0;
        for (FoodItem food : foodList) {
            total += food.getProtein();
        }
        return total;
    }

    // Get total carbs in this meal
    public double getTotalCarbs() {
        double total = 0;
        for (FoodItem food : foodList) {
            total += food.getCarbs();
        }
        return total;
    }

    // Get total fats in this meal
    public double getTotalFats() {
        double total = 0;
        for (FoodItem food : foodList) {
            total += food.getFats();
        }
        return total;
    }

    // Getters
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getMealName() { return mealName; }
    public String getMealDate() { return mealDate; }
    public ArrayList<FoodItem> getFoodList() { return foodList; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setMealName(String mealName) { this.mealName = mealName; }
    public void setMealDate(String mealDate) { this.mealDate = mealDate; }
    public void setFoodList(ArrayList<FoodItem> foodList) { this.foodList = foodList; }

    @Override
    public String toString() {
        return "Meal{id=" + id + ", name='" + mealName + "', userId=" + userId +
               ", date='" + mealDate + "', items=" + foodList.size() +
               ", totalCalories=" + getTotalCalories() + "}";
    }
}
