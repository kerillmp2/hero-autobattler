package core.player;

import core.traits.Trait;
import core.controllers.TraitContainer;

public class TraitsController {
    private final TraitContainer traitContainer;

    private TraitsController(TraitContainer traitContainer) {
        this.traitContainer = traitContainer;
    }

    public static TraitsController build() {
        return new TraitsController(new TraitContainer());
    }

    public void addTrait(Trait trait) {
        traitContainer.addTagValue(trait, 1);
    }

    public void removeTrait(Trait trait) {
        traitContainer.setTagValue(trait, Math.max(traitContainer.getTagValue(trait) - 1, 0));
    }

    public int getTraitValue(Trait trait) {
        return traitContainer.getTagValue(trait);
    }
}
