package UI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import UserManagement.Users;

public class Dashboard extends Application {

private Users currentUser;

public Dashboard(Users user) {
  this.currentUser = user;
}

@Override
public void start(Stage primaryStage) {
  // 欢迎信息
  Label welcomeLabel = new Label("Login success! Hi, " + currentUser.GetName());

  // 创建按钮
  Button attemptQuizButton = new Button("Attempt Quiz");
  Button historyScoreButton = new Button("History Score");
  Button leaderboardButton = new Button("Leaderboard");
  Button returnButton = new Button("Return");

  double buttonWidth = 200; // 指定按钮宽度
  attemptQuizButton.setPrefWidth(buttonWidth);
  historyScoreButton.setPrefWidth(buttonWidth);
  leaderboardButton.setPrefWidth(buttonWidth);
  returnButton.setPrefWidth(buttonWidth);

  // 设置按钮点击事件
  attemptQuizButton.setOnAction(e -> {
    // 跳转到SubjectChoosePage（需要实现）
    SubjectChoosePage subjectChoosePage = new SubjectChoosePage(currentUser);
    subjectChoosePage.start(primaryStage);
  });

  historyScoreButton.setOnAction(e -> {
    // 跳转到HistoryScorePage（需要实现）
    HistoryScorePage historyScorePage = new HistoryScorePage(currentUser);
    historyScorePage.start(primaryStage);
  });

  leaderboardButton.setOnAction(e -> {
    // 跳转到LeaderboardPage（需要实现）
    LeaderboardPage leaderboardPage = new LeaderboardPage(currentUser);
    leaderboardPage.start(primaryStage);
  });

  returnButton.setOnAction(e -> {
    MainPage mainPage = new MainPage();
    mainPage.start(primaryStage);
  });

  // 布局设置
  VBox root = new VBox(20, welcomeLabel, attemptQuizButton, historyScoreButton, leaderboardButton, returnButton);
  root.setStyle("-fx-alignment: center; -fx-padding: 50px;");

  // 创建场景
  Scene scene = new Scene(root, 400, 400);

  // 设置舞台
  primaryStage.setTitle("Dashboard");
  primaryStage.setScene(scene);
  primaryStage.show();
}
}
