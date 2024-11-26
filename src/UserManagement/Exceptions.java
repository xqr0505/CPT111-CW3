package UserManagement;

/**
 * This class contains custom exceptions for user management.
 */
public class Exceptions {

/**
 * Exception thrown when the score value is out of range.
 */
public static class ScoreValueOutOfRangeException extends RuntimeException {
  public ScoreValueOutOfRangeException() {
    super();
  }

  public ScoreValueOutOfRangeException(String e) {
    super(e);
  }
}

/**
 * Exception thrown when user information is invalid.
 */
public static class UserInformationInvalidException extends RuntimeException {
  public UserInformationInvalidException() {
    super();
  }

  public UserInformationInvalidException(String e) {
    super(e);
  }
}

/**
 * Exception thrown when a duplicate user is found.
 */
public static class DuplicateUserException extends RuntimeException {
  public DuplicateUserException() {
    super();
  }

  public DuplicateUserException(String e) {
    super(e);
  }
}

/**
 * Exception thrown when a user is not found.
 */
public static class UserNotFoundException extends Exception {
  public UserNotFoundException(String message) {
    super(message);
  }
}

/**
 * Exception thrown when the password is incorrect.
 */
public static class IncorrectPasswordException extends Exception {
  public IncorrectPasswordException(String message) {
    super(message);
  }
}
}