package edu.curtin.metrotik.ticketing;

import java.time.LocalDateTime;

/**
 * Contract for all values of tickets, the strategy pattern has been implemented
 * here.
 *
 * @author Kyer Potts
 */
public interface Ticket {
  /**
   * Provides a contract for tickets to register observers. These observers will
   * watch for specific events or changes within the ticket and respond to them
   * appropriately.
   *
   * @param observer the observer of the ticket.
   */
  public void addTicketObserver(TicketObserver observer);

  /**
   * Initiate a single zone of travel. The monorail only considers the value of
   * the zones travelled for ticketing purposes. This method will be called
   * whenever a zone is travelled, and manage it's own behaviour internally.
   *
   * @return true if the ticket successfully allowed the travel of a zone.
   */
  public boolean travelZone();

  /**
   * Provides a contract for retrieving the ID of a sinlge ticket. This will
   * mostly be used for record keeping purposes, or if a specific ticket is
   * required.
   *
   * @return the integer value of the id of the ticket
   */
  public int getTicketID();

  /**
   * Provides a contract for retrieving the timedate stamp at which the ticket
   * was purchased. This is used for record keeping purposes.
   */
  public LocalDateTime getPurchaseTimeDate();

  /**
   * Provides a contract for the ticket to provide information as to whether the
   * passenger has the ability to travel the requested number of zones, or if
   * the passenger needs to purchase additional tickets to complete the journey.
   *
   * @param zones the number of zones that have been requested to be travelled.
   *
   * @return the number of zones after a calculation has been applied to the
   *         zones allocated to the ticket and the number of zones requested to
   *         be travelled.
   */
  public int checkZoneTravel(int zones);
}
