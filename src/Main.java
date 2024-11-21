import UI.MainPage;
import core.Logical;

public class Main {
public static void main(String[] args) {
  Logical logical = Logical.getInstance()
                           .RegisterStartupFunctionHook(Logical::LoadRequiredInfo)
                           .RegisterAfterFunctionHook(Logical::SaveInfo);

  // 加载所需信息
  if (!logical.LoadRequiredInfo()) {
    System.err.println("Failed to load required information. Exiting.");
    System.exit(1);
  }

  // 启动 JavaFX 主界面
  try {
    MainPage.main(args);
  } catch (Exception e) {
    e.printStackTrace();
    System.err.println("Application encountered an error and will now exit.");
  } finally {
    // 保存信息
    if (!logical.AfterFunctionCall().SaveInfo()) {
      System.err.println("Failed to save information properly.");
    }
  }
}
}