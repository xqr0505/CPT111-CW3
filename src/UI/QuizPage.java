package UI;

import core.Logical;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import UserManagement.Users;
import xjtlu.cpt111.assignment.quiz.model.Question;
import xjtlu.cpt111.assignment.quiz.model.Option;
import QuestionManagement.QuestionManager;
import QuestionManagement.Exceptions.NoTopicFoundException;

import java.io.IOException;
import java.util.*;

/**
 * QuizPage class representing the user interface for taking a quiz.
 */
public class QuizPage extends Application {

private final   Users             currentUser;
private final   String            subject;
private         List<Question>    questions;
private         int               currentQuestionIndex  =   0;
private         int               totalScore            =   0;

private   Label             questionLabel;
private   ToggleGroup       optionsGroup;
private   List<RadioButton> optionButtons;
private   Button            nextButton;
private   Button            returnButton;

private   Stage             primaryStage;

private   QuestionManager   questionManager = Logical.getInstance().getQuestionManager();

// Map difficulty to score
private final Map<String, Integer> difficultyScoreMap = new HashMap<>();

/**
 * Constructor for QuizPage.
 *
 * @param user the current user
 * @param subject the subject of the quiz
 */
public QuizPage(Users user, String subject) {
  this.currentUser  =   user;
  this.subject      =   subject;

  // Initialize difficulty to score mapping
  difficultyScoreMap.put("EASY", 5);
  difficultyScoreMap.put("MEDIUM", 10);
  difficultyScoreMap.put("HARD", 15);
  difficultyScoreMap.put("VERY_HARD", 20);
}

@Override
public void start(Stage primaryStage) {
  this.primaryStage = primaryStage;

  // Initialize UI components
  questionLabel   =   new   Label();
  questionLabel.setWrapText(true);
  questionLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

  optionsGroup    =   new   ToggleGroup();
  optionButtons   =   new   ArrayList<>();

  // Create option buttons(set max options number to 5)
  int maxOptions = 5;
  for (int i = 0; i < maxOptions; i++) {
    RadioButton optionButton  =   new RadioButton();
    optionButton.setToggleGroup(optionsGroup);
    optionButton.setPrefWidth(500);
    optionButtons.add(optionButton);
  }

  // Create control buttons
  nextButton = new Button("Next");
  returnButton = new Button("Return");

  // Specify button width
  double buttonWidth = 150;
  nextButton.setPrefWidth(buttonWidth );
  returnButton.setPrefWidth(buttonWidth );

  // Set button styles
  returnButton.setStyle("-fx-background-color: #a3c5f4;");
  returnButton.setOnMouseEntered(e -> returnButton.setStyle("-fx-background-color: #d0e1f9"));
  returnButton.setOnMouseExited(e -> returnButton.setStyle("-fx-background-color: #a3c5f4;"));

  // Set button click events
  nextButton.setOnAction(e -> handleNextAction());
  returnButton.setOnAction(e -> {
    // Return to subject selection page
    SubjectChoosePage subjectChoosePage = new SubjectChoosePage(currentUser);
    subjectChoosePage.start(primaryStage);
  });

  // Layout settings
  VBox root = new VBox(15, questionLabel);
  root.setStyle("-fx-padding: 20px;");
  root.setAlignment(Pos.CENTER);

  // Add option buttons
  for (RadioButton optionButton : optionButtons) {
    root.getChildren().add(optionButton);
  }

  // Add control buttons
  HBox buttonBox = new HBox(20, nextButton, returnButton);
  buttonBox.setAlignment(Pos.CENTER);
  root.getChildren().add(buttonBox);

  // Create scene
  Scene scene = new Scene(root, 600, 400);

  // Set stage
  primaryStage.setTitle("Quiz - " + subject);
  primaryStage.setScene(scene);
  primaryStage.show();

  // Ensure questions are loaded
  loadQuestions();

  // Show the first question
  showQuestion();
}

/**
 * Load questions for the quiz.
 */
private void loadQuestions() {
  try {
    questions = questionManager.loadQuestionsForQuiz(subject);
  } catch (NoTopicFoundException e) {
    // If no questions found, show error message and return
    Alert alert = new Alert(Alert.AlertType.ERROR, "No questions found for this subject.", ButtonType.OK);
    alert.showAndWait();
    // Return to subject selection page
    SubjectChoosePage subjectChoosePage = new SubjectChoosePage(currentUser);
    subjectChoosePage.start(primaryStage);
  }
}

/**
 * Show the current question.
 */
private void showQuestion() {
  if (currentQuestionIndex >= questions.size()) {
    // All questions completed, show score
    showScore();
    return;
  }

  Question currentQuestion = questions.get(currentQuestionIndex);

  // Display question text
  questionLabel.setText("Question " + (currentQuestionIndex + 1) + ": " + currentQuestion.getQuestionStatement());

  // Get options
  Option[] options = currentQuestion.getOptions();
  List<Option> optionList = Arrays.asList(options);
  // Shuffle options already done in loadQuestions()

  // Display options
  for (int i = 0; i < optionButtons.size(); i++) {
    RadioButton optionButton = optionButtons.get(i);
    if (i < optionList.size()) {
      optionButton.setText(optionList.get(i).getAnswer());
      optionButton.setUserData(optionList.get(i));
      optionButton.setVisible(true);
    } else {
      optionButton.setVisible(false);
    }
  }

  // Change "Next" button text to "Submit" for the last question
  if (currentQuestionIndex == questions.size() - 1) {
    nextButton.setText("Submit");
  } else {
    nextButton.setText("Next");
  }
}

/**
 * Handle the action for the next button.
 */
private void handleNextAction() {
  // Check user selection
  Toggle selectedToggle = optionsGroup.getSelectedToggle();
  if (selectedToggle != null) {
    Option selectedOption = (Option) selectedToggle.getUserData();
    if (selectedOption.isCorrectAnswer()) {
      // Assign score based on question difficulty
      String difficulty  =  questions.get(currentQuestionIndex).getDifficulty().toString();
      int score          =  difficultyScoreMap.getOrDefault(difficulty, 0);
      totalScore        +=  score;
    }
  }
  // No selection is considered incorrect, no score added

  // Clear selection
  optionsGroup.selectToggle(null);

  // Move to the next question
  currentQuestionIndex++;
  showQuestion();
}

/**
 * Show the total score and save the result.
 */
private void showScore() {
  // Display total score and save the result
  Alert alert = new Alert(Alert.AlertType.INFORMATION, "Your total score is: " + totalScore, ButtonType.OK);
  alert.setHeaderText("Quiz finished!");
  alert.setTitle("Quiz Score");
  alert.showAndWait();

  // Record the score in the user's ScoreRecords
  currentUser.NewRecord(subject, totalScore);

  try {
    // Save score information
    Logical.getInstance().getUserManager().SaveUserInfo("resources/u.csv", "resources/s.csv");
  } catch (IOException ex) {
    // Handle save error
    Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Error saving score information.", ButtonType.OK);
    errorAlert.showAndWait();
    ex.printStackTrace();
  }

  // Return to Menu
  Menu menu = new Menu(currentUser);
  try {
    menu.start(primaryStage);
  } catch (Exception e) {
    e.printStackTrace();
  }
}
}