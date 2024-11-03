package game;

import Heros.EmpressThorina;
import Heros.GeneralKocioraw;
import Heros.KingMudface;
import Heros.LordRoyce;
import fileio.CardInput;

import java.util.ArrayList;

public class Player {
	private int mana;
	public Hero hero;
	private ArrayList<CardInput> hand;
	private ArrayList<CardInput> deck;
	private int wins;

	public Player(int mana, ArrayList<CardInput> deck, CardInput hero, int nrWins) {
		this.mana = mana;
		this.hand = new ArrayList<CardInput>();
		this.deck = deck;
		String name = hero.getName();
		if (name.equals("Lord Royce")) {
			this.hero = new LordRoyce(hero);
		} else if (name.equals("Empress Thorina")) {
			this.hero = new EmpressThorina(hero);
		} else if (name.equals("King Mudface")) {
			this.hero = new KingMudface(hero);
		} else if (name.equals("General Kocioraw")) {
			this.hero = new GeneralKocioraw(hero);
		}
		wins = nrWins;
	}

	public void setMana(int mana, Boolean ok) {
		if (ok)
			this.mana += mana;
	}

	public int getWins() {
		return wins;
	}

	public void newWin() {
		wins = wins + 1;
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
