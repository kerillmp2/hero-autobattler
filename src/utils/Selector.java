package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import core.controllers.utils.MessageController;
import core.shop.HasShopView;

public class Selector {

    public static int creatureSellingSelect(List<? extends HasName> boardCreatures) {
        return creatureSellingSelect(boardCreatures, new ArrayList<>(), true);
    }

    public static int creatureSellingSelect(List<? extends HasName> boardCreatures, List<? extends HasName> benchCreatures, boolean hasBackOption) {
        int selectedNumber = -1;
        int from = hasBackOption ? 0 : 1;
        while (selectedNumber == -1) {
            int counter = from;
            counter++;
            for (HasName ignored : boardCreatures) {
                counter += 1;
            }
            for (HasName ignored : benchCreatures) {
                counter += 1;
            }
            if (counter >= 1) {
                selectedNumber = readCommandNumber(from, counter - 1);
                if (selectedNumber == -1) {
                    MessageController.print("Input number from " + from + " to " + (counter - 1));
                }
            } else {
                selectedNumber = 0;
            }
        }
        return selectedNumber;
    }

    public static int select(List<?> options, int from) {
        int selectedNumber = -1;
        while (selectedNumber == -1) {
            int counter = from;
            for (Object ignored : options) {
                counter += 1;
            }
            if (counter >= 1) {
                selectedNumber = readCommandNumber(from, counter - 1);
                if (selectedNumber == -1) {
                    MessageController.print(
                            "Введите число от " + from + " до " + (counter - 1),
                            "Input number from " + from + " to " + (counter - 1)
                    );
                }
            } else {
                selectedNumber = 0;
            }
        }
        return selectedNumber;
    }

    public static int select(Object... options) {
        return select(List.of(options), 0);
    }

    public static int shopSelect(List<? extends HasShopView> items, Pair<String, Integer>... additionalOptions) {
        int selectedNumber = -1;

        while (selectedNumber == -1) {
            int counter = 0;
            for (HasShopView ignored : items) {
                counter += 1;
            }

            for (Pair<String, Integer> ignored : additionalOptions) {
                counter += 1;
            }

            if (counter >= 1) {
                selectedNumber = readCommandNumber(0, counter);
                if (selectedNumber == -1) {
                    MessageController.print(
                            "Введите число от 0 до " + counter,
                            "Input number from 0 to " + counter);
                }
            } else {
                selectedNumber = 0;
            }
        }
        return selectedNumber;
    }

    private static int readCommandNumber(int from, int to) {
        Scanner in = new Scanner(System.in);
        int input = 123123;
        try {
            input = in.nextInt();
            if (input >= from && input <= to) {
                return input;
            } else {
                return -1;
            }
        } catch (Exception e) {
            return -1;
        }
    }
}