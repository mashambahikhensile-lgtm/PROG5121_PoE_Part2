
package com.mycompany.chatapp.prog5121_poe_part1;

import java.util.Scanner;

/** Runs QuickChat registration, login, and the message menu in the console. */
public class PROG5121_PoE_Part1 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter your last name: ");
        String lastName = scanner.nextLine();

        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        System.out.print("Enter your cell phone number (+27 followed by 9 digits): ");
        String cellPhoneNumber = scanner.nextLine();

        Login login = new Login(
                firstName, lastName, username, password, cellPhoneNumber
        );

        if (login.checkUserName()) {
            System.out.println("Username successfully captured.");
        } else {
            System.out.println("Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.");
        }

        if (login.checkPasswordComplexity()) {
            System.out.println("Password successfully captured.");
        } else {
            System.out.println("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.");
        }

        if (login.checkCellPhoneNumber()) {
            System.out.println("Cell phone number successfully added.");
        } else {
            System.out.println("Cell phone number incorrectly formatted or does not contain international code.");
        }

        String registrationResult = login.registerUser();
        System.out.println(registrationResult);

        if (!registrationResult.equals("User registered successfully.")) {
            System.out.println("Registration failed. Please restart the application and register with valid details.");
            return;
        }

        System.out.print("Enter your username to login: ");
        String enteredUsername = scanner.nextLine();

        System.out.print("Enter your password to login: ");
        String enteredPassword = scanner.nextLine();

        boolean loginSuccessful = login.loginUser(
                enteredUsername, enteredPassword
        );

        System.out.println(login.returnLoginStatus(loginSuccessful));

        if (!loginSuccessful) {
            return;
        }

        System.out.println("Welcome to QuickChat.");

        System.out.print("How many messages would you like to enter? ");

        int numberOfMessages;

        try {
            numberOfMessages = Integer.parseInt(scanner.nextLine().trim());

            if (numberOfMessages < 0) {
                System.out.println("Please enter zero or a positive number.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid whole number.");
            return;
        }

        Message.setScanner(scanner);

        int messagesProcessed = 0;
        boolean quit = false;

        while (!quit) {
            System.out.println("\n1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.print("Choose an option: ");

            String option = scanner.nextLine().trim();

            switch (option) {
                case "1":
                    if (messagesProcessed >= numberOfMessages) {
                        System.out.println(
                                "You have entered the number of messages you selected."
                        );
                        break;
                    }

                    for (int i = messagesProcessed;
                            i < numberOfMessages; i++) {

                        System.out.println(
                                "\nMessage " + (i + 1)
                                + " of " + numberOfMessages
                        );

                        System.out.print(
                                "Enter recipient number (+27 followed by 9 digits): "
                        );
                        String recipient = scanner.nextLine().trim();

                        System.out.print(
                                "Enter your message (maximum 250 characters): "
                        );
                        String messageText = scanner.nextLine();

                        while (messageText.length() > 250) {
                            int excessCharacters = messageText.length() - 250;

                            System.out.println(
                                    "Message exceeds 250 characters by "
                                    + excessCharacters
                                    + "; please reduce the size."
                            );

                            System.out.print("Enter your message again: ");
                            messageText = scanner.nextLine();
                        }

                        Message message = new Message(
                                i + 1, recipient, messageText
                        );

                        System.out.println(
                                "Message ID generated: "
                                + message.getMessageID()
                        );

                        System.out.println(message.checkRecipientCell());
                        System.out.println(message.SentMessage());

                        if ("Sent".equals(message.getAction())) {
                            System.out.println(message.getDetails());
                        }

                        messagesProcessed++;
                    }

                    Message counter = new Message(0, "", "");
                    System.out.println(
                            "Total messages sent: "
                            + counter.returnTotalMessages()
                    );
                    break;

                case "2":
                    Message messagePrinter = new Message(0, "", "");
                    String sentMessages = messagePrinter.printMessages();

                    if (sentMessages.isEmpty()) {
                        System.out.println("No messages have been sent yet.");
                    } else {
                        System.out.println("Recently sent messages:");
                        System.out.println(sentMessages);
                    }
                    break;

                case "3":
                    quit = true;
                    System.out.println("Goodbye.");
                    break;

                default:
                    System.out.println(
                            "Invalid option. Please choose 1, 2, or 3."
                    );
            }
        }
    }
}
