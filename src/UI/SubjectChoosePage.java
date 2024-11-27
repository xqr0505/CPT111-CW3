package UI;

import core.Logical;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import UserManagement.Users;
import QuestionManagement.QuestionManager;
import QuestionManagement.Exceptions.NoTopicFoundException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * SubjectChoosePage class representing the user interface for choosing a subject.
 */
public class SubjectChoosePage{

private final Users           currentUser;
private final QuestionManager questionManager;


/**
 * Constructor for SubjectChoosePage.
 *
 * @param user the current user
 */
public SubjectChoosePage(Users user) {
  this.currentUser = user;
  this.questionManager = Logical.getInstance().getQuestionManager();
}

/**
 * Starts the SubjectChoosePage UI.
 *
 * @param primaryStage the primary stage for this application
 */
public void start(Stage primaryStage) {
  // Prompt message
  Label promptLabel = new Label("Please choose your subject:");

  // Create a VBox to hold subject buttons
  VBox subjectsBox = new VBox(10);
  subjectsBox.setStyle("-fx-alignment: center;");

  // Get topics from QuestionManager
  String[] topics;
  try {
    topics = questionManager.GetTopics();
    if (topics.length == 0) {
      throw new NoTopicFoundException("No topics available in the question bank.");
    }
  } catch (NoTopicFoundException e) {
    Label errorLabel = new Label("No subjects available. ");
    subjectsBox.getChildren().add(errorLabel);
    Button returnButton = new Button("Return");
    returnButton.setPrefWidth(250);
    returnButton.setStyle("-fx-background-color: #a3c5f4;");
    returnButton.setOnMouseEntered(ev -> returnButton.setStyle("-fx-background-color: #d0e1f9"));
    returnButton.setOnMouseExited(ev -> returnButton.setStyle("-fx-background-color: #a3c5f4;"));
    returnButton.setOnAction(ev -> {
      Menu menu = new Menu(currentUser);
      menu.start(primaryStage);
    });
    subjectsBox.getChildren().add(returnButton);
    // Layout settings
    VBox root = new VBox(10, promptLabel, subjectsBox);
    root.setStyle("-fx-alignment: center; -fx-padding: 50px;");
    // Create scene
    Scene scene = new Scene(root, 400, 400);
    // Set stage
    primaryStage.setTitle("Choose Subject");
    primaryStage.setScene(scene);
    primaryStage.show();
    return;
  } catch (Exception e) {
    Logger.getLogger(SubjectChoosePage.class.getName()).log(Level.SEVERE, "Error retrieving topics: ", e);
    Label errorLabel = new Label("An unexpected error occurred. ");
    subjectsBox.getChildren().add(errorLabel);
    Button returnButton = new Button("Return");
    returnButton.setPrefWidth(250);
    returnButton.setStyle("-fx-background-color: #a3c5f4;");
    returnButton.setOnMouseEntered(ev -> returnButton.setStyle("-fx-background-color: #d0e1f9"));
    returnButton.setOnMouseExited(ev -> returnButton.setStyle("-fx-background-color: #a3c5f4;"));
    returnButton.setOnAction(ev -> {
      Menu menu = new Menu(currentUser);
      menu.start(primaryStage);
    });
    subjectsBox.getChildren().add(returnButton);
    // Layout settings
    VBox root = new VBox(10, promptLabel, subjectsBox);
    root.setStyle("-fx-alignment: center; -fx-padding: 50px;");
    // Create scene
    Scene scene = new Scene(root, 400, 400);
    // Set stage
    primaryStage.setTitle("Choose Subject");
    primaryStage.setScene(scene);
    primaryStage.show();
    return;
  }
  // Dynamically create buttons based on topics
  for (String topic : topics) {
    Button topicButton = new Button(topic);
    topicButton.setPrefWidth(200);
    topicButton.setOnAction(e -> startQuiz(primaryStage, topic));
    subjectsBox.getChildren().add(topicButton);
  }

  // Create return button
  Button returnButton = new Button("Return");
  returnButton.setPrefWidth(200);
  returnButton.setStyle("-fx-background-color: #a3c5f4;");
  returnButton.setOnMouseEntered(e -> returnButton.setStyle("-fx-background-color: #d0e1f9"));
  returnButton.setOnMouseExited(e -> returnButton.setStyle("-fx-background-color: #a3c5f4;"));
  returnButton.setOnAction(e -> {
    MainPage mainPage = new MainPage();
    mainPage.start(primaryStage);
  });
  subjectsBox.getChildren().add(returnButton);

  // Layout settings
  VBox root = new VBox(20, promptLabel, subjectsBox);
  root.setStyle("-fx-alignment: center; -fx-padding: 50px;");

  // Create scene
  Scene scene = new Scene(root, 400, 400);

  // Set stage
  primaryStage.setTitle("Choose Subject");
  primaryStage.setScene(scene);
  primaryStage.show();
}

/**
 * Start the quiz for the selected subject.
 *
 * @param primaryStage the primary stage
 * @param subject the subject to start the quiz for
 */
private void startQuiz(Stage primaryStage, String subject) {
  QuizPage quizPage = new QuizPage(currentUser, subject);
  quizPage.start(primaryStage);
}
}
