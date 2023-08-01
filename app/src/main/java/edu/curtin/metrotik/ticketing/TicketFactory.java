package edu.curtin.metrotik.ticketing;

import java.time.LocalDateTime;
import java.util.logging.Logger;

/**
 * Basic factory method pattern for creating tickets. Not entirely happy with
 * this class, as the decision making process on which ticket is
 * created isn't entirely abstracted from user input. I could not think of
 * another use case for the factory method pattern. This implementation would be
 * much better served by the builder pattern, which I believe is the more useful
 * and superior instantiation pattern anyway, but the assignment brief has not
 * called for it.
 *
 * @author Kyer Potts
 */
public class TicketFactory {
  private static final Logger LOGGER = Logger.getLogger(TicketFactory.class.getName());
  // For the purposes of this program, ticketID's will be reset whenever the
  // program is re-run. Persistence was not part of the requirements and so no
  // effort has been made to have persisting elements.
  private int ticketID = 0;
  private int zoneAllocation = 0;
  private LocalDateTime validFrom = null;

  /**
   * Factory method to isntantiate tickets.
   *
   * @return a fully instantiated ticket.
   */
  public Ticket createTicket() {
    Ticket newTicket;
    // If the factory has been primed for a timed and a zone ticket it is in an
    // invalid state and it should throw an error.
    if (validFrom != null && zoneAllocation > 0) {
      LOGGER.severe(
          () -> "TicketFactory cannot be primed for a timed ticket and a zone ticket at the same time");
      throw new TicketInstantiationException(
          "TicketFactory cannot be primed for a timed ticket and a zone ticket at the same time");
    }
    // Ticket is primed for a timed ticket and should return a fully
    // instantiated timed ticket.
    if (validFrom != null) {
      // Whenever a ticket is instantiated, the ticketID variable must be
      // increased to ensure that tickets have unique ID's. In a proper
      // ticketing system this would be an abstracted object that references a
      // central data source for this information, however it is not required
      // for a simple demonstration program.
      ticketID++;
      newTicket = new TimedTicket(ticketID, LocalDateTime.now(), validFrom);
      LOGGER.info(() -> "TicketFactory has created a timed ticket");
      // Once a ticket is instantiated, the factory must be reset to a state in
      // which it is ready to be primed for the next ticket.
      validFrom = null;
      LOGGER.info(() -> "TicketFactory has been reset");
      return newTicket;
    }
    // Ticket is primed for a zone ticket and should return a fully instantiated
    // timed ticket.
    else if (zoneAllocation > 0) {
      // Whenever a ticket is instantiated, the ticketID variable must be
      // increased to ensure that tickets have unique ID's. In a proper
      // ticketing system this would be an abstracted object that references a
      // central data source for this information, however it is not required
      // for a simple demonstration program.
      ticketID++;
      newTicket = new ZoneTicket(ticketID, LocalDateTime.now(), zoneAllocation);
      LOGGER.info(() -> "TicketFactory has created a zone ticket");
      // Once a ticket is instantiated, the factory must be reset to a state in
      // which it is ready to be primed for the next ticket.
      zoneAllocation = 0;
      LOGGER.info(() -> "TicketFactory has been reset");
      return newTicket;
    }
    // If the factory has not been primed for the instantiation of a ticket it
    // should throw an exception to reflect the misuse of the object.
    else {
      LOGGER.severe(
          () -> "TicketFactory has not been adequately primed for ticket instantiation.");
      throw new TicketInstantiationException(
          "TicketFactory has not been adequately primed for ticket instantiation.");
    }
  }

  /**
   * Method to prime the factory for instantiation of a TimedTicket.
   *
   * @param the time that the next ticket to be instantiated will be valid from.
   */
  public void primeTicketFactory(LocalDateTime validFrom) {
    this.validFrom = validFrom;
    LOGGER.info(
        () -> "TicketFactory has been primed for a timed ticket with validFrom of " +
            validFrom.toString());
  }

  /**
   * Method to prime the factory for instantiation of a zone ticket.
   *
   * @param the number of zones for be allocated to the next instantiated zone.
   */
  public void primeTicketFactory(int zones) {
    this.zoneAllocation = zones;
    LOGGER.info(
        () -> "TicketFactory has been primed for a zone ticket with zoneAllocation of " +
            zones);
  }
}
