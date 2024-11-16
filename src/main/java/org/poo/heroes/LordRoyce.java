package org.poo.heroes;

import org.poo.fileio.CardInput;
import org.poo.game.Hero;
import org.poo.game.PlayedCard;

import java.util.ArrayList;

public class LordRoyce extends Hero {

    public LordRoyce(final CardInput cardInput) {
        super(cardInput);
    }

    /**
     * Freezes all cards in the specified row, preventing them from performing actions.
     *
     * @param affectedRow the row of played cards that will be affected and frozen
     */
    @Override
    public void useAbility(final ArrayList<PlayedCard> affectedRow) {
        for (PlayedCard card : affectedRow) {
            card.setFrozen(true);
        }
    }
}
