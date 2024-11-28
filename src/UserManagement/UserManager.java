package UserManagement;

import CsvUtils.CsvReader;
import CsvUtils.CsvWriter;
import CsvUtils.Table;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Logger;

/**
 * Manages user accounts and score records.
 */
public class UserManager {

private final Set<Users> m_users_ = new HashSet<>();

/**
 * Load User Account and Score information.
 *
 * @param infofp  account information file
 * @param scorefp score record information file
 * @return self, for chain-call
 * @throws IOException if file is not readable
 */
public UserManager LoadUserInfo(String infofp, String scorefp) throws IOException {
  // Load user information
  File infoFile = new File(infofp);
  if (!infoFile.exists() || infoFile.isDirectory()) {
    throw new FileNotFoundException("User info file not found: " + infofp);
  }

  String content = new String(Files.readAllBytes(Paths.get(infofp)), StandardCharsets.UTF_8);
  Table csv = CsvReader.ConstructTableFromCSV(content);
  LoadAccountInfoFromTable(csv);

  // Load score information
  File scoreFile = new File(scorefp);
  if (!scoreFile.exists() || scoreFile.isDirectory()) {
    throw new FileNotFoundException("Score file not found: " + scorefp);
  }

  content = new String(Files.readAllBytes(Paths.get(scorefp)), StandardCharsets.UTF_8);
  Table scoreTable = CsvReader.ConstructTableFromCSV(content);
  LoadScoreInfoFromTable(scoreTable);

  System.out.println("Total users loaded: " + m_users_.size());

  return this;
}

/**
 * Save User Account and Score Information to file.
 *
 * @param infofp  account file path
 * @param scorefp score record information file path
 * @return self, for chain-call
 * @throws IOException if file is not writable
 */
public UserManager SaveUserInfo(String infofp, String scorefp) throws IOException {
  // Save user information
  File infoFile = new File(infofp);
  try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(infoFile), StandardCharsets.UTF_8))) {
    writer.print(CsvWriter.GenerateContent(ExportAccountInfoToTable()));
  }

  // Save score information
  File scoreFile = new File(scorefp);
  try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(scoreFile), StandardCharsets.UTF_8))) {
    writer.print(CsvWriter.GenerateContent(ExportScoreInfoToTable()));
  }

  return this;
}

/**
 * Register a new user.
 *
 * @param user user object
 * @return self, for chain-call
 */
public UserManager RegisterUser(Users user) {
  for (Users existingUser : m_users_) {
    if (user.GetId().equals(existingUser.GetId())) {
      throw new UserManagement.Exceptions.DuplicateUserException("There exists a user whose id is " + user.GetId());
    }
  }
  m_users_.add(user);
  return this;
}

/**
 * Checks the login credentials of a user.
 *
 * @param userId   the user ID
 * @param password the password
 * @return the Users object if the login is successful
 * @throws Exceptions.UserNotFoundException if the user ID is not found
 * @throws Exceptions.IncorrectPasswordException if the password is incorrect
 */
public Users CheckLogin(String userId, String password) throws Exceptions.UserNotFoundException, Exceptions.IncorrectPasswordException {
  Users user = getUserById(userId);
  if (user == null) {
    throw new Exceptions.UserNotFoundException("User not found: " + userId);
  }
  if (!user.CheckPasswd(password)) {
    throw new Exceptions.IncorrectPasswordException("Incorrect password for user: " + userId);
  }
  return user;
}

/**
 * Export Account information to table.
 *
 * @return account info table
 */
public Table ExportAccountInfoToTable() {
  Table table = new Table();
  for (Users u : m_users_) {
    table.InsertLine(List.of(new String[]{
        u.GetId(), u.GetName(), u.GetPasswd()
    }));
  }
  return table;
}

/**
 * Load Account info from table.
 *
 * @param table account info table
 * @return self, for chain-call
 */
public UserManager LoadAccountInfoFromTable(Table table) {
  for (String[] l : table.GetTable()) {
    // Skip completely empty lines
    if (Arrays.stream(l).allMatch(String::isEmpty)) {
      continue;
    }

    String userId = l[0].trim();
    String userName = l[1].trim();
    String password = l[2].trim();

    try {
      RegisterUser(new Users(userId, userName, password));
    } catch (UserManagement.Exceptions.DuplicateUserException e) {
      Logger.getLogger("global").warning("Duplicate user ID found: " + userId + ". Skipping user.");
    } catch (UserManagement.Exceptions.UserInformationInvalidException e) {
      Logger.getLogger("global").warning("Failed to register user: " +Arrays.toString(l)+ e.getMessage()+". Skipping user.");
    }
  }
  return this;
}

/**
 * Export Score information to table.
 *
 * @return score info table
 */
public Table ExportScoreInfoToTable() {
  Table table = new Table();
  for (Users u : m_users_) {
    for (Map.Entry<String, ScoreRecords.TopicScores> entry : u.GetRecords().getAllRecords().entrySet()) {
      String topic = entry.getKey();
      ScoreRecords.TopicScores ts = entry.getValue();
      List<Integer> recentScores = ts.getRecentScores();
      Integer highestScore = ts.getHighestScore();

      // Construct score row
      String score1 = recentScores.size() >= 1 ? recentScores.get(0).toString() : "";
      String score2 = recentScores.size() >= 2 ? recentScores.get(1).toString() : "";
      String score3 = recentScores.size() >= 3 ? recentScores.get(2).toString() : "";
      String highest = highestScore != null ? highestScore.toString() : "";

      table.InsertLine(List.of(new String[]{
          u.GetId(), topic, score1, score2, score3, highest
      }));

    }
  }
  return table;
}

/**
 * Load score info from table.
 *
 * @param table score info table
 * @return self, for chain-call
 */
public UserManager LoadScoreInfoFromTable(Table table) {
  for (String[] l : table.GetTable()) {
    // Skip completely empty lines
    if (l == null || l.length == 0 || Arrays.stream(l).allMatch(String::isEmpty)) {
      continue;
    }

    // Parse and process the line
    String userId = l[0].trim();
    String topic = l[1].trim();
    String score1Str = l[2].trim();
    String score2Str = l[3].trim();
    String score3Str = l[4].trim();
    String highestScoreStr = l[5].trim();

    Users user = getUserById(userId);
    if (user != null) {
      try {
        if (topic.isEmpty()) {
          Logger.getLogger("global").warning("No topic information for user: " + userId + ". Skipping record.");
          continue;
        }
        if (user.GetRecords().getAllRecords().containsKey(topic)) {
          Logger.getLogger("global").warning("Record already exists for user: " + userId + " and topic: " + topic + ". Skipping record.");
          continue;
        }

        if (!score1Str.isEmpty()) user.NewRecord(topic, Integer.parseInt(score1Str));
        if (!score2Str.isEmpty()) user.NewRecord(topic, Integer.parseInt(score2Str));
        if (!score3Str.isEmpty()) user.NewRecord(topic, Integer.parseInt(score3Str));
        if (!highestScoreStr.isEmpty()) {
          int highestScore = Integer.parseInt(highestScoreStr);
          List<Integer> recentScores = user.GetTopicSpecifiedRecentRecords(topic);
          boolean skipRecord = false;
          for (Integer score : recentScores) {
            if (score != null && score > highestScore) {
              Logger.getLogger("global").warning("Highest score is lower than a recent score for user: " + userId + ". Skipping record.");
              skipRecord = true;
              break;
            }
          }
          if (!skipRecord) {
            user.SetTopicSpecifiedHighestRecord(topic, highestScore);
          }
        }
      } catch (NumberFormatException e) {
        Logger.getLogger("global").warning("Invalid score format in record: " + Arrays.toString(l)+ ". Skipping record.");
      } catch (Exceptions.ScoreValueOutOfRangeException e) {
        Logger.getLogger("global").warning("Invalid score value in record: " + Arrays.toString(l)+ ". Skipping record.");
      }
    } else {
      Logger.getLogger("global").warning("User not found for score record: " + Arrays.toString(l)+ ". Skipping record.");
    }
  }
  return this;
}

/**
 * Get user by ID.
 *
 * @param id user ID
 * @return Users object, or null if not found
 */
public Users getUserById(String id) {
  for (Users user : m_users_) {
    if (user.GetId().equals(id)) {
      return user;
    }
  }
  return null;
}

/**
 * Get all users.
 *
 * @return unmodifiable set of users
 */
public Set<Users> GetAllUsers() {
  return Collections.unmodifiableSet(m_users_);
}

/**
 * Get all topics answered by any user.
 *
 * @return an array of all answered topics
 */
public String[] getAllTopicsAnsweredByAnyUser() {
  Set<String> allTopics = new HashSet<>();
  for (Users user : m_users_) {
    String[] answeredTopics = user.getAllTopicsAnswered();
    for (String topic : answeredTopics) {
      allTopics.add(topic);
    }
  }
  return allTopics.toArray(new String[0]);
}
}

