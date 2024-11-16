package org.poo.heroes;

import org.poo.fileio.CardInput;
import org.poo.game.Hero;
import org.poo.game.PlayedCard;

import java.util.ArrayList;

public class GeneralKocioraw extends Hero {

    public GeneralKocioraw(final CardInput cardInput) {
        super(cardInput);
    }

    /**
     * Uses the General Kocioraw's ability to increase
     * the attack damage of each card
     * in the specified row by 1.
     *
     * @param affectedRow the row of played cards that will be affected by the hero's ability
     */
    @Override
    public void useAbility(final ArrayList<PlayedCard> affectedRow) {
        for (PlayedCard card : affectedRow) {
            card.setAttackDamage(card.getAttackDamage() + 1);
        }
    }
}
