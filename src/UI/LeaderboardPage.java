package UI;

import core.Logical;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import UserManagement.UserManager;
import UserManagement.Users;

import java.util.ArrayList;
import java.util.List;

/**
 * LeaderboardPage class representing the user interface for viewing the leaderboard.
 */
public class LeaderboardPage extends Application {

private final UserManager userManager = Logical.getInstance().getUserManager();
private Users currentUser;

/**
 * Constructor for LeaderboardPage.
 *
 * @param user the current user
 */
public LeaderboardPage(Users user) {
  this.currentUser = user;
}

@Override
public void start(Stage primaryStage) {
  // Prompt message
  Label promptLabel = new Label("Please choose the subject to view the leaderboard:");

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
  csButton.setOnAction(e -> showLeaderboard(primaryStage, "Computer Science"));
  eeButton.setOnAction(e -> showLeaderboard(primaryStage, "Electronic Engineering"));
  englishButton.setOnAction(e -> showLeaderboard(primaryStage, "English"));
  mathButton.setOnAction(e -> showLeaderboard(primaryStage, "Mathematics"));
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
  primaryStage.setTitle("Leaderboard");
  primaryStage.setScene(scene);
  primaryStage.show();
}

/**
 * Show leaderboard for the specified subject.
 *
 * @param primaryStage the primary stage
 * @param subject the subject to view the leaderboard for
 */
private void showLeaderboard(Stage primaryStage, String subject) {
  // Get all users
  List<Users> allUsers = List.copyOf(userManager.GetAllUsers());

  List<String> topUsers = new ArrayList<>();
  int highestScore = -1;

  // Iterate through all users to find the highest score
  for (Users user : allUsers) {
    Integer userHighestScore = user.GetTopicSpecifiedHighestRecord(subject);
    if (userHighestScore != null) {
      if (userHighestScore > highestScore) {
        highestScore = userHighestScore;
        topUsers.clear();
        topUsers.add(String.format("%s (ID: %s)", user.GetName(), user.GetId()));
      } else if (userHighestScore == highestScore) {
        topUsers.add(String.format("%s (ID: %s)", user.GetName(), user.GetId()));
      }
    }
  }

  // Build the display message
  String result;
  if (!topUsers.isEmpty()) {
    result = String.format("Top Users for %s with a score of %d:\n%s",
                           subject, highestScore, String.join("\n", topUsers));
  } else {
    result = "No scores available for this subject.";
  }

  // Display the result
  Alert alert = new Alert(Alert.AlertType.INFORMATION, result);
  alert.setHeaderText("Leaderboard");
  alert.setTitle("Leaderboard");
  alert.showAndWait();
}
}