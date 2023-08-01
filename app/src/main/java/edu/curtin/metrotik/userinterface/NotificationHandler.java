package edu.curtin.metrotik.userinterface;

import edu.curtin.metrotik.ticketing.Ticket;
import edu.curtin.metrotik.ticketing.TicketObserver;
import edu.curtin.metrotik.useraccounts.AccountEvent;
import edu.curtin.metrotik.useraccounts.AccountStateObserver;
import edu.curtin.metrotik.useraccounts.AccountTransactionObserver;
import java.util.LinkedList;
import java.util.Queue;

/**
 * This class is responsible for recieving information based on various events
 * that occur throughout program, and displaying them to the user. A queue is
 * implemented which stores each of the events as they occur in order, which are
 * then organised into a readable format whenever the program "ticks" through a
 * cycle.
 */
public class NotificationHandler implements TicketObserver,
    AccountStateObserver,
    AccountTransactionObserver {
  private Queue<String> notificationQueue = new LinkedList<>();

  @Override
  public void accountTransactionEventOccured(AccountEvent accountEvent) {
    notificationQueue.add(accountEvent.getMessage());
  }

  @Override
  public void accountStateUpdated(AccountEvent accountEvent) {
    notificationQueue.add(accountEvent.getMessage());
  }

  @Override
  public void invalidateTicket(Ticket ticket) {
    String invalidTicketMessage = "Ticket ID: " + ticket.getTicketID() + " is now invalid.";
    notificationQueue.add(invalidTicketMessage);
  }

  /**
   * Method loops through all notifications that have accumulated throughout a
   * tick of the program running and prints them to the screen.
   */
  public void printNotifications() {
    while (!notificationQueue.isEmpty()) {
      System.out.println(notificationQueue.poll());
    }
  }
}
