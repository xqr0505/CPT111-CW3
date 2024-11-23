package UI;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import UserManagement.Users;
import java.util.List;

/**
 * UserDashboardPage class representing the user interface for viewing history scores.
 */
public class UserDashboardPage{

private final Users currentUser;
private       VBox  alertBox;

/**
 * Constructor for UserDashboardPage.
 *
 * @param user the current user
 */
public UserDashboardPage(Users user) {
  this.currentUser = user;
}

public void start(Stage primaryStage) {
  // Prompt message
  Label promptLabel = new Label("Please choose the subject to view history scores:");

  // Reminder message
  Label reminderLabel = new Label("Please select a subject from the left.");
  reminderLabel.setStyle("-fx-font-size: 14px; -fx-text-alignment: center;");

  // Create subject buttons
  Button csButton = new Button("Computer Science");
  Button eeButton = new Button("Electronic Engineering");
  Button englishButton = new Button("English");
  Button mathButton = new Button("Mathematics");
  Button returnButton = new Button("Return");

  // Specify button width
  double buttonWidth = 250;
  csButton.setPrefWidth(buttonWidth);
  eeButton.setPrefWidth(buttonWidth);
  englishButton.setPrefWidth(buttonWidth);
  mathButton.setPrefWidth(buttonWidth);
  returnButton.setPrefWidth(buttonWidth);

  // Set button styles
  returnButton.setStyle("-fx-background-color: #a3c5f4;");
  returnButton.setOnMouseEntered(e -> returnButton.setStyle("-fx-background-color: #d0e1f9"));
  returnButton.setOnMouseExited(e -> returnButton.setStyle("-fx-background-color: #a3c5f4;"));

  // Set button click events
  csButton.setOnAction(e -> showHistoryScores("Computer Science"));
  eeButton.setOnAction(e -> showHistoryScores("Electronic Engineering"));
  englishButton.setOnAction(e -> showHistoryScores("English"));
  mathButton.setOnAction(e -> showHistoryScores("Mathematics"));
  returnButton.setOnAction(e -> {
    Menu menu = new Menu(currentUser);
    menu.start(primaryStage);
  });

  // Layout settings
  VBox buttonBox = new VBox(10, promptLabel, csButton, eeButton, englishButton, mathButton, returnButton);
  buttonBox.setStyle("-fx-alignment: center; -fx-padding: 50px;");
  buttonBox.setMinWidth(400);
  buttonBox.setMaxWidth(400);

  // Initialize alertBox
  alertBox = new VBox(reminderLabel);
  VBox alertContainer = new VBox(alertBox);
  alertContainer.setStyle("-fx-padding: 10px; -fx-alignment: center;");

  alertBox.setStyle("-fx-alignment: center; -fx-padding: 20px;-fx-background-color: white;" +
                    "-fx-border-radius: 10px; -fx-background-radius: 10px; -fx-border-color: #b1b1b1;" +
                    " -fx-border-width: 1px;");
  alertBox.setMinWidth(300);
  alertBox.setMaxWidth(300);
  alertBox.setMinHeight(300);

  // Main layout
  HBox root = new HBox(10, buttonBox, alertContainer);

  // Create scene
  Scene scene = new Scene(root, 750, 400);

  // Set stage
  primaryStage.setTitle("User Dashboard");
  primaryStage.setScene(scene);
  primaryStage.show();
}

/**
 * Show history scores for the specified subject.
 *
 * @param subject the subject to view history scores for
 */
private void showHistoryScores(String subject) {
  // Get the user's recent scores for the subject
  List<Integer> scoresList = currentUser.GetTopicSpecifiedRecentRecords(subject);

  // Update alert box
  alertBox.getChildren().clear();
  if (scoresList == null || scoresList.isEmpty()) {
    Label alertLabel = new Label("No history scores for this subject.");
    alertLabel.setStyle("-fx-font-size: 14px; -fx-text-alignment: center;");
    alertBox.getChildren().add(alertLabel);
    return;
  }

  // Get the highest score
  Integer highestScore = currentUser.GetTopicSpecifiedHighestRecord(subject);

  // Build the score display elements
  Label titleLabel = new Label(currentUser.GetName()+"： History Scores \n- " + subject);
  titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-alignment: center;");

  Label emptyLine = new Label("");

  VBox scoresBox = new VBox();
  scoresBox.setStyle("-fx-alignment: center; -fx-spacing: 5px;");

  for (int i = 0; i < scoresList.size(); i++) {
    Integer score = scoresList.get(i);
    if (score != null) {
      Label scoreLabel = new Label(String.format("Attempt %d:\t%3d", i + 1, score));
      scoreLabel.setStyle("-fx-font-size: 14px;");
      scoresBox.getChildren().add(scoreLabel);
    }
  }

  if (highestScore != null) {
    Label highestScoreLabel = new Label("\nHighest Score: " + highestScore);
    highestScoreLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-alignment: center;");
    alertBox.getChildren().addAll(titleLabel, emptyLine, scoresBox, highestScoreLabel);
  } else {
    alertBox.getChildren().addAll(titleLabel, emptyLine, scoresBox);
  }
}
}