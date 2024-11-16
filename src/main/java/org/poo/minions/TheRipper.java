package org.poo.minions;

import org.poo.fileio.CardInput;
import org.poo.game.PlayedCard;

public class TheRipper extends PlayedCard {

    public TheRipper(final CardInput card) {
        super(card);
    }

    /**
     * Reduces the attack damage of the targeted card by 2. If the resulting attack
     * damage is less than 0, it sets the attack damage to 0.
     *
     * @param attacked The card that is being targeted by the ability.
     */
    @Override
    public void useAbility(final PlayedCard attacked) {
        int attackDmg = attacked.getAttackDamage();
        attackDmg -= 2;
        if (attackDmg < 0) {
            attackDmg = 0;
        }
        attacked.setAttackDamage(attackDmg);
    }
}
