package game;

import fileio.CardInput;

import java.util.ArrayList;

public class Deck {
	private  ArrayList<CardInput> deck;

	public Deck(ArrayList<CardInput> deck) {
		this.deck = new ArrayList<CardInput>();
		for (CardInput card : deck) {
			this.deck.add(new CardInput(card));
		}
	}

	public ArrayList<CardInput> getDeck() {
		return deck;
	}

	public void setDeck(ArrayList<CardInput> deck) {
		this.deck = deck;
	}
}
