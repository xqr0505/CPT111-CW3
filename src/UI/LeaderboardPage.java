package UI;
import core.Logical;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import UserManagement.Users;
import UserManagement.UserManager;
import UserManagement.UserManager.ScoreEntry;

import java.util.*;
import java.util.stream.Collectors;

public class LeaderboardPage extends Application {

private Users currentUser;
private UserManager userManager = Logical.getInstance().getUserManager();

public LeaderboardPage(Users user) {
  this.currentUser = user;
}

@Override
public void start(Stage primaryStage) {
  // 提示信息
  Label promptLabel = new Label("Please choose the subject to view the leaderboard:");

  // 创建科目按钮
  Button csButton = new Button("Computer Science");
  Button eeButton = new Button("Electronic Engineering");
  Button englishButton = new Button("English");
  Button mathButton = new Button("Math");
  Button returnButton = new Button("Return");

  double buttonWidth = 250; // 指定按钮宽度
  csButton.setPrefWidth(buttonWidth);
  eeButton.setPrefWidth(buttonWidth);
  englishButton.setPrefWidth(buttonWidth);
  mathButton.setPrefWidth(buttonWidth);
  returnButton.setPrefWidth(buttonWidth);

  // 设置按钮点击事件
  csButton.setOnAction(e -> showLeaderboard(primaryStage, "Computer Science"));
  eeButton.setOnAction(e -> showLeaderboard(primaryStage, "Electronic Engineering"));
  englishButton.setOnAction(e -> showLeaderboard(primaryStage, "English"));
  mathButton.setOnAction(e -> showLeaderboard(primaryStage, "Math"));
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
  // 获取所有用户的成绩
  Map<String, List<ScoreEntry>> records = userManager.GetRecords();

  List<ScoreEntry> scoreEntries = records.get(subject);

  if (scoreEntries == null || scoreEntries.isEmpty()) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION, "No scores for this subject.", ButtonType.OK);
    alert.showAndWait();
    return;
  }

  // 计算每个用户的最高成绩
  Map<String, Integer> userBestScores = new HashMap<>();
  for (ScoreEntry entry : scoreEntries) {
    if (entry.score != null) {
      userBestScores.merge(entry.id, entry.score, Math::max);
    }
  }

  // 按成绩排序，取前三名
  List<Map.Entry<String, Integer>> topScores = userBestScores.entrySet()
                                                             .stream()
                                                             .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                                                             .limit(3)
                                                             .collect(Collectors.toList());

  // 构建排行榜显示字符串
  StringBuilder leaderboardText = new StringBuilder("Top 3 users for " + subject + ":\n");
  int rank = 1;
  for (Map.Entry<String, Integer> entry : topScores) {
    // 获取用户名
    Users user = userManager.getUserById(entry.getKey());
    String userName = user != null ? user.GetName() : entry.getKey();
    leaderboardText.append(rank).append(". ").append(userName).append(": ").append(entry.getValue()).append("\n");
    rank++;
  }

  // 显示排行榜
  Alert alert = new Alert(Alert.AlertType.INFORMATION, leaderboardText.toString(), ButtonType.OK);
  alert.showAndWait();
}
}

