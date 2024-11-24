package UI;

import core.Logical;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import UserManagement.UserManager;
import UserManagement.Users;

import java.util.ArrayList;
import java.util.List;

/**
 * LeaderboardPage class representing the user interface for viewing the leaderboard.
 */
public class LeaderboardPage{

private final UserManager userManager   =   Logical.getInstance().getUserManager();
private final Users       currentUser;
private       VBox        alertBox;

/**
 * Constructor for LeaderboardPage.
 *
 * @param user the current user
 */
public LeaderboardPage(Users user) {
  this.currentUser = user;
}

public void start(Stage primaryStage) {
  // Prompt message
  Label promptLabel     =   new Label("Please choose the subject to view the leaderboard:");

  // Reminder message
  Label reminderLabel = new Label("Please select a subject from the left.");
  reminderLabel.setStyle("-fx-font-size: 14px; -fx-text-alignment: center;");
  // Create subject buttons
  Button csButton       =   new Button("Computer Science");
  Button eeButton       =   new Button("Electronic Engineering");
  Button englishButton  =   new Button("English");
  Button mathButton     =   new Button("Mathematics");
  Button returnButton   =   new Button("Return");

  // Specify button width
  double buttonWidth    =   250;
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
  csButton.setOnAction(e -> showLeaderboard("Computer Science"));
  eeButton.setOnAction(e -> showLeaderboard("Electronic Engineering"));
  englishButton.setOnAction(e -> showLeaderboard("English"));
  mathButton.setOnAction(e -> showLeaderboard("Mathematics"));
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
  HBox root   =   new HBox(10, buttonBox, alertContainer);

  // Create scene
  Scene scene =   new Scene(root, 750, 400);

  // Set stage
  primaryStage.setTitle("Leaderboard");
  primaryStage.setScene(scene);
  primaryStage.show();
}

/**
 * Show leaderboard for the specified subject.
 *
 * @param subject the subject to view the leaderboard for
 */
private void showLeaderboard(String subject) {
  // Get all users
  List<Users>   allUsers  =   List.copyOf(userManager.GetAllUsers());
  List<String>  topUsers  =   new ArrayList<>();
  int highestScore        =   -1;

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


  // Update alert box
  alertBox.getChildren().clear();
  if (!topUsers.isEmpty()) {
    Label titleLabel  =   new Label("Top User(s) \n- " + subject);
    titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-alignment: center;");

    Label emptyLine   =   new Label("");

    VBox usersBox     =   new VBox();
    usersBox.setStyle("-fx-alignment: center; -fx-spacing: 5px;");

    for (String user : topUsers) {
      Label userLabel =   new Label(user);
      userLabel.setStyle("-fx-font-size: 14px;");
      usersBox.getChildren().add(userLabel);
    }

    Label scoreLabel  =  new Label("\nHighest Score: " + highestScore);
    scoreLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-alignment: center;");

    alertBox.getChildren().addAll(titleLabel, emptyLine, usersBox, scoreLabel);
  } else {
    Label alertLabel  =   new Label("No scores available for this subject.");
    alertLabel.setStyle("-fx-font-size: 14px; -fx-text-alignment: center;");
    alertBox.getChildren().add(alertLabel);
  }
}
}