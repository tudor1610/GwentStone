package game;

import Minions.*;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import fileio.CardInput;
import fileio.StartGameInput;

import java.util.ArrayList;
import java.util.HashMap;

public class ProcessActions {
	private ArrayList<BoardRow> board;

	public ProcessActions() {
		board = new ArrayList<>(4);
		for (int i = 0; i < 4; ++i)
			board.add(new BoardRow());
	}

	public void updateCardsAfterTurn() {
		for (BoardRow row : board) {
			for (PlayedCard card : row.getRow()) {
				card.setCanAttack(true);
				card.setFrozen(false);
			}
		}
	}

	private void getPlayerTurn(ArrayNode output, int idx) {
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode command = objectMapper.createObjectNode();
		command.put("command", "getPlayerTurn");
		command.put("output", idx + 1);
		output.add(command);
	}

	public void placeCard(Player player,int playerIdx, int handIdx, ArrayNode output) {
		playerIdx = (playerIdx + 1) % 2;
		if (player.getHand().size() <= 0 || handIdx >= player.getHand().size())
			return;
		if (player.getMana() < player.getHand().get(handIdx).getMana()) {
			//error + output
			ObjectMapper objectMapper = new ObjectMapper();
			ObjectNode command = objectMapper.createObjectNode();
			command.put("command", "placeCard");
			command.put("handIdx", handIdx);
			command.put("error", "Not enough mana to place card on table.");
			output.add(command);
		} else {
			String name = player.getHand().get(handIdx).getName();
			if (name.equals("Goliath") || name.equals("Warden") || name.equals("The Ripper") || name.equals("Miraj")) {
				//place on row 1/2 depending on playerIdx
				if (board.get(1 + playerIdx).getRow().size() >= 5) {
					//error + not enough space
					ObjectMapper objectMapper = new ObjectMapper();
					ObjectNode command = objectMapper.createObjectNode();
					command.put("command", "placeCard");
					command.put("handIdx", handIdx);
					command.put("error", "Cannot place card on table since row is full.");
					output.add(command);
				} else {
					CardInput card = player.getHand().get(handIdx);
					String cardName = card.getName();
					PlayedCard new_card;
					if (cardName.equals("Goliath")) {
						new_card = new Goliath(card);
					} else if (cardName.equals("Warden")) {
						new_card = new Warden(card);
					} else if (cardName.equals("The Ripper")) {
						new_card = new TheRipper(card);
					} else {
						new_card = new Miraj(card);
					}

					ArrayList<CardInput> hand = player.getHand();
					hand.remove(handIdx);
					player.setHand(hand);
					player.setMana(-new_card.getMana(), Boolean.TRUE);
					ArrayList<PlayedCard> row = board.get(1 + playerIdx).getRow();
					row.add(new_card);
					board.get(1 + playerIdx).setRow(row);
					//System.out.println("Placed card " + new_card.getName() + " on row " + (1 + playerIdx));
				}

			} else {
				//place on row 0/3
				if (board.get((4 - playerIdx) % 4).getRow().size() >= 5) {
					//error + not enough space
					ObjectMapper objectMapper = new ObjectMapper();
					ObjectNode command = objectMapper.createObjectNode();
					command.put("command", "placeCard");
					command.put("handIdx", handIdx);
					command.put("error", "Cannot place card on table since row is full.");
					output.add(command);
				} else {
					CardInput card = player.getHand().get(handIdx);
					String cardName = card.getName();
					PlayedCard new_card;
					if (cardName.equals("Berserker")) {
						new_card = new Berserker(card);
					} else if (cardName.equals("Disciple")) {
						new_card = new Disciple(card);
					} else if (cardName.equals("The Cursed One")) {
						new_card = new TheCursedOne(card);
					} else {
						new_card = new Sentinel(card);
					}
					ArrayList<CardInput> hand = player.getHand();
					hand.remove(handIdx);
					player.setHand(hand);
					player.setMana(-new_card.getMana(), Boolean.TRUE);
					ArrayList<PlayedCard> row = board.get((4 - playerIdx) % 4).getRow();
					row.add(new_card);
					board.get((4 - playerIdx) % 4).setRow(row);
					//System.out.println("Placed card " + new_card.getName() + " on row " + ((4 - playerIdx) % 4));
				}
			}
		}

	}

	public boolean isAttackValid(int x1, int x2, int y2) {

		String card = board.get(x2).getRow().get(y2).getName();
		if (card.equals("Goliath") || card.equals("Warden")) {
			return true;
		}

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

	public void cardUsesAttack(ActionsInput action, ArrayNode output) {
		int x1 = action.getCardAttacker().getX();
		int y1 = action.getCardAttacker().getY();
		int x2 = action.getCardAttacked().getX();
		int y2 = action.getCardAttacked().getY();

		if ((x1 < 0 || x1 > 3) || (x2 < 0 || x2 > 3) || (y1 >= board.get(x1).getRow().size()) || (y2 >= board.get(x2).getRow().size())) {
			return;
		}

		if ((x1 <= 1 && x2 <= 1) || (x1 >=2 && x2 >= 2)) {
			//error: card doesn't belong to enemy
			ObjectNode command = attackError(action);
			command.put("error", "Attacked card does not belong to the enemy.");
			output.add(command);

		} else if (!isAttackValid(x1, x2, y2)) {
			//error: card is not a tank
			ObjectNode command = attackError(action);
			command.put("error", "Attacked card is not of type 'Tank'.");
			output.add(command);
		} else if (!board.get(x1).getRow().get(y1).getCanAttack()) {
			//error: already attacked
			ObjectNode command = attackError(action);
			command.put("error", "Attacker card has already attacked this turn.");
			output.add(command);
		} else if (board.get(x1).getRow().get(y1).isFrozen()) {
			//error: card is frozen
			ObjectNode command = attackError(action);
			command.put("error", "Attacker card is frozen.");
			output.add(command);
		} else {
			PlayedCard bully = board.get(x1).getRow().get(y1);
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

	private ObjectNode attackError(ActionsInput action) {
		ObjectMapper objectMapper = new ObjectMapper();
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

	private ObjectNode abilityError(ActionsInput action) {
		ObjectMapper objectMapper = new ObjectMapper();
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

	public void  cardUsesAbility(ActionsInput action, ArrayNode output) {
		int x1 = action.getCardAttacker().getX();
		int y1 = action.getCardAttacker().getY();
		int x2 = action.getCardAttacked().getX();
		int y2 = action.getCardAttacked().getY();

		if ((x1 < 0 || x1 > 3) || (x2 < 0 || x2 > 3) || (y1 >= board.get(x1).getRow().size()) || (y2 >= board.get(x2).getRow().size())) {
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
			if ((x1 <= 1 && x2 <= 1) || (x1 >=2 && x2 >= 2)) {
				Disciple card = (Disciple) attacker;
				card.useAbility(attacked);
				board.get(x2).getRow().get(y2).setHealth(attacked.getHealth(), true);
				board.get(x1).getRow().get(y1).setCanAttack(false);
			} else {
				ObjectNode command = abilityError(action);
				command.put("error", "Attacked card does not belong to the current player.");
				output.add(command);
			}
		} else {
			if ((x1 <= 1 && x2 <= 1) || (x1 >=2 && x2 >= 2)) {
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
				if (attacker.getName().equals("The Ripper")) {
					TheRipper card = (TheRipper) attacker;
					card.useAbility(attacked);
				} else if (attacker.getName().equals("Miraj")) {
					Miraj card = (Miraj) attacker;
					card.useAbility(attacked);
				} else if (attacker.getName().equals("The Cursed One")) {
					TheCursedOne card = (TheCursedOne) attacker;
					card.useAbility(attacked);
				}
				board.get(x2).getRow().remove(y2);
				if (attacked.getHealth() > 0) {
					board.get(x2).getRow().add(y2, attacked);
				}
				board.get(x1).getRow().get(y1).setCanAttack(false);
			}
		}
	}

	public void useAttackHero() {

	}

	public void useHeroAbility() {

	}

	public void getCardsInHand(Player player,int idx, ArrayNode output) {
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode command = objectMapper.createObjectNode();
		command.put("command", "getCardsInHand");
		command.put("playerIdx", idx);
		ArrayList<CardInput> hand = player.getHand();
		outputCards(output, objectMapper, command, hand);
	}

	private void outputCards(ArrayNode output, ObjectMapper objectMapper, ObjectNode command, ArrayList<CardInput> hand) {
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

	public void getPlayerDeck(Player player, ArrayNode output, int idx) {
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode command = objectMapper.createObjectNode();
		command.put("command", "getPlayerDeck");
		command.put("playerIdx", idx);
		ArrayList<CardInput> deck = player.getDeck();
		outputCards(output, objectMapper, command, deck);
	}

	public void getCardsOnTable(ArrayNode output) {
		ObjectMapper objectMapper = new ObjectMapper();
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

	public void getPlayerHero(CardInput hero, ArrayNode output, int idx) {
		ObjectMapper objectMapper = new ObjectMapper();
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

	public void getCardAtPosition(ActionsInput action, ArrayNode output) {
		int x = action.getX();
		int y = action.getY();
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode command = objectMapper.createObjectNode();
		command.put("command", "getCardAtPosition");
		command.put("x", x);
		command.put("y", y);
		if (x < 0 || x > 3 || (y >= board.get(x).getRow().size())) {
			command.put("output", "No card available at that position.");
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

	public void getFrozenCardsOnTable() {

	}

	public void getPlayerMana(Player player, int idx, ArrayNode output) {
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode command = objectMapper.createObjectNode();
		command.put("command", "getPlayerMana");
		command.put("playerIdx", idx);
		command.put("output", player.getMana());
		output.add(command);
	}

	public void action(Player[] player, int currPlayer, ActionsInput action, ArrayNode output) {
		String command = action.getCommand();
		if (command.equals("getPlayerDeck")) {
			getPlayerDeck(player[action.getPlayerIdx() - 1], output, action.getPlayerIdx());
		} else if (command.equals("getPlayerHero")) {
			getPlayerHero(player[action.getPlayerIdx() - 1].getHero(), output, action.getPlayerIdx());
		} else if (command.equals("placeCard")) {
			placeCard(player[currPlayer], currPlayer, action.getHandIdx(), output);
		} else if (action.getCommand().equals("getPlayerTurn")) {
			getPlayerTurn(output, currPlayer);
		} else if (command.equals("getCardsInHand")) {
			getCardsInHand(player[action.getPlayerIdx() - 1], action.getPlayerIdx(), output);
		} else if (command.equals("getPlayerMana")) {
			getPlayerMana(player[action.getPlayerIdx() -1], action.getPlayerIdx(), output);
		} else if (command.equals("getCardsOnTable")) {
			getCardsOnTable(output);
		} else if (command.equals("cardUsesAttack")) {
			cardUsesAttack(action, output);
		} else if (command.equals("getCardAtPosition")) {
			getCardAtPosition(action, output);
		} else if (command.equals("cardUsesAbility")) {
			cardUsesAbility(action, output);
		}
	}

}
