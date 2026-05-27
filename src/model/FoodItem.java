package model;

/**
 * FoodItem model class - represents a food item with nutritional data.
 * Maps to the 'food_items' table in the database.
 * 
 * Syllabus: OOP (Unit II) - Encapsulation, Constructors
 */
public class FoodItem {
    private int id;
    private String name;
    private int calories;
    private double protein;
    private double carbs;
    private double fats;

    // Constructor without id (for creating new food items)
    public FoodItem(String name, int calories, double protein, double carbs, double fats) {
        this.name = name;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fats = fats;
    }

    // Constructor with id (for reading from database)
    public FoodItem(int id, String name, int calories, double protein, double carbs, double fats) {
        this.id = id;
        this.name = name;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fats = fats;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public int getCalories() { return calories; }
    public double getProtein() { return protein; }
    public double getCarbs() { return carbs; }
    public double getFats() { return fats; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setCalories(int calories) { this.calories = calories; }
    public void setProtein(double protein) { this.protein = protein; }
    public void setCarbs(double carbs) { this.carbs = carbs; }
    public void setFats(double fats) { this.fats = fats; }

    @Override
    public String toString() {
        return "FoodItem{id=" + id + ", name='" + name + "', calories=" + calories +
               ", protein=" + protein + "g, carbs=" + carbs + "g, fats=" + fats + "g}";
    }
}
