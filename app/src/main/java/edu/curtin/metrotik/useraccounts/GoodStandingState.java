package edu.curtin.metrotik.useraccounts;

import java.util.logging.Logger;

/**
 * Account state implementation for good standing. An account in good standing
 * allows the user to purchase tickets and ride the monorail without any
 * difficulties.
 *
 *
 * @author Kyer Potts
 */
public class GoodStandingState implements AccountState {
  private static final Logger LOGGER = Logger.getLogger(GoodStandingState.class.getName());

  private FundMutatorValidator mutatorValidator;

  public GoodStandingState(FundMutatorValidator fundMutatorValidator) {
    this.mutatorValidator = fundMutatorValidator;
    LOGGER.info(() -> "GoodStandingState initialised successfully");
  }

  /**
   * This method ensures that a credit can be added to the account. This method
   * does not make distinctions between types of credits, such as refunds or
   * direct deposits.
   */
  @Override
  public double creditFunds(double credit, double funds,
      UserAccount userAccount) {

    // Validate the credit parameter to prevent accounting errors.
    mutatorValidator.checkForInvalidFundMutator(credit);

    // Observers are notified of successful credit
    String eventMessage = "Credit of: " + credit + " successful.";
    userAccount.notifyAccountTransactionObservers(
        new AccountEvent(userAccount, eventMessage, true));
    // The credit is added to the funds and then returned in order to properly
    // update the variable stored within the encapsulating account class.
    LOGGER.info(() -> "Credit of: " + credit + " successful.");
    // The credit is added to the funds and then returned in order to properly
    // update the variable stored within the encapsulating account class.
    return funds + credit;
  }

  @Override
  public double debitFunds(double debit, double funds,
      UserAccount userAccount) {
    // Validate the debit parameter to prevent accounting errors.
    mutatorValidator.checkForInvalidFundMutator(debit);

    if (funds - debit < 0) {
      userAccount.setAccountState(userAccount.iNDEBT);
      LOGGER.info(() -> "Account changed from: " + getAccountState() +
          " to: " + userAccount.getState());

      // Whenever the account changes state, the observers must be notified.
      String eventMessage = "Account changed from: " + getAccountState() +
          " to: " + userAccount.getState();
      userAccount.notifyAccountStateObservers(
          new AccountEvent(userAccount, eventMessage, true));
    }

    // Observers are notified of successful debit
    String eventMessage = "Debit of: " + debit + " successful.";
    userAccount.notifyAccountTransactionObservers(
        new AccountEvent(userAccount, eventMessage, true));

    LOGGER.info(() -> "Debit of: " + debit + " successful.");
    return funds - debit;
  }

  @Override
  public void deactivateAccount(UserAccount userAccount) {
    userAccount.setAccountState(userAccount.dEACTIVATED);
    LOGGER.info(() -> "Account changed from: " + getAccountState() +
        " to: " + userAccount.getState());
    // Whenever the account changes state, the observers must be notified.
    String eventMessage = "Account changed from: " + getAccountState() +
        " to: " + userAccount.getState();
    userAccount.notifyAccountStateObservers(
        new AccountEvent(userAccount, eventMessage, true));
  }

  @Override
  public String getAccountState() {
    return "Good Standing";
  }
}
