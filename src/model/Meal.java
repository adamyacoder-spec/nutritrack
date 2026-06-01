package model;

import java.util.ArrayList;

public class Meal {
    private int id;
    private int userId;
    private String mealName;
    private String mealDate;
    private ArrayList<MealItem> mealItems;

    public static class MealItem {
        private FoodItem foodItem;
        private double amount;
        private String unit; // "g", "cup", "unit"
        private int calculatedCalories;
        private double calculatedProtein;
        private double calculatedCarbs;
        private double calculatedFats;

        public MealItem(FoodItem foodItem, double amount, String unit, int calculatedCalories, double calculatedProtein, double calculatedCarbs, double calculatedFats) {
            this.foodItem = foodItem;
            this.amount = amount;
            this.unit = unit;
            this.calculatedCalories = calculatedCalories;
            this.calculatedProtein = calculatedProtein;
            this.calculatedCarbs = calculatedCarbs;
            this.calculatedFats = calculatedFats;
        }

        public FoodItem getFoodItem() { return foodItem; }
        public double getAmount() { return amount; }
        public String getUnit() { return unit; }
        public int getCalculatedCalories() { return calculatedCalories; }
        public double getCalculatedProtein() { return calculatedProtein; }
        public double getCalculatedCarbs() { return calculatedCarbs; }
        public double getCalculatedFats() { return calculatedFats; }
    }

    public Meal(String mealName, int userId) {
        this.mealName = mealName;
        this.userId = userId;
        this.mealDate = java.time.LocalDate.now().toString();
        this.mealItems = new ArrayList<>();
    }

    public Meal(int id, String mealName, int userId, String mealDate) {
        this(mealName, userId);
        this.id = id;
        this.mealDate = mealDate;
    }

    public void addMealItem(MealItem item) { mealItems.add(item); }
    public void removeMealItem(MealItem item) { mealItems.remove(item); }

    public int getTotalCalories() {
        int total = 0;
        for (MealItem item : mealItems) total += item.getCalculatedCalories();
        return total;
    }

    public double getTotalProtein() {
        double total = 0;
        for (MealItem item : mealItems) total += item.getCalculatedProtein();
        return total;
    }

    public double getTotalCarbs() {
        double total = 0;
        for (MealItem item : mealItems) total += item.getCalculatedCarbs();
        return total;
    }

    public double getTotalFats() {
        double total = 0;
        for (MealItem item : mealItems) total += item.getCalculatedFats();
        return total;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getMealName() { return mealName; }
    public String getMealDate() { return mealDate; }
    public ArrayList<MealItem> getMealItems() { return mealItems; }

    public void setId(int id) { this.id = id; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setMealName(String mealName) { this.mealName = mealName; }
    public void setMealDate(String mealDate) { this.mealDate = mealDate; }
    public void setMealItems(ArrayList<MealItem> mealItems) { this.mealItems = mealItems; }

    @Override
    public String toString() {
        return "Meal{id=" + id + ", name='" + mealName + "', userId=" + userId +
               ", date='" + mealDate + "', items=" + mealItems.size() +
               ", totalCalories=" + getTotalCalories() + "}";
    }
}
