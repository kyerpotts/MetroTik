package edu.curtin.metrotik.useraccounts;

import edu.curtin.metrotik.ticketing.Ticket;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Contains all of the basic information and behaviour for a user account. This
 * class uses the state pattern to alter the behaviours of the account depending
 * on it's status.
 *
 * @author Kyer Potts
 */
public class UserAccount {
  private static final Logger LOGGER = Logger.getLogger(UserAccount.class.getName());
  // References the current state that the account exists in
  private AccountState currentState;

  // Convention normally dictates that constants are all caps, however the pmd
  // ruleset provided requires a lower case letter at the start of the variable
  // name.
  public final GoodStandingState gOODSTANDING;
  public final InDebtState iNDEBT;
  public final DeactivatedState dEACTIVATED;

  // Keeps a record of all tickets purchased by this account.
  private Set<Ticket> ticketHistory;

  // Contains all of the observers registered to this particular account
  private Set<AccountStateObserver> accountStateObservers;
  private Set<AccountTransactionObserver> accountTransactionObservers;

  // Private member fields
  private int accountID;
  private String firstName;
  private String lastName;
  private String email;
  private double funds;

  public UserAccount(int accountID, String firstName, String lastName,
      String email) {
    // FindMutatorValidator is dependency injected into the state objects at
    // this level because it is the lowest encapsulating element that does not
    // use the validator object. I find that the encapsulating level is the best
    // place to instantiate dependency injected objects, as it does not
    // interfere with testing and does not require everything to be instantiated
    // from the highest order objects.
    FundMutatorValidator mutatorValidator = new FundMutatorValidator();
    gOODSTANDING = new GoodStandingState(mutatorValidator);
    iNDEBT = new InDebtState(mutatorValidator);
    dEACTIVATED = new DeactivatedState(mutatorValidator);

    this.accountID = accountID;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;

    // New accounts must always be initialised to 0, this will change if
    // persistence is enabled.
    funds = 0.0;

    ticketHistory = new LinkedHashSet<>();

    accountStateObservers = new HashSet<>();
    accountTransactionObservers = new HashSet<>();

    // Account should always be initialised in GoodStanding. Accounts will only
    // move to other standings depending on external events.
    currentState = gOODSTANDING;

    LOGGER.info(() -> "New account created: " + this.toString());
  }

  // Getters for private fields.
  public int getAccountID() {
    return accountID;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getEmail() {
    return email;
  }

  public double getFunds() {
    return funds;
  }

  public String getState() {
    return currentState.getAccountState();
  }

  /**
   * Method for adding observers of the accounts state
   *
   * @param accountStateObserver the new observer to be added to the set of
   *                             AccountStateObservers
   */
  public void subscribeAccountStateObserver(AccountStateObserver accountStateObserver) {
    this.accountStateObservers.add(accountStateObserver);
    LOGGER.info(() -> "New observer added to account: " +
        accountStateObserver.toString());
  }

  /**
   * Method for adding observers of transaction events
   *
   * @param accountTransactionObserver the new observer to be added to the set
   *                                   of AccountTransactionObservers
   */
  public void subscribeAccountTransactionObserver(
      AccountTransactionObserver accountTransactionObserver) {
    this.accountTransactionObservers.add(accountTransactionObserver);
    LOGGER.info(() -> "New observer added to account: " +
        accountTransactionObserver.toString());
  }

  /**
   * Method for crediting funds to the account. Leverages the state pattern
   * implemented with this class to define behaviour.
   *
   * @param credit the amount to be credited to the account
   */
  public void creditFunds(double credit) {
    funds = currentState.creditFunds(credit, funds, this);
  }

  /**
   * Method for debiting funds to the account. Leverages the state pattern
   * implemented with this class to define behaviour.
   *
   * @param debit the amount to be debited from the account
   */
  public void debitFunds(double debit) {
    funds = currentState.debitFunds(debit, funds, this);
  }

  /**
   * Method for deactivating the account. Leverages the state pattern
   * implemented with this class to define behaviour.
   */
  public void deactivateAccount() {
    currentState.deactivateAccount(this);
  }

  /**
   * Adds a ticket to the ticket history set. Tickets must be unique, as they
   * will form a set.
   *
   * @param ticket the ticket to be added to the ticket history list
   */
  public void addTicketToTicketHistory(Ticket ticket) {
    ticketHistory.add(ticket);
    LOGGER.info(() -> "Ticket added to account: " + ticket.toString());
  }

  /**
   * Method allows the various account state classes leveraging the state
   * pattern to alter the state of the encapsulating class.
   *
   * @param newState the new state that the encapsulating class will exist as if
   *                 the method successfully transitions state
   */
  public void setAccountState(AccountState newState) {
    this.currentState = newState;
  }

  /**
   * Notifies each of the observers of a UserAccount object whenever there is a
   * state change. This method is called by the various state objects to notify
   * observers when a state change is successful.
   *
   * @param newEvent the event which is to be sent to all observers.
   */
  protected void notifyAccountStateObservers(AccountEvent newEvent) {
    for (AccountStateObserver obs : accountStateObservers) {
      obs.accountStateUpdated(newEvent);
    }
  }

  /**
   * Notifies each of the observers of a UserAccount object whenever there is a
   * transaction event. This method is called by each of the various states to
   * notify when a transaction is successful, unsuccessful and why.
   *
   * @param newEvent the event which is to be sent to all observers.
   */
  protected void notifyAccountTransactionObservers(AccountEvent newEvent) {
    for (AccountTransactionObserver obs : accountTransactionObservers) {
      obs.accountTransactionEventOccured(newEvent);
    }
  }
}
