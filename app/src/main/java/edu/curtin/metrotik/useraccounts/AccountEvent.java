
package edu.curtin.metrotik.useraccounts;

import java.util.logging.Logger;

/**
 * This class provides an encapsulated event object that can be passed to the
 * various observers. It contains a message detailing the event, as well as a
 * reference to the account object the event originated from.
 *
 * @author Kyer Potts
 */
public class AccountEvent {
  private static final Logger LOGGER = Logger.getLogger(AccountEvent.class.getName());
  private UserAccount origin;
  private String message;
  private boolean eventSuccess;

  public AccountEvent(UserAccount origin, String message,
      boolean eventSuccess) {
    this.origin = origin;
    this.message = message;
    this.eventSuccess = eventSuccess;
    LOGGER.info(() -> "Account event created successfully.");
  }

  // Only getters are required for this class, and event should remain
  // immutable.
  public UserAccount getOrigin() {
    return origin;
  }

  public String getMessage() {
    return message;
  }

  public boolean isEventSuccess() {
    return eventSuccess;
  }
}
