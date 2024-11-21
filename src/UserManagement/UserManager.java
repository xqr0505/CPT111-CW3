package UserManagement;

import CsvUtils.CsvReader;
import CsvUtils.CsvWriter;
import CsvUtils.Exceptions;
import CsvUtils.Table;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Logger;

public class UserManager {

private final Set<Users> m_users_ = new HashSet<>();

/**
 * Load User Account and Score information
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

  return this;
}

/**
 * Save User Account and Score Information to file
 *
 * @param infofp  account file path
 * @param scorefp score record file path
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
 * Register a new user
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
 * Detect whether the password is matching id
 *
 * @param id     id to login
 * @param passwd password
 * @return null if not matching, user if correct
 */
public Users CheckLogin(String id, String passwd) {
  for (Users user : m_users_) {
    if (user.GetId().equals(id) && user.CheckPasswd(passwd)) {
      return user;
    }
  }
  return null;
}

/**
 * Export Account information to table
 *
 * @return account info table
 */
public Table ExportAccountInfoToTable() {
  Table table = new Table();
  for (Users u : m_users_) {
    table.InsertLine(List.of(new String[] {
        u.GetId(), u.GetName(), u.GetPasswd()
    }));
    Logger.getLogger("global").info("Write one user's account info: id=" + u.GetId());
  }
  return table;
}

/**
 * Load Account info from table
 *
 * @param table account info table
 * @return self, for chain-call
 */
public UserManager LoadAccountInfoFromTable(Table table) {
  for (String[] l : table.GetTable()) {
    if (l.length <= 0) {
      continue;
    } else if (l.length < 3) {
      throw new Exceptions.IllegalSyntaxException("The Format of CSV file is not matching.");
    }
    String userId = l[0].trim();
    String userName = l[1].trim();
    String password = l[2].trim();

    // Add debug output
    System.out.println("Loading user: ID='" + userId + "', Name='" + userName + "', Password='" + password + "'");

    if (userId.isEmpty()) {
      System.out.println("Invalid user data: " + Arrays.toString(l));
      throw new UserManagement.Exceptions.UserInformationInvalidException("User ID cannot be empty");
    }
    RegisterUser(new Users(userId, userName, password));
    Logger.getLogger("global").info("Load one user: id=" + userId);
  }
  return this;
}

/**
 * Export Score information to table
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

      table.InsertLine(List.of(new String[] {
          u.GetId(), topic, score1, score2, score3, highest
      }));

      Logger.getLogger("global").info("Write one user's score record: id=" + u.GetId() + ", topic=" + topic + ", scores=[" + score1 + "," + score2 + "," + score3 + "], highest=" + highest);
    }
  }
  return table;
}

/**
 * Load score info from table
 *
 * @param table score info table
 * @return self, for chain-call
 */
public UserManager LoadScoreInfoFromTable(Table table) {
  for (String[] l : table.GetTable()) {
    if (l.length <= 0) {
      continue;
    } else if (l.length < 6) { // 6 columns: userId, topic, score1, score2, score3, highestScore
      throw new Exceptions.IllegalSyntaxException("The Format of CSV file is not matching.");
    }

    String userId = l[0].trim();
    String topic = l[1].trim();
    String score1Str = l[2].trim();
    String score2Str = l[3].trim();
    String score3Str = l[4].trim();
    String highestScoreStr = l[5].trim();

    Users user = getUserById(userId);
    if (user != null) {
      // 添加前三次分数记录
      if (!score1Str.isEmpty()) {
        try {
          int score1 = Integer.parseInt(score1Str);
          user.NewRecord(topic, score1);
        } catch (NumberFormatException e) {
          Logger.getLogger("global").warning("Invalid score format for score1: " + score1Str);
        }
      }
      if (!score2Str.isEmpty()) {
        try {
          int score2 = Integer.parseInt(score2Str);
          user.NewRecord(topic, score2);
        } catch (NumberFormatException e) {
          Logger.getLogger("global").warning("Invalid score format for score2: " + score2Str);
        }
      }
      if (!score3Str.isEmpty()) {
        try {
          int score3 = Integer.parseInt(score3Str);
          user.NewRecord(topic, score3);
        } catch (NumberFormatException e) {
          Logger.getLogger("global").warning("Invalid score format for score3: " + score3Str);
        }
      }

      // 更新最高分
      if (!highestScoreStr.isEmpty()) {
        try {
          Integer highestScore = Integer.parseInt(highestScoreStr);
          Integer currentHighest = user.GetTopicSpecifiedHighestRecord(topic);
          if (currentHighest == null || highestScore > currentHighest) {
            user.SetTopicSpecifiedHighestRecord(topic, highestScore);
          }
        } catch (NumberFormatException e) {
          Logger.getLogger("global").warning("Invalid highest score format: " + highestScoreStr);
        }
      }

      Logger.getLogger("global").info("Load one user's score record: id=" + userId + ", topic=" + topic
                                      + ", scores=[" + score1Str + "," + score2Str + "," + score3Str + "], highest=" + highestScoreStr);
    } else {
      Logger.getLogger("global").warning("User not found for score record: " + Arrays.toString(l));
    }
  }
  return this;
}


/**
 * Get user by ID
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
 * Get all users
 *
 * @return unmodifiable set of users
 */
public Set<Users> GetAllUsers() {
  return Collections.unmodifiableSet(m_users_);
}


}
