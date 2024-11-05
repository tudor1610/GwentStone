package heros;

import fileio.CardInput;
import game.Hero;
import game.PlayedCard;

import java.util.ArrayList;

public class KingMudface extends Hero {

    public KingMudface(final CardInput cardInput) {
        super(cardInput);
    }

    /**
     * Uses the hero's ability on a specified row of played cards by decreasing the health
     * of each card in the row by 1.
     *
     * @param affectedRow the row of played cards that will be affected by the hero's ability
     */
    @Override
    public void useAbility(final ArrayList<PlayedCard> affectedRow) {
        for (PlayedCard card : affectedRow) {
            card.setHealth(-1, false);
        }
    }
}
