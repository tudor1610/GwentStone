package Heros;

import fileio.CardInput;
import game.Hero;
import game.PlayedCard;

import java.util.ArrayList;

public class LordRoyce extends Hero {

	public LordRoyce (CardInput cardInput) {
		super(cardInput);
	}

	@Override
	public void useAbility(ArrayList<PlayedCard> affectedRow) {
		for (PlayedCard card : affectedRow) {
			card.setFrozen(true);
		}
	}
}
