package game;

import fileio.CardInput;

import java.util.ArrayList;

public class Player {
	private int mana;
	private CardInput hero;
	private ArrayList<CardInput> hand;
	private ArrayList<CardInput> deck;

	public Player(int mana, ArrayList<CardInput> deck, CardInput hero) {
		this.mana = mana;
		this.hero = hero;
		this.hand = new ArrayList<CardInput>();
		this.deck = deck;
	}

	public void setMana(int mana, Boolean ok) {
		if (ok == Boolean.TRUE)
			this.mana += mana;
		else
			this.mana = mana;
	}

	public void setHero(CardInput hero) {
		this.hero = hero;
	}

	public void setHand(ArrayList<CardInput> hand) {
		this.hand = hand;
	}

	public void setDeck(ArrayList<CardInput> deck) {
		this.deck = deck;
	}

	public int getMana() {
		return mana;
	}

	public ArrayList<CardInput> getHand() {
		return hand;
	}

	public ArrayList<CardInput> getDeck() {
		return deck;
	}

	public CardInput getHero() {
		return hero;
	}

	@Override
	public String toString() {
		return "Player{" +
				"hero=" + hero +
				", hand=" + hand +
				", deck=" + deck +
				", mana=" + mana +
				'}';
	}
}
