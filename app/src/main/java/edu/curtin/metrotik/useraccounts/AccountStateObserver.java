package edu.curtin.metrotik.useraccounts;

/**
 * Observer pattern is used to observe changes in the user account state, this
 * will enable the UI to react to changes within the account state and display
 * relevant information to the user.
 *
 * @author Kyer Potts
 */

public interface AccountStateObserver {
  /**
   * Provides a contract for observers of account state to recieve notification
   * of state changes.
   *
   * @param accountEvent the event object that provides additional details the
   *                     event.
   */
  public void accountStateUpdated(AccountEvent accountEvent);
}
