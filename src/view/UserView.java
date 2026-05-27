package view;

import java.util.Scanner;

/**
 * UserView - Console-based view for user input/output.
 * 
 * Syllabus: Input/Output (Unit III) - Reading from console
 *           MVC Pattern - View layer
 */
public class UserView {
    private Scanner scanner;

    public UserView() {
        this.scanner = new Scanner(System.in);
    }

    public String getName() {
        System.out.print("Enter your name: ");
        return scanner.nextLine();
    }

    public String getGender() {
        System.out.print("Enter gender (Male/Female): ");
        return scanner.nextLine();
    }

    public int getAge() {
        System.out.print("Enter your age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // consume newline
        return age;
    }

    public double getWeight() {
        System.out.print("Enter your weight (kg): ");
        double weight = scanner.nextDouble();
        scanner.nextLine();
        return weight;
    }

    public double getHeight() {
        System.out.print("Enter your height (cm): ");
        double height = scanner.nextDouble();
        scanner.nextLine();
        return height;
    }

    public String getGoal() {
        System.out.print("Enter your goal (lose/gain/maintain): ");
        return scanner.nextLine();
    }

    public void displayUserDetails(model.User user) {
        System.out.println("\n===== User Profile =====");
        System.out.println("ID:     " + user.getId());
        System.out.println("Name:   " + user.getName());
        System.out.println("Age:    " + user.getAge());
        System.out.println("Gender: " + user.getGender());
        System.out.println("Weight: " + user.getWeight() + " kg");
        System.out.println("Height: " + user.getHeight() + " cm");
        System.out.println("Goal:   " + user.getGoal());
        System.out.println("========================\n");
    }

    public void displayMessage(String message) {
        System.out.println(message);
    }

    public void close() {
        scanner.close();
    }
}
