package model;

/**
 * User model class - represents a user in the NutriTrack system.
 * Maps to the 'users' table in the database.
 * 
 * Syllabus: OOP (Unit II) - Encapsulation, Constructors, Getters/Setters
 */
public class User {
    private int id;
    private String name;
    private int age;
    private String gender;
    private double weight;
    private double height;
    private String goal;

    // Constructor without id (for creating new users)
    public User(String name, int age, String gender, double weight, double height, String goal) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.weight = weight;
        this.height = height;
        this.goal = goal;
    }

    // Constructor with id (for reading from database)
    public User(int id, String name, int age, String gender, double weight, double height, String goal) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.weight = weight;
        this.height = height;
        this.goal = goal;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public double getWeight() { return weight; }
    public double getHeight() { return height; }
    public String getGoal() { return goal; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
    public void setGender(String gender) { this.gender = gender; }
    public void setWeight(double weight) { this.weight = weight; }
    public void setHeight(double height) { this.height = height; }
    public void setGoal(String goal) { this.goal = goal; }

    @Override
    public String toString() {
        return "User{id=" + id + ", name='" + name + "', age=" + age +
               ", gender='" + gender + "', weight=" + weight +
               "kg, height=" + height + "cm, goal='" + goal + "'}";
    }
}
