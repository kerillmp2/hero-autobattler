package core.viewers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import core.creature.Creature;
import core.item.Item;
import utils.Constants;

public class ItemChoiceViewer extends Viewer {

    private static final int ITEM_OFFSET = Constants.MAP_OFFSET.value;
    private static final int ITEM_HEIGHT = Constants.ITEM_VIEW_HEIGHT.value;

    public static String getItemChoiceView(List<Item> items, Creature creature) {
        Window window = new Window();
        window.lineWithAngles();
        window.line(creature.getName() + " has reached level " + creature.getLevel() + "! Choose one item to equip!");
        window.lineWithAngles();
        window.line(creature.getShopView(false, false, false, true));
        window.lineWithAngles();
        List<List<String>> splittedItemViews = new ArrayList<>();
        for (Item item : items) {
            splittedItemViews.add(Arrays.stream(ItemViewer.getItemView(item).split("\n")).collect(Collectors.toList()));
        }
        for (int i = 0; i < ITEM_HEIGHT; i++) {
            StringBuilder inline = new StringBuilder();
            for (List<String> itemView : splittedItemViews) {
                inline.append(itemView.get(i));
                inline.append(" ".repeat(ITEM_OFFSET));
            }
            window.line(inline.toString());
        }
        window.emptyLine();
        window.lineWithAngles();
        window.list(items.stream().map(Item::getNameWithRarity).collect(Collectors.toList()),
                true, 1, false, false);
        window.lineWithAngles();
        return window.getView();
    }
}
