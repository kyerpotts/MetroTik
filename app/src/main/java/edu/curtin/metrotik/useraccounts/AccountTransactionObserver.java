
package edu.curtin.metrotik.useraccounts;

/**
 * Observer pattern is used to notify any interested parties when a transaction
 * has failed due to being invalid. This could be because the account is
 * currently in the wrong state.
 *
 * @author Kyer Potts
 */
public interface AccountTransactionObserver {
  /**
   * Provides a contract for observers of the account to be notified when a
   * transaction fails.
   *
   * @param accountEvent the event object that provides additional details the
   *                     event.
   */
  public void accountTransactionEventOccured(AccountEvent accountEvent);
}
