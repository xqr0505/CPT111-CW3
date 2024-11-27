package UI;

import UserManagement.Exceptions;
import core.Logical;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import UserManagement.Users;
import UserManagement.UserManager;

/**
 * LoginPage class representing the user interface for user login.
 */
public class LoginPage{

private UserManager userManager = Logical.getInstance().getUserManager();

/**
 * Starts the LoginPage UI.
 *
 * @param primaryStage the primary stage for this application
 */
public void start(Stage primaryStage) {
  // Create input fields
  Label userIdLabel       =   new Label("User ID:");
  TextField userIdField   =   new TextField();
  userIdField.setPromptText("User ID");

  Label passwordLabel           =   new Label("Password:");
  PasswordField passwordField   =   new PasswordField();
  passwordField.setPromptText("Password");

  // Specify button width
  double buttonWidth = 100;
  // Create buttons
  Button loginButton    =   new Button("Log In");
  loginButton.setPrefWidth(buttonWidth);
  Button returnButton = UIUtils.createReturnButton("Return",buttonWidth, ev -> {
    MainPage mainPage = new MainPage();
    mainPage.start(primaryStage);
  });

  // Message label
  Label messageLabel = new Label();

  // Set button click events
  loginButton.setOnAction(e -> {
    String userId   =   userIdField.getText();
    String password =   passwordField.getText();

    if (userId.isEmpty() || password.isEmpty()) {
      messageLabel.setText("Please enter User ID and Password.");
      return;
    }

    try {
      Users user = userManager.CheckLogin(userId, password);
      // Login successful, navigate to menu
      Menu menu = new Menu(user);
      menu.start(primaryStage);
    } catch (Exceptions.UserNotFoundException ex) {
      messageLabel.setText("User not found.");
    } catch (Exceptions.IncorrectPasswordException ex) {
      messageLabel.setText("Incorrect password.");
    }
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