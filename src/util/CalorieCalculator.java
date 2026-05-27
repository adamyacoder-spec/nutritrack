package util;

/**
 * CalorieCalculator - Calculates daily calorie needs using BMR formula.
 * 
 * Uses the Mifflin-St Jeor equation (most accurate BMR formula):
 * - Men:   BMR = 10 * weight(kg) + 6.25 * height(cm) - 5 * age - 161 + 166
 * - Women: BMR = 10 * weight(kg) + 6.25 * height(cm) - 5 * age - 161
 * 
 * Then adjusts based on the user's goal:
 * - "lose"     -> BMR - 500 calories (deficit)
 * - "gain"     -> BMR + 500 calories (surplus) 
 * - "maintain" -> BMR (no change)
 */
public class CalorieCalculator {

    /**
     * Calculate recommended daily calories based on user data and goal.
     *
     * @param weight User's weight in kg
     * @param height User's height in cm
     * @param age    User's age in years
     * @param gender "Male" or "Female"
     * @param goal   "lose", "gain", or "maintain"
     * @return Recommended daily calorie intake
     */
    public static double calculateCalories(double weight, double height, int age, String gender, String goal) {
        double bmr;

        // Calculate BMR based on gender
        if (gender.equalsIgnoreCase("Male")) {
            bmr = 10 * weight + 6.25 * height - 5 * age + 5;
        } else {
            bmr = 10 * weight + 6.25 * height - 5 * age - 161;
        }

        // Adjust BMR based on goal
        switch (goal.toLowerCase()) {
            case "lose":
                return bmr - 500;
            case "gain":
                return bmr + 500;
            case "maintain":
            default:
                return bmr;
        }
    }
}
