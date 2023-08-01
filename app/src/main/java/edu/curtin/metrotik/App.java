/**
 * This class is the entry point of the program. It is responsible for
 * instantiating and executing the main functionality of the program.
 *
 * @version 1.0
 * @Author Kyer Potts
 */
package edu.curtin.metrotik;

import edu.curtin.metrotik.controller.MainController;
import edu.curtin.metrotik.monorailsimulator.MonorailSimulator;
import edu.curtin.metrotik.ticketing.Ticket;
import edu.curtin.metrotik.ticketing.TicketFactory;
import edu.curtin.metrotik.ticketingsystem.TicketChain;
import edu.curtin.metrotik.ticketingsystem.TicketManager;
import edu.curtin.metrotik.useraccounts.UserAccount;
import edu.curtin.metrotik.userinterface.AccountDetails;
import edu.curtin.metrotik.userinterface.CurrentStation;
import edu.curtin.metrotik.userinterface.Menu;
import edu.curtin.metrotik.userinterface.NotificationHandler;
import java.util.Scanner;
import java.util.logging.Logger;

public class App {
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        // Hard coded some values into the user account. The account will be reset
        // every time the program is run.
        UserAccount userAccount = new UserAccount(18490, "Jimmy", "Barnes", "jimmy.barnes@yahoo.com");
        NotificationHandler notificationHandler = new NotificationHandler();
        TicketFactory ticketFactory = new TicketFactory();
        TicketManager ticketManager = instantiateTicketManager();
        MonorailSimulator monorailSimulator = new MonorailSimulator(1);
        MainController mainController = new MainController(ticketFactory, ticketManager, notificationHandler,
                userAccount, monorailSimulator);
        userAccount.subscribeAccountStateObserver(notificationHandler);
        userAccount.subscribeAccountTransactionObserver(notificationHandler);

        LOGGER.info(() -> "Program has initialised successfully");

        executeUI(mainController, notificationHandler);
    }

    private static void executeUI(MainController mainController,
            NotificationHandler notificationHandler) {
        try (Scanner input = new Scanner(System.in)) {
            Menu menu = new Menu(mainController, input);
            AccountDetails accountDetails = new AccountDetails(mainController);
            CurrentStation currentStation = new CurrentStation(mainController);

            boolean continueLoop = true;
            do {
                LOGGER.info(() -> "Program has entered main execution state.");
                accountDetails.printAccountStatus();
                accountDetails.printAccountBalance();
                currentStation.printCurrentStation();
                notificationHandler.printNotifications();

                // The user needs to be presented with the main options in order to make
                // a selection and proceed with the functionality of the program.
                menu.printMainOptions();
                String option = input.nextLine();

                switch (option) {
                    case "1":
                        LOGGER.info(
                                () -> "User has selected to view the zone travel options.");
                        menu.zoneTravelSelection();
                        break;
                    case "2":
                        LOGGER.info(
                                () -> "User has selected to view the ticket purchase options.");
                        menu.ticketPurchaseSelection();
                        break;
                    case "3":
                        LOGGER.info(() -> "User has selected to add funds.");
                        menu.addFundsSelection();
                        break;
                    case "4":
                        LOGGER.info(() -> "User has selected to deactivate their account.");
                        menu.deactivateAccount();
                        break;
                    case "5":
                        LOGGER.info(() -> "User has selected to exit the program.");
                        System.out.println("Exiting program...");
                        continueLoop = false;
                        break;
                    default:
                        LOGGER.info(() -> "User has selected an invalid option.");
                        System.out.println("Invalid input, please try again.");
                        break;
                }
            } while (continueLoop);
        }
    }

    private static TicketManager instantiateTicketManager() {
        TicketChain<Ticket> timedTicketChain = new TicketChain<>();
        TicketChain<Ticket> zoneTicketChain = new TicketChain<>();
        return new TicketManager(timedTicketChain, zoneTicketChain);
    }
}
