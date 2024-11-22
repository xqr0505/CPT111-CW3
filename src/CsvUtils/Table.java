package CsvUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Table {
protected final LinkedList<ArrayList<String>> m_table_    = new LinkedList<>(); // table
protected       ArrayList<String>             m_tableTitle_;  // title
protected       boolean                       m_hasTitle_ = false;              // weather the table has title
protected       int                           m_rows_     = 0;                  // rows
protected       int                           m_columns_  = 0;                  // cols
private final   List<String[]>                table;

public Table() {
  table = new ArrayList<>();
}

/**
 * Get total rows of the table
 *
 * @return Rows
 */
public int GetRows() {
  return m_rows_;
}

/**
 * Get total columns of the table
 *
 * @return cols
 */
public int GetCols() {
  return m_columns_;
}

/**
 * Get contents of the table, in array
 *
 * @return Contents of the table
 */
public String[][] GetTable() {
  String[][] ret = new String[m_rows_][m_columns_];
  for (int i = 0; i < m_rows_; i++) {
    for (int j = 0; j < m_columns_; j++) {
      if (i < m_table_.size() && j < m_table_.get(i).size()) {
        ret[i][j] = m_table_.get(i).get(j);
      } else {
        ret[i][j] = ""; // or any default value
      }
    }
  }
  return ret;
}

/**
 * Get Title for the table
 *
 * @return table title, if the table has not title, null
 */
public String[] GetTitle() {
  String[] ret;
  if (m_hasTitle_) {
    ret = m_tableTitle_.toArray(new String[0]);
  } else {
    ret = null;
  }
  return ret;
}

/**
 * Get element that lies on `row` row and `col` column
 *
 * @param row row of the element
 * @param col column of the element
 * @return element
 * @throws IndexOutOfBoundsException if the row or col is out of bound
 */
public String GetElement(int row, int col) throws IndexOutOfBoundsException {
  if (row >= m_rows_) {
    throw new IndexOutOfBoundsException(String.format("Totally have %d rows, the argument provided %d", m_rows_, row));
  }
  if (col >= m_columns_) {
    throw new IndexOutOfBoundsException(
        String.format("Totally have %d columns, the argument provided %d", m_columns_, col));
  }
  return m_table_.get(row).get(col);
}

/**
 * Set the value element that lies on `row` row and `col` column
 *
 * @param row row of the element
 * @param col column of the element
 * @param val new value for the element
 * @return self, for chain-call
 * @throws IndexOutOfBoundsException if the row or col is out of bound
 */
public Table SetElement(int row, int col, String val) throws IndexOutOfBoundsException {
  if (row >= m_rows_) {
    throw new IndexOutOfBoundsException(String.format("Totally have %d rows, the argument provided %d", m_rows_, row));
  }
  if (col >= m_columns_) {
    throw new IndexOutOfBoundsException(
        String.format("Totally have %d columns, the argument provided %d", m_columns_, col));
  }
  m_table_.get(row).set(col, val);
  return this;
}

/**
 * Set the value element that lies on `row` row and `col` column
 *
 * @param row    row of the element
 * @param col    column of the element
 * @param val    new value for the element
 * @param expand weather to expand cols and rows to match row and col provided
 * @return self, for chain-call
 * @throws IndexOutOfBoundsException if the row or col is out of bound
 */
public Table SetElement(int row, int col, String val, boolean expand) throws IndexOutOfBoundsException {
  if (! expand) {
    return SetElement(row, col, val);
  }
  while (row > m_rows_) {
    InsertLine();
  }
  if (col >= m_columns_) {
    m_columns_ = col + 1;
    Sync();
  }
  m_table_.get(row)
          .set(col, val);
  return this;
}

/**
 * Set the value element that lies on `row` row and `col` column
 *
 * @param row row of the element
 * @param val new value for the element
 * @return self, for chain-call
 * @throws IndexOutOfBoundsException if the row or col is out of bound
 */
public Table InsertElement(int row, String val) throws IndexOutOfBoundsException {
  while (row >= m_rows_) {
    InsertLine();
  }
  if (m_table_.get(row).size() + 1 > m_columns_) {
    throw new IndexOutOfBoundsException("Cannot add more Item into the table");
  }
  m_table_.get(row).add(val);
  return this;
}
/**
 * Set the value element that lies on `row' row and `col' column
 *
 * @param row    row of the element
 * @param val    new value for the element
 * @param expand weather to expand the table
 * @return self, for chain-call
 * @throws IndexOutOfBoundsException if the row or col is out of bound
 */
public Table InsertElement(int row, String val, boolean expand) throws IndexOutOfBoundsException {
  if (! expand) {
    return InsertElement(row, val);
  }
  while (row > m_rows_) {
    InsertLine();
  }
  if (m_table_.get(row)
              .size() + 1 >= m_columns_) {
    m_columns_ = m_table_.get(row)
                         .size() + 1;
  }
  m_table_.get(row)
          .add(val);
  return this;
}

/**
 * Insert an empty line
 *
 * @return self, for chain-call
 */
public Table InsertLine() {
  m_table_.add(new ArrayList<>(m_columns_));
  m_rows_++;
  return this;
}

public void InsertLine(String[] fields) {
  table.add(fields);
}
/**
 * Insert a new line
 *
 * @param line new contents
 * @return self, for chain-call
 * @throws IllegalArgumentException if the line is not cols long
 */
public Table InsertLine(List<String> line) throws IllegalArgumentException {
  if (m_columns_ == 0) {
    // 动态设置列数为插入行的长度
    m_columns_ = line.size();
  } else if (line.size() != m_columns_) {
    throw new IllegalArgumentException(String.format("Inserted line is not length of %s", m_columns_));
  }
  m_table_.add(new ArrayList<>(line));
  m_rows_++;
  return this;
}


/**
 * Insert an emtpy line after `index'
 *
 * @param index Where to insert a new line
 * @return self, for chain-call
 */
public Table InsertLine(int index) {
  m_table_.add(index, new ArrayList<>(m_columns_));
  m_rows_++;
  return this;
}

/**
 * Insert a new line just after `index'
 *
 * @param index Where to insert a new line
 * @param line  new contents
 * @return self, for chain-call
 * @throws IllegalArgumentException if the line is not cols long
 */
public Table InsertLine(int index, List<String> line) throws IllegalArgumentException {
  if (line.size() != m_columns_) {
    throw new IllegalArgumentException(String.format("Inserted line is not length of %s", m_columns_));
  }
  m_table_.add(index, new ArrayList<>(line));
  m_rows_++;
  return this;
}

/**
 * Remove Last Line
 *
 * @return self, for chain-call
 * @throws IndexOutOfBoundsException if the table is empty
 */
public Table RemoveLine() throws IndexOutOfBoundsException {
  if (m_rows_ <= 0 || m_table_.size() <= 0) {
    throw new IndexOutOfBoundsException("The Table is empty");
  }
  m_table_.removeLast();
  m_rows_--;
  return this;
}

/**
 * Remove `index' line
 *
 * @param index where to remove a line
 * @return self, for chain-call
 * @throws IndexOutOfBoundsException if the table is empty or index is larger than total rows
 */
public Table RemoveLine(int index) throws IndexOutOfBoundsException {
  if (m_rows_ <= 0 || m_table_.size() <= 0 || index >= m_rows_) {
    throw new IndexOutOfBoundsException(
        String.format("The Table is empty or index %d is out of bound, the total number of rows is %d", index,
                      m_rows_));
  }
  m_table_.remove(index);
  m_rows_--;
  return this;
}

/**
 * Set Title of the Table
 *
 * @param title new title
 * @return self, for chain-call
 */
public Table SetTitle(List<String> title) {
  m_hasTitle_   = true;
  m_tableTitle_ = new ArrayList<>(title);
  m_columns_    = title.size();
  return this;
}

/**
 * Set Title of the Table
 *
 * @param title new title
 * @return self, for chain-call
 */
public Table SetTitle(String... title) {
  m_hasTitle_   = true;
  m_tableTitle_ = new ArrayList<>(Arrays.stream(title)
                                        .toList());
  m_columns_    = m_tableTitle_.size();
  return this;
}

/**
 * Update table according to rows and cols
 *
 * @return self, for chain-call
 */
public Table Sync() {

  while (m_table_.size() < m_rows_) {
    m_table_.add(new ArrayList<>(m_columns_));
  }

  for (int i = 0; i < m_rows_; i++) {
    while (m_table_.get(i)
                   .size() < m_columns_) {
      m_table_.get(i)
              .add("");
    }
  }
  return this;
}

/**
 * Resize the table
 *
 * @param rows new rows
 * @param cols new cols
 * @return self, for chain-call
 */
public Table Resize(int rows, int cols) {
  m_rows_    = rows;
  m_columns_ = cols;
  return Sync();
}

/**
 * Resize the columns
 *
 * @param cols new cols
 * @return self, for chain-call
 */
public Table ReCols(int cols) {
  m_columns_ = cols;
  return Sync();

}

/**
 * Resize the rows
 *
 * @param rows new rows, padding latter
 * @return self, for chain-call
 */
public Table ReRows(int rows) {
  m_rows_ = rows;
  return Sync();

}

}


