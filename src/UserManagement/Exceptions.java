package UserManagement;

public class Exceptions {
public static class ScoreValueOutOfRangeException
    extends RuntimeException {
  public ScoreValueOutOfRangeException() {
    super();
  }

  public ScoreValueOutOfRangeException(String e) {
    super(e);
  }

}

public static class UserInformationInvalidException
    extends RuntimeException {
  UserInformationInvalidException() {
    super();
  }

  UserInformationInvalidException(String e) {
    super(e);
  }
}

public static class DuplicateUserException
    extends RuntimeException {
  public DuplicateUserException() {
    super();
  }

  public DuplicateUserException(String e) {
    super(e);
  }
}
}
