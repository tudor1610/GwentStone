package org.poo.game;

import org.poo.fileio.CardInput;

import java.util.ArrayList;

public final class Deck {
    private  ArrayList<CardInput> deck;

    public Deck(final ArrayList<CardInput> deck) {
        this.deck = new ArrayList<CardInput>();
        for (CardInput card : deck) {
            this.deck.add(new CardInput(card));
        }
    }

    public ArrayList<CardInput> getDeck() {
        return deck;
    }

    public void setDeck(final ArrayList<CardInput> deck) {
        this.deck = deck;
    }
}
