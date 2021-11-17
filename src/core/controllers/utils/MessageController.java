package core.controllers.utils;

import core.utils.Constants;

public class MessageController {
    public static void print(String message) {
        if (!message.equals("") && !message.equals(Constants.UNDEFINED.name)) {
            System.out.println(message);
        }
    }
}
