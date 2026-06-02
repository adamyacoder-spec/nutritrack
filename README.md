# 🥗 NutriTrack

A high-performance, modern Java desktop application designed to track users, food items, and daily calorie intake. Built with a robust **JavaFX** dashboard, integrated with a **MySQL** database, and structured using the **DAO (Data Access Object)** design pattern. 

---

## 🛠️ Technology Stack & Badges

![Java Version](https://img.shields.io/badge/Java-17%2B-orange?style=for-the-badge&logo=openjdk)
![MySQL](https://img.shields.io/badge/MySQL-9.7%2B-blue?style=for-the-badge&logo=mysql)
![JavaFX](https://img.shields.io/badge/JavaFX-23-green?style=for-the-badge&logo=java)
![License](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)

- **Frontend**: JavaFX (Styled using modern external CSS sheet `style.css` with a custom emerald glassmorphism theme)
- **Backend**: Core Java (OOP, multi-class structures, custom algorithms)
- **Database**: MySQL (JDBC API for relational persistence and date-based tracking)

---

## 🚀 Key Features

*   **⚖️ Portion Conversions**: Add food items and track consumption in **Grams (g)**, **Cups**, or **Pieces/Units**. Handles automated portion-to-weight conversions using calibrated serving weights.
*   **📅 Date-Based Persistence**: Track and store meals across multiple/previous days. The Daily Tracker loads and persists slot records (Breakfast, Lunch, Dinner, Snacks) directly in the database by date.
*   **🧠 Humanized Goals & Calculations**: Calculates BMR and calorie needs using the **Mifflin-St Jeor formula**, scaled by **Activity Levels** (Sedentary to Very Active) and adjusted for **Realistic Goals** (Healthy Deficit, Aggressive Deficit, Lean Bulk, Active Surplus, Maintenance).
*   **🎨 Premium GUI Dashboard**: Clean JavaFX layout featuring dynamic focus states, responsive cards with drop-shadows, and a **Live Nutrition Preview** as you type portion sizes.

---

## 🎥 Application Preview & Screenshots

### Demo Walkthrough
Watch a video walkthrough of the app's functionality:

<video src="images/demo_walkthrough.mp4" controls width="100%"></video>

### Screenshots

| 🏠 Home Dashboard | 👤 Profile & Goals |
|---|---|
| ![Home Dashboard](images/home_dashboard.png) | ![Profile & Goals](images/profile_goals.png) |

| 🍳 Daily Tracker (Breakfast) | 🥪 Daily Tracker (Lunch) |
|---|---|
| ![Daily Tracker Breakfast](images/tracker_breakfast.png) | ![Daily Tracker Lunch](images/tracker_lunch.png) |

| 🍲 Daily Tracker (Dinner) | 📊 Daily Summary & Progress |
|---|---|
| ![Daily Tracker Dinner](images/tracker_dinner.png) | ![Daily Summary](images/tracker_summary.png) |

---

## 🏛️ Architecture & Database Design

### Design Patterns
This project adheres to professional software engineering practices:
- **DAO Pattern (Data Access Object)**: Separates business logic from persistence. Database operations for `User`, `FoodItem`, and `Meal` are handled in dedicated classes within the `dao` package.
- **Model-View Separation**: JavaFX frontend classes (in the `view` package) are separated from the database query execution and data representation (in `model` and `dao` packages).

### Database Schema Structure
The schema is designed to support relations between users, food items, and date-based logged meals:
1.  **`users`**: Stores profile information (weight, height, age, gender, activity level, and target goals).
2.  **`food_items`**: Contains default and user-defined food items with standard nutrition profiles per serving weight.
3.  **`meals`**: Tracks portion size, slot (Breakfast, Lunch, etc.), and the date of consumption, mapped to a specific `user_id` and `food_item_id`.

---

## 📁 Project Directory Structure

    nutritrack/
    ├── src/
    │   ├── model/          User, FoodItem, Meal (Plain Old Java Objects - POJOs)
    │   ├── dao/            UserDAO, FoodItemDAO, MealDAO (Database persistence layers)
    │   ├── util/           DatabaseUtil (Connection pooling), CalorieCalculator (Algorithms)
    │   ├── view/           Dashboard (Styled JavaFX GUI), style.css (Emerald theme sheet)
    │   └── Main.java       Console-based application demo entry point
    ├── sql/
    │   └── schema.sql      Database schema generation script and default seeds
    ├── lib/                MySQL Connector/J and JavaFX SDK binaries (git-ignored)
    ├── images/             Screenshots and demo video of the application
    ├── LICENSE             MIT License details
    └── README.md           Project documentation

---

## ⚙️ Prerequisites & Setup

### Prerequisites
- **Java Development Kit (JDK)**: Version 17 or higher
- **MySQL Server**: Running on port `3307` with root password empty (configured in `DatabaseUtil.java`)
- **JavaFX SDK & MySQL Connector/J**: Placed inside the `lib/` directory

### 1. Database Setup
Start your MySQL server on port `3307` and initialize the schema:
```powershell
Get-Content sql/schema.sql -Raw | mysql -u root --port=3307
```

### 2. Compilation
Compile all Java source files:
```powershell
javac --module-path "lib\javafx-sdk-23.0.2\lib" --add-modules javafx.controls -cp "src;lib\mysql-connector-j-8.4.0.jar" src\Main.java src\model\*.java src\view\*.java src\dao\*.java src\util\*.java
```

### 3. Running the App

*   **To run the Console Demo:**
    ```powershell
    java -cp "src;lib\mysql-connector-j-8.4.0.jar" Main
    ```
*   **To run the JavaFX GUI Dashboard:**
    ```powershell
    java --module-path "lib\javafx-sdk-23.0.2\lib" --add-modules javafx.controls -cp "src;lib\mysql-connector-j-8.4.0.jar" view.Dashboard
    ```

---

## 🔮 Future Roadmap
- [ ] **Interactive Progress Charts**: Visual graphs (bar charts, line graphs) mapping calorie trends over time.
- [ ] **Custom Recipe Builder**: Allow users to combine food items into single reusable recipes.
- [ ] **Export to PDF**: Generate structured weekly or monthly nutrition reports.

---

## 📄 License
Distributed under the MIT License. See [LICENSE](LICENSE) for more details.

---

## 🤝 Connect with Me
- **GitHub**: [@adamyacoder-spec](https://github.com/adamyacoder-spec)
- **LinkedIn**: [Adamya Vats](https://www.linkedin.com/in/adamya-vats-kd1983)
