package core.controllers.utils;

import java.util.concurrent.ThreadLocalRandom;

public class RandomController {

    public static int randomInt(int to, boolean inclusive) {
        return randomInt(to + (inclusive ? 1 : 0));
    }

    public static int randomInt(int to) {
        return ThreadLocalRandom.current().nextInt(to);
    }

    public static int randomInt() {
        return ThreadLocalRandom.current().nextInt();
    }

    public static int randomInt(int from, int to, boolean inclusive) {
        return randomInt(from, to + (inclusive ? 1 : 0));
    }

    public static int randomInt(int from, int to) {
        return ThreadLocalRandom.current().nextInt(from, to);
    }
}
