package UI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import UserManagement.Users;

/**
 * Menu class representing the main user interface after login.
 */
public class Menu extends Application {

private Users currentUser;

/**
 * Constructor for Menu.
 *
 * @param user the current user
 */
public Menu(Users user) {
  this.currentUser = user;
}

@Override
public void start(Stage primaryStage) {
  // Welcome message
  Label welcomeLabel = new Label("Login success! Hi, " + currentUser.GetName());

  // Create buttons
  Button attemptQuizButton = new Button("Attempt Quiz");
  Button historyScoreButton = new Button("User Dashboard");
  Button leaderboardButton = new Button("Leaderboard");
  Button returnButton = new Button("Return");

  double buttonWidth = 200; // Specify button width
  attemptQuizButton.setPrefWidth(buttonWidth);
  historyScoreButton.setPrefWidth(buttonWidth);
  leaderboardButton.setPrefWidth(buttonWidth);
  returnButton.setPrefWidth(buttonWidth);

  // Set button click events
  attemptQuizButton.setOnAction(e -> {
    // Navigate to SubjectChoosePage
    SubjectChoosePage subjectChoosePage = new SubjectChoosePage(currentUser);
    subjectChoosePage.start(primaryStage);
  });

  historyScoreButton.setOnAction(e -> {
    // Navigate to HistoryScorePage
    UserDashboardPage userDashboardPage = new UserDashboardPage(currentUser);
    userDashboardPage.start(primaryStage);
  });

  leaderboardButton.setOnAction(e -> {
    // Navigate to LeaderboardPage
    LeaderboardPage leaderboardPage = new LeaderboardPage(currentUser);
    leaderboardPage.start(primaryStage);
  });

  returnButton.setOnAction(e -> {
    MainPage mainPage = new MainPage();
    mainPage.start(primaryStage);
  });

  // Layout settings
  VBox root = new VBox(20, welcomeLabel, attemptQuizButton, historyScoreButton, leaderboardButton, returnButton);
  root.setStyle("-fx-alignment: center; -fx-padding: 50px;");

  // Create scene
  Scene scene = new Scene(root, 400, 400);

  // Set stage
  primaryStage.setTitle("Menu");
  primaryStage.setScene(scene);
  primaryStage.show();
}
}