package edu.curtin.metrotik.ticketing;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * A ZoneTicket type is used for a single person to ride a specific number of
 * zones. If the zone allocation has been exhausted, the ticket is no longer
 * valid.
 *
 * @author Kyer Potts
 */
public class ZoneTicket implements Ticket {
  private static final Logger LOGGER = Logger.getLogger(ZoneTicket.class.getName());

  private Set<TicketObserver> ticketObservers = new HashSet<>();

  private final int ticketID;
  private final LocalDateTime timeDatePurchased;
  private int zoneAllocation;

  public ZoneTicket(int ticketID, LocalDateTime timeDatePurchased,
      int zoneAllocation) {
    this.ticketID = ticketID;
    this.timeDatePurchased = timeDatePurchased;
    // The number of zones allocated to the ticket must be recorded. The zone
    // allocations must only change whenever the ticket successfully allows a
    // user to travel.
    this.zoneAllocation = zoneAllocation;
    LOGGER.info(() -> "Zone ticket has been created.");
  }

  @Override
  public void addTicketObserver(TicketObserver observer) {
    this.ticketObservers.add(observer);
    LOGGER.info(() -> "Observer has been added to the zone ticket: " +
        observer.toString());
  }

  @Override
  public boolean travelZone() {
    // If the zoneAllocation is greater than 0, the ticket is valid and an
    // allocation can be spent on a zone of travel.
    if (zoneAllocation > 0) {
      zoneAllocation--;
      // A zone allocation was successfully used in order to travel a zone, and
      // the ticket must return a true value to reflect that.

      // Zone allocations must be checked again to determine ticket validity for
      // the purposes of notifying observers.
      if (zoneAllocation == 0) {
        LOGGER.info(() -> "Zone ticket allocation has been exhausted: " +
            this.toString());
        invalidateTicket();
      }
      LOGGER.info(() -> "Zone ticket has been used to travel a zone: " +
          this.toString());
      return true;
    } else {
      LOGGER.severe(
          () -> "Zone ticket is invalid and should not be available to be checked for travel.");
      throw new TicketingException(
          "Ticket is invalid and has been requested to be used for travel.");
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
    // zone allocations should never be less than 0. It should be impossible for
    // them to exist in this state.
    assert (this.zoneAllocation < 0);

    // The number of zones left required to travel after being calculated
    // against the zoneAllocation of the ticket must be determined in order
    // return the remainder.
    int zonesLeft = zones - this.zoneAllocation;

    // If the allocations of the ticket exceed the requested number of travel
    // zones, the method should return a result of 0, as in there are no more
    // zones left to account for.
    if (zonesLeft <= 0) {
      LOGGER.info(() -> "Zone ticket has enough allocations to travel: " +
          zones + " zones.");
      return 0;
    }
    // If the allocations left on the ticket are not sufficient to permit the
    // total requested travel, the remainder of the zones minus the zone
    // allocations of each particular ticket should be returned for further
    // calculation.
    else {
      LOGGER.info(() -> "Zone ticket only has enough allocation to travel: " +
          this.zoneAllocation + " out of " + zones +
          " zones. " + zonesLeft + " zones left.");
      return zonesLeft;
    }
  }

  /**
   * Method that notifies observers when a zone ticket is no longer valid.
   * Observers are used in this instance because the ticket does not need to see
   * what is being done with it once it is invalid, it only needs to update
   * interested parties on its state. The state pattern was not used here as it
   * was unnecessary to define different modes of behaviour. Once a ticket is
   * invalid it should be removed from containers that require it's use.
   */
  private void invalidateTicket() {
    LOGGER.info(() -> "Zone ticket has been invalidated: " + this.toString());
    for (TicketObserver observer : ticketObservers) {
      observer.invalidateTicket(this);
    }
  }
}
