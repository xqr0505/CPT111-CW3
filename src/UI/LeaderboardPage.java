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

import java.util.List;

public class LeaderboardPage extends Application {

private final UserManager userManager = Logical.getInstance().getUserManager();
private Users currentUser;

public LeaderboardPage(Users user) {
  this.currentUser = user;
}

@Override
public void start(Stage primaryStage) {
  // 创建提示信息
  Label promptLabel = new Label("Please choose the subject to view the leaderboard:");

  // 创建科目按钮
  Button csButton = new Button("Computer Science");
  Button eeButton = new Button("Electronic Engineering");
  Button englishButton = new Button("English");
  Button mathButton = new Button("Mathematics");
  Button returnButton = new Button("Return");

  double buttonWidth = 250; // 按钮宽度
  csButton.setPrefWidth(buttonWidth);
  eeButton.setPrefWidth(buttonWidth);
  englishButton.setPrefWidth(buttonWidth);
  mathButton.setPrefWidth(buttonWidth);
  returnButton.setPrefWidth(buttonWidth);

  // 设置按钮点击事件
  csButton.setOnAction(e -> showLeaderboard(primaryStage, "Computer Science"));
  eeButton.setOnAction(e -> showLeaderboard(primaryStage, "Electronic Engineering"));
  englishButton.setOnAction(e -> showLeaderboard(primaryStage, "English"));
  mathButton.setOnAction(e -> showLeaderboard(primaryStage, "Mathematics"));
  returnButton.setOnAction(e -> {
    Dashboard dashboard = new Dashboard(currentUser);
    dashboard.start(primaryStage);
  });

  // 布局设置
  VBox root = new VBox(10, promptLabel, csButton, eeButton, englishButton, mathButton, returnButton);
  root.setStyle("-fx-alignment: center; -fx-padding: 50px;");

  // 创建场景
  Scene scene = new Scene(root, 400, 400);

  // 设置舞台
  primaryStage.setTitle("Leaderboard");
  primaryStage.setScene(scene);
  primaryStage.show();
}

private void showLeaderboard(Stage primaryStage, String subject) {
  // 获取所有用户
  List<Users> allUsers = List.copyOf(userManager.GetAllUsers());

  String topUserId = null;
  String topUserName = null;
  int highestScore = -1;

  // 遍历所有用户，查找最高分
  for (Users user : allUsers) {
    Integer userHighestScore = user.GetTopicSpecifiedHighestRecord(subject);
    System.out.println("User: " + user.GetId() + ", Subject: " + subject + ", Highest Score: " + userHighestScore);
    if (userHighestScore != null && userHighestScore > highestScore) {
      highestScore = userHighestScore;
      topUserId = user.GetId();
      topUserName = user.GetName();
    }
  }

  // 构建显示信息
  String result;
  if (topUserId != null) {
    result = String.format("Top User for %s:\n%s (ID: %s) with a score of %d",
                           subject, topUserName, topUserId, highestScore);
  } else {
    result = "No scores available for this subject.";
  }

  // 显示结果
  Alert alert = new Alert(Alert.AlertType.INFORMATION, result);
  alert.setHeaderText("Leaderboard");
  alert.showAndWait();
}
}
