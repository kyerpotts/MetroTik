package edu.curtin.metrotik.controller;

import edu.curtin.metrotik.monorailsimulator.MonorailSimulator;
import edu.curtin.metrotik.ticketing.Ticket;
import edu.curtin.metrotik.ticketing.TicketFactory;
import edu.curtin.metrotik.ticketingsystem.TicketManager;
import edu.curtin.metrotik.useraccounts.UserAccount;
import edu.curtin.metrotik.userinterface.NotificationHandler;
import java.time.LocalDateTime;
import java.util.logging.Logger;

/**
 * This class is responsible for managing the control flow of the program and
 * passing information throughout the various systems implemented.
 *
 * @Author Kyer Potts
 */
public class MainController {
  private static final Logger LOGGER = Logger.getLogger(MainController.class.getName());
  // For some reason the pmd ruleset requires that I prefix constant variables
  // names with a lower case letter. Convention usually requires that constants
  // are in full upper case
  private final double rIMEDTICKETPRICE = 10.00;
  private final double zONETICKETPRICE = 5.00;

  private TicketFactory ticketFactory;
  private TicketManager ticketManager;
  private NotificationHandler notificationHandler;
  private UserAccount userAccount;
  private MonorailSimulator monorailSimulator;

  public MainController(TicketFactory ticketFactory,
      TicketManager ticketManager,
      NotificationHandler notificationHandler,
      UserAccount userAccount,
      MonorailSimulator monorailSimulator) {
    this.ticketFactory = ticketFactory;
    this.ticketManager = ticketManager;
    this.notificationHandler = notificationHandler;
    this.userAccount = userAccount;
    this.monorailSimulator = monorailSimulator;
    LOGGER.info(() -> "MainController has been instantiated successfully.");
  }

  /**
   * This method is responsible for handling a request to travel a zone.
   *
   * @param zones the number of zones the user wishes to travel.
   * @return true if the user successfully travels a zone, false otherwise.
   */
  public boolean requestZoneTravel(int zones) {
    // Tickets must first be checked to see if they are valid for the requested
    // zone travel.
    if (ticketManager.canRideForRequestedZones(zones)) {
      LOGGER.info(() -> "User has a valid ticket for the requested zones.");
      // If there is a valid ticket, then the user can travel the requested
      // zones. The ticketManager object only registers a single zone of travel
      // at a time, so a loop must be used in order to register the travel.
      for (int i = 0; i < zones; i++) {
        ticketManager.travelRequestedZones();
        monorailSimulator.travelZone();

        if (LOGGER.isLoggable(java.util.logging.Level.INFO)) {
          LOGGER.log(java.util.logging.Level.INFO,
              "User has travelled zone {0} of " + zones, i + 1);
        }
      }
      LOGGER.info(() -> "User has successfully travelled the requested zones.");
      return true;
    } else {
      LOGGER.info(
          () -> "User does not have a valid ticket for the requested zones.");
      return false;
    }
  }

  /**
   * This method is responsible for purchasing a timed ticket.
   *
   * @param validFrom the date and time from which the ticket will be valid.
   * @return true if the ticket was successfully purchased, false otherwise.
   */
  public boolean requestTimedTicketPurchase(LocalDateTime validFrom) {
    // The transaction must be successful before a ticket can be instantiated.
    if (transact(rIMEDTICKETPRICE)) {
      ticketFactory.primeTicketFactory(validFrom);
      Ticket newTicket = ticketFactory.createTicket();
      // The notification handler must be registered as an observer of the
      // ticket before it is added to the ticket manager.
      newTicket.addTicketObserver(notificationHandler);
      ticketManager.addTimedTicket(newTicket, userAccount);

      LOGGER.info(() -> "User has successfully purchased a timed ticket.");
      return true;
    }
    LOGGER.info(() -> "User has failed to purchase a timed ticket.");
    return false;
  }

  /**
   * This method is responsible for purchasing a zone ticket.
   *
   * @param zones the number of zones to be purchased.
   * @return true if the ticket was successfully purchased, false otherwise.
   */
  public boolean requestZoneTicketPurchase(int zones) {
    // The transaction must be successful before a ticket can be instantiated.
    if (transact(zONETICKETPRICE * zones)) {
      ticketFactory.primeTicketFactory(zones);
      Ticket newTicket = ticketFactory.createTicket();
      // The notification handler must be registered as an observer of the
      // ticket before it is added to the ticket manager.
      newTicket.addTicketObserver(notificationHandler);
      ticketManager.addZoneTicket(newTicket, userAccount);

      LOGGER.info(() -> "User has successfully purchased a zone ticket.");
      return true;
    }
    LOGGER.info(() -> "User has failed to purchase a zone ticket.");
    return false;
  }

  /**
   * This method is responsible for deactivating the user account.
   */
  public void deactivateAccount() {
    userAccount.deactivateAccount();
    LOGGER.info(() -> "User account has been deactivated.");
  }

  /**
   * This method is responsible for adding funds to the user account.
   */
  public void addFunds(double funds) {
    userAccount.creditFunds(funds);
    LOGGER.info(() -> "User account has been credited with " + funds + ".");
  }

  /**
   * This method is responsible for returning the user account balance.
   *
   * @return the user account balance.
   */
  public String getAccountBalance() {
    return String.valueOf(userAccount.getFunds());
  }

  /**
   * This method is responsible for returning the user account status.
   *
   * @return the user account status.
   */
  public String getAccountStatus() {
    return userAccount.getState();
  }

  /**
   * This method is responsible for returning the current station for the
   * purposes of displaying it to the UI
   *
   * @return the current station.
   */
  public String getCurrentStation() {
    return String.valueOf(monorailSimulator.getCurrentZone());
  }

  /**
   * This method is responsible for ensuring transaction are valid before
   * instantiating a new ticket.
   *
   * @param amount the amount of funds to be debited from the user account.
   *
   * @return true if the transaction is successful, false otherwise.
   */
  private boolean transact(double amount) {
    double currentfunds = userAccount.getFunds();
    userAccount.debitFunds(amount);
    if (userAccount.getFunds() == currentfunds - amount) {
      LOGGER.info(() -> "User account has been debited with " + amount + ".");
      return true;
    } else {
      LOGGER.info(
          () -> "User account has failed to be debited with " + amount + ".");
      return false;
    }
  }
}
