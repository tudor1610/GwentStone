package game;

import fileio.CardInput;

import java.util.ArrayList;

public class Hero {
	private int mana;
	private int health;
	private String description;
	private ArrayList<String> colors;
	private String name;
	private boolean canAttack;

	public Hero(CardInput cardInput) {
		mana = cardInput.getMana();
		health = 30;
		description = cardInput.getDescription();
		colors = cardInput.getColors();
		name = cardInput.getName();
		canAttack = true;
	}

	public void useAbility(ArrayList<PlayedCard> affectedRow) {
		System.out.println("Nu i bine");
	}

	public int getMana() {
		return mana;
	}

	public boolean isCanAttack() {
		return canAttack;
	}

	public void setCanAttack(boolean canAttack) {
		this.canAttack = canAttack;
	}

	public void setMana(int mana) {
		this.mana = mana;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<String> getColors() {
		return colors;
	}

	public void setColors(ArrayList<String> colors) {
		this.colors = colors;
	}
}
