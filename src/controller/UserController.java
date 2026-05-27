package controller;

import model.User;
import view.UserView;
import dao.UserDAO;

/**
 * UserController - MVC Controller linking User model with UserView.
 * Now also interacts with the database through UserDAO.
 * 
 * Syllabus: Packages & Interfaces (Unit II) - MVC architecture
 *           JDBC (Unit V) - Through DAO
 */
public class UserController {
    private User model;
    private UserView view;
    private UserDAO userDAO;

    public UserController(User model, UserView view) {
        this.model = model;
        this.view = view;
        this.userDAO = new UserDAO();
    }

    // Save user to database
    public int saveUser() {
        return userDAO.addUser(model);
    }

    // Load user from database
    public User loadUser(int id) {
        User user = userDAO.getUserById(id);
        if (user != null) {
            this.model = user;
        }
        return user;
    }

    // Update user in database
    public boolean updateUser() {
        return userDAO.updateUser(model);
    }

    // Delete user from database
    public boolean deleteUser(int id) {
        return userDAO.deleteUser(id);
    }

    // Display user details using the view
    public void displayUser() {
        view.displayUserDetails(model);
    }

    // Getters
    public User getModel() { return model; }
    public UserView getView() { return view; }
}
