package edu.curtin.metrotik.ticketingsystem;

import edu.curtin.metrotik.ticketing.Ticket;
import edu.curtin.metrotik.ticketing.TicketObserver;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * A container for various types of tickets attached to each account. Generics
 * are implemented here for better code reuse, as each type of ticket will need
 * a separate container
 *
 * @author Kyer Potts
 */
public class TicketChain<T extends Ticket> implements TicketObserver {
  private static final Logger LOGGER = Logger.getLogger(TicketChain.class.getName());
  private Set<T> tickets;

  public TicketChain() {
    tickets = new HashSet<>();
    LOGGER.info(() -> "Ticket chain has been initialised successfully");
  }

  public void addTicket(T ticket) {
    tickets.add(ticket);

    // These containers will observe each of it's individual tickets to
    // determine whether they need to be removed from the chain due to being
    // invalid
    ticket.addTicketObserver(this);
    LOGGER.info(() -> "Ticket has been added to the chain successfully: " +
        ticket.toString());
  }

  /**
   * Accessor method for the tickets contained within the chain.
   *
   * @return the set of tickets contained encapsulated within the chain.
   */
  public Set<T> getTickets() {
    return tickets;
  }

  /**
   * Method for invalidating a ticket. This method uses the observer pattern to
   * allow the ticket object to notify the observing objects when it becomes
   * invalid. In the case of a ticket chain, when a ticket becomes invalid, it
   * is removed from the chain as it is no longer required and should not be
   * checked again for the purposes of zone travel. If it is checked it is due
   * to an error state, and appropriate error handling has been implemented to
   * ensure that.
   *
   * @param ticket the ticket to be invalidated.
   */
  @Override
  public void invalidateTicket(Ticket ticket) {
    tickets.remove(ticket);
    LOGGER.info(
        () -> "Ticket has been invalidated and removed from the chain successfully: " +
            ticket.toString());
  }

  public boolean canTravelZones(int zones) {
    LOGGER.info(
        () -> "Checking whether the chain can travel " + zones + " zones");
    int remainingZones;
    // Iterating through all of the tickets in the chain to determine whether
    // there is appropriate allocation amongst all tickets to travel the
    // requested zones.
    for (Ticket ticket : tickets) {
      remainingZones = ticket.checkZoneTravel(zones);
      if (LOGGER.isLoggable(java.util.logging.Level.INFO)) {
        LOGGER.log(java.util.logging.Level.INFO,
            "Remaining zones left to check: " + remainingZones);
      }

      // As soon as the remaining zones drops to 0, there is appropriate ticket
      // allocation to allow for the travel over the requested zones.
      if (remainingZones == 0) {
        LOGGER.info(
            () -> "There is enough zone allocation to travel the requested zones");
        return true;
      }
    }

    // If there are still remaining zones, there isn't enough zone allocation
    // attributed to the tickets.
    LOGGER.info(
        () -> "There is not enough zone allocation to travel the requested zones");
    return false;
  }

  /**
   * Encapsulating method for travelling a single zone. This container is only
   * concerned with travelling a single zone at a time. Multiple zone travel
   * should be handled by an object containing core domain logic.
   *
   * @return true if the zone was successfully travelled and the appropriate
   *         allocation deducted from any enclosed tickets.
   */
  public boolean travelZone() {
    LOGGER.info(() -> "Attempting to travel a single zone");
    // Tickets must first be checked to determine validity. As soon as a valid
    // ticket is found, the travel of the zone is registered and internal
    // ticketing logic is handled by the ticket object.
    for (Ticket t : tickets) {
      if (t.travelZone()) {
        LOGGER.info(() -> "A ticket has been found to travel a single zone");
        return true;
      }
    }
    // If there are no valid tickets that can register a zone of travel, a false
    // value is returned to allow the domain logic components of the program to
    // manage control flow.
    LOGGER.info(
        () -> "There are no valid tickets that can register a zone of travel");
    return false;
  }
}
