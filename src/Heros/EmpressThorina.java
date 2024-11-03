package Heros;

import fileio.CardInput;
import game.Hero;
import game.PlayedCard;

import java.util.ArrayList;

public class EmpressThorina extends Hero {

	public EmpressThorina (CardInput cardInput) {
		super(cardInput);
	}

	@Override
	public void useAbility(ArrayList<PlayedCard> affectedRow) {
		int pos = 0;
		int health = 0;
		for (int i = 0 ; i < affectedRow.size(); ++i) {
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
