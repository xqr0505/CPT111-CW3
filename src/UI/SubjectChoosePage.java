package UI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import UserManagement.Users;

/**
 * SubjectChoosePage class representing the user interface for choosing a subject.
 */
public class SubjectChoosePage extends Application {

private Users currentUser;

/**
 * Constructor for SubjectChoosePage.
 *
 * @param user the current user
 */
public SubjectChoosePage(Users user) {
  this.currentUser = user;
}

@Override
public void start(Stage primaryStage) {
  // Prompt message
  Label promptLabel = new Label("Please choose your subject:");

  // Create subject buttons
  Button csButton = new Button("Computer Science");
  Button eeButton = new Button("Electronic Engineering");
  Button englishButton = new Button("English");
  Button mathButton = new Button("Mathematics");
  Button returnButton = new Button("Return");

  double buttonWidth = 200; // Specify button width
  csButton.setPrefWidth(buttonWidth);
  eeButton.setPrefWidth(buttonWidth);
  englishButton.setPrefWidth(buttonWidth);
  mathButton.setPrefWidth(buttonWidth);
  returnButton.setPrefWidth(buttonWidth);

  // Set button click events
  csButton.setOnAction(e -> startQuiz(primaryStage, "Computer Science"));
  eeButton.setOnAction(e -> startQuiz(primaryStage, "Electronic Engineering"));
  englishButton.setOnAction(e -> startQuiz(primaryStage, "English"));
  mathButton.setOnAction(e -> startQuiz(primaryStage, "Mathematics"));
  returnButton.setOnAction(e -> {
    Dashboard dashboard = new Dashboard(currentUser);
    dashboard.start(primaryStage);
  });

  // Layout settings
  VBox root = new VBox(10, promptLabel, csButton, eeButton, englishButton, mathButton, returnButton);
  root.setStyle("-fx-alignment: center; -fx-padding: 50px;");

  // Create scene
  Scene scene = new Scene(root, 400, 400);

  // Set stage
  primaryStage.setTitle("Choose Subject");
  primaryStage.setScene(scene);
  primaryStage.show();
}

/**
 * Start the quiz for the selected subject.
 *
 * @param primaryStage the primary stage
 * @param subject the subject to start the quiz for
 */
private void startQuiz(Stage primaryStage, String subject) {
  QuizPage quizPage = new QuizPage(currentUser, subject);
  quizPage.start(primaryStage);
}
}