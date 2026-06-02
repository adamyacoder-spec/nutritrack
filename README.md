# 🥗 NutriTrack

A desktop calorie and nutrition tracking application. Built to practice Object-Oriented Programming (OOP) in Java, modular GUI construction using **JavaFX**, database integration with **MySQL via JDBC**, and clean architectural patterns.

---

## 🛠️ Tech Stack & Architecture

![Java](https://img.shields.io/badge/Java-17%2B-orange?style=flat-square&logo=openjdk)
![MySQL](https://img.shields.io/badge/MySQL-9.7-blue?style=flat-square&logo=mysql)
![JavaFX](https://img.shields.io/badge/JavaFX-23-green?style=flat-square&logo=java)
![License](https://img.shields.io/badge/License-MIT-yellow?style=flat-square)

*   **Frontend UI**: JavaFX styled using a custom external CSS stylesheet for modern dark/vibrant aesthetics.
*   **Database Engine**: MySQL database accessed using JDBC (Java Database Connectivity) with prepared statements to prevent SQL injections.
*   **Architectural Pattern**: **DAO (Data Access Object) Pattern** separating database queries (e.g. CRUD user profiles, foods, and logs) from UI controllers.

---

## ✨ Features

*   **Date-Based Meal Tracking** — Log meal data across Breakfast, Lunch, Dinner, and Snacks for any calendar day.
*   **Portion Conversion Engine** — Add entries using grams, cups, or pieces with real-time calorie math.
*   **Target Calorie Calculator** — Uses the Mifflin-St Jeor equation to estimate daily targets based on user weight, height, age, gender, and physical activity level.
*   **Real-time Preview UI** — Calorie counts dynamically update as the user types portion size inputs.
*   **MySQL State Persistence** — Ensures user profiles, custom foods list, and daily meal journals are stored securely across app restarts.

---

## 🎥 Media & Walkthroughs

### Video Demo
<video src="images/demo_walkthrough.mp4" controls width="100%"></video>

### User Interface Screenshots

| Home Dashboard | Profile & Goals Setup |
|---|---|
| ![Home](images/home_dashboard.png) | ![Profile](images/profile_goals.png) |

| Daily Tracker | Daily Summary |
|---|---|
| ![Tracker](images/tracker_breakfast.png) | ![Summary](images/tracker_summary.png) |

---

## 📁 Repository Structure

```
nutritrack/
├── src/
│   ├── model/          # Pure Data Models (User, FoodItem, Meal)
│   ├── dao/            # Data Access Objects (UserDAO, FoodItemDAO, MealDAO)
│   ├── util/           # Common Helpers (DatabaseUtil, CalorieCalculator)
│   ├── view/           # JavaFX Layout classes and style.css
│   └── Main.java       # CLI / Console demonstration entry point
├── sql/
│   └── schema.sql      # Database schema creation and mock seed data
├── lib/                # JavaFX SDK binaries & MySQL JDBC Connector
├── images/             # UI Screenshots and walkthrough video
├── LICENSE
└── README.md
```

---

## 🧠 Architectural Lessons

*   **Decoupled Database Logic**: Implemented the **DAO pattern** to completely segregate raw SQL queries from JavaFX event listeners, making the code easier to maintain and test.
*   **Database Lifecycle Management**: Mastered JDBC connection lifecycles, using Java’s try-with-resources blocks to automate closing connections, statements, and result sets, preventing connection pool leaks.
*   **Declarative UI Styling**: Styled JavaFX layouts using external CSS selectors rather than in-line Java style setters to keep UI code clean and layout code consistent.

---

## ⚙️ Setup & Execution

### Prerequisites
*   Java Development Kit (JDK) 17 or higher.
*   A running instance of MySQL database (defaulting to port `3307`).
*   JavaFX SDK & MySQL Connector JAR inside the `/lib` folder.

### Setup Instructions

1.  **Initialize Database Schema**:
    Import the SQL schema to create database tables and load default food logs.
    ```powershell
    # Windows Powershell
    Get-Content sql/schema.sql -Raw | mysql -u root --port=3307
    ```

2.  **Compile Java Source Files**:
    Compile the java source tree with the module path configuration for JavaFX.
    ```powershell
    javac --module-path "lib\javafx-sdk-23.0.2\lib" --add-modules javafx.controls -cp "src;lib\mysql-connector-j-8.4.0.jar" src\Main.java src\model\*.java src\view\*.java src\dao\*.java src\util\*.java
    ```

3.  **Launch the JavaFX Dashboard**:
    ```powershell
    java --module-path "lib\javafx-sdk-23.0.2\lib" --add-modules javafx.controls -cp "src;lib\mysql-connector-j-8.4.0.jar" view.Dashboard
    ```

---

## 🔮 Roadmap / Future Features
- [ ] Add interactive data visualization charts to track weekly/monthly calorie history.
- [ ] Implement a Custom Recipes builder allowing combinations of multiple standard ingredients.
- [ ] Add functionality to export weekly nutritional summaries to CSV/PDF reports.

---

## 📄 License
Licensed under the MIT License — feel free to modify, study, or redistribute this project.

## 🤝 Connect
*   **LinkedIn**: [Adamya Vats](https://www.linkedin.com/in/adamya-vats-kd1983)
*   **GitHub**: [@adamyacoder-spec](https://github.com/adamyacoder-spec)
