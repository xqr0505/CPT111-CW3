package UserManagement;

import CsvUtils.CsvReader;
import CsvUtils.CsvWriter;
import CsvUtils.Exceptions;
import CsvUtils.Table;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import static UserManagement.Exceptions.DuplicateUserException;

public class UserManager {

private final Set<Users> m_users_ = new HashSet<>();

/**
 * Extract Records of each user
 *
 * @return Topic, ScoreRecords
 */
public Map<String, List<ScoreEntry>> GetRecords() {
  var ret = new HashMap<String, List<ScoreEntry>>();
  for (var u : m_users_) {
    for (var t : u.GetRecords()
                  .GetRecordTopics()) {
      if (! ret.containsKey(t)) {
        ret.put(t, new ArrayList<>());
      }
      for (var s : u.GetRecords()
                    .GetRecord(t)) {
        ret.get(t)
           .add(new ScoreEntry(u.GetId(), s));
      }
    }
  }
  return ret;
}

/**
 * Load User Account and Score information
 *
 * @param infofp  account information file
 * @param scorefp score record information file
 * @return self, for chain-call
 * @throws IOException if file is not readable
 */
public UserManager LoadUserInfo(String infofp, String scorefp) throws IOException {
  // Account info
  var file = new File(infofp);
  if (! file.exists() || file.isDirectory()) {
    throw new FileNotFoundException("No such file to be read");
  }
  var content = new byte[((int)file.length())];
  try (var inputStream = new FileInputStream(file)) {
    var v   = inputStream.read(content);
    var csv = CsvReader.ConstructTableFromCSV(new String(content));
    LoadAccountInfoFromTable(csv);
  } catch (DuplicateUserException e) {
    System.out.println(e.getMessage());
  }

  // Score
  file = new File(scorefp);
  if (! file.exists() || file.isDirectory()) {
    throw new FileNotFoundException("No such file to be read");
  }
  content = new byte[((int)file.length())];
  try (var inputStream = new FileInputStream(file)) {
    var v   = inputStream.read(content);
    var csv = CsvReader.ConstructTableFromCSV(new String(content));
    LoadScoreInfoFromTable(csv);
  } catch (DuplicateUserException | NumberFormatException e) {
    System.out.println(e.getMessage());
  }

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
  var file = new File(infofp);
  if (! file.exists() || file.isDirectory()) {
    throw new FileNotFoundException("No such file to be read");
  }
  try (var printer = new PrintWriter(file)) {
    printer.print(CsvWriter.GenerateContent(ExportAccountInfoToTable()));
  }
  file = new File(scorefp);
  if (! file.exists() || file.isDirectory()) {
    throw new FileNotFoundException("No such file to be read");
  }
  try (var printer = new PrintWriter(file)) {
    printer.print(CsvWriter.GenerateContent(ExportScoreInfoToTable()));
  }
  return this;
}

public UserManager RegisterUser(Users user) {
  m_users_.forEach((x) -> {
    if (user.GetId()
            .equals(x.GetId())) {
      throw new DuplicateUserException("There exists a user whose id is " + user.GetId());
    }
  });
  m_users_.add(user);
  return this;
}

/**
 * Detect weather ith passwd is matching id
 *
 * @param id     id to login
 * @param passwd passwd
 * @return null if not matching, user if correct
 */
public Users CheckLogin(String id, String passwd) {
  AtomicReference<Users> ret = new AtomicReference<>();
  m_users_.forEach(u -> {
    if (u.GetId()
         .equals(id) && u.CheckPasswd(passwd)) {
      ret.set(u);
    }
  });
  return ret.get();
}

/**
 * Add score record to user
 *
 * @param id    user to be added score record
 * @param topic topic of the record
 * @param score score of the record
 * @return self, for chain-call
 */
public UserManager AddRecord(String id, String topic, Integer score) {
  m_users_.forEach(u -> {
    if (u.GetId()
         .equals(id)) {
      u.NewRecord(topic, score);
    }
  });
  return this;
}

/**
 * Export Account information to table
 *
 * @return account info table
 */
public Table ExportAccountInfoToTable() {
  var table = new Table();
  for (var u : m_users_) {
    table.InsertLine()
         .InsertElement(table.GetRows() - 1, u.GetId(), true)
         .InsertElement(table.GetRows() - 1, u.GetName(), true)
         .InsertElement(table.GetRows() - 1, u.GetPasswd(), true);
    Logger.getLogger("global")
          .info("Write one user's score record: id:" + u.GetId());
  }
  return table;
}

/**
 * Load Account info from table
 *
 * @param table account info table
 * @return self, for chain-call
 */
//public UserManager LoadAccountInfoFromTable(Table table) {
//  for (var l : table.GetTable()) {
//    if (l.length <= 0) {
//      continue;
//    } else if (l.length < 3) {
//      throw new Exceptions.IllegalSyntaxException("The Format of CSV file is not matching.");
//    }
//    RegisterUser(new Users(l[0], l[1], l[2]));
//    Logger.getLogger("global")
//          .info("Load one user: id:" + l[0]);
//  }
//  return this;
//}
public UserManager LoadAccountInfoFromTable(Table table) {
  for (var l : table.GetTable()) {
    if (l.length <= 0) {
      continue;
    } else if (l.length < 3) {
      throw new Exceptions.IllegalSyntaxException("The Format of CSV file is not matching.");
    }
    String userId = l[0].trim();
    String userName = l[1].trim();
    String password = l[2].trim();
    if (userId.isEmpty()) {
      System.out.println("Invalid user data: " + Arrays.toString(l));
      throw new UserManagement.Exceptions.UserInformationInvalidException("User ID cannot be empty");
    }
    RegisterUser(new Users(userId, userName, password));
    Logger.getLogger("global").info("Load one user: id:" + userId);
  }
  return this;
}

/**
 * Load score info from table
 *
 * @param table score info table
 * @return self, for chain-call
 */
public UserManager LoadScoreInfoFromTable(Table table) {
  for (var l : table.GetTable()) {
    if (l.length <= 0) {
      continue;
    } else if (l.length < 5) {
      throw new Exceptions.IllegalSyntaxException("The Format of CSV file is not matching.");
    }
    m_users_.stream()
            .filter(x -> x.GetId()
                          .equals(l[0]))
            .toList()
            .get(0)
            .NewRecord(l[1], l[2].isEmpty() ? null : Integer.parseInt(l[2]))
            .NewRecord(l[1], l[3].isEmpty() ? null : Integer.parseInt(l[3]))
            .NewRecord(l[1], l[4].isEmpty() ? null : Integer.parseInt(l[4]));
    Logger.getLogger("global")
          .info("Load one user's score record: id:" + l[0]);
  }

  return this;
}

/**
 * Export Score information to table
 *
 * @return score info table
 */
public Table ExportScoreInfoToTable() {
  var table = new Table();
  for (var u : m_users_) {
    for (var t : u.GetRecords()
                  .GetRecordTopics()) {
      var scores = u.GetRecords()
                    .GetRecord(t);
      table.InsertLine()
           .InsertElement(table.GetRows() - 1, u.GetId(), true)
           .InsertElement(table.GetRows() - 1, t, true)
           .InsertElement(table.GetRows() - 1, scores[0] == null ? "" : scores[0].toString(), true)
           .InsertElement(table.GetRows() - 1, scores[1] == null ? "" : scores[1].toString(), true)
           .InsertElement(table.GetRows() - 1, scores[2] == null ? "" : scores[2].toString(), true);
      Logger.getLogger("global")
            .info("Write one user's score record: id:" + u.GetId());
    }
  }
  return table;
}

public Users getUserById(String id) {
  for (Users user : m_users_) {
    if (user.GetId().equals(id)) {
      return user;
    }
  }
  return null;
}

public static class ScoreEntry {
  public String  id;
  public Integer score;

  public ScoreEntry(String id, Integer score) {
    this.id    = id;
    this.score = score;
  }
}
}
