package UI;

import QuestionManagement.Exceptions;
import QuestionManagement.QuestionManager;
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

private final UserManager     userManager   =   Logical.getInstance().getUserManager();
private final Users           currentUser;
private       VBox            alertBox;

/**
 * Constructor for LeaderboardPage.
 *
 * @param user the current user
 */
public LeaderboardPage(Users user) {
  this.currentUser = user;
}

/**
 * Starts the LeaderboardPage UI.
 *
 * @param primaryStage the primary stage for this application
 */
public void start(Stage primaryStage) {
  // Prompt message
  Label promptLabel = new Label("Please choose the subject to view the leaderboard:");

  // Reminder message
  Label reminderLabel = new Label("Please select a subject from the left.");
  reminderLabel.setStyle("-fx-font-size: 14px; -fx-text-alignment: center;");

  // Create a VBox to hold subject buttons
  VBox buttonBox = new VBox(10, promptLabel);
  buttonBox.setStyle("-fx-alignment: center; -fx-padding: 50px;");
  buttonBox.setMinWidth(400);
  buttonBox.setMaxWidth(400);

  // Specify button width
  double buttonWidth = 200;

  // Get topics from all users' answered topics
  String[] topics = userManager.getAllTopicsAnsweredByAnyUser();
  if (topics.length == 0) {
    Label errorLabel = new Label("No subjects available.");
    buttonBox.getChildren().add(errorLabel);
    Button returnButton = UIUtils.createReturnButton("Return", buttonWidth, ev -> {
      Menu menu = new Menu(currentUser);
      menu.start(primaryStage);
    });
    buttonBox.getChildren().add(returnButton);
    VBox root = new VBox(10, promptLabel, buttonBox);
    root.setStyle("-fx-alignment: center; -fx-padding: 50px;");
    Scene scene = new Scene(root, 400, 400);
    primaryStage.setTitle("Leaderboard");
    primaryStage.setScene(scene);
    primaryStage.show();
    return;
  }

  // Dynamically create buttons based on topics
  for (String topic : topics) {
    Button topicButton = new Button(topic);
    topicButton.setPrefWidth(buttonWidth);
    topicButton.setOnAction(e -> showLeaderboard(topic));
    buttonBox.getChildren().add(topicButton);
  }

  // Create return button
  Button returnButton = UIUtils.createReturnButton("Return", buttonWidth, ev -> {
    Menu menu = new Menu(currentUser);
    menu.start(primaryStage);
  });
  buttonBox.getChildren().add(returnButton);

  // Initialize alertBox
  alertBox = new VBox(reminderLabel);
  VBox alertContainer = new VBox(alertBox);
  alertContainer.setStyle("-fx-padding: 10px; -fx-alignment: center;");
  alertBox.setStyle("-fx-alignment: center; -fx-padding: 20px; -fx-background-color: white; -fx-border-radius: 10px; -fx-background-radius: 10px; -fx-border-color: #b1b1b1; -fx-border-width: 1px;");
  alertBox.setMinWidth(300);
  alertBox.setMaxWidth(300);
  alertBox.setMinHeight(300);

  // Main layout
  HBox root = new HBox(10, buttonBox, alertContainer);

  // Create scene
  Scene scene = new Scene(root, 750, 400);

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