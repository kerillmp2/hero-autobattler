package core.utils;

public class MessageController {
    public static void print(String message) {
        if (!message.equals("") && !message.equals(Constants.UNDEFINED.name)) {
            System.out.println(message);
        }
    }
}
