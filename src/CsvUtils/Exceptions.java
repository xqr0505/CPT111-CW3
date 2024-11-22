package CsvUtils;

public class Exceptions {
public static class IllegalSyntaxException
    extends RuntimeException {

/**
   * Constructs a new IllegalSyntaxException with {@code null} as its detail message.
   */
  public IllegalSyntaxException() {
    super();
  }

  /**
   * Constructs a new IllegalSyntaxException with the specified detail message.
   *
   * @param e the detail message
   */
  public IllegalSyntaxException(String e) {
    super(e);

  }
}

}
