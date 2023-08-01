package edu.curtin.metrotik.useraccounts;

import java.util.logging.Logger;

/**
 * Account state implementation for in debt. An account in debt standing will
 * not allow for further debits to occur. The account must be returned to good
 * standing first before making further debits
 *
 *
 * @author Kyer Potts
 */
public class InDebtState implements AccountState {
  private static final Logger LOGGER = Logger.getLogger(InDebtState.class.getName());

  private FundMutatorValidator mutatorValidator;

  public InDebtState(FundMutatorValidator fundMutatorValidator) {
    this.mutatorValidator = fundMutatorValidator;
  }

  @Override
  public double creditFunds(double credit, double funds,
      UserAccount userAccount) {

    // Validate the credit parameter to prevent accounting errors.
    mutatorValidator.checkForInvalidFundMutator(credit);

    // If the account is returned to a positive value, then the business logic
    // dictates that it should be returned to a good standing state.
    if (funds + credit > 0) {
      userAccount.setAccountState(userAccount.gOODSTANDING);
      LOGGER.info(() -> "Account changed from: " + getAccountState() +
          " to: " + userAccount.getState());

      // Whenever the account changes state, the observers must be notified.
      String eventMessage = "Account changed from: " + getAccountState() +
          " to: " + userAccount.getState();
      userAccount.notifyAccountStateObservers(
          new AccountEvent(userAccount, eventMessage, true));
    }

    // Observers are notified of successful credit
    String eventMessage = "Credit of: " + credit + " successful.";
    userAccount.notifyAccountTransactionObservers(
        new AccountEvent(userAccount, eventMessage, true));
    // The credit is added to the funds and then returned in order to properly
    // update the variable stored within the encapsulating account class.
    LOGGER.info(() -> "Credit of: " + credit + " successful.");
    return funds + credit;
  }

  @Override
  public double debitFunds(double debit, double funds,
      UserAccount userAccount) {
    mutatorValidator.checkForInvalidFundMutator(debit);

    LOGGER.info(
        () -> "Debit of: " + debit +
            " unsuccessful. Cannot deduct funds from account state: " +
            getAccountState());

    // Observers are notified of unsuccessful credit
    String eventMessage = "Debit of :" + debit +
        " unsuccessful. Cannot deduct funds from account state: " +
        getAccountState();
    userAccount.notifyAccountTransactionObservers(
        new AccountEvent(userAccount, eventMessage, false));

    return funds;
  }

  @Override
  public void deactivateAccount(UserAccount userAccount) {
    LOGGER.info(
        () -> "Account in state: " + getAccountState() +
            " cannot be deactivated. The account must be returned to good standing before being deactivated.");
    // Whenever a state change is requested and it is unsuccessful, observers
    // should be notified.
    String eventMessage = "Accounts in state: " + getAccountState() +
        " cannot be deactivated. The account must be returned to good standing before being deactivated.";
    userAccount.notifyAccountStateObservers(
        new AccountEvent(userAccount, eventMessage, false));
  }

  @Override
  public String getAccountState() {
    return "In Debt";
  }
}
