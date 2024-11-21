package CsvUtils;

public class Exceptions {
public static class IllegalSyntaxException
    extends RuntimeException {
  public IllegalSyntaxException() {
    super();
  }

  public IllegalSyntaxException(String e) {
    super(e);

  }
}

}
