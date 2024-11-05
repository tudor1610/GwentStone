package heros;

import fileio.CardInput;
import game.Hero;
import game.PlayedCard;

import java.util.ArrayList;

public class EmpressThorina extends Hero {

    public EmpressThorina(final CardInput cardInput) {
        super(cardInput);
    }

    /**
     * Uses the Empress Thorina's ability on the specified row of played cards.
     * The ability targets the card with the highest health in the row and removes it.
     *
     * @param affectedRow the row of played cards that will be affected by the ability
     */
    @Override
    public void useAbility(final ArrayList<PlayedCard> affectedRow) {
        int pos = 0;
        int health = 0;
        for (int i = 0; i < affectedRow.size(); ++i) {
            if (affectedRow.get(i).getHealth() > health) {
                health = affectedRow.get(i).getHealth();
                pos = i;
            }
        }
        if (affectedRow.size() > 0) {
            affectedRow.remove(pos);
        }

    }
}
