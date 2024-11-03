package Heros;

import fileio.CardInput;
import game.Hero;
import game.PlayedCard;

import java.util.ArrayList;

public class KingMudface extends Hero {

	public KingMudface (CardInput cardInput) {
		super(cardInput);
	}

	@Override
	public void useAbility(ArrayList<PlayedCard> affectedRow) {
		for (PlayedCard card : affectedRow) {
			card.setHealth(-1, false);
		}
	}
}
