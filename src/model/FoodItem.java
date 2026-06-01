package model;

public class FoodItem {
    private int id;
    private String name;
    private int caloriesPer100g;
    private double proteinPer100g;
    private double carbsPer100g;
    private double fatsPer100g;
    private double weightPerCup;
    private double weightPerUnit;

    public FoodItem(String name, int caloriesPer100g, double proteinPer100g, double carbsPer100g, double fatsPer100g, double weightPerCup, double weightPerUnit) {
        this.name = name;
        this.caloriesPer100g = caloriesPer100g;
        this.proteinPer100g = proteinPer100g;
        this.carbsPer100g = carbsPer100g;
        this.fatsPer100g = fatsPer100g;
        this.weightPerCup = weightPerCup;
        this.weightPerUnit = weightPerUnit;
    }

    public FoodItem(int id, String name, int caloriesPer100g, double proteinPer100g, double carbsPer100g, double fatsPer100g, double weightPerCup, double weightPerUnit) {
        this(name, caloriesPer100g, proteinPer100g, carbsPer100g, fatsPer100g, weightPerCup, weightPerUnit);
        this.id = id;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getCaloriesPer100g() { return caloriesPer100g; }
    public double getProteinPer100g() { return proteinPer100g; }
    public double getCarbsPer100g() { return carbsPer100g; }
    public double getFatsPer100g() { return fatsPer100g; }
    public double getWeightPerCup() { return weightPerCup; }
    public double getWeightPerUnit() { return weightPerUnit; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setCaloriesPer100g(int caloriesPer100g) { this.caloriesPer100g = caloriesPer100g; }
    public void setProteinPer100g(double proteinPer100g) { this.proteinPer100g = proteinPer100g; }
    public void setCarbsPer100g(double carbsPer100g) { this.carbsPer100g = carbsPer100g; }
    public void setFatsPer100g(double fatsPer100g) { this.fatsPer100g = fatsPer100g; }
    public void setWeightPerCup(double weightPerCup) { this.weightPerCup = weightPerCup; }
    public void setWeightPerUnit(double weightPerUnit) { this.weightPerUnit = weightPerUnit; }

    @Override
    public String toString() {
        return "FoodItem{id=" + id + ", name='" + name + "', calories/100g=" + caloriesPer100g +
               ", protein/100g=" + proteinPer100g + "g, carbs/100g=" + carbsPer100g +
               "g, fats/100g=" + fatsPer100g + "g, weight/cup=" + weightPerCup +
               "g, weight/unit=" + weightPerUnit + "g}";
    }
}
