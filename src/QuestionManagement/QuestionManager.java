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
}
