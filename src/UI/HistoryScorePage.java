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
import java.util.Arrays;
import java.util.List;

public class HistoryScorePage extends Application {

private Users currentUser;

public HistoryScorePage(Users user) {
  this.currentUser = user;
}

@Override
public void start(Stage primaryStage) {
  // 提示信息
  Label promptLabel = new Label("Please choose the subject to view history scores:");

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
  csButton.setOnAction(e -> showHistoryScores(primaryStage, "Computer Science"));
  eeButton.setOnAction(e -> showHistoryScores(primaryStage, "Electronic Engineering"));
  englishButton.setOnAction(e -> showHistoryScores(primaryStage, "English"));
  mathButton.setOnAction(e -> showHistoryScores(primaryStage, "Math"));
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
  primaryStage.setTitle("History Scores");
  primaryStage.setScene(scene);
  primaryStage.show();
}

private void showHistoryScores(Stage primaryStage, String subject) {
  // 获取用户的最近三次成绩
  List<Integer> scoresList = currentUser.GetTopicSpecifiedRecentRecords(subject);

  if (scoresList == null || scoresList.isEmpty()) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION, "No history scores for this subject.", ButtonType.OK);
    alert.showAndWait();
    return;
  }

  // 获取最高分
  Integer highestScore = currentUser.GetTopicSpecifiedHighestRecord(subject);

  // 构建成绩显示字符串
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

  // 显示成绩
  Alert alert = new Alert(Alert.AlertType.INFORMATION, scoreText.toString(), ButtonType.OK);
  alert.showAndWait();
}

}
