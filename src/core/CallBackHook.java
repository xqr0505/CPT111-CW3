package core;
/**
 * Interface representing a callback hook.
 */
public class CallBackHook {

public interface Hook {

  /**
   * Apply the hook to the given Logical object.
   *
   * @param o the Logical object to apply the hook to
   * @return true if the hook was successfully applied, false otherwise
   */
  boolean Apply(Logical o);
}
}
