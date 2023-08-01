package edu.curtin.metrotik.ticketing;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * A TimedTicket type is used for a single person to ride the monorail for a
 * specific time period. If the time allocation has been exhausted, the ticket
 * is no longer valid.
 *
 * @author Kyer Potts
 */
public class TimedTicket implements Ticket {
  private static final Logger LOGGER = Logger.getLogger(TimedTicket.class.getName());

  private Set<TicketObserver> ticketObservers = new HashSet<>();

  private final int ticketID;
  private final LocalDateTime timeDatePurchased;
  private final LocalDateTime validFrom;
  private final LocalDateTime validTo;

  public TimedTicket(int ticketID, LocalDateTime timeDatePurchased,
      LocalDateTime validFrom) {
    this.timeDatePurchased = timeDatePurchased;
    this.ticketID = ticketID;
    this.validFrom = validFrom;
    // daily tickets should always be valid for 24 hours from the validFrom
    // date.
    this.validTo = validFrom.plusHours(24);
    LOGGER.info(() -> "Timed ticket has been created.");
  }

  @Override
  public void addTicketObserver(TicketObserver observer) {
    ticketObservers.add(observer);
    LOGGER.info(() -> "Observer has been added to the timed ticket: " +
        observer.toString());
  }

  @Override
  public boolean travelZone() {
    // The current date and time must be captured in order to determine the
    // validity of the ticket.
    LocalDateTime currentTime = LocalDateTime.now();

    // We need to check whether the current time falls within the validated
    // ticket parameters. If so, the ticket is valid and the passenger may use
    // it to travel.
    if (isValid(currentTime)) {
      LOGGER.info(
          () -> "Timed ticket has been used to travel: " + this.toString());
      return true;
    }
    // if the ticket is now expired, the relevant observers need to be notified
    // of the change in state.
    else if (currentTime.isAfter(validTo)) {
      invalidateTicket();
      LOGGER.info(() -> "Timed ticket has expired: " + this.toString());
      return false;
    }
    // for all other possible states that the ticket may exist within, it is not
    // currently valid for travel, however it may not yet have expired. It
    // should return a false value and prevent the passenger from travelling.
    else {
      LOGGER.info(
          () -> "Timed ticket is not valid for travel: " + this.toString());
      return false;
    }
  }

  @Override
  public int getTicketID() {
    return this.ticketID;
  }

  @Override
  public LocalDateTime getPurchaseTimeDate() {
    return this.timeDatePurchased;
  }

  @Override
  public int checkZoneTravel(int zones) {
    // If the ticket is currently valid, it can travel all requested zones as
    // timed tickets have inexhaustable zone allocations. As such a valid result
    // will consume all requested travel zones and should return a result of 0,
    // as in there are no more requested zones left to purchase tickets for.
    LocalDateTime currentTime = LocalDateTime.now();
    if (isValid(currentTime)) {
      LOGGER.info(() -> "Timed ticket can travel the requested zones: " +
          this.toString());
      return 0;
    } else {
      // If the ticket is invalid, all requested travel zones should be
      // returned, a timed ticket should absorb all requested zones, or none of
      // them.
      LOGGER.info(() -> "Timed ticket cannot travel the requested zones: " +
          this.toString());
      return zones;
    }
  }

  /**
   * Method that notifies observers when a timed ticket is no longer valid.
   * Observers are used in this instance because the ticket does not need to see
   * what is being done with it once it is invalid, it only needs to update
   * interested parties on its state. The state pattern was not used here as it
   * was unnecessary to define different modes of behaviour. Once a ticket is
   * invalid it should be removed from containers that require it's use.
   */
  private void invalidateTicket() {
    LOGGER.info(() -> "Timed ticket has been invalidated: " + this.toString());
    for (TicketObserver observer : ticketObservers) {
      observer.invalidateTicket(this);
    }
  }

  /**
   * Helper method to determine whether the ticket is currently valid given the
   * current date and time.
   *
   * @param currentTime the current date and time that the validity of the
   *                    ticket is being checked.
   * @return if the ticket is currently valid, return true, otherwise invalid
   *         tickets should return false.
   */
  private boolean isValid(LocalDateTime currentTime) {
    LOGGER.info(() -> "Checking if timed ticket is valid: " + this.toString());
    if (validFrom.isBefore(currentTime) && currentTime.isBefore(validTo)) {
      LOGGER.info(() -> "Timed ticket is valid: " + this.toString());
      return true;
    } else {
      LOGGER.info(() -> "Timed ticket is not valid: " + this.toString());
      return false;
    }
  }

  /**
   * Overridden toString method to provide a string representation of the timed
   * ticket object.
   *
   * @return a string representation of the timed ticket object.
   */
  @Override
  public String toString() {
    return "TimedTicket{"
        + "ticketID=" + ticketID + ", validFrom=" + validFrom +
        ", validTo=" + validTo + '}';
  }
}
