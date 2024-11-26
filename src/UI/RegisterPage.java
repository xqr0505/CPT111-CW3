package UI;

import core.Logical;
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
public class RegisterPage{

private UserManager userManager = Logical.getInstance()
                                         .getUserManager();
/**
 * Starts the RegisterPage UI.
 *
 * @param primaryStage the primary stage for this application
 */
public void start(Stage primaryStage) {
  // Create input fields
  Label userIdLabel       =   new Label("New User ID:");
  TextField userIdField   =   new TextField();
  userIdField.setPromptText("User ID");

  Label usernameLabel     =   new Label("New User Name:");
  TextField userNameField =   new TextField();
  userNameField.setPromptText("User Name");

  Label passwordLabel           =   new Label("New Password:");
  PasswordField passwordField   =   new PasswordField();
  passwordField.setPromptText("Password");

  Label confirmLabel                  =   new Label("Confirm your Password:");
  PasswordField confirmPasswordField  =   new PasswordField();
  confirmPasswordField.setPromptText("Confirm Password");

  // Create buttons
  Button signupButton   =   new Button("Sign Up");
  Button returnButton   =   new Button("Return");

  // Specify button width
  double buttonWidth = 100;
  signupButton.setPrefWidth(buttonWidth);
  returnButton.setPrefWidth(buttonWidth);

  // Set button styles
  returnButton.setStyle("-fx-background-color: #a3c5f4;");
  returnButton.setOnMouseEntered(e -> returnButton.setStyle("-fx-background-color: #d0e1f9"));
  returnButton.setOnMouseExited(e -> returnButton.setStyle("-fx-background-color: #a3c5f4;"));

  // Message label
  Label messageLabel = new Label();

  // Set button click events
  signupButton.setOnAction(e -> {
    String userId           =   userIdField.getText();
    String userName         =   userNameField.getText();
    String password         =   passwordField.getText();
    String confirmPassword  =   confirmPasswordField.getText();

    if (!password.equals(confirmPassword)) {
      messageLabel.setText("Passwords do not match.");
      return;
    }

    // Register new user
    try {
      // Create new user and register
      Users newUser = new Users(userId, userName, password);
      userManager.RegisterUser(newUser);

      // Save user information to file
      userManager.SaveUserInfo("resources/user.csv", "resources/score.csv");

      // Auto login and navigate to Menu
      Menu menu = new Menu(newUser); // Create Menu page
      menu.start(primaryStage); // Navigate to Menu

    } catch (DuplicateUserException ex) {
      messageLabel.setText("User ID already exists.");
    } catch (UserManagement.Exceptions.UserInformationInvalidException ex) {
        messageLabel.setText("User ID, name, and password cannot be empty");
    } catch (IOException ex) {
      messageLabel.setText("Error saving user information.");
    }
  });

  returnButton.setOnAction(e -> {
    MainPage mainPage = new MainPage();
    mainPage.start(primaryStage);
  });

  // Layout settings
  VBox root = new VBox(10, userIdLabel,userIdField, usernameLabel,
                       userNameField, passwordLabel,passwordField,
                       confirmLabel,confirmPasswordField, signupButton,
                       returnButton, messageLabel);
  root.setStyle("-fx-alignment: center; -fx-padding: 50px;");

  // Create scene
  Scene scene = new Scene(root, 400, 400);

  // Set stage
  primaryStage.setTitle("User Registration");
  primaryStage.setScene(scene);
  primaryStage.show();
}
}