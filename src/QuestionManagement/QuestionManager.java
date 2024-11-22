package QuestionManagement;

import QuestionManagement.Exceptions.NoTopicFoundException;
import xjtlu.cpt111.assignment.quiz.model.Option;
import xjtlu.cpt111.assignment.quiz.model.Question;
import xjtlu.cpt111.assignment.quiz.util.IOUtilities;

import java.util.*;
import java.util.logging.Logger;

public class QuestionManager {

private final Map<String, List<Question>> m_questions_ = new HashMap<>();

/**
 * Load questions from question database
 *
 * @param fp database path
 * @return self, for chain-call
 */
public QuestionManager LoadQuestions(String fp) {
  Question[] questions = IOUtilities.readQuestions(fp);
  for (var q : questions) {
    if (q.getOptions().length < 2 ||
        q.getTopic().isEmpty() ||
        Arrays.stream(q.getOptions())
              .filter(Option::isCorrectAnswer)
              .count() != 1) {
      Logger.getLogger("global")
            .info("Illegal question detracted.");
      continue;
    }
    if (! m_questions_.containsKey(q.getTopic())) {
      m_questions_.put(q.getTopic(), new ArrayList<>());
    }
    m_questions_.get(q.getTopic())
                .add(q);
  }

  return this;
}

/**
 * Get Questions according to topic
 *
 * @param topic topic
 * @return Questions fulls into the topic
 * @throws NoTopicFoundException if the topic has no question
 */
public Question[] GetQuestions(String topic) throws NoTopicFoundException {
  if (! m_questions_.containsKey(topic)) {
    throw new NoTopicFoundException("No question full into such topic: " + topic);
  }
  return m_questions_.get(topic)
                     .toArray(new Question[0]);
}

/**
 * Return all topics
 *
 * @return topics
 */
public String[] GetTopics() {
  return m_questions_.keySet()
                     .toArray(new String[0]);
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
 * Load questions for the quiz.
 *
 * @param subject the subject of the quiz
 * @return the list of selected questions
 * @throws NoTopicFoundException if no questions found for the subject
 */
public List<Question> loadQuestionsForQuiz(String subject) throws NoTopicFoundException {
  // Get all questions
  Question[] allQuestions = GetQuestions(subject);
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

  // Shuffle options for each question
  for (Question q : selectedQuestions) {
    List<Option> optionList = new ArrayList<>(Arrays.asList(q.getOptions()));
    Collections.shuffle(optionList);
    q.setOptions(List.of(optionList.toArray(new Option[0])));
  }

  return selectedQuestions;
}


}
