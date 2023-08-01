package edu.curtin.metrotik.userinterface;

import edu.curtin.metrotik.controller.MainController;

/**
 * This class is resposible for displaying various account details to the user.
 *
 * @Author Kyer Potts
 */
public class AccountDetails {
  private MainController controller;

  public AccountDetails(MainController controller) {
    this.controller = controller;
  }

  /**
   * This method is responsible for displaying the account balance to the user.
   */
  public void printAccountBalance() {
    System.out.println("Your current balance is: " +
        controller.getAccountBalance());
  }

  /**
   * This method is responsible for displaying the account status to the user.
   */
  public void printAccountStatus() {
    System.out.println("Your account is currently: " +
        controller.getAccountStatus());
  }
}
