# CPT111 Group Course Work 3 For Year 2024
==========================================
## Project Description
This project is a **Quiz System** built using Java, which is implemented with JavaFX for the user interface. It provides functionalities for users to register, log in, take quizzes, view their score history, and check leaderboards.
### 1. User Management
**1.1 Registration:**
Users can sign up by providing user ID(must be unique), name, and password. Users will be automatically logged in after successful registration. The system can save the user information in to a CSV file(u.csv).
- u.csv:  3 columns(`user id`, `user name`, and `password`)

**1.2 Login:**
User can log in with their ID and password, and are verified against the stored CSV data.
### 2. Quiz Functionality
**2.1 Topic Selection:**
User can select a topic from Computer Science, Electronic Engineering, English and Mathematics
**2.2 Question Selection:**
1. Question are randomly selected from XML files in questionsBank.
2. Only validate questions are selected. (has more than one answer available for selection,and has only one correct answer)
3. The order of answers available for selection is shuffled every time before display. 
4. Topic: Each quiz contain questions from the selected topic. 
5. Difficulty: Each quiz contains 8 questions: 2 easy, 2 medium 2 hard, 2 very hard. 
6. The total score: 100(easy: 5 points, medium: 10 points, hard: 15 points, very hard: 20 points)

**2.3 Score Management:**
User scores are stored and updated in a CSV file(s.csv)
- s.csv:  3 columns(`userId`, `topic`, `score1`, `score2`, `score3`（the previous 3 tests attempted) , `highestScore`)
### 3. User dashboard and Leaderboard
**3.1  User dashboard:**
User dashboard shows the user's score history, including recent scores for each topic (up to the last 3 attempts) and the highest score

**3.2 Leaderboard:**
Display the names of the users with the highest score in each topic

