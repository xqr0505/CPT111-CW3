package UI;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class UIUtils {

/**
 * Creates a button with the specified text, width, and action.
 *
 * @param text the text to display on the button
 * @param width the width of the button
 * @param action the action to be performed when the button is clicked
 * @return the created button
 */
public static Button createReturnButton(String text,double width, EventHandler<ActionEvent> action) {
  Button returnButton = new Button(text);
  returnButton.setPrefWidth(width);
  returnButton.setStyle("-fx-background-color: #a3c5f4;");
  returnButton.setOnMouseEntered(e -> returnButton.setStyle("-fx-background-color: #d0e1f9"));
  returnButton.setOnMouseExited(e -> returnButton.setStyle("-fx-background-color: #a3c5f4;"));
  returnButton.setOnAction(action);
  return returnButton;
}
}