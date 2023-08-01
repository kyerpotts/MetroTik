package edu.curtin.metrotik.userinterface;

import edu.curtin.metrotik.controller.MainController;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * This class is responsible for displaying options and handling direct input
 * given by the user.
 *
 * @author Kyer Potts
 */
public class Menu {
  private MainController controller;
  private Scanner input;

  public Menu(MainController controller, Scanner input) {
    this.controller = controller;
    this.input = input;
  }

  /**
   * Method displays the main options to the user so that they can properly
   * interact with the program.
   */
  public void printMainOptions() {
    System.out.println("Please choose from one of the following options");
    System.out.println("1: Ride zones");
    System.out.println("2: Purchase Ticket");
    System.out.println("3: Add Funds");
    System.out.println("4: Deactivate Account");
    System.out.println("5: Exit");
  }

  public void addFundsSelection() {
    boolean repeatInputCapture = true;
    do {
      System.out.println(
          "Please enter the amount of funds you would like to add, or enter 0 to return to the previous menu: ");
      try {
        double funds = input.nextDouble();
        // The newline character must be consumed after the nextDouble() method
        input.nextLine();

        repeatInputCapture = false;

        // If the user enters 0, return to the previous menu.
        if (funds == 0) {
          return;
        }

        // The controller handles the request to add funds.
        controller.addFunds(funds);
      } catch (InputMismatchException e) {
        System.out.println("Invalid input, please enter a number only.");
      }
    } while (repeatInputCapture);
  }

  public void deactivateAccount() {
    controller.deactivateAccount();
  }

  /**
   * Method to handle user input for zone travel.
   */
  public void zoneTravelSelection() {
    boolean repeatInputCapture = true;
    do {
      System.out.println(
          "Please enter the number of zones you would like to ride, or enter 0 to return to the previous menu: ");
      try {
        int zones = input.nextInt();
        // The newline character must be consumed after the nextInt() method
        input.nextLine();

        repeatInputCapture = false;

        // If the user enters 0, return to the previous menu.
        if (zones == 0) {
          return;
        }

        // The controller handles the request to travel zones. If ticketing is
        // not sufficient, the user is notified.
        if (!controller.requestZoneTravel(zones)) {
          System.out.println(
              "You do not have the ticketing required to travel " + zones +
                  " zones. Please purchase the appropriate ticketing.");
          return;
        }
      } catch (InputMismatchException e) {
        System.out.println("Invalid input, please enter an integer only.");
      }
    } while (repeatInputCapture);
  }

  /**
   * Method to handle user input for ticket purchasing.
   */
  public void ticketPurchaseSelection() {
    boolean repeatInputCapture = true;
    do {
      System.out.println(
          "Please enter 1 for a timed ticket, 2 for a zone ticket, or enter 0 to return to the previous menu: ");
      try {
        String selection = input.nextLine();
        repeatInputCapture = false;

        switch (selection) {
          case "0":
            return;
          case "1":
            // The user has chosen to purchase a timed ticket.
            timedTicketPurchase();
            break;
          case "2":
            // The user has chosen to purchase a zone ticket.
            zoneTicketPurchase();
            break;
          default:
            System.out.println("Invalid input, please enter 1, 2, or 0.");
            break;
        }

      } catch (InputMismatchException e) {
        System.out.println("Invalid input, please enter an integer only.");
      }
    } while (repeatInputCapture);
  }

  /**
   * Method to handle user input for timed ticket purchasing.
   */
  private void timedTicketPurchase() {
    boolean repeatInputCapture = true;
    System.out.println(
        " Please enter the date and time you would like to be valid from in the Format: yyyy-MM-dd. Or enter 0 to return to the previous menu:");
    do {
      try {
        String dateTime = input.nextLine();

        // If the user enters 0, return to the previous menu.
        if (dateTime.equals("0")) {
          return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate validFromDate = LocalDate.parse(dateTime, formatter);
        LocalDateTime validFrom = LocalDateTime.of(validFromDate, LocalTime.MIN);

        repeatInputCapture = false;
        // The controller handles the request to purchase a timed ticket. If
        // the user does not have sufficient funds, they are notified.
        if (!controller.requestTimedTicketPurchase(validFrom)) {
          System.out.println(
              "You do not have sufficient funds to purchase a timed ticket. Please add funds to your account.");
          return;
        }
      } catch (InputMismatchException e) {
        System.out.println("Invalid input, please enter an integer only.");
      } catch (DateTimeParseException e) {
        System.out.println(
            "Invalid input, please enter a date and time in the format yyyy-MM-dd.");
      }
    } while (repeatInputCapture);
  }

  /**
   * Method to handle user input for zone ticket purchasing.
   */
  private void zoneTicketPurchase() {
    boolean repeatInputCapture = true;
    do {
      System.out.println(
          "Please enter the number of zones you would like to purchase, or enter 0 to return to the previous menu: ");
      try {
        int zones = input.nextInt();
        // The newline character must be consumed after the nextInt() method
        input.nextLine();

        repeatInputCapture = false;

        // If the user enters 0, return to the previous menu.
        if (zones == 0) {
          return;
        }

        // The controller handles the request to purchase a zone ticket. If
        // the user does not have sufficient funds, they are notified.
        if (!controller.requestZoneTicketPurchase(zones)) {
          System.out.println(
              "You do not have sufficient funds to purchase a zone ticket. Please add funds to your account.");
          return;
        }
      } catch (InputMismatchException e) {
        System.out.println("Invalid input, please enter an integer only.");
      }
    } while (repeatInputCapture);
  }
}
