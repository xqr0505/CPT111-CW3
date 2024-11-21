package QuestionManagement;

public class Exceptions {
public static class NoTopicFoundException
    extends RuntimeException {
  public NoTopicFoundException() {
    super();
  }

  public NoTopicFoundException(String e) {
    super(e);
  }
}
}
