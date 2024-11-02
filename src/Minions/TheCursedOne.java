package Minions;

import fileio.CardInput;
import game.PlayedCard;

public class TheCursedOne extends PlayedCard {
	public TheCursedOne(CardInput card) {
		super(card);
	}

	@Override
	public void useAbility(PlayedCard attacked) {
		int health = attacked.getHealth();
		int attackDmg = attacked.getAttackDamage();
		attacked.setAttackDamage(health);
		attacked.setHealth(attackDmg, true);
	}
}
