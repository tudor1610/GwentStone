package game;

import fileio.CardInput;

import java.util.ArrayList;

public class PlayedCard {
	private int mana;
	private int attackDamage;
	private int health;
	private String description;
	private ArrayList<String> colors;
	private String name;
	private boolean frozen;
	private boolean canAttack;

	public PlayedCard() {

	}

	public PlayedCard(CardInput card) {
		mana = card.getMana();
		attackDamage = card.getAttackDamage();
		health = card.getHealth();
		description = card.getDescription();
		colors = new ArrayList<>();
		colors.addAll(card.getColors());
		name = card.getName();
		frozen = false;
		canAttack = true;
	}

	public void useAbility(PlayedCard attacked) {
		System.out.println("Ceva clar nu a mers bine");
	}

	public boolean getCanAttack() {
		return canAttack;
	}

	public void setCanAttack(boolean canAttack) {
		this.canAttack = canAttack;
	}

	public int getMana() {
		return mana;
	}

	public void setMana(int mana) {
		this.mana = mana;
	}

	public int getAttackDamage() {
		return attackDamage;
	}

	public void setAttackDamage(int attackDamage) {
		this.attackDamage = attackDamage;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health, Boolean k) {
		if (k) {
			this.health = health;
		} else {
			this.health = this.health - health;
		}

	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ArrayList<String> getColors() {
		return colors;
	}

	public void setColors(ArrayList<String> colors) {
		this.colors = colors;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isFrozen() {
		return frozen;
	}

	public void setFrozen(boolean frozen) {
		this.frozen = frozen;
	}
}
