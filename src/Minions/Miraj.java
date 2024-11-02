package Minions;

import fileio.CardInput;
import game.PlayedCard;

public class Miraj extends PlayedCard {
	public Miraj(CardInput card) {
		super(card);
	}

	@Override
	public void useAbility(PlayedCard attacked) {
		int health =  attacked.getHealth();
		int myHealth = this.getHealth();
		this.setHealth(health, true);
		attacked.setHealth(myHealth, true);
	}
}
