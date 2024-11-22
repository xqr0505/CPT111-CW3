package core;

import QuestionManagement.QuestionManager;
import UserManagement.UserManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Main class for logical operations.
 */
public class Logical {

private static final Logical s_instance_ = new Logical();

private final UserManager m_users_ = new UserManager();
private final QuestionManager m_questions_ = new QuestionManager();
private final List<CallBackHook.Hook> m_prepareHooks_ = new ArrayList<>();
private final List<CallBackHook.Hook> m_quitHooks_ = new ArrayList<>();

private Logical() {
}

/**
 * Get the singleton instance of Logical.
 *
 * @return the singleton instance
 */
public static Logical getInstance() {
  return s_instance_;
}

/**
 * Get the UserManager instance.
 *
 * @return the UserManager instance
 */
public UserManager getUserManager() {
  return m_users_;
}

/**
 * Get the QuestionManager instance.
 *
 * @return the QuestionManager instance
 */
public QuestionManager getQuestionManager() {
  return m_questions_;
}

/**
 * Register a startup function hook.
 *
 * @param hook the hook to register
 * @return the Logical instance
 */
public Logical RegisterStartupFunctionHook(CallBackHook.Hook hook) {
  m_prepareHooks_.add(hook);
  return this;
}

/**
 * Register an after function hook.
 *
 * @param hook the hook to register
 * @return the Logical instance
 */
public Logical RegisterAfterFunctionHook(CallBackHook.Hook hook) {
  m_quitHooks_.add(hook);
  return this;
}

/**
 * Load required information.
 *
 * @return true if successful, false otherwise
 */
public boolean LoadRequiredInfo() {
  boolean success = true;
  try {
    m_users_.LoadUserInfo("resources/u.csv", "resources/s.csv");
    m_questions_.LoadQuestions("resources/questionsBank");
  } catch (IOException e) {
    Logger.getLogger("global")
          .info(e.getMessage());
    success = false;
  }
  return success;
}

/**
 * Save information.
 *
 * @return true if successful, false otherwise
 */
public boolean SaveInfo() {
  boolean success = true;
  try {
    m_users_.SaveUserInfo("resources/u.csv", "resources/s.csv");
  } catch (IOException e) {
    Logger.getLogger("global")
          .info(e.getMessage());
    success = false;
  }
  return success;
}

/**
 * Call startup functions.
 *
 * @return the Logical instance
 */
public Logical StartupFunctionCall() {
  for (var fun : m_prepareHooks_) {
    if (!fun.Apply(this)) {
      Logger.getLogger("global")
            .info("1 Prepare task failed.");
    }
  }
  return this;
}

/**
 * Call after functions.
 *
 * @return the Logical instance
 */
public Logical AfterFunctionCall() {
  for (var fun : m_quitHooks_) {
    if (!fun.Apply(this)) {
      Logger.getLogger("global")
            .info("1 Quit task failed.");
    }
  }
  return this;
}
}