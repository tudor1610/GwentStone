package minions;

import fileio.CardInput;
import game.PlayedCard;

public class TheCursedOne extends PlayedCard {
    public TheCursedOne(final CardInput card) {
        super(card);
    }

    /**
     * Swaps the health and attack damage values of the targeted card.
     *
     * @param attacked the card whose health and attack damage will be swapped
     */
    @Override
    public void useAbility(final PlayedCard attacked) {
        int health = attacked.getHealth();
        int attackDmg = attacked.getAttackDamage();
        attacked.setAttackDamage(health);
        attacked.setHealth(attackDmg, true);
    }
}
