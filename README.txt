Critera:
1. Code Quality and PMD rules
   All of my code has been adequately commented with javadoc and inline comments explaining the functionality of the code.
   The PMD ruleset provided has been used and checked using the ./gradlew check command, and there are no violations in the code.

2. Code responsibilities
   I have divided class and package responsibilities closely to a MVC pattern. I have not used a model package to hold all of the model
   components as it was unnecessary for an application of this size. Each class has a clearly defined purpose, and hold true to the
   single responsibility principle. Methods have been kept short and concise, and have been named appropriately to describe their purpose.

3. Error Handling
   There are only a small number of customs exceptions present in the code, as there isn't a great deal of possible error states in the
   application. I have only used Runtime exceptions for these cases, as any of these error states should not be recoverable. Logging has
   been implemented appropriately to keep track of these errors, as well as any information that may have led to the errors. Assert
   statements have been used sparingly throughout the code, and user input has been validated where necessary.

4. Factory Pattern
   I have used the factory pattern within the application to generate different types of tickets. There are some issues with this use of
   the factory pattern. The factory must be "primed" for instantiation by providing the necessary data to create the ticket. Once the
   factory has been primed it will instatiate the appropriate ticket type, or fall into a runtime error state due to misuse of the component.

5. Observer Pattern
   The observer pattern has been used several times throughout the program. It is used to notify UI components of changes that occur to the
   model independently of input. This information is useful to the user, and would not be directly accessable without the observer pattern.
   It is used a second time to manage ticket invalidation. When a ticket is no longer valid, it notifies observers (in this case, a container
   for the tickets) to allow the container of the tickets to discard them without having to check each ticket individually with every
   operation that occurs in the lifecycle of the application.

6. State Pattern
   The state pattern is used within this application to manage the UserAccount object, and the functionality the program can provide based
   upon those states. The number of states is quite small, however I feel this should be sufficient to show the implementation of the pattern.

7. UML Diagrams
   I have written the UML diagrams using PlantUML. I have tried to keep it as concise and readable as possible. PlantUML is a great tool,
   but it has limitations for organising and distributing the diagram components effectively when the diagram becomes too large. The
   relationships between classes, as well as all of the necessary components, packages and classes have been included in the class diagram.
   The state diagram is much smaller, as it only reflects the 3 states that the UserAccount can exist within it any one time. There are
   also only a small number of transitions. I have ensured to include all of the transitions, as well as the guards and actions appropriate
    to each transition.

8. Generics
   I was able to include a generic container within the application. The container is a wrapper for tickets and provides functionality
   for handling and invalidating tickets as they expire. The container is generic, and can be used to store any type of ticket.


User Guide:

To run the application, execute the ./gradlew command from the terminal.

This program does not have any persistence functionality, as that was not required in the assignment specification. I have hard coded
several elements of the application in order to save time and complexity.

When the application runs, you will be provided with a terminal interface, providing 5 options to execute the main functions of the program.
The options are as follows:

1. Ride Zones:
   This option will allow the user to ride the number of zones they specify in the follow up menu. If the user does not have appropriate
   ticketing, the application will prevent the user from riding that number of zones. The user will be notified that they require additional
   tickets to ride that number of zones.

2. Purchase Ticket:
   This option will allow a user to purchase a ticket, depending on the current state of their account. The state of the account should
   always be visible above the main menu. If the account is in Good Standing, the user will be able to purchase any number of tickets
   until the account enters arrears. The account is able to enter arrears from this state to allow fast purchasing of tickets in a rush.
   If the account is in an In Debt state, they will not be able to purchase tickets until this state is resolved. The user will be notified
   of this when they attempt to purchase a ticket.
   If the account has been Deactivated, the user will not be able to purchase tickets until the funds have been exhausted. It cannot enter
   arrears. Once deactivated, the account cannot be reactivated again.

3. Add Funds:
   This option allows the user to add funds to their account in order to purchase tickets. Adding funds is only available from the Good
   Standing and In Debt states. If the account has been Deactivated, the user will not be able to add additional funds.

4. Deactivate Account.
   If the user no longer wishes to be able to add funds to their account. They can deactivate it. Accounts must be in Good Standing in
   order to be Deactivated. This is to ensure that the account is not left in arrears on deactivation. Once deactivated, the account cannot
   be reactivated again. The user will still be able to use any funds still remaining in the account to purchase tickets, however they will
   be prevented from adding any additional funds to the account.

5. Exit:
   This option will exit the application.

Further functionality was intended for this appication, however I am still recovering from a string of illnesses including surgery and Covid.
I believe this is sufficient to demonstrate the use of the patterns required from the assignment specification.
