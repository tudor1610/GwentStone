package game;

import fileio.CardInput;

import java.util.ArrayList;

/**
 * Represents a card that has been played in a game, including its attributes
 * such as mana, attack damage, health, description, colors, name, and statuses
 * like frozen or ability to attack.
 */
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

    public PlayedCard(final CardInput card) {
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

    /**
     * Uses the ability of the current card on a targeted card.
     *
     * @param attacked the card that is being targeted by the ability
     */
    public void useAbility(final PlayedCard attacked) {

    }

    /**
     * Checks if the card can attack.
     *
     * @return true if the card can attack, false otherwise.
     */
    public boolean getCanAttack() {
        return canAttack;
    }

    /**
     * Sets whether the card can attack.
     *
     * @param canAttack the new attack capability status to set.
     */
    public void setCanAttack(final boolean canAttack) {
        this.canAttack = canAttack;
    }

    /**
     * Retrieves the mana value of the card.
     *
     * @return the mana value of the card.
     */
    public int getMana() {
        return mana;
    }

    /**
     * Sets the mana value for this card.
     *
     **/
    public void setMana(final int mana) {
        this.mana = mana;
    }

    /**
     * Retrieves the attack damage of the card.
     *
     * @return the attack damage value of the card.
     */
    public int getAttackDamage() {
        return attackDamage;
    }

    /**
     * Sets the attack damage value for this card.
     *
     * @param attackDamage the new attack damage to set
     */
    public void setAttackDamage(final int attackDamage) {
        this.attackDamage = attackDamage;
    }

    /**
     * Retrieves the health of the card.
     *
     * @return the health value of the card.
     */
    public int getHealth() {
        return health;
    }

    /**
     * Sets the health of the card. If the flag is set to true,
     * the health is assigned the new value.
     * If the flag is set to false, the health is decreased by the specified value.
     *
     * @param hp the value to set or decrease the health by
     * @param k a flag indicating whether to set (true) or decrease (false) the health
     */
    public void setHealth(final int hp, final Boolean k) {
        if (k) {
            this.health = hp;
        } else {
            this.health -= hp;
        }
    }

    /**
     * Retrieves the description of the card.
     *
     * @return the description of the card.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description for the card.
     *
     * @param description the new description to set
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Retrieves the colors associated with the card.
     *
     */
    public ArrayList<String> getColors() {
        return colors;
    }

    /**
     * Sets the colors for the card.
     *
     * @param colors the colors to be set for the card
     */
    public void setColors(final ArrayList<String> colors) {
        this.colors = colors;
    }

    /**
     * Retrieves the name of the card.
     *
     * @return the name of the card.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the card.
     *
     * @param name the new name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Checks if the card is currently frozen.
     *
     * @return true if the card is frozen, false otherwise.
     */
    public boolean isFrozen() {
        return frozen;
    }

    /**
     * Sets the frozen status of the card.
     *
     * @param frozen the new frozen status to set
     */
    public void setFrozen(final boolean frozen) {
        this.frozen = frozen;
    }
}
