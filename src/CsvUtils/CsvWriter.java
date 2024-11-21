package CsvUtils;

public class CsvWriter {

/**
 * Convert a string into a valid csv element token
 *
 * @param src string to be converted
 * @return A token
 * @throws NullPointerException if src is null
 */
public static String GenerateToken(String src) throws NullPointerException {
  if (src == null) {
    throw new NullPointerException("String cannot be null");
  }
  boolean       needEscape    = src.contains("\"") || src.contains(",") || src.contains("\n") || src.contains("\r");
  StringBuilder stringBuilder = new StringBuilder().append(needEscape ? '"' : "");
  for (int i = 0; ! src.isEmpty() && i < src.length(); i++) {
    char c = src.charAt(i);
    if (needEscape && c == '"') {
      stringBuilder.append("\"");
    }
    stringBuilder.append(c);
  }
  return stringBuilder.append(needEscape ? '"' : "")
                      .toString();
}

/**
 * Convert whole csv object into text
 *
 * @param table csv to be ported
 * @return formatted text
 */
public static String GenerateContent(Table table) {
  StringBuilder stringBuilder = new StringBuilder();
  for (var l : table.GetTable()) {
    for (int i = 0; i < l.length; i++) {
      stringBuilder.append(GenerateToken(l[i]));
      stringBuilder.append(i < l.length - 1 ? "," : "");
    }
    stringBuilder.append('\n');
  }
  return stringBuilder.toString();
}

}

