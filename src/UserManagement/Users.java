package UserManagement;

import java.util.List;

/**
 * Represents a user with an ID, name, password, and score records.
 */
public class Users {
private final   String        m_id_;
private final   String        m_name_;
private final   ScoreRecords  m_record_ = new ScoreRecords();
private         String        m_passwd_;

/**
 * Constructs a new user.
 *
 * @param id the user ID
 * @param name the username
 * @param passwd the user password
 * @throws Exceptions.UserInformationInvalidException if the ID or name is empty
 */
public Users(String id, String name, String passwd) throws Exceptions.UserInformationInvalidException {
  if (id.isEmpty()) {
    throw new Exceptions.UserInformationInvalidException("User ID cannot be empty");
  }
  if (name.isEmpty()) {
    throw new Exceptions.UserInformationInvalidException("User's name cannot be empty");
  }
  m_id_     =   id;
  m_name_   =   name;
  m_passwd_ =   passwd;
}

/**
 * Gets the user password.
 *
 * @return the user password
 */
public String GetPasswd() {
  return m_passwd_;
}

/**
 * Checks if the provided password matches the user's password.
 *
 * @param passwd the password to check
 * @return true if the password matches, false otherwise
 */
public boolean CheckPasswd(String passwd) {
  return m_passwd_.equals(passwd);
}

/**
 * Sets the user password.
 *
 * @param passwd the new password
 * @return the user object
 */
public Users setPasswd(String passwd) {
  this.m_passwd_ = passwd;
  return this;
}

/**
 * Gets the username.
 *
 * @return the username
 */
public String GetName() {
  return m_name_;
}

/**
 * Gets the user ID.
 *
 * @return the user ID
 */
public String GetId() {
  return m_id_;
}

/**
 * Gets the user's score records.
 *
 * @return the score records
 */
public ScoreRecords GetRecords() {
  return m_record_;
}

/**
 * Gets all topics the user has answered.
 *
 * @return an array of all answered topics
 */
public String[] GetAnsweredTopics() {
  return m_record_.getAllRecords().keySet().toArray(new String[0]);
}

/**
 * Adds a new score record for a specific topic.
 *
 * @param topic the name of the topic
 * @param score the score to be added
 * @throws Exceptions.ScoreValueOutOfRangeException if the score is out of the valid range (0-100)
 */
public void NewRecord(String topic, Integer score) {
  if (score == null) {
    return;
  }
  if (score < 0 || score > 100) {
    throw new Exceptions.ScoreValueOutOfRangeException("Score value is out of range: " + score);
  }
  m_record_.addScore(topic, score);
  Integer currentHighest = GetTopicSpecifiedHighestRecord(topic);
  if (currentHighest == null || score > currentHighest) {
    SetTopicSpecifiedHighestRecord(topic, score);
  }
}

/**
 * Gets the most recent three scores for a specific topic.
 *
 * @param topic the name of the topic
 * @return a list of the most recent three scores for the specified topic
 */
public List<Integer> GetTopicSpecifiedRecentRecords(String topic) {
  ScoreRecords.TopicScores ts = m_record_.getTopicScores(topic);
  return ts != null ? ts.getRecentScores() : List.of();
}

/**
 * Gets the highest score for a specific topic.
 *
 * @param topic the name of the topic
 * @return the highest score for the specified topic
 */
public Integer GetTopicSpecifiedHighestRecord(String topic) {
  ScoreRecords.TopicScores ts = m_record_.getTopicScores(topic);
  return ts != null ? ts.getHighestScore() : null;
}

/**
 * Sets the highest score for a specific topic.
 *
 * @param topic the name of the topic
 * @param score the highest score to set
 */
public void SetTopicSpecifiedHighestRecord(String topic, Integer score) {
  ScoreRecords.TopicScores ts = m_record_.getTopicScores(topic);
  if (ts == null) {
    m_record_.addScore(topic, score);
    ts = m_record_.getTopicScores(topic);
  }
  ts.setHighestScore(score);
}
}