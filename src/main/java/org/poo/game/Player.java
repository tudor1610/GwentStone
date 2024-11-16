package org.poo.game;



import org.poo.heroes.GeneralKocioraw;
import org.poo.heroes.KingMudface;
import org.poo.heroes.LordRoyce;
import org.poo.fileio.CardInput;
import org.poo.heroes.EmpressThorina;

import java.util.ArrayList;

public class Player {
    private int mana;
    private Hero hero;
    private ArrayList<CardInput> hand;
    private ArrayList<CardInput> deck;
    private int wins;

    public Player(final int startMana, final ArrayList<CardInput> deck,
                  final CardInput hero, final int nrWins) {
        this.mana = startMana;
        this.hand = new ArrayList<CardInput>();
        this.deck = deck;
        String name = hero.getName();
        switch (name) {
            case "Lord Royce":
                this.hero = new LordRoyce(hero);
                break;
            case "Empress Thorina":
                this.hero = new EmpressThorina(hero);
                break;
            case "King Mudface":
                this.hero = new KingMudface(hero);
                break;
            default:
                this.hero = new GeneralKocioraw(hero);
                break;
        }
        wins = nrWins;
    }

    /**
     * Retrieves the hero assigned to the player.
     *
     * @return the player's current Hero object.
     */
    public Hero getHero() {
        return hero;
    }

    /**
     * Sets the hero for the player.
     *
     * @param hero the Hero object to be assigned to the player.
     */
    public void setHero(final Hero hero) {
        this.hero = hero;
    }

    /**
     * Updates the player's mana based on the provided value.
     *
     * @param addMana the amount of mana to be added or deducted from the current mana.
     * @param ok a boolean flag indicating whether the mana update should be applied.
     */
    public void setMana(final int addMana, final Boolean ok) {
        if (ok) {
            this.mana += addMana;
        }
    }

    /**
     * Retrieves the number of wins the player has achieved.
     *
     * @return the player's win count as an integer.
     */
    public int getWins() {
        return wins;
    }

    /**
     * Increments the win count for the player by one.
     */
    public void newWin() {
        wins = wins + 1;
    }

    /**
     * Sets the hand of cards for the player.
     *
     * @param hand an ArrayList containing CardInput objects representing the player's hand.
     */
    public void setHand(final ArrayList<CardInput> hand) {
        this.hand = hand;
    }

    /**
     * Sets the deck of cards for the player.
     *
     * @param deck an ArrayList containing CardInput objects representing the player's deck.
     */
    public void setDeck(final ArrayList<CardInput> deck) {
        this.deck = deck;
    }

    /**
     * Retrieves the current mana of the player.
     *
     * @return the player's mana as an integer.
     */
    public int getMana() {
        return mana;
    }

    /**
     * Retrieves the current hand of the player.
     *
     * @return an ArrayList containing the cards in the player's hand.
     */
    public ArrayList<CardInput> getHand() {
        return hand;
    }

    /**
     * Retrieves the deck of cards*/
    public ArrayList<CardInput> getDeck() {
        return deck;
    }


    /**
     * Returns a string representation of the Player object.
     *
     * @return A string representation of the Player object, including the hero,
     *         hand, deck, and mana fields.
     */
    @Override
    public String toString() {
        return "Player{"
                + "hero=" + hero
                + ", hand=" + hand
                + ", deck=" + deck
                + ", mana=" + mana
                + '}';
    }
}
