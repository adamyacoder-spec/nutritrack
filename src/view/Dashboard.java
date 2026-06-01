package view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import dao.UserDAO;
import dao.FoodItemDAO;
import dao.MealDAO;
import model.User;
import model.FoodItem;
import model.Meal;
import util.CalorieCalculator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Dashboard extends Application {

    private UserDAO userDAO = new UserDAO();
    private FoodItemDAO foodDAO = new FoodItemDAO();
    private MealDAO mealDAO = new MealDAO();
    private ArrayList<FoodItem> cachedFoods = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("NutriTrack - Nutrition & Goal Tracker");

        // Cache the list of foods for dropdowns
        reloadCachedFoods();

        TabPane tabPane = new TabPane();
        tabPane.getStyleClass().add("tab-pane");

        Tab homeTab = new Tab("Home", createHomePane());
        Tab userTab = new Tab("Profile & Goals", createUserPane());
        Tab foodTab = new Tab("Food Database", createFoodPane());
        Tab mealTab = new Tab("Log Meals", createMealPane());
        Tab trackerTab = new Tab("Daily Tracker", createDailyTrackerPane());

        homeTab.setClosable(false);
        userTab.setClosable(false);
        foodTab.setClosable(false);
        mealTab.setClosable(false);
        trackerTab.setClosable(false);

        tabPane.getTabs().addAll(homeTab, userTab, foodTab, mealTab, trackerTab);

        Scene scene = new Scene(tabPane, 980, 720);
        // Load the external CSS file
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void reloadCachedFoods() {
        cachedFoods = foodDAO.getAllFoodItems();
    }

    private FoodItem getFoodFromCache(int id) {
        for (FoodItem f : cachedFoods) {
            if (f.getId() == id) return f;
        }
        return null;
    }

    private VBox createHomePane() {
        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(40));
        vbox.setAlignment(Pos.CENTER);
        vbox.getStyleClass().add("card");

        Label title = new Label("NutriTrack");
        title.getStyleClass().add("main-title");

        Label subtitle = new Label("Smart Personal Nutrition & Calorie Assistant");
        subtitle.getStyleClass().add("subtitle");

        Label info = new Label("Take control of your diet. Define goals, record meals by grams/cups, and track daily progress.");
        info.setStyle("-fx-font-size: 14px; -fx-text-fill: #475569; -fx-text-alignment: center;");

        Separator sep = new Separator();
        sep.setPadding(new Insets(10, 0, 10, 0));

        Label statsLabel = new Label("Database Statistics");
        statsLabel.getStyleClass().add("section-heading");

        HBox statsBox = new HBox(40);
        statsBox.setAlignment(Pos.CENTER);

        VBox userStat = new VBox(5, new Label("Total Profiles"), new Label(String.valueOf(userDAO.getAllUsers().size())));
        userStat.setAlignment(Pos.CENTER);
        userStat.setStyle("-fx-background-color: #f8fafc; -fx-padding: 15; -fx-background-radius: 8; -fx-border-color: #e2e8f0; -fx-border-radius: 8;");
        userStat.getChildren().get(1).setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #10b981;");

        VBox foodStat = new VBox(5, new Label("Food Database"), new Label(String.valueOf(cachedFoods.size())));
        foodStat.setAlignment(Pos.CENTER);
        foodStat.setStyle("-fx-background-color: #f8fafc; -fx-padding: 15; -fx-background-radius: 8; -fx-border-color: #e2e8f0; -fx-border-radius: 8;");
        foodStat.getChildren().get(1).setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #10b981;");

        statsBox.getChildren().addAll(userStat, foodStat);

        vbox.getChildren().addAll(title, subtitle, sep, info, statsLabel, statsBox);
        return vbox;
    }

    private VBox createUserPane() {
        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(25));

        VBox formCard = new VBox(12);
        formCard.getStyleClass().add("card");

        Label heading = new Label("Profile Setup & Calorie Recommendation");
        heading.getStyleClass().add("section-heading");

        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(12);

        TextField nameField = new TextField();
        nameField.setPromptText("Enter full name");
        
        TextField ageField = new TextField();
        ageField.setPromptText("Enter age in years");
        
        ComboBox<String> genderBox = new ComboBox<>();
        genderBox.getItems().addAll("Male", "Female");
        genderBox.setValue("Male");
        
        TextField weightField = new TextField();
        weightField.setPromptText("Weight in kg");
        
        TextField heightField = new TextField();
        heightField.setPromptText("Height in cm");
        
        ComboBox<String> goalBox = new ComboBox<>();
        goalBox.getItems().addAll(
            "Lose Weight (Healthy Deficit)",
            "Lose Weight (Aggressive Deficit)",
            "Maintain Weight",
            "Gain Muscle (Lean Bulk)",
            "Gain Weight (Active Surplus)"
        );
        goalBox.setValue("Maintain Weight");

        ComboBox<String> activityBox = new ComboBox<>();
        activityBox.getItems().addAll(
            "Sedentary (little or no exercise)",
            "Lightly Active (exercise 1-3 days/wk)",
            "Moderately Active (exercise 3-5 days/wk)",
            "Very Active (exercise 6-7 days/wk)"
        );
        activityBox.setValue("Sedentary (little or no exercise)");

        form.add(new Label("Name:"), 0, 0);          form.add(nameField, 1, 0);
        form.add(new Label("Age:"), 0, 1);           form.add(ageField, 1, 1);
        form.add(new Label("Gender:"), 0, 2);        form.add(genderBox, 1, 2);
        form.add(new Label("Weight (kg):"), 2, 0);   form.add(weightField, 3, 0);
        form.add(new Label("Height (cm):"), 2, 1);   form.add(heightField, 3, 1);
        form.add(new Label("Target Goal:"), 0, 3);    form.add(goalBox, 1, 3, 3, 1);
        form.add(new Label("Activity Level:"), 0, 4);form.add(activityBox, 1, 4, 3, 1);

        Button addBtn = new Button("Register User");
        addBtn.getStyleClass().add("btn-primary");
        
        Button calcBtn = new Button("Calculate Needs");
        calcBtn.getStyleClass().add("btn-secondary");

        Label statusLabel = new Label("");
        Label calorieLabel = new Label("");

        TextArea userListArea = new TextArea();
        userListArea.setEditable(false);
        userListArea.setPrefHeight(150);
        refreshUserList(userListArea);

        addBtn.setOnAction(e -> {
            try {
                String name = nameField.getText().trim();
                int age = Integer.parseInt(ageField.getText().trim());
                String gender = genderBox.getValue();
                double weight = Double.parseDouble(weightField.getText().trim());
                double height = Double.parseDouble(heightField.getText().trim());
                String goal = goalBox.getValue();
                String activity = activityBox.getValue().split(" \\(")[0]; // parse clean name

                if (name.isEmpty()) { 
                    statusLabel.setText("Please enter a name!");
                    statusLabel.getStyleClass().setAll("status-error");
                    return; 
                }

                User user = new User(name, age, gender, weight, height, goal, activity);
                int id = userDAO.addUser(user);

                if (id > 0) {
                    statusLabel.setText("Success! User registered with ID: " + id);
                    statusLabel.getStyleClass().setAll("status-success");
                    nameField.clear(); ageField.clear(); weightField.clear(); heightField.clear();
                    refreshUserList(userListArea);
                } else {
                    statusLabel.setText("Failed to save user.");
                    statusLabel.getStyleClass().setAll("status-error");
                }
            } catch (NumberFormatException ex) {
                statusLabel.setText("Please enter valid positive numeric values.");
                statusLabel.getStyleClass().setAll("status-error");
            }
        });

        calcBtn.setOnAction(e -> {
            try {
                double weight = Double.parseDouble(weightField.getText().trim());
                double height = Double.parseDouble(heightField.getText().trim());
                int age = Integer.parseInt(ageField.getText().trim());
                String gender = genderBox.getValue();
                String goal = goalBox.getValue();
                String activity = activityBox.getValue().split(" \\(")[0];

                double cal = CalorieCalculator.calculateCalories(weight, height, age, gender, goal, activity);
                calorieLabel.setText("Target calories: " + String.format("%.0f", cal) + " kcal/day");
                calorieLabel.setStyle("-fx-text-fill: #10b981; -fx-font-weight: bold; -fx-font-size: 14px;");
            } catch (NumberFormatException ex) {
                calorieLabel.setText("Fill in age, weight, and height first!");
                calorieLabel.getStyleClass().setAll("status-error");
            }
        });

        formCard.getChildren().addAll(heading, form, new HBox(10, addBtn, calcBtn), statusLabel, calorieLabel);

        Label listHeading = new Label("Registered User Profiles");
        listHeading.getStyleClass().add("section-heading");

        vbox.getChildren().addAll(formCard, listHeading, userListArea);
        return vbox;
    }

    private void refreshUserList(TextArea area) {
        StringBuilder sb = new StringBuilder();
        ArrayList<User> users = userDAO.getAllUsers();
        if (users.isEmpty()) {
            sb.append("No user profiles created yet.");
        } else {
            sb.append(String.format("%-4s %-20s %-5s %-8s %-8s %-8s %-25s %-15s\n", "ID", "Name", "Age", "Gender", "Weight", "Height", "Goal", "Activity"));
            sb.append("-".repeat(95)).append("\n");
            for (User u : users) {
                sb.append(String.format("%-4d %-20s %-5d %-8s %-8.1f %-8.1f %-25s %-15s\n",
                    u.getId(), u.getName(), u.getAge(), u.getGender(), u.getWeight(), u.getHeight(), u.getGoal(), u.getActivityLevel()));
            }
        }
        area.setText(sb.toString());
    }

    private VBox createFoodPane() {
        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(25));

        VBox formCard = new VBox(12);
        formCard.getStyleClass().add("card");

        Label heading = new Label("Expand Food Database");
        heading.getStyleClass().add("section-heading");

        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(10);

        TextField foodNameField = new TextField(); 
        foodNameField.setPromptText("e.g. Avocado");
        
        TextField calField = new TextField();      
        calField.setPromptText("Calories / 100g");
        
        TextField proteinField = new TextField();  
        proteinField.setPromptText("Protein (g) / 100g");
        
        TextField carbsField = new TextField();    
        carbsField.setPromptText("Carbs (g) / 100g");
        
        TextField fatsField = new TextField();     
        fatsField.setPromptText("Fats (g) / 100g");
        
        TextField cupField = new TextField("0");      
        cupField.setPromptText("1 cup weight (grams) - optional");
        
        TextField unitField = new TextField("0");     
        unitField.setPromptText("1 piece weight (grams) - optional");

        form.add(new Label("Name:"), 0, 0);                  form.add(foodNameField, 1, 0);
        form.add(new Label("Calories/100g:"), 0, 1);         form.add(calField, 1, 1);
        form.add(new Label("Protein/100g:"), 0, 2);          form.add(proteinField, 1, 2);
        form.add(new Label("Carbs/100g:"), 2, 0);            form.add(carbsField, 3, 0);
        form.add(new Label("Fats/100g:"), 2, 1);             form.add(fatsField, 3, 1);
        form.add(new Label("Weight of 1 Cup (g):"), 2, 2);   form.add(cupField, 3, 2);
        form.add(new Label("Weight of 1 Piece (g):"), 0, 3); form.add(unitField, 1, 3);

        Button addFoodBtn = new Button("Add to Database");
        addFoodBtn.getStyleClass().add("btn-primary");
        
        Label foodStatus = new Label("");

        TextField searchField = new TextField(); 
        searchField.setPromptText("Search food name...");
        Button searchBtn = new Button("Search");
        searchBtn.getStyleClass().add("btn-secondary");
        Button showAllBtn = new Button("Show All");
        showAllBtn.getStyleClass().add("btn-secondary");

        TextArea foodListArea = new TextArea();
        foodListArea.setEditable(false);
        foodListArea.setPrefHeight(180);
        refreshFoodList(foodListArea, null);

        addFoodBtn.setOnAction(e -> {
            try {
                String name = foodNameField.getText().trim();
                int cal = Integer.parseInt(calField.getText().trim());
                double protein = Double.parseDouble(proteinField.getText().trim());
                double carbs = Double.parseDouble(carbsField.getText().trim());
                double fats = Double.parseDouble(fatsField.getText().trim());
                double cupWeight = Double.parseDouble(cupField.getText().trim());
                double unitWeight = Double.parseDouble(unitField.getText().trim());

                if (name.isEmpty()) { 
                    foodStatus.setText("Food name cannot be empty.");
                    foodStatus.getStyleClass().setAll("status-error");
                    return; 
                }

                FoodItem food = new FoodItem(name, cal, protein, carbs, fats, cupWeight, unitWeight);
                int id = foodDAO.addFoodItem(food);
                if (id > 0) {
                    foodStatus.setText("Success! Added " + name + " to DB.");
                    foodStatus.getStyleClass().setAll("status-success");
                    foodNameField.clear(); calField.clear(); proteinField.clear();
                    carbsField.clear(); fatsField.clear(); cupField.setText("0"); unitField.setText("0");
                    reloadCachedFoods();
                    refreshFoodList(foodListArea, null);
                }
            } catch (NumberFormatException ex) {
                foodStatus.setText("Please enter valid numeric fields.");
                foodStatus.getStyleClass().setAll("status-error");
            }
        });

        searchBtn.setOnAction(e -> {
            String q = searchField.getText().trim();
            if (!q.isEmpty()) refreshFoodList(foodListArea, q);
        });

        showAllBtn.setOnAction(e -> { searchField.clear(); refreshFoodList(foodListArea, null); });

        formCard.getChildren().addAll(heading, form, addFoodBtn, foodStatus);

        VBox searchBox = new VBox(8, new Label("Search Food Database:"), new HBox(10, searchField, searchBtn, showAllBtn), foodListArea);

        vbox.getChildren().addAll(formCard, searchBox);
        return vbox;
    }

    private void refreshFoodList(TextArea area, String query) {
        StringBuilder sb = new StringBuilder();
        ArrayList<FoodItem> foods = (query != null && !query.isEmpty())
                ? foodDAO.searchFoodByName(query) : foodDAO.getAllFoodItems();

        if (foods.isEmpty()) {
            sb.append("No matching food items found.");
        } else {
            sb.append(String.format("%-5s %-20s %-12s %-12s %-12s %-12s %-12s %-12s\n", 
                "ID", "Name", "Cal/100g", "Protein/100g", "Carbs/100g", "Fats/100g", "Cup Wt(g)", "Piece Wt(g)"));
            sb.append("-".repeat(95)).append("\n");
            for (FoodItem f : foods) {
                sb.append(String.format("%-5d %-20s %-12d %-12.1fg %-12.1fg %-12.1fg %-12.1fg %-12.1fg\n",
                    f.getId(), f.getName(), f.getCaloriesPer100g(), f.getProteinPer100g(), f.getCarbsPer100g(), f.getFatsPer100g(), f.getWeightPerCup(), f.getWeightPerUnit()));
            }
        }
        area.setText(sb.toString());
    }

    private VBox createMealPane() {
        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(25));

        ArrayList<Meal.MealItem> currentMealBuilderItems = new ArrayList<>();

        VBox formCard = new VBox(12);
        formCard.getStyleClass().add("card");

        Label heading = new Label("Log a New Meal");
        heading.getStyleClass().add("section-heading");

        TextField userIdField = new TextField();   
        userIdField.setPromptText("User ID"); 
        userIdField.setPrefWidth(80);
        
        TextField mealNameField = new TextField(); 
        mealNameField.setPromptText("Meal label");

        DatePicker datePicker = new DatePicker(LocalDate.now());
        datePicker.setPrefWidth(140);

        HBox mealHeader = new HBox(15, new Label("User ID:"), userIdField, new Label("Meal Label:"), mealNameField, new Label("Date:"), datePicker);
        mealHeader.setAlignment(Pos.CENTER_LEFT);

        // Dynamic Food Selector
        ComboBox<String> foodCombo = new ComboBox<>();
        foodCombo.setPromptText("Choose food item");
        foodCombo.setPrefWidth(220);
        for (FoodItem f : cachedFoods) {
            foodCombo.getItems().add(f.getId() + " - " + f.getName());
        }

        ComboBox<String> unitCombo = new ComboBox<>();
        unitCombo.setPromptText("Unit");
        unitCombo.setPrefWidth(120);

        TextField amountField = new TextField();
        amountField.setPromptText("Amount");
        amountField.setPrefWidth(80);

        Label previewLabel = new Label("Preview: 0 kcal, 0g protein, 0g carbs, 0g fats");
        previewLabel.setStyle("-fx-text-fill: #64748b; -fx-font-style: italic;");

        // Set action to load compatible units dynamically based on database properties
        foodCombo.setOnAction(e -> {
            String selectedStr = foodCombo.getValue();
            unitCombo.getItems().clear();
            if (selectedStr != null) {
                int foodId = Integer.parseInt(selectedStr.split(" - ")[0].trim());
                FoodItem food = getFoodFromCache(foodId);
                if (food != null) {
                    unitCombo.getItems().add("Grams (g)");
                    if (food.getWeightPerCup() > 0) {
                        unitCombo.getItems().add("Cups");
                    }
                    if (food.getWeightPerUnit() > 0) {
                        unitCombo.getItems().add("Pieces / Units");
                    }
                    unitCombo.setValue("Grams (g)");
                }
            }
        });

        // Trigger dynamic nutrition math preview
        Runnable triggerPreview = () -> {
            updatePreviewLabel(foodCombo, unitCombo, amountField, previewLabel, cachedFoods);
        };
        unitCombo.setOnAction(e -> triggerPreview.run());
        amountField.textProperty().addListener((obs, oldVal, newVal) -> triggerPreview.run());

        Button addItemBtn = new Button("Add Food Item");
        addItemBtn.getStyleClass().add("btn-secondary");

        ListView<String> mealListView = new ListView<>();
        mealListView.setPrefHeight(120);

        addItemBtn.setOnAction(e -> {
            String selectedFoodStr = foodCombo.getValue();
            String selectedUnit = unitCombo.getValue();
            String amountText = amountField.getText().trim();

            if (selectedFoodStr == null || selectedUnit == null || amountText.isEmpty()) return;

            try {
                double amount = Double.parseDouble(amountText);
                int foodId = Integer.parseInt(selectedFoodStr.split(" - ")[0].trim());
                FoodItem food = getFoodFromCache(foodId);

                if (food != null) {
                    double grams = amount;
                    String shortUnit = "g";
                    if (selectedUnit.contains("Cups")) {
                        grams = amount * food.getWeightPerCup();
                        shortUnit = "cup";
                    } else if (selectedUnit.contains("Pieces")) {
                        grams = amount * food.getWeightPerUnit();
                        shortUnit = "unit";
                    }

                    double factor = grams / 100.0;
                    int cals = (int) Math.round(food.getCaloriesPer100g() * factor);
                    double prot = food.getProteinPer100g() * factor;
                    double carbs = food.getCarbsPer100g() * factor;
                    double fats = food.getFatsPer100g() * factor;

                    Meal.MealItem item = new Meal.MealItem(food, amount, shortUnit, cals, prot, carbs, fats);
                    currentMealBuilderItems.add(item);

                    mealListView.getItems().add(
                        String.format("%s - %.1f %s (%d kcal, %.1fg P, %.1fg C, %.1fg F)", 
                            food.getName(), amount, shortUnit, cals, prot, carbs, fats)
                    );

                    // Reset selectors
                    foodCombo.setValue(null);
                    amountField.clear();
                    unitCombo.getItems().clear();
                    previewLabel.setText("Preview: 0 kcal, 0g protein, 0g carbs, 0g fats");
                }
            } catch (NumberFormatException ex) {
                // ignore
            }
        });

        Button saveMealBtn = new Button("Save Logged Meal");
        saveMealBtn.getStyleClass().add("btn-primary");
        Label saveStatus = new Label("");

        saveMealBtn.setOnAction(e -> {
            try {
                int userId = Integer.parseInt(userIdField.getText().trim());
                String mealLabel = mealNameField.getText().trim();
                LocalDate chosenDate = datePicker.getValue();

                if (mealLabel.isEmpty()) {
                    saveStatus.setText("Please name your meal!");
                    saveStatus.getStyleClass().setAll("status-error");
                    return;
                }
                if (chosenDate == null) {
                    saveStatus.setText("Please pick a valid date!");
                    saveStatus.getStyleClass().setAll("status-error");
                    return;
                }
                if (currentMealBuilderItems.isEmpty()) {
                    saveStatus.setText("No food items added!");
                    saveStatus.getStyleClass().setAll("status-error");
                    return;
                }

                Meal meal = new Meal(mealLabel, userId);
                meal.setMealDate(chosenDate.toString());
                for (Meal.MealItem item : currentMealBuilderItems) {
                    meal.addMealItem(item);
                }

                int mealId = mealDAO.addMeal(meal);
                if (mealId > 0) {
                    saveStatus.setText("Logged successfully! " + meal.getTotalCalories() + " kcal total.");
                    saveStatus.getStyleClass().setAll("status-success");
                    mealNameField.clear();
                    mealListView.getItems().clear();
                    currentMealBuilderItems.clear();
                } else {
                    saveStatus.setText("Logging failed.");
                    saveStatus.getStyleClass().setAll("status-error");
                }
            } catch (NumberFormatException ex) {
                saveStatus.setText("Please enter a valid User ID.");
                saveStatus.getStyleClass().setAll("status-error");
            }
        });

        HBox addRow = new HBox(12, foodCombo, unitCombo, amountField, addItemBtn);
        addRow.setAlignment(Pos.CENTER_LEFT);

        formCard.getChildren().addAll(heading, mealHeader, addRow, previewLabel, new Label("Added items in this log:"), mealListView, new HBox(15, saveMealBtn, saveStatus));

        // Meal History Viewer
        VBox historyCard = new VBox(10);
        Label historyHeading = new Label("Logged History");
        historyHeading.getStyleClass().add("section-heading");
        
        TextField historyUserField = new TextField();
        historyUserField.setPromptText("User ID");
        historyUserField.setPrefWidth(80);
        
        Button loadHistoryBtn = new Button("View Logs");
        loadHistoryBtn.getStyleClass().add("btn-secondary");

        TextArea historyDisplay = new TextArea();
        historyDisplay.setEditable(false);
        historyDisplay.setPrefHeight(150);

        loadHistoryBtn.setOnAction(e -> {
            try {
                int uid = Integer.parseInt(historyUserField.getText().trim());
                ArrayList<Meal> meals = mealDAO.getMealsByUserId(uid);
                StringBuilder sb = new StringBuilder();
                if (meals.isEmpty()) {
                    sb.append("No meal records found for user ID: ").append(uid);
                } else {
                    for (Meal m : meals) {
                        sb.append(String.format("Meal: %-15s | Date: %-10s | Total: %d kcal (P: %.1fg, C: %.1fg, F: %.1fg)\n", 
                            m.getMealName(), m.getMealDate(), m.getTotalCalories(), m.getTotalProtein(), m.getTotalCarbs(), m.getTotalFats()));
                        for (Meal.MealItem item : m.getMealItems()) {
                            sb.append(String.format("  -> %-18s: %.1f %-5s (%d kcal, P: %.1fg, C: %.1fg, F: %.1fg)\n",
                                item.getFoodItem().getName(), item.getAmount(), item.getUnit(), item.getCalculatedCalories(), 
                                item.getCalculatedProtein(), item.getCalculatedCarbs(), item.getCalculatedFats()));
                        }
                        sb.append("\n");
                    }
                }
                historyDisplay.setText(sb.toString());
            } catch (NumberFormatException ex) {
                historyDisplay.setText("Invalid User ID.");
            }
        });

        historyCard.getChildren().addAll(historyHeading, new HBox(10, new Label("Filter by User ID:"), historyUserField, loadHistoryBtn), historyDisplay);

        vbox.getChildren().addAll(formCard, historyCard);
        return vbox;
    }

    private ScrollPane createDailyTrackerPane() {
        Map<String, ArrayList<Meal.MealItem>> slotFoods = new LinkedHashMap<>();
        String[] slots = { "Breakfast", "Morning Snack", "Lunch", "Evening Snack", "Dinner" };
        for (String s : slots) slotFoods.put(s, new ArrayList<>());

        VBox root = new VBox(15);
        root.setPadding(new Insets(25));

        Label titleLbl = new Label("Daily Nutrition Planner");
        titleLbl.getStyleClass().add("main-title");
        Label subtitleLbl = new Label("Persist your nutrition data across days. Load past dates and track calorie intake.");
        subtitleLbl.getStyleClass().add("subtitle");

        // Persistence Header Inputs
        TextField trackerUserIdField = new TextField();
        trackerUserIdField.setPromptText("User ID");
        trackerUserIdField.setPrefWidth(80);

        DatePicker trackerDatePicker = new DatePicker(LocalDate.now());
        trackerDatePicker.setPrefWidth(140);

        Button loadDayBtn = new Button("Load Day Data");
        loadDayBtn.getStyleClass().add("btn-primary");

        Label trackerStatus = new Label("Please load a User ID & Date to start tracking.");
        trackerStatus.setStyle("-fx-font-weight: bold; -fx-text-fill: #475569;");

        HBox trackingHeader = new HBox(15, new Label("User ID:"), trackerUserIdField, new Label("Date:"), trackerDatePicker, loadDayBtn);
        trackingHeader.setAlignment(Pos.CENTER_LEFT);
        trackingHeader.setStyle("-fx-background-color: #ffffff; -fx-padding: 15; -fx-background-radius: 8; -fx-border-color: #cbd5e1; -fx-border-radius: 8;");

        root.getChildren().addAll(titleLbl, subtitleLbl, trackingHeader, trackerStatus, new Separator());

        Map<String, TextArea> slotDisplays = new LinkedHashMap<>();

        // Helper tracker reloader
        Runnable reloadTrackerAction = () -> {
            try {
                int uid = Integer.parseInt(trackerUserIdField.getText().trim());
                LocalDate date = trackerDatePicker.getValue();
                if (date == null) {
                    trackerStatus.setText("Pick a valid date!");
                    trackerStatus.getStyleClass().setAll("status-error");
                    return;
                }
                
                // Clear active arrays
                for (String s : slotFoods.keySet()) slotFoods.get(s).clear();

                // Query DB
                ArrayList<Meal> meals = mealDAO.getMealsByUserIdAndDate(uid, date.toString());
                for (Meal m : meals) {
                    String cleanName = m.getMealName();
                    // Match standard slot names
                    for (String slotName : slots) {
                        if (slotName.equalsIgnoreCase(cleanName)) {
                            slotFoods.get(slotName).addAll(m.getMealItems());
                        }
                    }
                }

                // Refresh text displays
                for (String slotName : slots) {
                    refreshSlotDisplay(slotDisplays.get(slotName), slotFoods.get(slotName));
                }

                trackerStatus.setText(String.format("Loaded tracker data for User %d on %s.", uid, date));
                trackerStatus.getStyleClass().setAll("status-success");

            } catch (NumberFormatException ex) {
                trackerStatus.setText("Enter a valid numeric User ID!");
                trackerStatus.getStyleClass().setAll("status-error");
            }
        };

        loadDayBtn.setOnAction(e -> reloadTrackerAction.run());

        for (String slot : slots) {
            VBox card = new VBox(10);
            card.getStyleClass().add("card");

            Label slotLbl = new Label(slot);
            slotLbl.getStyleClass().add("section-heading");

            ComboBox<String> foodCombo = new ComboBox<>();
            foodCombo.setPromptText("Choose food item...");
            foodCombo.setPrefWidth(220);
            for (FoodItem f : cachedFoods) {
                foodCombo.getItems().add(f.getId() + " - " + f.getName());
            }

            ComboBox<String> unitCombo = new ComboBox<>();
            unitCombo.setPromptText("Unit");
            unitCombo.setPrefWidth(120);

            TextField amountField = new TextField("1");
            amountField.setPrefWidth(60);

            Label previewLabel = new Label("Preview: 0 kcal");
            previewLabel.setStyle("-fx-text-fill: #64748b; -fx-font-style: italic;");

            foodCombo.setOnAction(e -> {
                String val = foodCombo.getValue();
                unitCombo.getItems().clear();
                if (val != null) {
                    int foodId = Integer.parseInt(val.split(" - ")[0].trim());
                    FoodItem food = getFoodFromCache(foodId);
                    if (food != null) {
                        unitCombo.getItems().add("Grams (g)");
                        if (food.getWeightPerCup() > 0) {
                            unitCombo.getItems().add("Cups");
                        }
                        if (food.getWeightPerUnit() > 0) {
                            unitCombo.getItems().add("Pieces / Units");
                        }
                        unitCombo.setValue("Grams (g)");
                    }
                }
            });

            // Live preview math
            Runnable triggerPreview = () -> {
                updatePreviewLabel(foodCombo, unitCombo, amountField, previewLabel, cachedFoods);
            };
            unitCombo.setOnAction(e -> triggerPreview.run());
            amountField.textProperty().addListener((obs, old, nv) -> triggerPreview.run());

            Button addBtn = new Button("Add");
            addBtn.getStyleClass().add("btn-primary");
            
            Button clearBtn = new Button("Clear");
            clearBtn.getStyleClass().add("btn-danger");

            HBox row = new HBox(12, new Label("Food:"), foodCombo, unitCombo, new Label("Qty:"), amountField, addBtn, clearBtn);
            row.setAlignment(Pos.CENTER_LEFT);

            TextArea display = new TextArea();
            display.setEditable(false);
            display.setPrefHeight(90);
            display.setPromptText("No foods added to " + slot);
            slotDisplays.put(slot, display);

            final String currentSlot = slot;
            addBtn.setOnAction(e -> {
                try {
                    int uid = Integer.parseInt(trackerUserIdField.getText().trim());
                    LocalDate date = trackerDatePicker.getValue();
                    if (date == null) {
                        trackerStatus.setText("Pick a valid date first!");
                        trackerStatus.getStyleClass().setAll("status-error");
                        return;
                    }

                    String selectedFoodStr = foodCombo.getValue();
                    String selectedUnit = unitCombo.getValue();
                    String amountText = amountField.getText().trim();

                    if (selectedFoodStr == null || selectedUnit == null || amountText.isEmpty()) return;

                    double amount = Double.parseDouble(amountText);
                    int foodId = Integer.parseInt(selectedFoodStr.split(" - ")[0].trim());
                    FoodItem food = getFoodFromCache(foodId);

                    if (food != null) {
                        double grams = amount;
                        String shortUnit = "g";
                        if (selectedUnit.contains("Cups")) {
                            grams = amount * food.getWeightPerCup();
                            shortUnit = "cup";
                        } else if (selectedUnit.contains("Pieces")) {
                            grams = amount * food.getWeightPerUnit();
                            shortUnit = "unit";
                        }

                        double factor = grams / 100.0;
                        int cals = (int) Math.round(food.getCaloriesPer100g() * factor);
                        double prot = food.getProteinPer100g() * factor;
                        double carbs = food.getCarbsPer100g() * factor;
                        double fats = food.getFatsPer100g() * factor;

                        Meal.MealItem item = new Meal.MealItem(food, amount, shortUnit, cals, prot, carbs, fats);

                        // PERSIST IMMEDIATELY
                        // Query meals for this user and date
                        ArrayList<Meal> meals = mealDAO.getMealsByUserIdAndDate(uid, date.toString());
                        Meal targetMeal = null;
                        for (Meal m : meals) {
                            if (m.getMealName().equalsIgnoreCase(currentSlot)) {
                                targetMeal = m;
                                break;
                            }
                        }

                        if (targetMeal == null) {
                            // Create a new meal header for this slot
                            Meal newMeal = new Meal(currentSlot, uid);
                            newMeal.setMealDate(date.toString());
                            newMeal.addMealItem(item);
                            mealDAO.addMeal(newMeal);
                        } else {
                            // Append food item to existing meal
                            mealDAO.addFoodToMeal(targetMeal.getId(), item);
                        }

                        // Reload data
                        reloadTrackerAction.run();

                        // Clear forms
                        foodCombo.setValue(null);
                        amountField.setText("1");
                        unitCombo.getItems().clear();
                        previewLabel.setText("Preview: 0 kcal");
                    }
                } catch (NumberFormatException ex) {
                    trackerStatus.setText("Valid User ID, date, and positive quantity required!");
                    trackerStatus.getStyleClass().setAll("status-error");
                }
            });

            clearBtn.setOnAction(e -> {
                try {
                    int uid = Integer.parseInt(trackerUserIdField.getText().trim());
                    LocalDate date = trackerDatePicker.getValue();
                    if (date == null) return;

                    // Query meals for this user and date
                    ArrayList<Meal> meals = mealDAO.getMealsByUserIdAndDate(uid, date.toString());
                    for (Meal m : meals) {
                        if (m.getMealName().equalsIgnoreCase(currentSlot)) {
                            mealDAO.deleteMeal(m.getId());
                            break;
                        }
                    }

                    // Reload
                    reloadTrackerAction.run();
                } catch (NumberFormatException ex) {}
            });

            card.getChildren().addAll(slotLbl, row, previewLabel, display);
            root.getChildren().add(card);
        }

        Button calcDayBtn = new Button("Calculate Summary");
        calcDayBtn.getStyleClass().add("btn-primary");

        VBox summaryCard = new VBox(10);
        summaryCard.getStyleClass().add("summary-card");
        summaryCard.setVisible(false);

        Label summaryTitle = new Label("Daily Summary");
        summaryTitle.getStyleClass().add("summary-title");
        
        TextArea summaryArea = new TextArea();
        summaryArea.setEditable(false);
        summaryArea.setPrefHeight(180);
        summaryCard.getChildren().addAll(summaryTitle, summaryArea);

        calcDayBtn.setOnAction(e -> {
            int totalCal = 0; double totalProt = 0, totalCarb = 0, totalFat = 0;
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%-18s %8s %9s %9s %9s%n", "Meal Slot", "Kcal", "Prot(g)", "Carb(g)", "Fat(g)"));
            sb.append("-".repeat(58)).append("\n");

            for (String slot : slots) {
                ArrayList<Meal.MealItem> list = slotFoods.get(slot);
                int sCal = 0; double sP = 0, sC = 0, sF = 0;
                for (Meal.MealItem item : list) {
                    sCal += item.getCalculatedCalories();
                    sP += item.getCalculatedProtein();
                    sC += item.getCalculatedCarbs();
                    sF += item.getCalculatedFats();
                }
                sb.append(String.format("%-18s %8d %9.1f %9.1f %9.1f%n", slot, sCal, sP, sC, sF));
                totalCal += sCal; totalProt += sP; totalCarb += sC; totalFat += sF;
            }
            sb.append("-".repeat(58)).append("\n");
            sb.append(String.format("%-18s %8d %9.1f %9.1f %9.1f%n", "TOTAL", totalCal, totalProt, totalCarb, totalFat));

            double pct = (totalCal / 2000.0) * 100.0;
            sb.append(String.format("\nGoal Progress (vs standard 2000 kcal): %.1f%%\n", pct));
            if (pct < 85) sb.append("Status: Under calorie goal today.");
            else if (pct > 115) sb.append("Status: Exceeded target threshold.");
            else sb.append("Status: Great job! Hit target range.");

            summaryArea.setText(sb.toString());
            summaryCard.setVisible(true);
        });

        root.getChildren().addAll(calcDayBtn, summaryCard);

        ScrollPane scroll = new ScrollPane(root);
        scroll.setFitToWidth(true);
        return scroll;
    }

    private void updatePreviewLabel(ComboBox<String> foodCombo, ComboBox<String> unitCombo, TextField amountField, Label previewLabel, ArrayList<FoodItem> allFoods) {
        String selectedFoodStr = foodCombo.getValue();
        String selectedUnit = unitCombo.getValue();
        String amountText = amountField.getText();

        if (selectedFoodStr == null || selectedUnit == null || amountText.isEmpty()) {
            previewLabel.setText("Preview: 0 kcal, 0g protein, 0g carbs, 0g fats");
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);
            if (amount < 0) throw new NumberFormatException();

            int foodId = Integer.parseInt(selectedFoodStr.split(" - ")[0].trim());
            FoodItem food = getFoodFromCache(foodId);

            if (food != null) {
                double grams = amount;
                if (selectedUnit.contains("Cups")) {
                    grams = amount * food.getWeightPerCup();
                } else if (selectedUnit.contains("Pieces")) {
                    grams = amount * food.getWeightPerUnit();
                }

                double factor = grams / 100.0;
                int cals = (int) Math.round(food.getCaloriesPer100g() * factor);
                double prot = food.getProteinPer100g() * factor;
                double carbs = food.getCarbsPer100g() * factor;
                double fats = food.getFatsPer100g() * factor;

                previewLabel.setText(String.format("Preview: %d kcal, %.1fg protein, %.1fg carbs, %.1fg fats (approx. %.0fg)",
                    cals, prot, carbs, fats, grams));
            }
        } catch (NumberFormatException ex) {
            previewLabel.setText("Preview: Enter valid amount");
        }
    }

    private void refreshSlotDisplay(TextArea display, ArrayList<Meal.MealItem> foods) {
        if (foods.isEmpty()) { display.clear(); return; }
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-18s %6s %7s %7s %7s %6s%n", "Food", "Kcal", "Prot", "Carbs", "Fats", "Amount"));
        sb.append("-".repeat(58)).append("\n");
        int totCal = 0; double totP = 0, totC = 0, totF = 0;
        for (Meal.MealItem f : foods) {
            String name = f.getFoodItem().getName().length() > 18 ? f.getFoodItem().getName().substring(0, 15) + "..." : f.getFoodItem().getName();
            sb.append(String.format("%-18s %6d %6.1fg %6.1fg %6.1fg %5.1f%s%n", 
                name, f.getCalculatedCalories(), f.getCalculatedProtein(), f.getCalculatedCarbs(), f.getCalculatedFats(), f.getAmount(), f.getUnit()));
            totCal += f.getCalculatedCalories(); totP += f.getCalculatedProtein(); totC += f.getCalculatedCarbs(); totF += f.getCalculatedFats();
        }
        sb.append("-".repeat(58)).append("\n");
        sb.append(String.format("%-18s %6d %6.1fg %6.1fg %6.1fg%n", "Subtotal", totCal, totP, totC, totF));
        display.setText(sb.toString());
    }

    public static void main(String[] args) { launch(args); }
}
