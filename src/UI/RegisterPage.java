package UI;

import core.Logical;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import UserManagement.Users;
import UserManagement.UserManager;
import UserManagement.Exceptions.DuplicateUserException;

import java.io.IOException;

public class RegisterPage extends Application {

private UserManager userManager = Logical.getInstance().getUserManager();

@Override
public void start(Stage primaryStage) {
  // 创建输入字段
  TextField userIdField = new TextField();
  userIdField.setPromptText("User ID");

  TextField userNameField = new TextField();
  userNameField.setPromptText("User Name");

  PasswordField passwordField = new PasswordField();
  passwordField.setPromptText("Password");

  PasswordField confirmPasswordField = new PasswordField();
  confirmPasswordField.setPromptText("Confirm Password");

  // 创建按钮
  Button signupButton = new Button("Sign Up");
  Button returnButton = new Button("Return");

  // 提示标签
  Label messageLabel = new Label();

  // 设置按钮点击事件
  signupButton.setOnAction(e -> {
    String userId = userIdField.getText();
    String userName = userNameField.getText();
    String password = passwordField.getText();
    String confirmPassword = confirmPasswordField.getText();

    if (userId.isEmpty() || userName.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
      messageLabel.setText("Please fill in all fields.");
      return;
    }

    if (!password.equals(confirmPassword)) {
      messageLabel.setText("Passwords do not match.");
      return;
    }

    try {
      // 创建新用户并注册
      Users newUser = new Users(userId, userName, password);
      userManager.RegisterUser(newUser);

      // 保存用户信息到文件
      userManager.SaveUserInfo("resources/u.csv", "resources/s.csv");

      // 自动登录并跳转到 Dashboard
      messageLabel.setText("Registration successful! Logging you in...");
      Dashboard dashboard = new Dashboard(newUser); // 创建 Dashboard 页面
      dashboard.start(primaryStage); // 跳转到 Dashboard

    } catch (DuplicateUserException ex) {
      messageLabel.setText("User ID already exists.");
    } catch (UserManagement.Exceptions.UserInformationInvalidException ex) {
      messageLabel.setText("Invalid user information.");
    } catch (IOException ex) {
      messageLabel.setText("Error saving user information.");
    }
  });

  returnButton.setOnAction(e -> {
    MainPage mainPage = new MainPage();
    mainPage.start(primaryStage);
  });

  // 布局设置
  VBox root = new VBox(10, userIdField, userNameField, passwordField, confirmPasswordField, signupButton, returnButton, messageLabel);
  root.setStyle("-fx-alignment: center; -fx-padding: 50px;");

  // 创建场景
  Scene scene = new Scene(root, 400, 400);

  // 设置舞台
  primaryStage.setTitle("User Registration");
  primaryStage.setScene(scene);
  primaryStage.show();
}
}
