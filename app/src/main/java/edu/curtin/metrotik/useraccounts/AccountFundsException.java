package edu.curtin.metrotik.useraccounts;

/**
 * This exception should be thrown whenever there is invalid input given to any
 * of the methods that mutate the private funds variable in a UserAccount.
 * This ensures that plus and minus values are handled accordingly, and do no
 * produce accounting errors.
 *
 * @author Kyer Potts
 */
public class AccountFundsException extends RuntimeException {
  public AccountFundsException(String message) {
    super(message);
  }
}
