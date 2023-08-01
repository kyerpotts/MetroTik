package edu.curtin.metrotik.ticketing;

/**
 * This exception should be thrown whenever a ticket has been found to be in an
 * illegal state, such as being requested to travel when zone allocations are
 * already exhausted.
 *
 * @author Kyer Potts
 */
public class TicketingException extends RuntimeException {
  public TicketingException(String message) {
    super(message);
  }
}
