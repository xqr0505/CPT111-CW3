package UserManagement;

import java.util.HashMap;
import java.util.Map;

public class ScoreRecords {
private static final int                    s_MAXIMUM_RECORDS = 3;               // How many records need to record for a topic
private final        Map<String, Integer[]> m_scoreRecords_   = new HashMap<>(); // Topic and corresponding scores
private final        int                    m_maximScore_;                       // Maximum score

/**
 * Constructor for ScoreRecords
 *
 * @param maximScore max score
 */
public ScoreRecords(int maximScore) {
  m_maximScore_ = maximScore;
}

/**
 * <p>Add a record, If the record not exist, create it, otherwise, update it.</p>
 *
 * @param topic specific topic
 * @param score score to be recorded
 * @return self, for chain-call
 * @throws Exceptions.ScoreValueOutOfRangeException if the score provide is neither null nor meaningful value
 */
public ScoreRecords NewRecord(String topic, Integer score) throws Exceptions.ScoreValueOutOfRangeException {
  // Assume null means no record,
  // Thus if it is larger than max score or less than 0, the score is meaningless
  if (score != null && (score > m_maximScore_ || score < 0)) {
    throw new Exceptions.ScoreValueOutOfRangeException(
        String.format("Maximum score is %d, while providing %d", m_maximScore_, score));
  }

  // If the given is stored
  if (m_scoreRecords_.containsKey(topic)) {
    for (int i = s_MAXIMUM_RECORDS - 1; i > 0; i--) {
      m_scoreRecords_.get(topic)[i] = m_scoreRecords_.get(topic)[i - 1];
    }
    m_scoreRecords_.get(topic)[0] = score;
  } else {
    // Create and store it to the first slot
    m_scoreRecords_.put(topic, new Integer[s_MAXIMUM_RECORDS]);
    m_scoreRecords_.get(topic)[0] = score;
  }

  return this;
}

/**
 * Get all topics answered
 *
 * @return Topics answered
 */
public String[] GetRecordTopics() {
  return m_scoreRecords_.keySet()
                        .toArray(new String[0]);
}

/**
 * Get all Record according to given topic
 *
 * @param topic specific topic
 * @return Records of the given topic
 */
public Integer[] GetRecord(String topic) {
  return m_scoreRecords_.get(topic)
                        .clone();
}

/**
 * Get Maximum Score
 *
 * @return Maximum Score
 */
public Integer GetMaxScore() {
  return m_maximScore_;
}

// TODO: Port To CSV

}
