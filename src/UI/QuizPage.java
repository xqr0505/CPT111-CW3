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

import java.util.*;

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

public QuizPage(Users user, String subject) {
  this.currentUser = user;
  this.subject = subject;

  // 初始化难度与分数映射
  difficultyScoreMap.put("EASY", 5);
  difficultyScoreMap.put("MEDIUM", 10);
  difficultyScoreMap.put("HARD", 15);
  difficultyScoreMap.put("VERY_HARD", 20);
}

@Override
public void start(Stage primaryStage) {
  this.primaryStage = primaryStage;

  // 初始化界面组件
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

  // 设置按钮点击事件
  nextButton.setOnAction(e -> handleNextAction());
  returnButton.setOnAction(e -> {
    // 返回科目选择界面
    SubjectChoosePage subjectChoosePage = new SubjectChoosePage(currentUser);
    subjectChoosePage.start(primaryStage);
  });

  // 布局设置
  VBox root = new VBox(15, questionLabel);
  root.setStyle("-fx-padding: 20px;");
  root.setAlignment(Pos.CENTER);

  // 添加选项按钮
  for (RadioButton optionButton : optionButtons) {
    root.getChildren().add(optionButton);
  }

  // 添加控制按钮
  HBox buttonBox = new HBox(20, nextButton, returnButton);
  buttonBox.setAlignment(Pos.CENTER);
  root.getChildren().add(buttonBox);

  // 创建场景
  Scene scene = new Scene(root, 600, 400);

  // 设置舞台
  primaryStage.setTitle("Quiz - " + subject);
  primaryStage.setScene(scene);
  primaryStage.show();

  // 确保题目已经加载
  loadQuestions();

  // 显示第一题
  showQuestion();
}

private void loadQuestions() {
  // 按新规则加载题目：每种难度2道，共8道
  try {
    // 获取所有题目
    Question[] allQuestions = questionManager.GetQuestions(subject);
    List<Question> easy = new ArrayList<>();
    List<Question> medium = new ArrayList<>();
    List<Question> hard = new ArrayList<>();
    List<Question> veryHard = new ArrayList<>();

    for (Question q : allQuestions) {
      String difficulty = q.getDifficulty().toString(); // 假设getDifficulty返回字符串
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
          // 未识别的难度，可以记录日志或忽略
          break;
      }
    }

    // 随机选择2道题
    List<Question> selectedQuestions = new ArrayList<>();
    selectedQuestions.addAll(selectRandomQuestions(easy, 2));
    selectedQuestions.addAll(selectRandomQuestions(medium, 2));
    selectedQuestions.addAll(selectRandomQuestions(hard, 2));
    selectedQuestions.addAll(selectRandomQuestions(veryHard, 2));

    // 打乱题目顺序
    Collections.shuffle(selectedQuestions);

    questions = selectedQuestions;

    // 打乱每道题的选项顺序
    for (Question q : questions) {
      List<Option> optionList = new ArrayList<>(Arrays.asList(q.getOptions()));
      Collections.shuffle(optionList);
      // 假设Option类可以通过构造函数或其他方式重新排列
      q.setOptions(List.of(optionList.toArray(new Option[0])));
    }

  } catch (NoTopicFoundException e) {
    // 如果没有找到题目，显示错误信息并返回
    Alert alert = new Alert(Alert.AlertType.ERROR, "No questions found for this subject.", ButtonType.OK);
    alert.showAndWait();
    // 返回科目选择界面
    SubjectChoosePage subjectChoosePage = new SubjectChoosePage(currentUser);
    subjectChoosePage.start(primaryStage);
  }
}

private List<Question> selectRandomQuestions(List<Question> questions, int count) {
  List<Question> selected = new ArrayList<>();
  Collections.shuffle(questions);
  for (int i = 0; i < count && i < questions.size(); i++) {
    selected.add(questions.get(i));
  }
  return selected;
}

private void showQuestion() {
  if (currentQuestionIndex >= questions.size()) {
    // 所有题目已完成，显示分数
    showScore();
    return;
  }

  Question currentQuestion = questions.get(currentQuestionIndex);

  // 显示题干
  questionLabel.setText("Question " + (currentQuestionIndex + 1) + ": " + currentQuestion.getQuestionStatement());

  // 获取选项
  Option[] options = currentQuestion.getOptions();
  List<Option> optionList = Arrays.asList(options);
  // 随机打乱选项顺序已经在loadQuestions()中进行了

  // 显示选项
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

  // 最后一题时，将“Next”按钮文字改为“Submit”
  if (currentQuestionIndex == questions.size() - 1) {
    nextButton.setText("Submit");
  } else {
    nextButton.setText("Next");
  }
}

private void handleNextAction() {
  // 检查用户选择
  Toggle selectedToggle = optionsGroup.getSelectedToggle();
  if (selectedToggle != null) {
    Option selectedOption = (Option) selectedToggle.getUserData();
    if (selectedOption.isCorrectAnswer()) {
      // 根据题目难度分配分数
      String difficulty = questions.get(currentQuestionIndex).getDifficulty().toString();
      int score = difficultyScoreMap.getOrDefault(difficulty, 0);
      totalScore += score;
    }
  }
  // 未选择则视为错误，不加分

  // 清除选择
  optionsGroup.selectToggle(null);

  // 进入下一题
  currentQuestionIndex++;
  showQuestion();
}

private void showScore() {
  // 显示总分并保存成绩
  Alert alert = new Alert(Alert.AlertType.INFORMATION, "Quiz finished!\nYour total score is: " + totalScore, ButtonType.OK);
  alert.showAndWait();

  // 将成绩记录到用户的ScoreRecords中
  currentUser.NewRecord(subject, totalScore);

  // 返回Dashboard
  Dashboard dashboard = new Dashboard(currentUser);
  dashboard.start(primaryStage);
}
}
