package CsvUtils;

import java.util.ArrayList;
import java.util.List;

public class CsvReader {

/**
 * Get Next Token From source
 *
 * @param src Source String to be processed
 * @return Next token, emtpy if reaches end of string
 * @throws Exceptions.IllegalSyntaxException If the source is not in a valid csv format
 */
public static String NextToken(String src) throws Exceptions.IllegalSyntaxException {
  StringBuilder stringBuilder      = new StringBuilder();
  boolean       startWithQuotation = false;
  boolean       hasQuotation       = false;

  loop:
  for (int currentPosition = 0; ! src.isEmpty() && currentPosition < src.length(); currentPosition++) {
    Character c = src.charAt(currentPosition);
    switch (c) {
      case ',': case '\n': case '\r':
        if (0 == currentPosition) {
          stringBuilder.append(c);
        }
        if (! startWithQuotation || hasQuotation) {
          break loop;
        }
      case '"':
        if (0 == currentPosition && '"' == c) {
          startWithQuotation = true;
        }
        hasQuotation = ('"' == c) && (! (currentPosition == 0));
      case ' ': default:
        if (startWithQuotation && hasQuotation && c != '"') {
          throw new Exceptions.IllegalSyntaxException(
              "A CSV element must end with quotation if the element is start with quotation");
        }
        stringBuilder.append(c);
    }
  }
  return stringBuilder.toString();
}


/**
 * Parse Token to Final String
 *
 * @param token Token to be parsed
 * @return parsed token
 */
public static String ParseToken(String token) throws Exceptions.IllegalSyntaxException {
  StringBuilder stringBuilder      = new StringBuilder();
  boolean       startWithQuotation = false;
  boolean       hasQuotation       = false;

  loop:
  for (int currentPosition = 0; ! token.isEmpty() && currentPosition < token.length(); currentPosition++) {
    Character c = token.charAt(currentPosition);
    switch (c) {
      case '"':
        if (currentPosition == 0) {
          startWithQuotation = true;
          hasQuotation       = false;
          continue;
        }
        if (startWithQuotation) {
          hasQuotation = ! hasQuotation;
          if (hasQuotation) {
            continue;
          }
        }
        stringBuilder.append(c);
        break;
      case ',': case '\r': case '\n':
        if (currentPosition == 0) {
          stringBuilder.append(c);
          break loop;
        }
        if (! startWithQuotation || hasQuotation) {
          // Using short-circuit logic to present (!startWithQuotation) || (startWithQuotation && hasQuotation)
          break loop; // End the current token parsing
        }
        stringBuilder.append(c);
        break;
      case ' ': default:
        if (startWithQuotation && hasQuotation) {
          throw new Exceptions.IllegalSyntaxException(
              "A CSV element must end with quotation if the element is start with quotation");
        }
        stringBuilder.append(c);
        break;
    }
  }
  if (startWithQuotation && ! hasQuotation) {
    throw new Exceptions.IllegalSyntaxException(
        "A CSV element must end with quotation if the element is start with quotation");
  }
  return stringBuilder.toString();
}

/**
 * Parse Source CSV String into Table object
 *
 * @param src Source String to be processed
 * @return a Table object to be manipulated
 */
public static Table ConstructTableFromCSV(String src) {
  Table   ret         = new Table();
  boolean lastSplitor = false;
  String  token;
  char    c           = '\0';

  ret.InsertLine();
  for (int rows = 0, cols = 0; ! (token = NextToken(src)).isEmpty(); ) {
    c = token.charAt(0);
    switch (c) {
      case '\n': case '\r':
        ret.InsertLine();
        cols = 0;
        rows++;
      case ',':
        if (lastSplitor) {
          ret.InsertElement(rows - (c != ',' ? 1 : 0), "", true);
          cols++;
        }
        lastSplitor = true;
        break;
      default:
        ret.InsertElement(rows, ParseToken(token), true);
        cols++;
        lastSplitor = false;
        break;
    }
    src = src.substring(token.length());
  }

  // If source is end with return
  // Then there will be an empty line
  if (c == '\n' || c == '\r') {
    ret.RemoveLine();
  }
  return ret;
}

/**
 * Parse Source CSV String into Table object
 *
 * @param src      Source String to be processed
 * @param hastitle if the csv contains title information
 * @return a Table object to be manipulated
 */
public static Table ConstructTableFromCSV(String src, boolean hastitle) {
  Table   ret         = new Table();
  boolean lastSplitor = false;
  String  token;
  char    c           = '\0';

  ret.InsertLine();

  List<String> title = new ArrayList<>();
  loop:
  for (int cols = 0; hastitle && ! (token = NextToken(src)).isEmpty(); ) {
    c = token.charAt(0);
    switch (c) {
      case '\n': case '\r':
        ret.SetTitle(title);
        break loop;
      case ',':
        if (lastSplitor) {
          title.add("");
          cols++;
        }
        lastSplitor = true;
        break;
      default:
        title.add(ParseToken(token));
        cols++;
        lastSplitor = false;
        break;
    }
    src = src.substring(token.length());
  }

  for (int rows = 0, cols = 0; ! (token = NextToken(src)).isEmpty(); ) {
    c = token.charAt(0);
    switch (c) {
      case '\n': case '\r':
        ret.InsertLine();
        cols = 0;
        rows++;
      case ',':
        if (lastSplitor) {
          ret.SetElement(rows - (c != ',' ? 1 : 0), cols, "");
          cols++;
        }
        lastSplitor = true;
        break;
      default:
        ret.SetElement(rows, cols, ParseToken(token));
        cols++;
        lastSplitor = false;
        break;
    }
    src = src.substring(token.length());
  }

  // If source is end with return
  // Then there will be an empty line
  if (c == '\n' || c == '\r') {
    ret.RemoveLine();
  }
  return ret;
}

}

