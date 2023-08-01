package edu.curtin.metrotik.userinterface;

import edu.curtin.metrotik.controller.MainController;

/**
 * This class is responsible for displaying the current station to the user.
 */
public class CurrentStation {
  private MainController controller;

  public CurrentStation(MainController controller) {
    this.controller = controller;
  }

  /**
   * This method is responsible for displaying the current station to the user.
   */
  public void printCurrentStation() {
    System.out.println("You are currently at zone: " +
        controller.getCurrentStation());
  }
}
