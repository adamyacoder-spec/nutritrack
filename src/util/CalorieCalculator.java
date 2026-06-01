package util;

public class CalorieCalculator {

    public static double calculateCalories(double weight, double height, int age, String gender, String goal, String activityLevel) {
        double bmr;

        // Mifflin-St Jeor BMR calculation
        if (gender != null && gender.equalsIgnoreCase("Male")) {
            bmr = 10.0 * weight + 6.25 * height - 5.0 * age + 5.0;
        } else {
            bmr = 10.0 * weight + 6.25 * height - 5.0 * age - 161.0;
        }

        // Apply activity level factor
        double activityFactor = 1.2; // default to Sedentary
        if (activityLevel != null) {
            switch (activityLevel.toLowerCase()) {
                case "lightly active":
                case "lightly_active":
                    activityFactor = 1.375;
                    break;
                case "moderately active":
                case "moderately_active":
                    activityFactor = 1.55;
                    break;
                case "very active":
                case "very_active":
                    activityFactor = 1.725;
                    break;
                case "sedentary":
                default:
                    activityFactor = 1.2;
                    break;
            }
        }

        double tdee = bmr * activityFactor;

        // Adjust for goal
        double offset = 0;
        if (goal != null) {
            String lowerGoal = goal.toLowerCase();
            if (lowerGoal.contains("lose") && lowerGoal.contains("aggressive")) {
                offset = -700.0;
            } else if (lowerGoal.contains("lose")) {
                offset = -350.0;
            } else if (lowerGoal.contains("muscle") || (lowerGoal.contains("gain") && lowerGoal.contains("lean"))) {
                offset = 300.0;
            } else if (lowerGoal.contains("gain")) {
                offset = 500.0;
            }
        }

        return Math.max(1200.0, tdee + offset); // set a safe minimum floor of 1200 kcal
    }
}
