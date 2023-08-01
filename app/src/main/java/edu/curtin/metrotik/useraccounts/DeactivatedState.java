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
public class DeactivatedState implements AccountState {
  private static final Logger LOGGER = Logger.getLogger(DeactivatedState.class.getName());

  private FundMutatorValidator mutatorValidator;

  public DeactivatedState(FundMutatorValidator fundMutatorValidator) {
    this.mutatorValidator = fundMutatorValidator;

    LOGGER.info(() -> "DeactivatedState has been initialised successfully");
  }

  @Override
  public double creditFunds(double credit, double funds,
      UserAccount userAccount) {
    // Validate the credit parameter to prevent accounting errors.
    mutatorValidator.checkForInvalidFundMutator(credit);

    // Observers are notified of unsuccessful credit
    String eventMessage = "Credit of: " + credit + " unsuccessful. Account is deactivated.";
    userAccount.notifyAccountTransactionObservers(
        new AccountEvent(userAccount, eventMessage, false));
    LOGGER.info(() -> "Credit of: " + credit +
        " unsuccessful. Account is deactivated.");
    // The current funds are returned so as not to change the state of funds in
    // the account.
    return funds;
  }

  @Override
  public double debitFunds(double debit, double funds,
      UserAccount userAccount) {
    // Validate the credit parameter to prevent accounting errors.
    mutatorValidator.checkForInvalidFundMutator(debit);

    // funds can still be debited from a deactivated account, however the
    // account must not end up in arrears.
    if (funds - debit >= 0) {
      // Observers are notified of successful debit
      String eventMessage = "Debit of: " + debit + " successful.";
      userAccount.notifyAccountTransactionObservers(
          new AccountEvent(userAccount, eventMessage, true));

      LOGGER.info(() -> "Debit of: " + debit + " successful.");

      return funds - debit;
    }
    // Observers are notified of unsuccessful debit if there are not enough
    // funds left in the account.
    String eventMessage = "Debit of: " + debit +
        " unsuccessful. Deactivated account must only use remaining credit. It cannot enter arrears.";
    userAccount.notifyAccountTransactionObservers(
        new AccountEvent(userAccount, eventMessage, false));

    LOGGER.info(
        () -> "Debit of: " + debit +
            " unsuccessful. Deactivated account must only use remaining credit. It cannot enter arrears.");

    return funds;
  }

  @Override
  public void deactivateAccount(UserAccount userAccount) {
    // State change failures must notify observers
    String eventMessage = "Account has already been deactivated.";
    userAccount.notifyAccountStateObservers(
        new AccountEvent(userAccount, eventMessage, false));

    LOGGER.info(() -> "Account has already been deactivated.");
  }

  @Override
  public String getAccountState() {
    return "Deactivated";
  }
}
