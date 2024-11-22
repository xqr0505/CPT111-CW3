package UI;

import core.Logical;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import UserManagement.Users;
import xjtlu.cpt111.assignment.quiz.model.Question;
import xjtlu.cpt111.assignment.quiz.model.Option;
import QuestionManagement.QuestionManager;
import QuestionManagement.Exceptions.NoTopicFoundException;

import java.io.IOException;
import java.util.*;

/**
 * QuizPage class representing the user interface for taking a quiz.
 */
public class QuizPage extends Application {

private Users currentUser;
private String subject;
private List<Question> questions;
private int currentQuestionIndex = 0;
private int totalScore = 0;

private Label questionLabel;
private ToggleGroup optionsGroup;
private List<RadioButton> optionButtons;
private Button nextButton;
private Button returnButton;

private Stage primaryStage;

private QuestionManager questionManager = Logical.getInstance().getQuestionManager();

// Map difficulty to score
private final Map<String, Integer> difficultyScoreMap = new HashMap<>();

/**
 * Constructor for QuizPage.
 *
 * @param user the current user
 * @param subject the subject of the quiz
 */
public QuizPage(Users user, String subject) {
  this.currentUser = user;
  this.subject = subject;

  // Initialize difficulty to score mapping
  difficultyScoreMap.put("EASY", 5);
  difficultyScoreMap.put("MEDIUM", 10);
  difficultyScoreMap.put("HARD", 15);
  difficultyScoreMap.put("VERY_HARD", 20);
}

@Override
public void start(Stage primaryStage) {
  this.primaryStage = primaryStage;

  // Initialize UI components
  questionLabel = new Label();
  questionLabel.setWrapText(true);
  questionLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

  optionsGroup = new ToggleGroup();
  optionButtons = new ArrayList<>();
  for (int i = 0; i < 5; i++) {
    RadioButton optionButton = new RadioButton();
    optionButton.setToggleGroup(optionsGroup);
    optionButton.setPrefWidth(500);
    optionButtons.add(optionButton);
  }

  nextButton = new Button("Next");
  nextButton.setPrefWidth(150);
  returnButton = new Button("Return");
  returnButton.setPrefWidth(150);

  // Set button click events
  nextButton.setOnAction(e -> handleNextAction());
  returnButton.setOnAction(e -> {
    // Return to subject selection page
    SubjectChoosePage subjectChoosePage = new SubjectChoosePage(currentUser);
    subjectChoosePage.start(primaryStage);
  });

  // Layout settings
  VBox root = new VBox(15, questionLabel);
  root.setStyle("-fx-padding: 20px;");
  root.setAlignment(Pos.CENTER);

  // Add option buttons
  for (RadioButton optionButton : optionButtons) {
    root.getChildren().add(optionButton);
  }

  // Add control buttons
  HBox buttonBox = new HBox(20, nextButton, returnButton);
  buttonBox.setAlignment(Pos.CENTER);
  root.getChildren().add(buttonBox);

  // Create scene
  Scene scene = new Scene(root, 600, 400);

  // Set stage
  primaryStage.setTitle("Quiz - " + subject);
  primaryStage.setScene(scene);
  primaryStage.show();

  // Ensure questions are loaded
  loadQuestions();

  // Show the first question
  showQuestion();
}

/**
 * Load questions for the quiz.
 */
private void loadQuestions() {
  // Load questions according to the new rule: 2 questions per difficulty, 8 questions in total
  try {
    // Get all questions
    Question[] allQuestions = questionManager.GetQuestions(subject);
    List<Question> easy = new ArrayList<>();
    List<Question> medium = new ArrayList<>();
    List<Question> hard = new ArrayList<>();
    List<Question> veryHard = new ArrayList<>();

    for (Question q : allQuestions) {
      String difficulty = q.getDifficulty().toString(); // Assume getDifficulty returns a string
      switch (difficulty) {
        case "EASY":
          easy.add(q);
          break;
        case "MEDIUM":
          medium.add(q);
          break;
        case "HARD":
          hard.add(q);
          break;
        case "VERY_HARD":
          veryHard.add(q);
          break;
        default:
          // Unrecognized difficulty, can log or ignore
          break;
      }
    }

    // Randomly select 2 questions
    List<Question> selectedQuestions = new ArrayList<>();
    selectedQuestions.addAll(selectRandomQuestions(easy, 2));
    selectedQuestions.addAll(selectRandomQuestions(medium, 2));
    selectedQuestions.addAll(selectRandomQuestions(hard, 2));
    selectedQuestions.addAll(selectRandomQuestions(veryHard, 2));

    // Shuffle question order
    Collections.shuffle(selectedQuestions);

    questions = selectedQuestions;

    // Shuffle options for each question
    for (Question q : questions) {
      List<Option> optionList = new ArrayList<>(Arrays.asList(q.getOptions()));
      Collections.shuffle(optionList);
      // Assume Option class can be rearranged via constructor or other methods
      q.setOptions(List.of(optionList.toArray(new Option[0])));
    }

  } catch (NoTopicFoundException e) {
    // If no questions found, show error message and return
    Alert alert = new Alert(Alert.AlertType.ERROR, "No questions found for this subject.", ButtonType.OK);
    alert.showAndWait();
    // Return to subject selection page
    SubjectChoosePage subjectChoosePage = new SubjectChoosePage(currentUser);
    subjectChoosePage.start(primaryStage);
  }
}

/**
 * Select random questions from a list.
 *
 * @param questions the list of questions
 * @param count the number of questions to select
 * @return the selected questions
 */
private List<Question> selectRandomQuestions(List<Question> questions, int count) {
  List<Question> selected = new ArrayList<>();
  Collections.shuffle(questions);
  for (int i = 0; i < count && i < questions.size(); i++) {
    selected.add(questions.get(i));
  }
  return selected;
}

/**
 * Show the current question.
 */
private void showQuestion() {
  if (currentQuestionIndex >= questions.size()) {
    // All questions completed, show score
    showScore();
    return;
  }

  Question currentQuestion = questions.get(currentQuestionIndex);

  // Display question text
  questionLabel.setText("Question " + (currentQuestionIndex + 1) + ": " + currentQuestion.getQuestionStatement());

  // Get options
  Option[] options = currentQuestion.getOptions();
  List<Option> optionList = Arrays.asList(options);
  // Shuffle options already done in loadQuestions()

  // Display options
  for (int i = 0; i < optionButtons.size(); i++) {
    RadioButton optionButton = optionButtons.get(i);
    if (i < optionList.size()) {
      optionButton.setText(optionList.get(i).getAnswer());
      optionButton.setUserData(optionList.get(i));
      optionButton.setVisible(true);
    } else {
      optionButton.setVisible(false);
    }
  }

  // Change "Next" button text to "Submit" for the last question
  if (currentQuestionIndex == questions.size() - 1) {
    nextButton.setText("Submit");
  } else {
    nextButton.setText("Next");
  }
}

/**
 * Handle the action for the next button.
 */
private void handleNextAction() {
  // Check user selection
  Toggle selectedToggle = optionsGroup.getSelectedToggle();
  if (selectedToggle != null) {
    Option selectedOption = (Option) selectedToggle.getUserData();
    if (selectedOption.isCorrectAnswer()) {
      // Assign score based on question difficulty
      String difficulty = questions.get(currentQuestionIndex).getDifficulty().toString();
      int score = difficultyScoreMap.getOrDefault(difficulty, 0);
      totalScore += score;
    }
  }
  // No selection is considered incorrect, no score added

  // Clear selection
  optionsGroup.selectToggle(null);

  // Move to the next question
  currentQuestionIndex++;
  showQuestion();
}

/**
 * Show the total score and save the result.
 */
private void showScore() {
  // Display total score and save the result
  Alert alert = new Alert(Alert.AlertType.INFORMATION, "Your total score is: " + totalScore, ButtonType.OK);
  alert.setHeaderText("Quiz finished!");
  alert.setTitle("Quiz Score");
  alert.showAndWait();

  // Record the score in the user's ScoreRecords
  currentUser.NewRecord(subject, totalScore);

  try {
    // Save score information
    Logical.getInstance().getUserManager().SaveUserInfo("resources/u.csv", "resources/s.csv");
  } catch (IOException ex) {
    // Handle save error
    Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Error saving score information.", ButtonType.OK);
    errorAlert.showAndWait();
    ex.printStackTrace();
  }

  // Return to Menu
  Menu menu = new Menu(currentUser);
  try {
    menu.start(primaryStage);
  } catch (Exception e) {
    e.printStackTrace();
  }
}
}