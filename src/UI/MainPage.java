package UI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * MainPage class representing the main user interface for the quiz system.
 */
public class MainPage extends Application {

@Override
public void start(Stage primaryStage) {
  Label promptLabel = new Label("Login or Register to start the quiz.");

  // Create buttons
  Button loginButton = new Button("User Login");
  Button registerButton = new Button("User Register");

  double buttonWidth = 150;
  loginButton.setPrefWidth(buttonWidth);
  registerButton.setPrefWidth(buttonWidth);

  // Set button click events
  loginButton.setOnAction(e -> {
    LoginPage loginPage = new LoginPage();
    loginPage.start(primaryStage);
  });

  registerButton.setOnAction(e -> {
    RegisterPage registerPage = new RegisterPage();
    registerPage.start(primaryStage);
  });

  // Layout settings
  VBox root = new VBox(20, promptLabel, loginButton, registerButton);
  root.setStyle("-fx-alignment: center; -fx-padding: 50px;");

  // Create scene
  Scene scene = new Scene(root, 400, 300);

  // Set primary stage
  primaryStage.setTitle("Quiz System");
  primaryStage.setScene(scene);
  primaryStage.show();
}

public static void main(String[] args) {
  launch(args);
}
}