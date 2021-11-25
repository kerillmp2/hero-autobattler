package core.creature.skills;

import core.battlefield.BattlefieldCreature;
import core.controllers.BattleController;

public interface CreatureSkill {
    String cast(BattleController battleController, BattlefieldCreature user);
}
