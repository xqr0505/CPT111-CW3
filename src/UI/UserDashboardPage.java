package UI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import UserManagement.Users;
import java.util.List;

/**
 * HistoryScorePage class representing the user interface for viewing history scores.
 */
public class UserDashboardPage extends Application {

private Users currentUser;

/**
 * Constructor for UserDashboardPage.
 *
 * @param user the current user
 */
public UserDashboardPage(Users user) {
  this.currentUser = user;
}

@Override
public void start(Stage primaryStage) {
  // Prompt message
  Label promptLabel = new Label("Please choose the subject to view history scores:");

  // Create subject buttons
  Button csButton = new Button("Computer Science");
  Button eeButton = new Button("Electronic Engineering");
  Button englishButton = new Button("English");
  Button mathButton = new Button("Mathematics");
  Button returnButton = new Button("Return");

  double buttonWidth = 250; // Specify button width
  csButton.setPrefWidth(buttonWidth);
  eeButton.setPrefWidth(buttonWidth);
  englishButton.setPrefWidth(buttonWidth);
  mathButton.setPrefWidth(buttonWidth);
  returnButton.setPrefWidth(buttonWidth);

  // Set button click events
  csButton.setOnAction(e -> showHistoryScores(primaryStage, "Computer Science"));
  eeButton.setOnAction(e -> showHistoryScores(primaryStage, "Electronic Engineering"));
  englishButton.setOnAction(e -> showHistoryScores(primaryStage, "English"));
  mathButton.setOnAction(e -> showHistoryScores(primaryStage, "Mathematics"));
  returnButton.setOnAction(e -> {
    Menu menu = new Menu(currentUser);
    menu.start(primaryStage);
  });

  // Layout settings
  VBox root = new VBox(10, promptLabel, csButton, eeButton, englishButton, mathButton, returnButton);
  root.setStyle("-fx-alignment: center; -fx-padding: 50px;");

  // Create scene
  Scene scene = new Scene(root, 400, 400);

  // Set stage
  primaryStage.setTitle("User Dashboard");
  primaryStage.setScene(scene);
  primaryStage.show();
}

/**
 * Show history scores for the specified subject.
 *
 * @param primaryStage the primary stage
 * @param subject the subject to view history scores for
 */
private void showHistoryScores(Stage primaryStage, String subject) {
  // Get the user's recent scores for the subject
  List<Integer> scoresList = currentUser.GetTopicSpecifiedRecentRecords(subject);

  if (scoresList == null || scoresList.isEmpty()) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION, "No history scores for this subject.", ButtonType.OK);
    alert.setHeaderText("No Scores!");
    alert.setTitle("User Dashboard");
    alert.showAndWait();
    return;
  }

  // Get the highest score
  Integer highestScore = currentUser.GetTopicSpecifiedHighestRecord(subject);

  // Build the score display string
  StringBuilder scoreText = new StringBuilder("Your last 3 scores for " + subject + ":\n");
  for (int i = 0; i < scoresList.size(); i++) {
    Integer score = scoresList.get(i);
    if (score != null) {
      scoreText.append("Attempt ").append(i + 1).append(": ").append(score).append("\n");
    }
  }

  if (highestScore != null) {
    scoreText.append("Highest Score: ").append(highestScore).append("\n");
  }

  // Display the scores
  Alert alert = new Alert(Alert.AlertType.INFORMATION, scoreText.toString(), ButtonType.OK);
  alert.setHeaderText("History Score");
  alert.setTitle("User Dashboard");
  alert.showAndWait();
}
}