package core.viewers;

import java.util.ArrayList;
import java.util.List;

import core.creature.Creature;
import core.creature.CreatureTag;
import core.creature.stat.Stat;
import core.creature.stat.StatsContainer;
import core.traits.Trait;
import utils.Constants;

public class CreatureShopViewer extends Viewer {

    public static String getShopViewFor(Creature creature, StatsContainer statsContainer, boolean showLevel, boolean showCost, boolean showTraits, boolean showStats) {
        String name = creature.getName();
        int level = creature.getLevel();
        if (name.equals("Продано") || name.equals("Пусто") || name.equals("Sold") || name.equals("Empty")) {
            return name;
        }
        StringBuilder view = new StringBuilder();
        view.append(name);
        if (showLevel) {
            if (showCost) {
                view.append(" <").append(level).append(">")
                        .append(" (").append(creature.getCost()).append(")")
                        .append(" ".repeat(Constants.MAX_NAME_LEN.value - (String.valueOf(level).length() + String.valueOf(creature.getCost()).length() + name.length() + 4)));
            } else {
                view.append(" <").append(level).append(">")
                        .append(" ".repeat(Constants.MAX_NAME_LEN.value - (String.valueOf(level).length() + name.length() - 1)));
            }
        } else {
            if (showCost) {
                view.append(" (").append(creature.getCost()).append(")")
                        .append(" ".repeat(Constants.MAX_NAME_LEN.value - (String.valueOf(creature.getCost()).length() + name.length() + 3)));
            } else {
                view.append(" ".repeat(Constants.MAX_NAME_LEN.value - name.length() + 1));
            }
        }
        int traitsLen = 0;
        if (showTraits) {
            view.append("[");
            List<Trait> traits = new ArrayList<>(creature.getTraitContainer().getTags());
            for (int i = 0; i < traits.size(); i++) {
                view.append(traits.get(i));
                traitsLen += traits.get(i).getName().length();
                if (i < traits.size() - 1) {
                    view.append(", ");
                    traitsLen += 2;
                }
            }
            view.append("]");
            view.append(" ".repeat(Constants.MAX_TRAIT_NAME_LEN.value * 3 - traitsLen + 1));
        }

        StringBuilder statsView = new StringBuilder();

        if (showStats) {
            statsView.append("[AD: ").append(statsContainer.getTagValue(Stat.ATTACK)).append(", ").append("HP: ").append(statsContainer.getTagValue(Stat.HP)).append("]");
            statsView.append(" ".repeat(Constants.AD_HP_LEN.value - statsView.length()));
            statsView.append("<")
                    .append("PhysArm: ").append(statsContainer.getTagValue(Stat.PHYSICAL_ARMOR)).append(", ")
                    .append("MagArm: ").append(statsContainer.getTagValue(Stat.MAGIC_ARMOR)).append(", ")
                    .append("SP: ").append(statsContainer.getTagValue(Stat.SPELL_POWER)).append(", ")
                    .append("Speed: ").append(statsContainer.getTagValue(Stat.SPEED)).append(">");
            view.append(statsView);
            List<String> tagsView = new ArrayList<>();
            if (creature.hasTag(CreatureTag.POISONOUS)) {
                tagsView.add("Poison " + creature.getTagValue(CreatureTag.POISONOUS));
            }
            if (tagsView.size() > 0) {
                view.append(" {");
                view.append(tagsView.get(0));
                for (int i = 1; i < tagsView.size(); i++) {
                    view.append(", ").append(tagsView.get(i));
                }
                view.append("}");
            }
        }
        return view.toString();
    }
}
