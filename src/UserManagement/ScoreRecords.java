package UserManagement;

import java.util.*;

public class ScoreRecords {

private final Map<String, TopicScores> records;

public ScoreRecords() {
  this.records = new HashMap<>();
}

/**
 * Add a new score record
 *
 * @param topic the name of the topic
 * @param score the score
 */
public void addScore(String topic, Integer score) {
  records.putIfAbsent(topic, new TopicScores());
  TopicScores ts = records.get(topic);
  ts.addScore(score);
}

/**
 * Get all score records for all topics
 *
 * @return a map of topics and their corresponding score records
 */
public Map<String, TopicScores> getAllRecords() {
  return records;
}

/**
 * Get score records for a specific topic
 *
 * @param topic the name of the topic
 * @return the score records for the specified topic
 */
public TopicScores getRecordsByTopic(String topic) {
  return records.getOrDefault(topic, new TopicScores());
}

/**
 * Get score records for a specific topic
 *
 * @param topic the name of the topic
 * @return the score records for the specified topic, or null if not found
 */
public TopicScores getTopicScores(String topic) {
  return records.get(topic);
}

/**
 * Set the highest score for a specific topic
 *
 * @param topic the name of the topic
 * @param score the highest score to set
 */
public void setHighestScore(String topic, Integer score) {
  TopicScores ts = records.get(topic);
  if (ts == null) {
    ts = new TopicScores();
    records.put(topic, ts);
  }
  ts.setHighestScore(score);
}

/**
 * Inner class representing the score records for a topic
 */
public static class TopicScores {
  private final LinkedList<Integer> recentScores;
  private Integer highestScore;

  public TopicScores() {
    this.recentScores = new LinkedList<>();
    this.highestScore = null;
  }

  /**
   * Add a score record
   *
   * @param score the score
   */
  public void addScore(Integer score) {
    if (score == null) return;

    // Keep the most recent three scores
    if (recentScores.size() == 3) {
      recentScores.removeLast();
    }
    recentScores.addFirst(score);

    // Update the highest score
    if (highestScore == null || score > highestScore) {
      highestScore = score;
    }
  }

  /**
   * Get the recent scores
   *
   * @return an unmodifiable list of recent scores
   */
  public List<Integer> getRecentScores() {
    return Collections.unmodifiableList(recentScores);
  }

  /**
   * Get the highest score
   *
   * @return the highest score
   */
  public Integer getHighestScore() {
    return highestScore;
  }

  /**
   * Set the highest score
   *
   * @param score the highest score to set
   */
  public void setHighestScore(Integer score) {
    this.highestScore = score;
  }
}
}