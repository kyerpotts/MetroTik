package edu.curtin.metrotik.useraccounts;

/**
 * State pattern interface provides a contract to handle the various behaviours
 * associated with all states the UserAccount object may have.
 *
 * @author Kyer Potts
 */
public interface AccountState {

  /**
   * Provides a contract for the state pattern to handle credits to a
   * UserAccounts funds
   *
   * @param credit      the amount to be credited to the account
   * @param funds       the current funds held by the account
   * @param userAccount required in order to update any observers and mutate the
   *                    account state if there are any changes
   * @return the result of the mutation of the funds variable
   */
  public double creditFunds(double credit, double funds,
      UserAccount userAccount)
      throws AccountFundsException;

  /**
   * Provides a contract for the state pattern to handle debits to a
   * UserAccounts funds
   *
   * @param debit       the amount to be debited to the account
   * @param funds       the current funds held by the account
   * @param userAccount required in order to update any observers and mutate the
   *                    account state if there are any changes
   * @return the result of the mutation of the funds variable
   */
  public double debitFunds(double debit, double funds, UserAccount userAccount);

  /**
   * Provides a contract for deactivating a UserAccount
   *
   * @param userAccount required in order to update any observers and mutate the
   *                    account state if there are any changes
   */
  public void deactivateAccount(UserAccount userAccount);

  /**
   * Provides a contract for the retrieval of the current state of the account
   * as a String.
   *
   * @return the current state of the account as a string.
   */
  public String getAccountState();
}
