package QuestionManagement;

public class Exceptions {
public static class NoTopicFoundException
    extends RuntimeException {

  /**
   * Constructs a new NoTopicFoundException with {@code null} as its detail message.
   */
  public NoTopicFoundException() {
    super();
  }

  /**
   * Constructs a new NoTopicFoundException with the specified detail message.
   *
   * @param e the detail message
   */
  public NoTopicFoundException(String e) {
    super(e);
  }

}

public static class NotEnoughQuestionsException
    extends IllegalArgumentException {
  /**
   * Constructs a new NotEnoughQuestionsException with {@code null} as its detail message.
   */
  public NotEnoughQuestionsException() {
    super();
  }

  /**
   * Constructs a new NotEnoughQuestionsException with the specified detail message.
   *
   * @param e the detail message
   */
  public NotEnoughQuestionsException(String e) {
    super(e);
  }

}
}


