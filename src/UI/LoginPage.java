package UI;

import core.Logical;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import UserManagement.Users;
import UserManagement.UserManager;

/**
 * LoginPage class representing the user interface for user login.
 */
public class LoginPage extends Application {

private UserManager userManager = Logical.getInstance().getUserManager();

@Override
public void start(Stage primaryStage) {
  // Create input fields
  Label userIdLabel       =   new Label("User ID:");
  TextField userIdField   =   new TextField();
  userIdField.setPromptText("User ID");

  Label passwordLabel           =   new Label("Password:");
  PasswordField passwordField   =   new PasswordField();
  passwordField.setPromptText("Password");

  // Create buttons
  Button loginButton    =   new Button("Log In");
  Button returnButton   =   new Button("Return");

  // Specify button width
  double buttonWidth = 100;
  loginButton.setPrefWidth(buttonWidth);
  returnButton.setPrefWidth(buttonWidth);

  // Set button styles
  returnButton.setStyle("-fx-background-color: #a3c5f4;");
  returnButton.setOnMouseEntered(e -> returnButton.setStyle("-fx-background-color: #d0e1f9"));
  returnButton.setOnMouseExited(e -> returnButton.setStyle("-fx-background-color: #a3c5f4;"));

  // Message label
  Label messageLabel = new Label();

  // Set button click events
  loginButton.setOnAction(e -> {
    String userId = userIdField.getText();
    String password = passwordField.getText();

    if (userId.isEmpty() || password.isEmpty()) {
      messageLabel.setText("Please enter User ID and Password.");
      return;
    }

    Users user = userManager.CheckLogin(userId, password);
    if (user != null) {
      // Login successful, navigate to menu
      Menu menu = new Menu(user);
      menu.start(primaryStage);
    } else {
      messageLabel.setText("Invalid User ID or Password.");
    }
  });

  returnButton.setOnAction(e -> {
    MainPage mainPage = new MainPage();
    mainPage.start(primaryStage);
  });

  // Layout settings
  VBox root = new VBox(10, userIdLabel,userIdField, passwordLabel,
                       passwordField, loginButton, returnButton, messageLabel);
  root.setStyle("-fx-alignment: center; -fx-padding: 50px;");

  // Create scene
  Scene scene = new Scene(root, 400, 300);

  // Set stage
  primaryStage.setTitle("User Login");
  primaryStage.setScene(scene);
  primaryStage.show();
}
}