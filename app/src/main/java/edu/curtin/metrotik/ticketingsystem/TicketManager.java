package edu.curtin.metrotik.ticketingsystem;

import edu.curtin.metrotik.ticketing.*;
import edu.curtin.metrotik.useraccounts.UserAccount;
import java.util.logging.Logger;

/**
 * This class encapsulates the various ticket chains and manages the business
 * logic required on the different ticketing types.
 *
 * @author Kyer Potts
 */
public class TicketManager {
  private static final Logger LOGGER = Logger.getLogger(TicketManager.class.getName());
  // Each of the various ticket types are handled by the ticket manager. These
  // are managed within
  private TicketChain<Ticket> zoneTickets;
  private TicketChain<Ticket> timedTickets;

  public TicketManager(TicketChain<Ticket> zoneTickets,
      TicketChain<Ticket> timedTickets) {
    this.zoneTickets = zoneTickets;
    this.timedTickets = timedTickets;
    LOGGER.info(() -> "TicketManager has been instantiated successfully.");
  }

  /**
   * Simple method for adding zone tickets to the appropriate container, and
   * also insert it into the purchase history of the user account.
   *
   * @param zoneTicket  the instantiated zone ticket to be added to the
   *                    ticketManager.
   * @param userAccount the account to which the ticket will be added to the
   *                    purchase history.
   */
  public void addZoneTicket(Ticket zoneTicket, UserAccount userAccount) {
    zoneTickets.addTicket(zoneTicket);
    userAccount.addTicketToTicketHistory(zoneTicket);
    LOGGER.info(() -> "Zone ticket has been added to the ticket manager: " +
        zoneTicket.toString());
  }

  /**
   * Simple method for adding timed tickets to the appropriate container. It is
   * also inserted into the purchase history of the user account.
   *
   * @param timedTicket the instantiated timed ticket to be added to the
   *                    TicketManager.
   * @param userAccount the account to which the ticket will be added to the
   *                    purchase history.
   */
  public void addTimedTicket(Ticket timedTicket, UserAccount userAccount) {
    timedTickets.addTicket(timedTicket);
    userAccount.addTicketToTicketHistory(timedTicket);
    LOGGER.info(() -> "Timed ticket has been added to the ticket manager: " +
        timedTicket.toString());
  }

  /**
   * This method applies the business logic for checking whether the zones
   * requested to be travelled can. Business logic dictates that zone allocated
   * tickets will only be used if there are no currently valid timed tickets
   * available.
   *
   * @param timedTicket the instantiated timed ticket to be added to the
   *                    TicketManager.
   * @param userAccount the account to which the ticket will be added to the
   *                    purchase history.
   *
   * @return will return true if there is sufficient ticket
   *         allocation to travel the requested zones.
   */
  public boolean canRideForRequestedZones(int zones) {
    // Timed tickets are checked first as per business logic. There is no need
    // to use zone allocation resources if there is already a valid timed ticket
    // currently in effect. Only when all timed tickets have been checked, is it
    // necessary to check zone allocated tickets to determine whether there is
    // appropriate ticketing for travel.
    if (timedTickets.canTravelZones(zones)) {
      LOGGER.info(
          () -> "Timed tickets can been used to travel requested zones.");
      return true;
    } else if (zoneTickets.canTravelZones(zones)) {
      LOGGER.info(
          () -> "Zone tickets can been used to travel requested zones.");
      return true;
    } else {
      // If the timed and zone tickets do no have appropriate allocation for the
      // requested travel, false is returned to state that additional ticketing
      // will be required for travel.
      LOGGER.info(() -> "No tickets can been used to travel requested zones.");
      return false;
    }
  }

  /**
   * Encapsulating method for travelling a zone. This method contains core
   * domain logic related to the order in which tickets must first be checked in
   * order to determine validity and register a single zone of travel. A valid
   * timed ticket must always be used before consuming a zone allocation on a
   * zone ticket.
   *
   * @return true if the zone was successfully travelled.
   */
  public boolean travelRequestedZones() {
    if (timedTickets.travelZone()) {
      LOGGER.info(() -> "Timed ticket has been used to travel a single zone.");
      return true;
    } else if (zoneTickets.travelZone()) {
      LOGGER.info(() -> "Zone ticket has been used to travel a single zone.");
      return true;
    } else {
      // There were no valid tickets that could be used to travel a single zone.
      LOGGER.info(() -> "No tickets could be used to travel a single zone.");
      return false;
    }
  }
}
