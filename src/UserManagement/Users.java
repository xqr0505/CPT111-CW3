package UserManagement;

public class Users {

private final String       m_id_;                             // user's id, should be unique, and cannot be empty
private final String       m_name_;                           // user's name cannot be empty
private final ScoreRecords m_record_ = new ScoreRecords(100);
private       String       m_passwd_;                         // user's passwd

/**
 * Construct a new User
 *
 * @param id     user's id, can't be empty or duplicate
 * @param name   user's name, can't be empty
 * @param passwd user's passwd,
 * @throws Exceptions.UserInformationInvalidException if the content is not matching the requirement
 */
public Users(String id, String name, String passwd) throws Exceptions.UserInformationInvalidException {
  if (id.isEmpty()) {
    throw new Exceptions.UserInformationInvalidException("User ID cannot be empty");
  }
  if (name.isEmpty()) {
    throw new Exceptions.UserInformationInvalidException("User's name cannot be empty");
  }
  m_id_     = id;
  m_name_   = name;
  m_passwd_ = passwd;
}

/**
 * Get User password
 *
 * @return User password, notnull
 */
public String GetPasswd() {
  return m_passwd_;
}

/**
 * Check wether the passwd provided matching the user
 *
 * @param passwd given passwd
 * @return if it is matching
 */
public boolean CheckPasswd(String passwd) {
  return m_passwd_.equals(passwd);
}

/**
 * Set a new passwd
 *
 * @param passwd new passwd
 * @return self, for chain-call
 */
public Users setPasswd(String passwd) {
  this.m_passwd_ = passwd;
  return this;
}

/**
 * Get User Real Name
 *
 * @return User Name, notnull
 */
public String GetName() {
  return m_name_;
}

/**
 * Get User ID
 *
 * @return User ID, notnull
 */
public String GetId() {
  return m_id_;
}

/**
 * Get User Score Record Database
 *
 * @return User Score Record Database
 */
public ScoreRecords GetRecords() {
  return m_record_;
}

/**
 * Wrapper for ScoreRecord::AddRecord
 *
 * @param topic Specified topic
 * @param score New record
 * @return This, for chain-call
 */
public Users NewRecord(String topic, Integer score) {
  m_record_.NewRecord(topic, score);
  return this;
}

/**
 * Wrapper for ScoreRecord::GetRecordTopics
 *
 * @return All Topics Answered
 */
public String[] GetAnsweredTopics() {
  return m_record_.GetRecordTopics();
}

/**
 * Wrapper for ScoreRecord::GetRecord
 *
 * @param topic specific topic
 * @return records associated with the topic
 */
public Integer[] GetTopicSpecifiedRecords(String topic) {
  return m_record_.GetRecord(topic);
}

// TODO: PortToCSV

}
