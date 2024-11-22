import UI.MainPage;
import core.Logical;

/**
 * Main class to start the application.
 */
public class Main {
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
  try {
    MainPage.main(args);
  } catch (Exception e) {
    e.printStackTrace();
    System.err.println("Application encountered an error and will now exit.");
  } finally {
    // Save information
    if (!logical.AfterFunctionCall().SaveInfo()) {
      System.err.println("Failed to save information properly.");
    }
  }
}
}