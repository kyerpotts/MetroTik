package edu.curtin.metrotik.useraccounts;

import java.util.logging.Logger;

/**
 * Helper class used to ensure mutation of account funds does not cause
 * accounting errors when invalid values are supplied.
 *
 * @author Kyer Potts
 */
public class FundMutatorValidator {
  private static final Logger LOGGER = Logger.getLogger(FundMutatorValidator.class.getName());

  /**
   * Function for mutation validation. Mutation of the funds variable must
   * always occur with a positive value.
   *
   * @throws AccountFundsException is thrown when an invalid value is passed via
   *                               the mutator param. Values passed via the
   *                               credit param must always be positive,
   *                               otherwise there will be accounting errors in
   *                               the funds of the account.
   *                               Using an absolute value was also considered
   *                               here, however I decided that
   *                               handling the potential error in this way may
   *                               create unintended side
   *                               effects.
   */
  public void checkForInvalidFundMutator(double mutator) {
    if (mutator < 0.0) {

      LOGGER.warning(
          () -> "Invalid value passed. Fund mutation parameter must never be less than 0.0. Value passed was: " +
              mutator);
      throw new AccountFundsException(
          "Invalid value passed. Fund mutation parameter must never be less than 0.0. Value passed was: " +
              mutator);
    }
  }
}
