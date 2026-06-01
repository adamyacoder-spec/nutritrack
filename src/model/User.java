package model;

public class User {
    private int id;
    private String name;
    private int age;
    private String gender;
    private double weight;
    private double height;
    private String goal;
    private String activityLevel;

    public User(String name, int age, String gender, double weight, double height, String goal, String activityLevel) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.weight = weight;
        this.height = height;
        this.goal = goal;
        this.activityLevel = activityLevel;
    }

    public User(int id, String name, int age, String gender, double weight, double height, String goal, String activityLevel) {
        this(name, age, gender, weight, height, goal, activityLevel);
        this.id = id;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public double getWeight() { return weight; }
    public double getHeight() { return height; }
    public String getGoal() { return goal; }
    public String getActivityLevel() { return activityLevel; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
    public void setGender(String gender) { this.gender = gender; }
    public void setWeight(double weight) { this.weight = weight; }
    public void setHeight(double height) { this.height = height; }
    public void setGoal(String goal) { this.goal = goal; }
    public void setActivityLevel(String activityLevel) { this.activityLevel = activityLevel; }

    @Override
    public String toString() {
        return "User{id=" + id + ", name='" + name + "', age=" + age +
               ", gender='" + gender + "', weight=" + weight + "kg, height=" + height +
               "cm, goal='" + goal + "', activityLevel='" + activityLevel + "'}";
    }
}
