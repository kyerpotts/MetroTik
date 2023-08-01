package edu.curtin.metrotik.monorailsimulator;

import java.util.logging.Logger;

public class MonorailSimulator {
  private static final Logger LOGGER = Logger.getLogger(MonorailSimulator.class.getName());
  private int currentZone;

  public MonorailSimulator(int currentZone) {
    this.currentZone = currentZone;
    LOGGER.info(() -> "MonorailSimulator has been initialised successfully");
  }

  /**
   * This method is responsible for travelling a single zone. The monorail is a
   * circular travel system, with 10 zones, so if the current zone is 10, then
   * the next zone is 1.
   */
  public void travelZone() {
    if (currentZone == 10) {
      currentZone = 1;
    } else {
      currentZone++;
    }
    LOGGER.info(() -> "MonorailSimulator has travelled to the next zone");
  }

  /**
   * Simple getter method for retrieving the current zone.
   */
  public int getCurrentZone() {
    return currentZone;
  }
}
