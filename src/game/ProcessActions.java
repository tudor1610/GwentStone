package game;

import minions.Disciple;
import minions.Miraj;
import minions.TheCursedOne;
import minions.TheRipper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import fileio.CardInput;

import java.util.ArrayList;

public final class ProcessActions {

    private ArrayList<BoardRow> board;
    private ObjectMapper objectMapper;

    public ProcessActions() {
        final int nrRows = 4;
        board = new ArrayList<>(nrRows);
        for (int i = 0; i < nrRows; ++i) {
            board.add(new BoardRow());
        }
        objectMapper = new ObjectMapper();
    }

    /**
     * Updates the cards on the board for the specified player after their turn.
     *
     * @param currPlayer the index of the current player. If 1, updates the first two rows;
     * otherwise, updates the last two rows. Resets the cards in the respective rows by
     * enabling their ability to attack and thawing any frozen status.
     */
    public void updateCardsAfterTurn(final int currPlayer) {
        if (currPlayer == 1) {
            for (int i = 0; i <= 1; ++i) {
                for (PlayedCard card : board.get(i).getRow()) {
                    card.setCanAttack(true);
                    card.setFrozen(false);
                }
            }
        } else {
            final int finalRow = 3;
            for (int i = 2; i <= finalRow; ++i) {
                for (PlayedCard card : board.get(i).getRow()) {
                    card.setCanAttack(true);
                    card.setFrozen(false);
                }
            }
        }

    }

    /**
     * Updates the status of the heroes after a turn, enabling them to attack.
     *
     * @param player An array of Player objects whose heroes' attack ability will be reset.
     */
    public void updateHerosAfterTurn(final Player[] player) {
        player[0].getHero().setCanAttack(true);
        player[1].getHero().setCanAttack(true);
    }

    /**
     * Adds a command object to the given ArrayNode indicating the player's turn.
     *
     * @param output the ArrayNode to which the command object will be added
     * @param idx the index representing the player's turn
     */
    private void getPlayerTurn(final ArrayNode output, final int idx) {
        ObjectNode command = objectMapper.createObjectNode();
        command.put("command", "getPlayerTurn");
        command.put("output", idx + 1);
        output.add(command);
    }

    /**
     * Places a card from the player's hand onto the game board following certain rules
     * and updates the game state accordingly. If there is not enough mana or space on
     * the board, an appropriate error message is added to the output.
     *
     * @param player The player who is attempting to place the card.
     * @param playerIdx Index of the player attempting the action.
     * @param handIdx Index of the card in the player's hand to be placed.
     * @param output An ArrayNode to capture the command output, including any errors.
     */
    public void placeCard(final Player player, final int playerIdx,
                          final int handIdx, final ArrayNode output) {
        final int rowNumber = 4;
        final int rowLength = 5;
        int idx = (playerIdx + 1) % 2;
        int idx2 = (rowNumber - idx) % rowNumber;
        if (player.getHand().isEmpty() || handIdx >= player.getHand().size()) {
            return;
        }
        if (player.getMana() < player.getHand().get(handIdx).getMana()) {
            //error + output
            ObjectNode command = objectMapper.createObjectNode();
            command.put("command", "placeCard");
            command.put("handIdx", handIdx);
            command.put("error", "Not enough mana to place card on table.");
            output.add(command);
        } else {
            String name = player.getHand().get(handIdx).getName();
            if (name.equals("Goliath") || name.equals("Warden")
                    || name.equals("The Ripper") || name.equals("Miraj")) {
                //place on row 1/2 depending on playerIdx
                if (board.get(1 + idx).getRow().size() >= rowLength) {
                    //error + not enough space
                    ObjectNode command = objectMapper.createObjectNode();
                    command.put("command", "placeCard");
                    command.put("handIdx", handIdx);
                    command.put("error", "Cannot place card on table since row is full.");
                    output.add(command);
                } else {
                    CardInput card = player.getHand().get(handIdx);
                    String cardName = card.getName();
                    PlayedCard newCard;
                    switch (cardName) {
                        case "The Ripper":
                            newCard = new TheRipper(card);
                            break;
                        case "Miraj":
                            newCard = new Miraj(card);
                            break;
                        default:
                            newCard = new PlayedCard(card);
                    }

                    ArrayList<CardInput> hand = player.getHand();
                    hand.remove(handIdx);
                    player.setHand(hand);
                    player.setMana(-newCard.getMana(), Boolean.TRUE);
                    ArrayList<PlayedCard> row = board.get(1 + idx).getRow();
                    row.add(newCard);
                    board.get(1 + idx).setRow(row);
                }

            } else {
                //place on row 0/3
                final int maxSize = 5;
                if (board.get(idx2)
                        .getRow().size() >= maxSize) {
                    //error + not enough space
                    ObjectNode command = objectMapper.createObjectNode();
                    command.put("command", "placeCard");
                    command.put("handIdx", handIdx);
                    command.put("error",
                            "Cannot place card on table since row is full.");
                    output.add(command);
                } else {
                    CardInput card = player.getHand().get(handIdx);
                    String cardName = card.getName();
                    PlayedCard newCard;
                    switch (cardName) {
                        case "Disciple":
                            newCard = new Disciple(card);
                            break;
                        case "The Cursed One":
                            newCard = new TheCursedOne(card);
                            break;
                        default:
                            newCard = new PlayedCard(card);
                    }
                    ArrayList<CardInput> hand = player.getHand();
                    hand.remove(handIdx);
                    player.setHand(hand);
                    player.setMana(-newCard.getMana(), Boolean.TRUE);
                    ArrayList<PlayedCard> row = board
                            .get(idx2)
                            .getRow();
                    row.add(newCard);
                    board.get(idx2).setRow(row);
                }
            }
        }
    }

    /**
     * Checks if an attack is valid based on the provided x1 coordinate.
     *
     * @param x1 the x-coordinate to check for valid attack conditions.
     * @return true if the attack is valid, false otherwise.
     */
    public boolean isAttackValid(final int x1) {
        if (x1 <= 1) {
            ArrayList<PlayedCard> row = board.get(2).getRow();
            for (PlayedCard minion : row) {
                if (minion.getName().equals("Warden") || minion.getName().equals("Goliath")) {
                    return false;
                }
            }
        } else {
            ArrayList<PlayedCard> row = board.get(1).getRow();
            for (PlayedCard minion : row) {
                if (minion.getName().equals("Warden") || minion.getName().equals("Goliath")) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Validates if an attack is allowed on the given coordinates.
     * The method checks specific card names to determine if the attack is valid.
     *
     * @param x1 the x-coordinate of the attacking entity
     * @param x2 the x-coordinate of the target location
     * @param y2 the y-coordinate of the target location
     * @return true if the attack is valid according to the rules
     *          based on the card names, otherwise false
     */
    public boolean isAttackValid(final int x1, final int x2, final int y2) {

        String card = board.get(x2).getRow().get(y2).getName();
        if (card.equals("Goliath") || card.equals("Warden")) {
            return true;
        }
        return isAttackValid(x1);
    }

    /**
     * Executes the attack action between two cards on the board.
     *
     * @param action The action input containing the coordinates of
     *               the attacking and attacked cards.
     * @param output The output node where error messages or results will be added.
     */
    public void cardUsesAttack(final ActionsInput action, final ArrayNode output) {
        int x1 = action.getCardAttacker().getX();
        int y1 = action.getCardAttacker().getY();
        int x2 = action.getCardAttacked().getX();
        int y2 = action.getCardAttacked().getY();

        int boardSize = board.size();
        if ((x1 < 0 || x1 > boardSize) || (x2 < 0 || x2 > boardSize)
                || (y1 >= board.get(x1).getRow().size())
                || (y2 >= board.get(x2).getRow().size())) {
            return;
        }

        PlayedCard bully = board.get(x1).getRow().get(y1);

        if ((x1 <= 1 && x2 <= 1) || (x1 >= 2 && x2 >= 2)) {
            //error: card doesn't belong to enemy
            ObjectNode command = attackError(action);
            command.put("error", "Attacked card does not belong to the enemy.");
            output.add(command);

        } else if (!isAttackValid(x1, x2, y2)) {
            //error: card is not a tank
            ObjectNode command = attackError(action);
            command.put("error", "Attacked card is not of type 'Tank'.");
            output.add(command);
        } else if (!bully.getCanAttack()) {
            //error: already attacked
            ObjectNode command = attackError(action);
            command.put("error", "Attacker card has already attacked this turn.");
            output.add(command);
        } else if (bully.isFrozen()) {
            //error: card is frozen
            ObjectNode command = attackError(action);
            command.put("error", "Attacker card is frozen.");
            output.add(command);
        } else {
            bully.setCanAttack(false);
            PlayedCard defender = board.get(x2).getRow().get(y2);
            defender.setHealth(bully.getAttackDamage(), false);
            if (defender.getHealth() <= 0) {
                board.get(x2).getRow().remove(y2);
            } else {
                //if it fails, try .remove(y1) + .add(y1, bully)
                board.get(x1).getRow().get(y1).setCanAttack(false);
                board.get(x2).getRow().get(y2).setHealth(defender.getHealth(), true);
            }
        }
    }

    /**
     * Generates a JSON object representing an attack error based on the provided action input.
     *
     * @param action The action input containing details about the card attacker and card attacked.
     * @return A JSON object that specifies the command and
     *          coordinates of the attacker and attacked cards.
     */
    private ObjectNode attackError(final ActionsInput action) {
        ObjectNode command = objectMapper.createObjectNode();
        command.put("command", "cardUsesAttack");
        ObjectNode coords1 = objectMapper.createObjectNode();
        coords1.put("x", action.getCardAttacker().getX());
        coords1.put("y", action.getCardAttacker().getY());
        command.set("cardAttacker", coords1);

        ObjectNode coords2 = objectMapper.createObjectNode();
        coords2.put("x", action.getCardAttacked().getX());
        coords2.put("y", action.getCardAttacked().getY());
        command.set("cardAttacked", coords2);
        return command;
    }

    /**
     * Creates an error response for an attack on the hero.
     * This method generates an ObjectNode containing the command name "useAttackHero"
     * and the coordinates of the attacking card.
     *
     * @param action The ActionsInput object containing the details of the card attack action,
     *               including the coordinates of the attacking card.
     * @return ObjectNode representing the command structure for the error response.
     */
    private ObjectNode attackHeroError(final ActionsInput action) {
        ObjectNode command = objectMapper.createObjectNode();
        command.put("command", "useAttackHero");
        ObjectNode coords1 = objectMapper.createObjectNode();
        coords1.put("x", action.getCardAttacker().getX());
        coords1.put("y", action.getCardAttacker().getY());
        command.set("cardAttacker", coords1);
        return command;
    }

    /**
     * Creates an error response for an ability use action between two cards.
     * This method generates an ObjectNode containing the command name "cardUsesAbility"
     * and the coordinates of both the attacking and attacked cards.
     *
     * @param action The ActionsInput object containing the details of the card ability action,
     *               including the coordinates of the card using the ability and the target card.
     * @return ObjectNode representing the command structure for the error response.
     */
    private ObjectNode abilityError(final ActionsInput action) {
        ObjectNode command = objectMapper.createObjectNode();
        command.put("command", "cardUsesAbility");
        ObjectNode coords1 = objectMapper.createObjectNode();
        coords1.put("x", action.getCardAttacker().getX());
        coords1.put("y", action.getCardAttacker().getY());
        command.set("cardAttacker", coords1);

        ObjectNode coords2 = objectMapper.createObjectNode();
        coords2.put("x", action.getCardAttacked().getX());
        coords2.put("y", action.getCardAttacked().getY());
        command.set("cardAttacked", coords2);
        return command;
    }

    /**
     * Handles cases where a card uses its ability on another card.
     * The method performs various checks to ensure the validity of the action,
     * such as checking if the attacker is frozen, if it has
     * already attacked, and if the target belongs to the correct player or is of the correct type.
     * If an action is invalid, appropriate error messages are added to the output.
     *
     * @param action The action containing details of the card using
     *               the ability and the target card.
     * @param output The output array where any error messages are added
     *               in case the action is invalid.
     */
    public void  cardUsesAbility(final ActionsInput action, final ArrayNode output) {
        int x1 = action.getCardAttacker().getX();
        int y1 = action.getCardAttacker().getY();
        int x2 = action.getCardAttacked().getX();
        int y2 = action.getCardAttacked().getY();

        if ((x1 < 0 || x1 > board.size()) || (x2 < 0 || x2 > board.size())
                || (y1 >= board.get(x1).getRow().size())
                || (y2 >= board.get(x2).getRow().size())) {
            return;
        }
        PlayedCard attacker = board.get(x1).getRow().get(y1);
        PlayedCard attacked = board.get(x2).getRow().get(y2);
        if (board.get(x1).getRow().get(y1).isFrozen()) {
            //error: card is frozen
            ObjectNode command = abilityError(action);
            command.put("error", "Attacker card is frozen.");
            output.add(command);
        } else if (!board.get(x1).getRow().get(y1).getCanAttack()) {
            //error: already attacked
            ObjectNode command = abilityError(action);
            command.put("error", "Attacker card has already attacked this turn.");
            output.add(command);
        } else if (attacker.getName().equals("Disciple")) {
            if ((x1 <= 1 && x2 <= 1) || (x1 >= 2 && x2 >= 2)) {
                board.get(x1).getRow().get(y1).useAbility(attacked);
                board.get(x2).getRow().get(y2)
                        .setHealth(attacked.getHealth(), true);
                board.get(x1).getRow().get(y1).setCanAttack(false);
            } else {
                ObjectNode command = abilityError(action);
                command.put("error",
                        "Attacked card does not belong to the current player.");
                output.add(command);
            }
        } else {
            if ((x1 <= 1 && x2 <= 1) || (x1 >= 2 && x2 >= 2)) {
                //error: card doesn't belong to enemy
                ObjectNode command = abilityError(action);
                command.put("error", "Attacked card does not belong to the enemy.");
                output.add(command);
            } else if (!isAttackValid(x1, x2, y2)) {
                //error: card is not a tank
                ObjectNode command = abilityError(action);
                command.put("error", "Attacked card is not of type 'Tank'.");
                output.add(command);
            } else {
                board.get(x1).getRow().get(y1).useAbility(attacked);
                board.get(x2).getRow().remove(y2);
                if (attacked.getHealth() > 0) {
                    board.get(x2).getRow().add(y2, attacked);
                }
                board.get(x1).getRow().get(y1).setCanAttack(false);
            }
        }
    }

    /**
     * Executes an attack on the enemy hero by a specific card.
     * This method performs checks to ensure that the attacker card is valid
     * and can execute the attack, then reduces the enemy hero's health
     * accordingly. If the enemy hero's health drops to zero or below,
     * the game ends and the current player is declared the winner.
     *
     * @param player     An array of Player objects representing the players in the game.
     * @param currPlayer The index of the current player (0 for player one, 1 for player two).
     * @param action     An ActionsInput object containing the details of the attack action,
     *                    including the coordinates of the attacking card.
     * @param output     An ArrayNode to which any errors, confirmations of the attack,
     *                   or end-of-game notifications will be added.
     */
    public void useAttackHero(final Player[] player, final int currPlayer,
                              final ActionsInput action, final ArrayNode output) {

        int x = action.getCardAttacker().getX();
        int y = action.getCardAttacker().getY();
        if (!(x >= 0 && x <= board.size()) || (y >= board.get(x).getRow().size())) {
            return;
        }
        PlayedCard attacker = board.get(x).getRow().get(y);
        if (attacker.isFrozen()) {
            //error: card is frozen
            ObjectNode command = attackHeroError(action);
            command.put("error", "Attacker card is frozen.");
            output.add(command);
        } else if (!attacker.getCanAttack()) {
            //error: already attacked
            ObjectNode command = attackHeroError(action);
            command.put("error",
                    "Attacker card has already attacked this turn.");
            output.add(command);
        } else if (!isAttackValid(x)) {
            //error: card is not a tank
            ObjectNode command = attackHeroError(action);
            command.put("error", "Attacked card is not of type 'Tank'.");
            output.add(command);
        } else {
            int heroHealth = player[(currPlayer + 1) % 2].getHero().getHealth();
            heroHealth -= attacker.getAttackDamage();
            board.get(x).getRow().get(y).setCanAttack(false);
            if (heroHealth <= 0) {
                //win currPlayer
                ObjectNode command = objectMapper.createObjectNode();
                if (currPlayer == 1) {
                    command.put("gameEnded",
                            "Player two killed the enemy hero.");
                    player[1].newWin();
                } else {
                    command.put("gameEnded",
                            "Player one killed the enemy hero.");
                    player[0].newWin();
                }
                output.add(command);
            } else {
                player[(currPlayer + 1) % 2].getHero().setHealth(heroHealth);
            }
        }

    }

    /**
     * Executes the hero ability for a specific player during their turn. This method
     * performs various checks to ensure that the hero's ability can be used, such as
     * verifying that the player has enough mana, ensuring that the hero hasn't already
     * attacked this turn, and confirming that the targeted row belongs to the correct
     * player.
     *
     * @param player     The Player object who is using the hero ability.
     * @param currPlayer The index of the current player (0 for player one, 1 for player two).
     * @param action     An ActionsInput object containing the details of the action, including
     *                   the affected row.
     * @param output     An ArrayNode to which any errors or
     *                   confirmations of the action will be added.
     */
    public void useHeroAbility(final Player player, final int currPlayer,
                               final ActionsInput action, final ArrayNode output) {
        String hero = player.getHero().getName();
        ObjectNode command = objectMapper.createObjectNode();
        if (player.getMana() < player.getHero().getMana()) {
            //error: not enough mana
            command.put("command", "useHeroAbility");
            command.put("affectedRow", action.getAffectedRow());
            command.put("error", "Not enough mana to use hero's ability.");
            output.add(command);
        } else if (!player.getHero().isCanAttack()) {
            //error: hero attacked this turn
            command.put("command", "useHeroAbility");
            command.put("affectedRow", action.getAffectedRow());
            command.put("error", "Hero has already attacked this turn.");
            output.add(command);
        } else if (hero.equals("Lord Royce") || hero.equals("Empress Thorina")) {
            if (!((currPlayer == 0 && action.getAffectedRow() >= 2)
                   || (currPlayer == 1 && action.getAffectedRow() <= 1))) {
                //use ability
                player.getHero().useAbility(board.get(action.getAffectedRow()).getRow());
                player.getHero().setCanAttack(false);
                player.setMana(-player.getHero().getMana(), true);
            } else {
                //error: row does not belong to enemy
                command.put("command", "useHeroAbility");
                command.put("affectedRow", action.getAffectedRow());
                command.put("error", "Selected row does not belong to the enemy.");
                output.add(command);
            }
        } else {
            if (!((currPlayer == 0 && action.getAffectedRow() >= 2)
                   || (currPlayer == 1 && action.getAffectedRow() <= 1))) {
                //error: row does not belong to  currPlayer
                command.put("command", "useHeroAbility");
                command.put("affectedRow", action.getAffectedRow());
                command.put("error",
                        "Selected row does not belong to the current player.");
                output.add(command);
            } else {
                //use ability
                player.getHero().useAbility(board
                        .get(action.getAffectedRow())
                        .getRow());
                player.getHero().setCanAttack(false);
                player.setMana(-player.getHero().getMana(), true);
            }
        }
    }

    /**
     * Retrieves the cards in the hand of a specified player and adds
     * the serialized card details to the provided output array.
     *
     * @param player The Player object whose hand of cards is to be retrieved.
     * @param idx The index representing the player (0 for player one, 1 for player two).
     * @param output The ArrayNode to which the serialized card details will be added.
     */
    public void getCardsInHand(final Player player, final int idx, final ArrayNode output) {
        ObjectNode command = objectMapper.createObjectNode();
        command.put("command", "getCardsInHand");
        command.put("playerIdx", idx);
        ArrayList<CardInput> hand = player.getHand();
        outputCards(output, command, hand);
    }

    /**
     * Serializes the details of the cards in the specified hand and adds the serialized
     * data to the provided output array.
     *
     * @param output  The ArrayNode to which the serialized card details will be added.
     * @param command The ObjectNode representing the command structure, which will be
     *                populated with the serialized card details.
     * @param hand    The list of CardInput objects representing the cards in hand to be
     *                serialized and outputted.
     */
    private void outputCards(final ArrayNode output, final ObjectNode command,
                             final ArrayList<CardInput> hand) {
        ArrayNode deckArrayNode = objectMapper.createArrayNode();
        for (CardInput card : hand) {
            ObjectNode cardNode = objectMapper.createObjectNode();
            cardNode.put("mana", card.getMana());
            cardNode.put("attackDamage", card.getAttackDamage());
            cardNode.put("health", card.getHealth());
            cardNode.put("description", card.getDescription());

            ArrayList<String> colors = card.getColors();
            ArrayNode colorsArrayNode = objectMapper.createArrayNode();
            for (String color : colors) {
                colorsArrayNode.add(color);
            }
            cardNode.set("colors", colorsArrayNode);

            cardNode.put("name", card.getName());
            deckArrayNode.add(cardNode);
        }
        command.set("output", deckArrayNode);
        output.add(command);
    }

    /**
     * Retrieves the deck of the specified player and adds
     * the details to the provided output array.
     *
     * @param player The Player object whose deck is to be retrieved.
     * @param output The ArrayNode to which the details of the player's deck will be added.
     * @param idx The index representing the player (0 for player one, 1 for player two).
     */
    public void getPlayerDeck(final Player player, final ArrayNode output, final int idx) {
        ObjectNode command = objectMapper.createObjectNode();
        command.put("command", "getPlayerDeck");
        command.put("playerIdx", idx);
        ArrayList<CardInput> deck = player.getDeck();
        outputCards(output, command, deck);
    }

    /**
     * Fetches the details of all the cards currently on the table and adds this information
     * to the provided output array.
     *
     * @param output The ArrayNode to which the details of the cards on the table will be added.
     */
    public void getCardsOnTable(final ArrayNode output) {
        ObjectNode command = objectMapper.createObjectNode();
        command.put("command", "getCardsOnTable");
        ArrayNode boardArray = objectMapper.createArrayNode();
        for (BoardRow row : board) {
            ArrayNode rowArray = objectMapper.createArrayNode();
            for (PlayedCard card : row.getRow()) {
                ObjectNode cardNode = objectMapper.createObjectNode();
                cardNode.put("mana", card.getMana());
                cardNode.put("attackDamage", card.getAttackDamage());
                cardNode.put("health", card.getHealth());
                cardNode.put("description", card.getDescription());

                ArrayList<String> colors = card.getColors();
                ArrayNode colorsArrayNode = objectMapper.createArrayNode();
                for (String color : colors) {
                    colorsArrayNode.add(color);
                }
                cardNode.set("colors", colorsArrayNode);

                cardNode.put("name", card.getName());
                rowArray.add(cardNode);
            }
            boardArray.add(rowArray);
        }
        command.set("output", boardArray);
        output.add(command);
    }

    /**
     * Retrieves the details of a specific hero and adds the information
     * to the provided output array.
     *
     * @param hero   The Hero object whose details are to be retrieved.
     * @param output The ArrayNode to which the hero details will be added.
     * @param idx    The index representing the player (0 for player one, 1 for player two).
     */
    public void getPlayerHero(final Hero hero, final ArrayNode output, final int idx) {

        ObjectNode command = objectMapper.createObjectNode();
        command.put("command", "getPlayerHero");
        command.put("playerIdx", idx);

        ObjectNode cardNode = objectMapper.createObjectNode();
        cardNode.put("mana", hero.getMana());
        cardNode.put("description", hero.getDescription());
        ArrayList<String> heroColors = hero.getColors();
        ArrayNode colorsArrayNode = objectMapper.createArrayNode();
        for (String color : heroColors) {
            colorsArrayNode.add(color);
        }
        cardNode.set("colors", colorsArrayNode);
        cardNode.put("name", hero.getName());
        cardNode.put("health", hero.getHealth());
        command.set("output", cardNode);

        output.add(command);
    }

    /**
     * Retrieves the card located at a specific position on the board
     * and adds its details to the provided output array. If no card is
     * present at the specified position, an appropriate message is added
     * to the output array.
     *
     * @param action The action containing the x and y coordinates
     *                 of the desired card position.
     * @param output The ArrayNode to which the details of the retrieved card
     *               or an error message will be added.
     */
    public void getCardAtPosition(final ActionsInput action,
                                  final ArrayNode output) {
        int x = action.getX();
        int y = action.getY();

        ObjectNode command = objectMapper.createObjectNode();
        command.put("command", "getCardAtPosition");
        command.put("x", x);
        command.put("y", y);
        if ((x < 0 || x > board.size()) || (y >= board.get(x).getRow().size())) {
            command.put("output",
                    "No card available at that position.");
        } else {
            PlayedCard card = board.get(x).getRow().get(y);
            ObjectNode cardNode = objectMapper.createObjectNode();
            cardNode.put("mana", card.getMana());
            cardNode.put("attackDamage", card.getAttackDamage());
            cardNode.put("health", card.getHealth());
            cardNode.put("description", card.getDescription());

            ArrayList<String> colors = card.getColors();
            ArrayNode colorsArrayNode = objectMapper.createArrayNode();
            for (String color : colors) {
                colorsArrayNode.add(color);
            }
            cardNode.set("colors", colorsArrayNode);
            cardNode.put("name", card.getName());
            command.set("output", cardNode);
        }
        output.add(command);
    }

    /**
     * Collects all frozen cards from the board and adds their
     * details to the provided output array.
     *
     * @param output The ArrayNode to which the details of the frozen cards will be added.
     */
    public void getFrozenCardsOnTable(final ArrayNode output) {

        ObjectNode command = objectMapper.createObjectNode();
        ArrayNode arrayFrozen = objectMapper.createArrayNode();
        command.put("command", "getFrozenCardsOnTable");
        for (BoardRow row : board) {
            for (PlayedCard card : row.getRow()) {
                if (card.isFrozen()) {
                    ObjectNode cardNode = objectMapper.createObjectNode();
                    cardNode.put("mana", card.getMana());
                    cardNode.put("attackDamage", card.getAttackDamage());
                    cardNode.put("health", card.getHealth());
                    cardNode.put("description", card.getDescription());

                    ArrayList<String> colors = card.getColors();
                    ArrayNode colorsArrayNode = objectMapper.createArrayNode();
                    for (String color : colors) {
                        colorsArrayNode.add(color);
                    }
                    cardNode.set("colors", colorsArrayNode);

                    cardNode.put("name", card.getName());
                    arrayFrozen.add(cardNode);
                }
            }
        }
        command.set("output", arrayFrozen);
        output.add(command);
    }

    /**
     * Retrieves the mana of a specific player
     * and adds this information to the provided output array.
     *
     * @param player The Player object representing the player whose mana is to be retrieved.
     * @param idx The index representing the player (0 for player one, 1 for player two).
     * @param output The ArrayNode to which the player's current mana will be added.
     */
    public void getPlayerMana(final Player player, final int idx, final ArrayNode output) {

        ObjectNode command = objectMapper.createObjectNode();
        command.put("command", "getPlayerMana");
        command.put("playerIdx", idx);
        command.put("output", player.getMana());
        output.add(command);
    }

    /**
     * Adds the number of wins for a specified player into the provided output array.
     *
     * @param player The player whose win count is to be retrieved.
     * @param idx The index representing the player (0 for player one, 1 for player two).
     * @param output The ArrayNode to which the player's win count will be added.
     */
    public void getPlayerWins(final Player player, final int idx, final ArrayNode output) {

        ObjectNode command = objectMapper.createObjectNode();
        if (idx == 0) {
            command.put("command", "getPlayerOneWins");
        } else {
            command.put("command", "getPlayerTwoWins");
        }
        command.put("output", player.getWins());
        output.add(command);
    }

    /**
     * Logs the total number of games played into the provided output array.
     *
     * @param nrGames The total number of games played.
     * @param output The output node to which the total number of games played will be added.
     */
    public void getTotalGamesPlayed(final int nrGames, final ArrayNode output) {

        ObjectNode command = objectMapper.createObjectNode();
        command.put("command", "getTotalGamesPlayed");
        command.put("output", nrGames);
        output.add(command);
    }

    /**
     * Executes the specified action for the current player.
     *
     * @param player      An array of Player objects representing the players in the game.
     * @param currPlayer  The index of the current player in the player array.
     * @param action      An ActionsInput object containing the action to be executed.
     * @param output      An ArrayNode object to which the results of the action.
     */
    public void action(final Player[] player, final int currPlayer,
                       final ActionsInput action, final ArrayNode output) {
        String command = action.getCommand();
        if (command.equals("getPlayerDeck")) {
            getPlayerDeck(player[action.getPlayerIdx() - 1],
                    output, action.getPlayerIdx());
        } else if (command.equals("getPlayerHero")) {
            getPlayerHero(player[action.getPlayerIdx() - 1].getHero(),
                    output, action.getPlayerIdx());
        } else if (command.equals("placeCard")) {
            placeCard(player[currPlayer], currPlayer, action.getHandIdx(), output);
        } else if (action.getCommand().equals("getPlayerTurn")) {
            getPlayerTurn(output, currPlayer);
        } else if (command.equals("getCardsInHand")) {
            getCardsInHand(player[action.getPlayerIdx() - 1],
                    action.getPlayerIdx(), output);
        } else if (command.equals("getPlayerMana")) {
            getPlayerMana(player[action.getPlayerIdx() - 1],
                    action.getPlayerIdx(), output);
        } else if (command.equals("getCardsOnTable")) {
            getCardsOnTable(output);
        } else if (command.equals("cardUsesAttack")) {
            cardUsesAttack(action, output);
        } else if (command.equals("getCardAtPosition")) {
            getCardAtPosition(action, output);
        } else if (command.equals("cardUsesAbility")) {
            cardUsesAbility(action, output);
        } else if (command.equals("useAttackHero")) {
            useAttackHero(player, currPlayer, action, output);
        } else if (command.equals("useHeroAbility")) {
            useHeroAbility(player[currPlayer], currPlayer, action, output);
        } else if (command.equals("getFrozenCardsOnTable")) {
            getFrozenCardsOnTable(output);
        } else if (command.equals("getPlayerOneWins")) {
            getPlayerWins(player[0], 0, output);
        } else if (command.equals("getPlayerTwoWins")) {
            getPlayerWins(player[1], 1, output);
        }
    }
}
