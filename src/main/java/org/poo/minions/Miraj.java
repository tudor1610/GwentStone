package org.poo.minions;

import org.poo.fileio.CardInput;
import org.poo.game.PlayedCard;

public class Miraj extends PlayedCard {
    public Miraj(final CardInput card) {
        super(card);
    }

    /**
     * Uses the ability of the Miraj card on a targeted card.
     * The ability swaps the health values
     * of the Miraj card and the targeted card.
     *
     * @param attacked the card that is being targeted by the Miraj's ability
     */
    @Override
    public void useAbility(final PlayedCard attacked) {
        int health =  attacked.getHealth();
        int myHealth = this.getHealth();
        this.setHealth(health, true);
        attacked.setHealth(myHealth, true);
    }
}
