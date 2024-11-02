package Minions;

import fileio.CardInput;
import game.PlayedCard;
import org.w3c.dom.ls.LSOutput;


public class TheRipper extends PlayedCard {

	public TheRipper(CardInput card) {
		super(card);
	}

	@Override
	public void useAbility(PlayedCard attacked) {
		int attackDmg = attacked.getAttackDamage();
		attackDmg -= 2;
		if (attackDmg < 0) {
			attackDmg = 0;
		}
		attacked.setAttackDamage(attackDmg);
	}
}
