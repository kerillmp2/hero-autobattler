package core.controllers.utils;

import utils.Constants;

public class MessageController {
    public static void print(String message) {
        if (Constants.PRINT_MESSAGES_IN_CONTROLLER.value == 1) {
            if (!message.equals("") && !message.equals(Constants.UNDEFINED.name)) {
                System.out.println(message);
            }
        }
    }

    public static void print(String messageRU, String messageENG) {
        if (Constants.PRINT_RU_MESSAGES.value == 1) {
            print(messageRU);
        }
        if (Constants.PRINT_ENG_MESSAGES.value == 1) {
            print(messageENG);
        }
    }

    public static void forcedPrint(String message) {
        if (!message.equals("") && !message.equals(Constants.UNDEFINED.name)) {
            System.out.println(message);
        }
    }
}
