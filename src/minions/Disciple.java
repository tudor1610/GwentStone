package minions;

import fileio.CardInput;
import game.PlayedCard;

public class Disciple extends PlayedCard {
    public Disciple(final CardInput card) {
        super(card);
    }

    /**
     * Uses the ability of the Disciple card on a targeted card.
     * This specific implementation decreases the health of the targeted
     * card by 2 points.
     *
     * @param attacked the card that is being targeted*/
    @Override
    public void useAbility(final PlayedCard attacked) {
        final int damage = -2;
        attacked.setHealth(damage, false);
    }
}
