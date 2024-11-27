package UI;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import UserManagement.Users;

/**
 * Menu class representing the main user interface after login.
 */
public class Menu{

private Users currentUser;

/**
 * Constructor for Menu.
 *
 * @param user the current user
 */
public Menu(Users user) {
  this.currentUser = user;
}

/**
 * Starts the Menu UI.
 *
 * @param primaryStage the primary stage for this application
 */
public void start(Stage primaryStage) {
  // Welcome message
  Label welcomeLabel  =   new Label("Login success! Hi, ");
  welcomeLabel.setStyle("-fx-font-size: 16px;");
  Text userNameText   =   new Text(currentUser.GetName());
  userNameText.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
  TextFlow textFlow   =   new TextFlow(welcomeLabel, userNameText);
  textFlow.setTextAlignment(TextAlignment.CENTER);

  // Specify button width
  double buttonWidth = 200;

  // Create buttons
  Button attemptQuizButton  =   new Button("Attempt Quiz");
  attemptQuizButton.setPrefWidth(buttonWidth);
  Button historyScoreButton =   new Button("User Dashboard");
  historyScoreButton.setPrefWidth(buttonWidth);
  Button leaderboardButton  =   new Button("Leaderboard");
  leaderboardButton.setPrefWidth(buttonWidth);
    Button returnButton = UIUtils.createReturnButton("Log out", buttonWidth, e -> {
    MainPage mainPage  =  new MainPage();
    mainPage.start(primaryStage);
  });

  // Set button click events
  attemptQuizButton.setOnAction(e -> {
    // Navigate to SubjectChoosePage
    SubjectChoosePage subjectChoosePage   =   new SubjectChoosePage(currentUser);
    subjectChoosePage.start(primaryStage);
  });

  historyScoreButton.setOnAction(e -> {
    // Navigate to HistoryScorePage
    UserDashboardPage userDashboardPage   =   new UserDashboardPage(currentUser);
    userDashboardPage.start(primaryStage);
  });

  leaderboardButton.setOnAction(e -> {
    // Navigate to LeaderboardPage
    LeaderboardPage leaderboardPage   =   new LeaderboardPage(currentUser);
    leaderboardPage.start(primaryStage);
  });

  // Layout settings
  VBox root   =   new VBox(20, textFlow, attemptQuizButton, historyScoreButton, leaderboardButton, returnButton);
  root.setStyle("-fx-alignment: center; -fx-padding: 50px;");

  // Create scene
  Scene scene  =  new Scene(root, 400, 400);

  // Set stage
  primaryStage.setTitle("Menu");
  primaryStage.setScene(scene);
  primaryStage.show();
}
}