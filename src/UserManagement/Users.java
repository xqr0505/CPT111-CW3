package UserManagement;

import java.util.List;

public class Users {
private final String m_id_;
private final String m_name_;
private final ScoreRecords m_record_ = new ScoreRecords();
private String m_passwd_;

public Users(String id, String name, String passwd) throws Exceptions.UserInformationInvalidException {
  if (id.isEmpty()) {
    throw new Exceptions.UserInformationInvalidException("User ID cannot be empty");
  }
  if (name.isEmpty()) {
    throw new Exceptions.UserInformationInvalidException("User's name cannot be empty");
  }
  m_id_ = id;
  m_name_ = name;
  m_passwd_ = passwd;
}

public String GetPasswd() {
  return m_passwd_;
}

public boolean CheckPasswd(String passwd) {
  return m_passwd_.equals(passwd);
}

public Users setPasswd(String passwd) {
  this.m_passwd_ = passwd;
  return this;
}

public String GetName() {
  return m_name_;
}

public String GetId() {
  return m_id_;
}

public ScoreRecords GetRecords() {
  return m_record_;
}

/**
 * Add a new score record
 *
 * @param topic the name of the topic
 * @param score the score
 * @return the user object
 */
public Users NewRecord(String topic, Integer score) {
  if (score == null) {
    return this; // 如果分数为空，直接返回
  }
  m_record_.addScore(topic, score);
  // 自动更新最高分
  Integer currentHighest = GetTopicSpecifiedHighestRecord(topic);
  if (currentHighest == null || score > currentHighest) {
    SetTopicSpecifiedHighestRecord(topic, score);
  }
  return this;
}

/**
 * Get all topics the user has answered
 *
 * @return an array of all answered topics
 */
public String[] GetAnsweredTopics() {
  return m_record_.getAllRecords().keySet().toArray(new String[0]);
}

/**
 * Get the most recent three scores for a specific topic
 *
 * @param topic the name of the topic
 * @return a list of the most recent three scores for the specified topic
 */
public List<Integer> GetTopicSpecifiedRecentRecords(String topic) {
  ScoreRecords.TopicScores ts = m_record_.getTopicScores(topic);
  return ts != null ? ts.getRecentScores() : List.of(); // 返回空列表以防止空指针异常
}

/**
 * Get the highest score for a specific topic
 *
 * @param topic the name of the topic
 * @return the highest score for the specified topic
 */
public Integer GetTopicSpecifiedHighestRecord(String topic) {
  ScoreRecords.TopicScores ts = m_record_.getTopicScores(topic);
  return ts != null ? ts.getHighestScore() : null;
}

/**
 * Set the highest score for a specific topic
 *
 * @param topic the name of the topic
 * @param score the highest score to set
 */
public void SetTopicSpecifiedHighestRecord(String topic, Integer score) {
  m_record_.setHighestScore(topic, score);
}
}
