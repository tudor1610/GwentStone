package org.poo.game;

import org.poo.fileio.CardInput;

import java.util.ArrayList;

public class Hero {
    private int mana;
    private int health;
    private String description;
    private ArrayList<String> colors;
    private String name;
    private boolean canAttack;

    public Hero(final CardInput cardInput) {
        final int hp = 30;
        mana = cardInput.getMana();
        health = hp;
        description = cardInput.getDescription();
        colors = cardInput.getColors();
        name = cardInput.getName();
        canAttack = true;
    }

    /**
     * Uses the hero's ability on the specified row of played cards.
     *
     * @param affectedRow the row of played cards that will be affected by the hero's ability
     */
    public void useAbility(final ArrayList<PlayedCard> affectedRow) {
    }

    /**
     * Retrieves the current mana of the hero.
     *
     * @return the current mana of the hero
     */
    public int getMana() {
        return mana;
    }

    /**
     * Checks if the hero is currently able to attack.
     *
     * @return a boolean indicating whether the hero can attack
     */
    public boolean isCanAttack() {
        return canAttack;
    }

    /**
     * Sets the ability of the hero to attack.
     *
     * @param canAttack a boolean indicating*/
    public void setCanAttack(final boolean canAttack) {
        this.canAttack = canAttack;
    }

    /**
     * Sets the mana value for the hero.
     *
     * @param mana the new mana value to set
     */
    public void setMana(final int mana) {
        this.mana = mana;
    }

    /**
     * Retrieves the description of the hero.
     *
     * @return the description of the hero
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description for the hero.
     *
     * @param description the new description to set
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Retrieves the current health of the hero.
     *
     * @return the hero's*/
    public int getHealth() {
        return health;
    }

    /**
     * Sets the health of the hero.
     *
     * @param health the new health value to set
     */
    public void setHealth(final int health) {
        this.health = health;
    }

    /**
     * Retrieves the name of the hero.
     *
     * @return the name of the hero
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name for the hero.
     *
     * @param name the new name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Retrieves the list of colors associated with the hero.
     *
     * @return an ArrayList of Strings representing the hero's colors.
     */
    public ArrayList<String> getColors() {
        return colors;
    }

    /**
     * Sets the colors associated with the hero.
     *
     * @param colors the list of colors to*/
    public void setColors(final ArrayList<String> colors) {
        this.colors = colors;
    }
}
