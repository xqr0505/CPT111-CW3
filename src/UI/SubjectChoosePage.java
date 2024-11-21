package UI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import UserManagement.Users;

public class SubjectChoosePage extends Application {

private Users currentUser;

public SubjectChoosePage(Users user) {
  this.currentUser = user;
}

@Override
public void start(Stage primaryStage) {
  // 提示信息
  Label promptLabel = new Label("Please choose your subject:");

  // 创建科目按钮
  Button csButton = new Button("Computer Science");
  Button eeButton = new Button("Electronic Engineering");
  Button englishButton = new Button("English");
  Button mathButton = new Button("Mathematics");
  Button returnButton = new Button("Return");

  double buttonWidth = 200; // 指定按钮宽度
  csButton.setPrefWidth(buttonWidth);
  eeButton.setPrefWidth(buttonWidth);
  englishButton.setPrefWidth(buttonWidth);
  mathButton.setPrefWidth(buttonWidth);
  returnButton.setPrefWidth(buttonWidth);

  // 设置按钮点击事件
  csButton.setOnAction(e -> {
    startQuiz(primaryStage, "Computer Science");
  });

  eeButton.setOnAction(e -> {
    startQuiz(primaryStage, "Electronic Engineering");
  });

  englishButton.setOnAction(e -> {
    startQuiz(primaryStage, "English");
  });

  mathButton.setOnAction(e -> {
    startQuiz(primaryStage, "Mathematics");
  });

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
  primaryStage.setTitle("Choose Subject");
  primaryStage.setScene(scene);
  primaryStage.show();
}

private void startQuiz(Stage primaryStage, String subject) {
  // 启动QuizPage，开始测验
  QuizPage quizPage = new QuizPage(currentUser, subject);
  quizPage.start(primaryStage);
}
}
