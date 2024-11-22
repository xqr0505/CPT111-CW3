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

/**
 * RegisterPage class representing the user interface for user registration.
 */
public class RegisterPage extends Application {

private UserManager userManager = Logical.getInstance()
                                         .getUserManager();

@Override
public void start(Stage primaryStage) {
  // Create input fields
  TextField userIdField = new TextField();
  userIdField.setPromptText("User ID");

  TextField userNameField = new TextField();
  userNameField.setPromptText("User Name");

  PasswordField passwordField = new PasswordField();
  passwordField.setPromptText("Password");

  PasswordField confirmPasswordField = new PasswordField();
  confirmPasswordField.setPromptText("Confirm Password");

  // Create buttons
  Button signupButton = new Button("Sign Up");
  Button returnButton = new Button("Return");

  // Message label
  Label messageLabel = new Label();

  // Set button click events
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
      // Create new user and register
      Users newUser = new Users(userId, userName, password);
      userManager.RegisterUser(newUser);

      // Save user information to file
      userManager.SaveUserInfo("resources/u.csv", "resources/s.csv");

      // Auto login and navigate to Dashboard
      messageLabel.setText("Registration successful! Logging you in...");
      Dashboard dashboard = new Dashboard(newUser); // Create Dashboard page
      dashboard.start(primaryStage); // Navigate to Dashboard

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

  // Layout settings
  VBox root = new VBox(10, userIdField, userNameField, passwordField, confirmPasswordField, signupButton, returnButton, messageLabel);
  root.setStyle("-fx-alignment: center; -fx-padding: 50px;");

  // Create scene
  Scene scene = new Scene(root, 400, 400);

  // Set stage
  primaryStage.setTitle("User Registration");
  primaryStage.setScene(scene);
  primaryStage.show();
}
}