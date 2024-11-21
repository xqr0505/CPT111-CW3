package UI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainPage extends Application {

@Override
public void start(Stage primaryStage) {
  // 创建按钮
  Button loginButton = new Button("User Login");
  Button registerButton = new Button("User Register");

  double buttonWidth = 150; // 指定按钮宽度
  loginButton.setPrefWidth(buttonWidth);
  registerButton.setPrefWidth(buttonWidth);

  // 设置按钮点击事件
  loginButton.setOnAction(e -> {
    LoginPage loginPage = new LoginPage();
    loginPage.start(primaryStage);
  });

  registerButton.setOnAction(e -> {
    RegisterPage registerPage = new RegisterPage();
    registerPage.start(primaryStage);
  });

  // 布局设置
  VBox root = new VBox(20, loginButton, registerButton);
  root.setStyle("-fx-alignment: center; -fx-padding: 50px;");

  // 创建场景
  Scene scene = new Scene(root, 400, 300);

  // 设置主舞台
  primaryStage.setTitle("Quiz System");
  primaryStage.setScene(scene);
  primaryStage.show();
}

public static void main(String[] args) {
  launch(args);
}
}