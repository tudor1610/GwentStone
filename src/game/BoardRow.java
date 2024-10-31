package game;

import java.util.ArrayList;

public class BoardRow {
	private ArrayList<PlayedCard>row;

	public BoardRow() {
		row = new ArrayList<>(5);
	}

	public ArrayList<PlayedCard> getRow() {
		return row;
	}

	public void setRow(ArrayList<PlayedCard> row) {
		this.row = row;
	}
}
