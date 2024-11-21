package UI;
import core.Logical;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import UserManagement.Users;
import UserManagement.UserManager;

public class LoginPage extends Application {

private UserManager userManager = Logical.getInstance()
                                         .getUserManager();

@Override
public void start(Stage primaryStage) {
  // 创建输入字段
  TextField userIdField = new TextField();
  userIdField.setPromptText("User ID");

  PasswordField passwordField = new PasswordField();
  passwordField.setPromptText("Password");

  // 创建按钮
  Button loginButton  = new Button("Login");
  Button returnButton = new Button("Return");

  // 提示标签
  Label messageLabel = new Label();

  // 设置按钮点击事件
  loginButton.setOnAction(e -> {
    String userId   = userIdField.getText();
    String password = passwordField.getText();

    if (userId.isEmpty() || password.isEmpty()) {
      messageLabel.setText("Please enter User ID and Password.");
      return;
    }

    Users user = userManager.CheckLogin(userId, password);
    if (user != null) {
      // 登录成功，跳转到Dashboard
      Dashboard dashboard = new Dashboard(user);
      dashboard.start(primaryStage);
    } else {
      messageLabel.setText("Invalid User ID or Password.");
    }
  });

  returnButton.setOnAction(e -> {
    MainPage mainPage = new MainPage();
    mainPage.start(primaryStage);
  });

  // 布局设置
  VBox root = new VBox(10, userIdField, passwordField, loginButton, returnButton, messageLabel);
  root.setStyle("-fx-alignment: center; -fx-padding: 50px;");

  // 创建场景
  Scene scene = new Scene(root, 400, 300);

  // 设置舞台
  primaryStage.setTitle("User Login");
  primaryStage.setScene(scene);
  primaryStage.show();
}
}