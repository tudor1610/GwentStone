package Heros;

import fileio.CardInput;
import game.Hero;
import game.PlayedCard;

import java.util.ArrayList;

public class GeneralKocioraw extends Hero {

	public GeneralKocioraw (CardInput cardInput) {
		super(cardInput);
	}

	@Override
	public void useAbility(ArrayList<PlayedCard> affectedRow) {
		for (PlayedCard card : affectedRow) {
			card.setAttackDamage(card.getAttackDamage() + 1);
		}
	}
}
