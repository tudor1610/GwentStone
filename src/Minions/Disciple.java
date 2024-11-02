package Minions;

import fileio.CardInput;
import game.PlayedCard;

public class Disciple extends PlayedCard {
	public Disciple(CardInput card) {
		super(card);
	}

	@Override
	public void useAbility(PlayedCard attacked) {
		attacked.setHealth(-2, false);
	}
}
