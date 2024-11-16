package org.poo.game;

import java.util.ArrayList;

public final class BoardRow {
    private ArrayList<PlayedCard> row;

    public BoardRow() {
        row = new ArrayList<PlayedCard>();
    }

    public ArrayList<PlayedCard> getRow() {
        return row;
    }

    public void setRow(final ArrayList<PlayedCard> row) {
        this.row = row;
    }
}
