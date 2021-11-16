package core.utils;

import java.util.List;
import java.util.Scanner;

import core.shop.HasShopView;
import core.shop.ShopView;

public class Selector {

    public static int select(List<? extends HasName> options) {
        int selectedNumber = -1;
        while (selectedNumber == -1) {
            int counter = 0;
            for (HasName option : options) {
                MessageController.print(String.format("%d. %s\n", counter, option.getName()));
                counter += 1;
            }
            if (counter >= 1) {
                selectedNumber = readCommandNumber(0, counter - 1);
                if (selectedNumber == -1) {
                    MessageController.print("Введите число от 0 до " + (counter - 1));
                }
            } else {
                selectedNumber = 0;
            }
        }
        return selectedNumber;
    }

    public static int shopSelect(List<? extends HasShopView> items, Pair<String, Integer>... additionalOptions) {
        int selectedNumber = -1;

        while (selectedNumber == -1) {
            int counter = 0;
            MessageController.print("0. Покинуть магазин\n");
            for (HasShopView item : items) {
                counter += 1;
                MessageController.print(String.format("%d. %s\n", counter, item.getShopView()));
            }

            for (Pair<String, Integer> option : additionalOptions) {
                counter += 1;
                MessageController.print(String.format("%d. %s (%d)\n", counter, option.first, option.second));
            }

            MessageController.print(ShopView.getFooter());

            if (counter >= 1) {
                selectedNumber = readCommandNumber(0, counter);
                if (selectedNumber == -1) {
                    MessageController.print("Введите число от 0 до " + counter);
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