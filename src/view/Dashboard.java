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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Dashboard - Main JavaFX application window.
 * Simple GUI for NutriTrack with tabs for Users, Food, Meals.
 */
public class Dashboard extends Application {

    private UserDAO userDAO = new UserDAO();
    private FoodItemDAO foodDAO = new FoodItemDAO();
    private MealDAO mealDAO = new MealDAO();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("NutriTrack - Nutrition Tracker");

        // Create tab pane
        TabPane tabPane = new TabPane();

        // Create tabs
        Tab homeTab      = new Tab("🏠 Home",          createHomePane());
        Tab userTab      = new Tab("👤 Users",          createUserPane());
        Tab foodTab      = new Tab("🥗 Food Items",     createFoodPane());
        Tab mealTab      = new Tab("🍽️ Meals",          createMealPane());
        Tab trackerTab   = new Tab("📊 Daily Tracker",  createDailyTrackerPane());

        // Tabs cannot be closed
        homeTab.setClosable(false);
        userTab.setClosable(false);
        foodTab.setClosable(false);
        mealTab.setClosable(false);
        trackerTab.setClosable(false);

        tabPane.getTabs().addAll(homeTab, userTab, foodTab, mealTab, trackerTab);

        Scene scene = new Scene(tabPane, 900, 650);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // =============================================
    // HOME TAB
    // =============================================
    private VBox createHomePane() {
        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(30));
        vbox.setAlignment(Pos.CENTER);

        Label title = new Label("NutriTrack");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        Label subtitle = new Label("Nutrition Tracking System");
        subtitle.setStyle("-fx-font-size: 14px; -fx-text-fill: gray;");

        Label info = new Label("Track your daily food intake, calories and macronutrients.");
        info.setStyle("-fx-font-size: 12px;");

        // Stats
        Label statsLabel = new Label("Quick Stats:");
        statsLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        int userCount = userDAO.getAllUsers().size();
        int foodCount = foodDAO.getAllFoodItems().size();

        Label usersCount = new Label("Total Users: " + userCount);
        Label foodsCount = new Label("Food Items in Database: " + foodCount);

        vbox.getChildren().addAll(title, subtitle, new Separator(), info, statsLabel, usersCount, foodsCount);
        return vbox;
    }

    // =============================================
    // USERS TAB
    // =============================================
    private VBox createUserPane() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(15));

        Label heading = new Label("Add New User");
        heading.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Form fields
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(8);

        TextField nameField = new TextField();
        nameField.setPromptText("Enter name");
        TextField ageField = new TextField();
        ageField.setPromptText("Enter age");
        ComboBox<String> genderBox = new ComboBox<>();
        genderBox.getItems().addAll("Male", "Female");
        genderBox.setValue("Male");
        TextField weightField = new TextField();
        weightField.setPromptText("Weight in kg");
        TextField heightField = new TextField();
        heightField.setPromptText("Height in cm");
        ComboBox<String> goalBox = new ComboBox<>();
        goalBox.getItems().addAll("lose", "gain", "maintain");
        goalBox.setValue("maintain");

        form.add(new Label("Name:"), 0, 0);
        form.add(nameField, 1, 0);
        form.add(new Label("Age:"), 0, 1);
        form.add(ageField, 1, 1);
        form.add(new Label("Gender:"), 0, 2);
        form.add(genderBox, 1, 2);
        form.add(new Label("Weight (kg):"), 0, 3);
        form.add(weightField, 1, 3);
        form.add(new Label("Height (cm):"), 0, 4);
        form.add(heightField, 1, 4);
        form.add(new Label("Goal:"), 0, 5);
        form.add(goalBox, 1, 5);

        Button addBtn = new Button("Add User");
        Label statusLabel = new Label("");

        // Button to show calorie recommendation
        Button calcBtn = new Button("Calculate Calories");
        Label calorieLabel = new Label("");

        // User list area
        Label listHeading = new Label("All Users:");
        listHeading.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        TextArea userListArea = new TextArea();
        userListArea.setEditable(false);
        userListArea.setPrefHeight(120);

        // Load users on start
        refreshUserList(userListArea);

        // Add user button action
        addBtn.setOnAction(e -> {
            try {
                String name = nameField.getText().trim();
                int age = Integer.parseInt(ageField.getText().trim());
                String gender = genderBox.getValue();
                double weight = Double.parseDouble(weightField.getText().trim());
                double height = Double.parseDouble(heightField.getText().trim());
                String goal = goalBox.getValue();

                if (name.isEmpty()) {
                    statusLabel.setText("Please enter a name!");
                    return;
                }

                User user = new User(name, age, gender, weight, height, goal);
                int id = userDAO.addUser(user);

                if (id > 0) {
                    statusLabel.setText("User added! ID: " + id);
                    statusLabel.setStyle("-fx-text-fill: green;");
                    // Clear fields
                    nameField.clear();
                    ageField.clear();
                    weightField.clear();
                    heightField.clear();
                    refreshUserList(userListArea);
                } else {
                    statusLabel.setText("Error adding user.");
                    statusLabel.setStyle("-fx-text-fill: red;");
                }
            } catch (NumberFormatException ex) {
                statusLabel.setText("Please enter valid numbers for age, weight, height.");
                statusLabel.setStyle("-fx-text-fill: red;");
            }
        });

        // Calculate calories button
        calcBtn.setOnAction(e -> {
            try {
                double weight = Double.parseDouble(weightField.getText().trim());
                double height = Double.parseDouble(heightField.getText().trim());
                int age = Integer.parseInt(ageField.getText().trim());
                String gender = genderBox.getValue();
                String goal = goalBox.getValue();

                double cal = CalorieCalculator.calculateCalories(weight, height, age, gender, goal);
                calorieLabel.setText("Recommended: " + String.format("%.0f", cal) + " kcal/day");
                calorieLabel.setStyle("-fx-text-fill: blue; -fx-font-weight: bold;");
            } catch (NumberFormatException ex) {
                calorieLabel.setText("Fill in age, weight, height first!");
                calorieLabel.setStyle("-fx-text-fill: red;");
            }
        });

        HBox buttons = new HBox(10, addBtn, calcBtn);

        vbox.getChildren().addAll(heading, form, buttons, statusLabel, calorieLabel,
                new Separator(), listHeading, userListArea);
        return vbox;
    }

    private void refreshUserList(TextArea area) {
        StringBuilder sb = new StringBuilder();
        ArrayList<User> users = userDAO.getAllUsers();
        if (users.isEmpty()) {
            sb.append("No users found.");
        } else {
            for (User u : users) {
                sb.append("ID: ").append(u.getId())
                  .append(" | ").append(u.getName())
                  .append(" | Age: ").append(u.getAge())
                  .append(" | ").append(u.getGender())
                  .append(" | ").append(u.getWeight()).append("kg")
                  .append(" | Goal: ").append(u.getGoal())
                  .append("\n");
            }
        }
        area.setText(sb.toString());
    }

    // =============================================
    // FOOD ITEMS TAB
    // =============================================
    private VBox createFoodPane() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(15));

        Label heading = new Label("Add Food Item");
        heading.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Form
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(8);

        TextField foodNameField = new TextField();
        foodNameField.setPromptText("Food name");
        TextField calField = new TextField();
        calField.setPromptText("Calories");
        TextField proteinField = new TextField();
        proteinField.setPromptText("Protein (g)");
        TextField carbsField = new TextField();
        carbsField.setPromptText("Carbs (g)");
        TextField fatsField = new TextField();
        fatsField.setPromptText("Fats (g)");

        form.add(new Label("Name:"), 0, 0);
        form.add(foodNameField, 1, 0);
        form.add(new Label("Calories:"), 0, 1);
        form.add(calField, 1, 1);
        form.add(new Label("Protein (g):"), 0, 2);
        form.add(proteinField, 1, 2);
        form.add(new Label("Carbs (g):"), 0, 3);
        form.add(carbsField, 1, 3);
        form.add(new Label("Fats (g):"), 0, 4);
        form.add(fatsField, 1, 4);

        Button addFoodBtn = new Button("Add Food Item");
        Label foodStatus = new Label("");

        // Search
        Label searchLabel = new Label("Search Food:");
        searchLabel.setStyle("-fx-font-weight: bold;");
        HBox searchBox = new HBox(10);
        TextField searchField = new TextField();
        searchField.setPromptText("Search by name...");
        Button searchBtn = new Button("Search");
        Button showAllBtn = new Button("Show All");
        searchBox.getChildren().addAll(searchField, searchBtn, showAllBtn);

        // Food list
        TextArea foodListArea = new TextArea();
        foodListArea.setEditable(false);
        foodListArea.setPrefHeight(150);

        // Load food items
        refreshFoodList(foodListArea, null);

        // Add food button
        addFoodBtn.setOnAction(e -> {
            try {
                String name = foodNameField.getText().trim();
                int cal = Integer.parseInt(calField.getText().trim());
                double protein = Double.parseDouble(proteinField.getText().trim());
                double carbs = Double.parseDouble(carbsField.getText().trim());
                double fats = Double.parseDouble(fatsField.getText().trim());

                if (name.isEmpty()) {
                    foodStatus.setText("Enter food name!");
                    return;
                }

                FoodItem food = new FoodItem(name, cal, protein, carbs, fats);
                int id = foodDAO.addFoodItem(food);

                if (id > 0) {
                    foodStatus.setText("Food added! - " + name);
                    foodStatus.setStyle("-fx-text-fill: green;");
                    foodNameField.clear();
                    calField.clear();
                    proteinField.clear();
                    carbsField.clear();
                    fatsField.clear();
                    refreshFoodList(foodListArea, null);
                }
            } catch (NumberFormatException ex) {
                foodStatus.setText("Enter valid numbers!");
                foodStatus.setStyle("-fx-text-fill: red;");
            }
        });

        // Search button
        searchBtn.setOnAction(e -> {
            String query = searchField.getText().trim();
            if (!query.isEmpty()) {
                refreshFoodList(foodListArea, query);
            }
        });

        // Show all button
        showAllBtn.setOnAction(e -> {
            searchField.clear();
            refreshFoodList(foodListArea, null);
        });

        vbox.getChildren().addAll(heading, form, addFoodBtn, foodStatus,
                new Separator(), searchLabel, searchBox, foodListArea);
        return vbox;
    }

    private void refreshFoodList(TextArea area, String searchQuery) {
        StringBuilder sb = new StringBuilder();
        ArrayList<FoodItem> foods;

        if (searchQuery != null && !searchQuery.isEmpty()) {
            foods = foodDAO.searchFoodByName(searchQuery);
            sb.append("Search results for '").append(searchQuery).append("':\n\n");
        } else {
            foods = foodDAO.getAllFoodItems();
        }

        if (foods.isEmpty()) {
            sb.append("No food items found.");
        } else {
            sb.append(String.format("%-5s %-20s %-8s %-10s %-10s %-10s\n",
                    "ID", "Name", "Cal", "Protein", "Carbs", "Fats"));
            sb.append("-".repeat(65)).append("\n");
            for (FoodItem f : foods) {
                sb.append(String.format("%-5d %-20s %-8d %-10.1fg %-10.1fg %-10.1fg\n",
                        f.getId(), f.getName(), f.getCalories(),
                        f.getProtein(), f.getCarbs(), f.getFats()));
            }
        }
        area.setText(sb.toString());
    }

    // =============================================
    // MEALS TAB
    // =============================================
    private VBox createMealPane() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(15));

        Label heading = new Label("Create Meal");
        heading.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Meal form
        HBox mealForm = new HBox(10);
        TextField mealNameField = new TextField();
        mealNameField.setPromptText("Meal name (e.g. Breakfast)");
        TextField userIdField = new TextField();
        userIdField.setPromptText("User ID");
        userIdField.setPrefWidth(80);
        mealForm.getChildren().addAll(new Label("Meal Name:"), mealNameField,
                new Label("User ID:"), userIdField);

        // Food selection
        Label selectLabel = new Label("Add food items to meal:");
        selectLabel.setStyle("-fx-font-weight: bold;");

        // Load food items into a list view
        ListView<String> foodListView = new ListView<>();
        foodListView.setPrefHeight(120);
        foodListView.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);

        ArrayList<FoodItem> allFoods = foodDAO.getAllFoodItems();
        for (FoodItem f : allFoods) {
            foodListView.getItems().add(f.getId() + " - " + f.getName() + " (" + f.getCalories() + " cal)");
        }

        Label selectHint = new Label("Hold Ctrl to select multiple items");
        selectHint.setStyle("-fx-font-size: 10px; -fx-text-fill: gray;");

        Button createMealBtn = new Button("Create Meal");
        Button refreshBtn = new Button("Refresh Food List");
        Label mealStatus = new Label("");

        HBox mealButtons = new HBox(10, createMealBtn, refreshBtn);

        // Meal history
        Label historyLabel = new Label("Meal History:");
        historyLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        HBox historySearch = new HBox(10);
        TextField historyUserId = new TextField();
        historyUserId.setPromptText("User ID");
        historyUserId.setPrefWidth(80);
        Button loadMealsBtn = new Button("Load Meals");
        historySearch.getChildren().addAll(new Label("Show meals for User ID:"), historyUserId, loadMealsBtn);

        TextArea mealHistoryArea = new TextArea();
        mealHistoryArea.setEditable(false);
        mealHistoryArea.setPrefHeight(120);

        // Create meal button
        createMealBtn.setOnAction(e -> {
            try {
                String mealName = mealNameField.getText().trim();
                int userId = Integer.parseInt(userIdField.getText().trim());

                if (mealName.isEmpty()) {
                    mealStatus.setText("Enter a meal name!");
                    mealStatus.setStyle("-fx-text-fill: red;");
                    return;
                }

                // Get selected food items
                var selected = foodListView.getSelectionModel().getSelectedItems();
                if (selected.isEmpty()) {
                    mealStatus.setText("Select at least one food item!");
                    mealStatus.setStyle("-fx-text-fill: red;");
                    return;
                }

                Meal meal = new Meal(mealName, userId);

                // Parse food IDs from selection
                for (String item : selected) {
                    int foodId = Integer.parseInt(item.split(" - ")[0].trim());
                    FoodItem food = foodDAO.getFoodItemById(foodId);
                    if (food != null) {
                        meal.addFood(food);
                    }
                }

                int mealId = mealDAO.addMeal(meal);
                if (mealId > 0) {
                    mealStatus.setText("Meal created! Total: " + meal.getTotalCalories() + " kcal");
                    mealStatus.setStyle("-fx-text-fill: green;");
                    mealNameField.clear();
                }
            } catch (NumberFormatException ex) {
                mealStatus.setText("Enter a valid User ID!");
                mealStatus.setStyle("-fx-text-fill: red;");
            }
        });

        // Refresh food list
        refreshBtn.setOnAction(e -> {
            foodListView.getItems().clear();
            ArrayList<FoodItem> foods = foodDAO.getAllFoodItems();
            for (FoodItem f : foods) {
                foodListView.getItems().add(f.getId() + " - " + f.getName() + " (" + f.getCalories() + " cal)");
            }
        });

        // Load meal history
        loadMealsBtn.setOnAction(e -> {
            try {
                int userId = Integer.parseInt(historyUserId.getText().trim());
                ArrayList<Meal> meals = mealDAO.getMealsByUserId(userId);
                StringBuilder sb = new StringBuilder();

                if (meals.isEmpty()) {
                    sb.append("No meals found for User ID: ").append(userId);
                } else {
                    for (Meal m : meals) {
                        sb.append("Meal: ").append(m.getMealName())
                          .append(" | Date: ").append(m.getMealDate())
                          .append(" | Total: ").append(m.getTotalCalories()).append(" kcal\n");
                        for (FoodItem f : m.getFoodList()) {
                            sb.append("   -> ").append(f.getName())
                              .append(" (").append(f.getCalories()).append(" cal, ")
                              .append(f.getProtein()).append("g protein)\n");
                        }
                        sb.append("\n");
                    }
                }
                mealHistoryArea.setText(sb.toString());
            } catch (NumberFormatException ex) {
                mealHistoryArea.setText("Enter a valid User ID!");
            }
        });

        vbox.getChildren().addAll(heading, mealForm, selectLabel, foodListView, selectHint,
                mealButtons, mealStatus,
                new Separator(), historyLabel, historySearch, mealHistoryArea);
        return vbox;
    }

    // =====================================================================
    // DAILY TRACKER TAB
    // Breakfast | Morning Snack | Lunch | Evening Snack | Dinner
    // =====================================================================
    private ScrollPane createDailyTrackerPane() {

        // Map from meal slot -> list of chosen FoodItems
        Map<String, ArrayList<FoodItem>> slotFoods = new LinkedHashMap<>();
        String[] slots = {
            "☀️ Breakfast",
            "🍎 Morning Snack",
            "🍱 Lunch",
            "🫐 Evening Snack",
            "🌙 Dinner"
        };
        for (String s : slots) slotFoods.put(s, new ArrayList<>());

        VBox root = new VBox(14);
        root.setPadding(new Insets(18));
        root.setStyle("-fx-background-color: #f4f7fb;");

        // ---- Title ------------------------------------------------
        Label titleLbl = new Label("📊  Daily Calorie Tracker");
        titleLbl.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; "
                        + "-fx-text-fill: #1a237e;");

        Label subtitleLbl = new Label("Add food to each meal slot, then press Calculate at the bottom.");
        subtitleLbl.setStyle("-fx-font-size: 12px; -fx-text-fill: #546e7a;");

        root.getChildren().addAll(titleLbl, subtitleLbl, makeDivider());

        // ---- Per-slot panels ---------------------------------------
        // We keep one TextArea per slot to show added items live
        Map<String, TextArea> slotDisplays = new LinkedHashMap<>();

        // Load all food items once for the combo-boxes
        ArrayList<FoodItem> allFoods = foodDAO.getAllFoodItems();

        for (String slot : slots) {
            VBox card = new VBox(8);
            card.setPadding(new Insets(12));
            card.setStyle("-fx-background-color: #ffffff; "
                        + "-fx-border-color: #cfd8dc; "
                        + "-fx-border-radius: 8; "
                        + "-fx-background-radius: 8;");

            Label slotLbl = new Label(slot);
            slotLbl.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #283593;");

            // Food selector
            HBox row = new HBox(10);
            row.setAlignment(Pos.CENTER_LEFT);

            ComboBox<String> foodCombo = new ComboBox<>();
            foodCombo.setPromptText("Choose a food item...");
            foodCombo.setPrefWidth(260);
            for (FoodItem f : allFoods) {
                foodCombo.getItems().add(f.getId() + " | " + f.getName()
                        + "  (" + f.getCalories() + " kcal)");
            }

            TextField qtyField = new TextField("1");
            qtyField.setPrefWidth(50);
            qtyField.setStyle("-fx-alignment: center;");

            Button addBtn = new Button("+ Add");
            addBtn.setStyle("-fx-background-color: #3949ab; -fx-text-fill: white; "
                          + "-fx-font-weight: bold; -fx-background-radius: 6;");

            Button clearBtn = new Button("Clear");
            clearBtn.setStyle("-fx-background-color: #e53935; -fx-text-fill: white; "
                            + "-fx-background-radius: 6;");

            row.getChildren().addAll(
                new Label("Food:"), foodCombo,
                new Label("Qty:"), qtyField,
                addBtn, clearBtn
            );

            // Display area for this slot
            TextArea display = new TextArea();
            display.setEditable(false);
            display.setPrefHeight(85);
            display.setStyle("-fx-font-family: monospace; -fx-font-size: 11px; "
                           + "-fx-control-inner-background: #f9fbe7;");
            display.setPromptText("No foods added yet.");
            slotDisplays.put(slot, display);

            // Add food to this slot
            final String currentSlot = slot;
            addBtn.setOnAction(e -> {
                String selected = foodCombo.getValue();
                if (selected == null) return;
                int qty;
                try { qty = Math.max(1, Integer.parseInt(qtyField.getText().trim())); }
                catch (NumberFormatException ex) { qty = 1; }

                int foodId = Integer.parseInt(selected.split(" \\| ")[0].trim());
                FoodItem food = foodDAO.getFoodItemById(foodId);
                if (food == null) return;

                ArrayList<FoodItem> list = slotFoods.get(currentSlot);
                for (int i = 0; i < qty; i++) list.add(food);

                refreshSlotDisplay(display, list, currentSlot);
                foodCombo.setValue(null);
                qtyField.setText("1");
            });

            // Clear this slot
            clearBtn.setOnAction(e -> {
                slotFoods.get(currentSlot).clear();
                display.clear();
                display.setPromptText("No foods added yet.");
            });

            card.getChildren().addAll(slotLbl, row, display);
            root.getChildren().add(card);
        }

        root.getChildren().add(makeDivider());

        // ---- Summary / Calculate button ----------------------------
        Button calcDayBtn = new Button("⚡  Calculate Full Day Nutrition");
        calcDayBtn.setStyle("-fx-background-color: #1a237e; -fx-text-fill: white; "
                          + "-fx-font-size: 14px; -fx-font-weight: bold; "
                          + "-fx-padding: 10 24 10 24; -fx-background-radius: 8;");

        // Summary panel
        VBox summaryCard = new VBox(10);
        summaryCard.setPadding(new Insets(14));
        summaryCard.setStyle("-fx-background-color: #e8eaf6; "
                           + "-fx-border-color: #3949ab; "
                           + "-fx-border-width: 2; "
                           + "-fx-border-radius: 8; "
                           + "-fx-background-radius: 8;");
        summaryCard.setVisible(false);

        Label summaryTitle = new Label("📋  Daily Nutrition Summary");
        summaryTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1a237e;");

        TextArea summaryArea = new TextArea();
        summaryArea.setEditable(false);
        summaryArea.setPrefHeight(240);
        summaryArea.setStyle("-fx-font-family: monospace; -fx-font-size: 12px; "
                           + "-fx-control-inner-background: #ffffff;");

        // Macro progress bars row
        GridPane barsGrid = new GridPane();
        barsGrid.setHgap(12);
        barsGrid.setVgap(6);

        Label calBar  = new Label();
        Label protBar = new Label();
        Label carbBar = new Label();
        Label fatBar  = new Label();
        Label[] barLabels = {calBar, protBar, carbBar, fatBar};
        String[] barColors = {"#e53935", "#8e24aa", "#039be5", "#fb8c00"};
        String[] barNames  = {"Calories", "Protein", "Carbs", "Fats"};
        for (int i = 0; i < 4; i++) {
            barsGrid.add(new Label(barNames[i] + ":"), 0, i);
            barsGrid.add(barLabels[i], 1, i);
        }

        summaryCard.getChildren().addAll(summaryTitle, summaryArea, barsGrid);

        calcDayBtn.setOnAction(e -> {
            int totalCal  = 0;
            double totalProt = 0, totalCarb = 0, totalFat = 0;
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%-22s %7s %9s %9s %9s%n",
                    "Meal Slot", "Kcal", "Prot(g)", "Carb(g)", "Fat(g)"));
            sb.append("─".repeat(60)).append("\n");

            for (String slot : slots) {
                ArrayList<FoodItem> list = slotFoods.get(slot);
                int slotCal = 0; double slotP = 0, slotC = 0, slotF = 0;
                for (FoodItem f : list) {
                    slotCal += f.getCalories();
                    slotP   += f.getProtein();
                    slotC   += f.getCarbs();
                    slotF   += f.getFats();
                }
                // strip emoji for the table
                String cleanName = slot.replaceAll("[^\\x00-\\x7F]", "").trim();
                if (cleanName.isEmpty()) cleanName = slot;
                sb.append(String.format("%-22s %7d %9.1f %9.1f %9.1f%n",
                        cleanName, slotCal, slotP, slotC, slotF));
                totalCal  += slotCal;
                totalProt += slotP;
                totalCarb += slotC;
                totalFat  += slotF;
            }
            sb.append("─".repeat(60)).append("\n");
            sb.append(String.format("%-22s %7d %9.1f %9.1f %9.1f%n",
                    "TOTAL DAY", totalCal, totalProt, totalCarb, totalFat));

            // Calorie goal hint
            double goalPct = totalCal / 2000.0 * 100;
            sb.append("\n");
            sb.append(String.format("Goal progress (vs 2000 kcal): %.1f%%%n", goalPct));
            if (goalPct < 80)       sb.append("⚠️  You're significantly under your calorie goal today.");
            else if (goalPct > 120) sb.append("⚠️  You've exceeded your calorie goal today.");
            else                    sb.append("✅  You're within a healthy calorie range!");

            summaryArea.setText(sb.toString());

            // Update inline labels
            calBar.setText(totalCal + " kcal");
            calBar.setStyle("-fx-font-weight: bold; -fx-text-fill: #e53935;");
            protBar.setText(String.format("%.1fg", totalProt));
            protBar.setStyle("-fx-font-weight: bold; -fx-text-fill: #8e24aa;");
            carbBar.setText(String.format("%.1fg", totalCarb));
            carbBar.setStyle("-fx-font-weight: bold; -fx-text-fill: #039be5;");
            fatBar.setText(String.format("%.1fg", totalFat));
            fatBar.setStyle("-fx-font-weight: bold; -fx-text-fill: #fb8c00;");

            summaryCard.setVisible(true);
        });

        HBox calcRow = new HBox(calcDayBtn);
        calcRow.setAlignment(Pos.CENTER);

        root.getChildren().addAll(calcRow, summaryCard);

        ScrollPane scroll = new ScrollPane(root);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: #f4f7fb; -fx-background-color: #f4f7fb;");
        return scroll;
    }

    /** Refresh a slot's display TextArea with current foods. */
    private void refreshSlotDisplay(TextArea display,
                                    ArrayList<FoodItem> foods, String slotName) {
        if (foods.isEmpty()) {
            display.clear();
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-22s %6s %8s %8s %8s%n",
                "Food", "Kcal", "Prot", "Carbs", "Fats"));
        sb.append("-".repeat(56)).append("\n");
        int totCal = 0; double totP = 0, totC = 0, totF = 0;
        for (FoodItem f : foods) {
            sb.append(String.format("%-22s %6d %7.1fg %7.1fg %7.1fg%n",
                    f.getName().length() > 22 ? f.getName().substring(0, 19) + "..." : f.getName(),
                    f.getCalories(), f.getProtein(), f.getCarbs(), f.getFats()));
            totCal += f.getCalories(); totP += f.getProtein();
            totC   += f.getCarbs();   totF += f.getFats();
        }
        sb.append("-".repeat(56)).append("\n");
        sb.append(String.format("%-22s %6d %7.1fg %7.1fg %7.1fg%n",
                "Subtotal", totCal, totP, totC, totF));
        display.setText(sb.toString());
    }

    /** Thin horizontal divider line. */
    private Separator makeDivider() {
        Separator sep = new Separator();
        sep.setStyle("-fx-border-color: #b0bec5;");
        return sep;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
