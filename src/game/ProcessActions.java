package game;

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

		} else {
			String name = player.getHand().get(handIdx).getName();
			if (name.equals("Goliath") || name.equals("Warden") || name.equals("The Ripper") || name.equals("Miraj")) {
				//place on row 1/2 depending on playerIdx
				if (board.get(1 + playerIdx).getRow().size() >= 5) {
					//error + not enough space
				} else {
					PlayedCard card = new PlayedCard(player.getHand().get(handIdx), Boolean.FALSE);
					ArrayList<CardInput> hand = player.getHand();
					hand.remove(handIdx);
					player.setHand(hand);
					ArrayList<PlayedCard> row = board.get(1 + playerIdx).getRow();
					row.add(card);
					board.get(1 + playerIdx).setRow(row);
				}

			} else {
				//place on row 0/3
				if (board.get((4 - playerIdx) % 4).getRow().size() >= 5) {
					//error + not enough space
				} else {
					PlayedCard card = new PlayedCard(player.getHand().get(handIdx), Boolean.FALSE);
					ArrayList<CardInput> hand = player.getHand();
					hand.remove(handIdx);
					player.setHand(hand);
					ArrayList<PlayedCard> row = board.get((4 - playerIdx) % 4).getRow();
					row.add(card);
					board.get((4 - playerIdx) % 4).setRow(row);
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

	public void getCardsInHand() {

	}

	public void getPlayerDeck(Player player, ArrayNode output, int idx) {
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode command = objectMapper.createObjectNode();
		command.put("command", "getPlayerDeck");
		command.put("playerIdx", idx);
		ArrayList<CardInput> deck = player.getDeck();
		ArrayNode deckArrayNode = objectMapper.createArrayNode();
		for (CardInput card : deck) {
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

	public void getCardsOnTable() {

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
		}
	}

}
