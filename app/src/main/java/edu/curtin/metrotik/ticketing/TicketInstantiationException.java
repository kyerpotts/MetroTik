package edu.curtin.metrotik.ticketing;

/**
 * This exception should be thrown whenever the ticket factory is requested to
 * create a ticket without first being primed.
 *
 * @author Kyer Potts
 */
public class TicketInstantiationException extends RuntimeException {
  public TicketInstantiationException(String message) {
    super(message);
  }
}
