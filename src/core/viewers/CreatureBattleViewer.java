package core.viewers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import core.creature.CreatureTag;
import core.item.Item;
import utils.Constants;
import utils.Pair;
import utils.TagContainer;

public class CreatureBattleViewer extends Viewer {

    public static List<String> getCreatureView(String name, int attack, int hp,
                                               List<Item> items, TagContainer<CreatureTag> creatureTags, List<Pair<String, String>> additionalParams) {
        int rowSize = Constants.BATTLE_VIEW_LENGTH.value;
        int height = Constants.BATTLE_VIEW_HEIGHT.value;
        int offset = Constants.BATTLE_VIEW_OFFSET.value;
        int curHeight = 0;
        int curLength = 0;
        StringBuilder view = new StringBuilder();
        String curAttack = " ".repeat(offset) + attack;
        String curHp = hp + " ".repeat(offset);
        String[] nameParts = name.split(" ");

        view.append("+").append("-".repeat(rowSize)).append("+\n");
        curHeight += 1;

        for (String namePart : nameParts) {
            if (curHeight >= height - 3) {
                break;
            }
            if (namePart.length() > rowSize) {
                continue;
            }
            if (curLength > 0) {
                if (namePart.length() + curLength < rowSize) {
                    view.append(" ").append(namePart);
                    curLength += namePart.length() + 1;
                } else {
                    view.append(" ".repeat(rowSize - curLength)).append("|\n");
                    view.append("|").append(" ".repeat(offset)).append(namePart);
                    curLength = namePart.length() + offset;
                    curHeight += 1;
                    continue;
                }
            } else {
                view.append("|");
                if (namePart.length() + curLength <= rowSize) {
                    view.append(" ".repeat(offset)).append(namePart);
                    curLength += namePart.length() + offset;
                } else {
                    view.append(" ".repeat(rowSize - curLength)).append("|\n");
                    view.append("|").append(" ".repeat(offset)).append(namePart);
                    curLength = namePart.length() + offset;
                    curHeight += 1;
                    continue;
                }
            }
        }
        view.append(" ".repeat(rowSize - curLength)).append("|\n");

        view.append("+").append("-".repeat(rowSize)).append("+\n");

        for (Item item : items) {
            if (!item.getName().equals("Bottomless Bag")) {
                StringBuilder itemRow = new StringBuilder();
                itemRow.append("|").append(" ".repeat(offset)).append(item.getName());
                itemRow.append(" ".repeat(rowSize - itemRow.length() + 1)).append("|\n");
                view.append(itemRow);
                curHeight++;
            }
        }

        if (creatureTags.getTagValue(CreatureTag.POISONOUS) > 0) {
            StringBuilder poisonRow = new StringBuilder();
            poisonRow.append("|").append(" ".repeat(offset)).append("Poison: ").append(creatureTags.getTagValue(CreatureTag.POISONOUS));
            poisonRow.append(" ".repeat(rowSize - poisonRow.length() + 1)).append("|\n");
            view.append(poisonRow);
            curHeight++;
        }

        for (Pair<String, String> param : additionalParams) {
            if ((height - 4) - curHeight < 0) {
                break;
            }
            StringBuilder paramRow = new StringBuilder();
            paramRow.append("|").append(" ".repeat(offset)).append(param.first).append(": ").append(param.second);
            paramRow.append(" ".repeat(rowSize - paramRow.length() + 1)).append("|\n");
            view.append(paramRow);
            curHeight++;
        }

        view.append(("|" + " ".repeat(rowSize) + "|\n").repeat((height - 4) - curHeight));

        view.append("|").append(curAttack).append(" ".repeat(rowSize - curHp.length() - curAttack.length())).append(curHp).append("|\n");
        view.append("+").append("-".repeat(rowSize)).append("+\n");

        return Arrays.stream(view.toString().split("\n")).collect(Collectors.toList());
    }
}
