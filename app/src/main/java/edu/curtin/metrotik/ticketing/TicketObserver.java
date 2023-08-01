package edu.curtin.metrotik.ticketing;

/**
 * This interface provides a contract for observing the various states of a
 * ticket. This allows various under the hood functionality to exist, such as
 * removing tickets from a chain when they are no longer valid.
 *
 * @author Kyer Potts
 */
public interface TicketObserver {
  public void invalidateTicket(Ticket ticket);
}
