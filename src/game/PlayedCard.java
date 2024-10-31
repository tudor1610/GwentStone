package game;

import fileio.CardInput;

public class PlayedCard {
	private CardInput card;
	private boolean frozen;

	public PlayedCard(CardInput card, Boolean isFrozen) {
		this.card = card;
		frozen = isFrozen;
	}

}
