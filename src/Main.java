
import UI.MainPage;
import core.Logical;

public class Main {
public static void main(String[] args) {
  var logical = Logical.getInstance()
                       .RegisterStartupFunctionHook(Logical::LoadRequiredInfo)
                       .RegisterAfterFunctionHook(Logical::SaveInfo)
                       .StartupFunctionCall();

  MainPage.main(args);

  logical.AfterFunctionCall();
}
}
