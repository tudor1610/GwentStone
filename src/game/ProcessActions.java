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

	private void getPlayerTurn(ArrayNode output, int idx) {
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode command = objectMapper.createObjectNode();
		command.put("command", "getPlayerTurn");
		command.put("output", idx + 1);
		output.add(command);
	}

	public void placeCard(Player player,int playerIdx, int handIdx, ArrayNode output) {
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
					switch (cardName) {
						case "Goliath" -> {
							new_card = new Goliath(card);
						}
						case "Warden" -> {
							new_card = new Warden(card);
						}
						case "The Ripper" -> {
							new_card = new TheRipper(card);
						}
						default -> {
							new_card = new Miraj(card);
						}
					}
					ArrayList<CardInput> hand = player.getHand();
					hand.remove(handIdx);
					player.setHand(hand);
					player.setMana(-new_card.getMana(), Boolean.TRUE);
					ArrayList<PlayedCard> row = board.get(1 + playerIdx).getRow();
					row.add(new_card);
					board.get(1 + playerIdx).setRow(row);
					System.out.println("Placed card " + new_card.getName() + " on row " + (1 + playerIdx));
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
					switch (cardName) {
						case "Berserker" -> {
							new_card = new Berserker(card);
						}
						case "Disciple" -> {
							new_card = new Disciple(card);
						}
						case "TheCursedOne" -> {
							new_card =  new TheCursedOne(card);
						}
						default -> {
							new_card = new Sentinel(card);
						}

					}
					ArrayList<CardInput> hand = player.getHand();
					hand.remove(handIdx);
					player.setHand(hand);
					player.setMana(-new_card.getMana(), Boolean.TRUE);
					ArrayList<PlayedCard> row = board.get((4 - playerIdx) % 4).getRow();
					row.add(new_card);
					board.get((4 - playerIdx) % 4).setRow(row);
					System.out.println("Placed card " + new_card.getName() + " on row " + ((4 - playerIdx) % 4));
				}
			}
		}

	}

	public void cardUsesAttack() {

	}

	public void  cardUsesAbility() {

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
		for (int i = 3; i >= 0; --i) {
			ArrayNode rowArray = objectMapper.createArrayNode();
			for (PlayedCard card : board.get(i).getRow()) {
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

	public void getCardAtPosition() {

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
		}
	}

}
