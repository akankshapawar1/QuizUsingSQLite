# Offline Quiz Application

This project is a **time-based offline quiz application** designed to assess users across multiple categories and difficulty levels. User responses are recorded in an SQLite database, and the quiz logic includes both time constraints and dynamic scoring based on response speed.

## Key Features

- 👤 **User Registration**: Users must enter their information before starting the quiz. Details are stored in an SQLite database.
- 🗂️ **Quiz Categories**:
  - Java
  - Database
  - Android
- 🧩 **Question Levels**:
  - Each category has 3 levels: Easy, Medium, and Difficult.
  - Each level contains 10 multiple-choice questions (4 options each).
- ⏱️ **Time Constraints**:
  - Each question has a time limit of **30 seconds**.
  - If the time exceeds, the question auto-submits and moves to the next.
  - The total quiz duration is capped at **5 minutes**.
- 🧮 **Dynamic Scoring System**:
  - ✅ Correct answer within **first 10 seconds** → **+4 points**
  - ✅ Correct answer between **10–20 seconds** → **+2 points**
  - ✅ Correct answer after **20 seconds** → **+1 point**
  - ❌ Incorrect answers → **0 points**
- 📝 **Final Score**:
  - Displayed at the end of the quiz.
  - Automatically updated in the database.
