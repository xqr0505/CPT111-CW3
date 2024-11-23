import UI.MainPage;
import core.Logical;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main class to start the application.
 */
public class Main extends Application {
public static void main(String[] args) {
  Logical logical = Logical.getInstance()
                           .RegisterStartupFunctionHook(Logical::LoadRequiredInfo)
                           .RegisterAfterFunctionHook(Logical::SaveInfo);

  // Load required information
  if (!logical.LoadRequiredInfo()) {
    System.err.println("Failed to load required information. Exiting.");
    System.exit(1);
  }

  // Start JavaFX main interface
  new Thread(() -> Application.launch(Main.class, args)).start();
}

@Override
public void start(Stage primaryStage) {
  try {
    MainPage mainPage = new MainPage();
    mainPage.start(primaryStage);
  } catch (Exception e) {
    e.printStackTrace();
    System.err.println("Application encountered an error and will now exit.");
  } finally {
    // Save information
    Logical logical = Logical.getInstance();
    if (!logical.AfterFunctionCall().SaveInfo()) {
      System.err.println("Failed to save information properly.");
    }
  }
}
}