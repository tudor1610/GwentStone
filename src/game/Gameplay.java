package game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static java.util.Collections.shuffle;

public class Gameplay {
	public Gameplay() {

	}

	private void UpdatePlayersHands(Player[] players, int round) {
		//update player hand + mana
			if (round < 10) {
				players[0].setMana(round, Boolean.TRUE);
				players[1].setMana(round, Boolean.TRUE);
			} else {
				players[0].setMana(10, Boolean.FALSE);
				players[1].setMana(10, Boolean.FALSE);
			}
			if (!players[0].getDeck().isEmpty() && !players[1].getDeck().isEmpty()) {
				players[0].getHand().add(players[0].getDeck().get(0));
				players[0].getDeck().remove(0);
				players[1].getHand().add(players[1].getDeck().get(0));
				players[1].getDeck().remove(0);
			}
	}

	public void startGame(Input game, ArrayNode output) {

		ArrayList<GameInput> rounds = game.getGames();
		DecksInput p1Decks = game.getPlayerOneDecks();
		DecksInput p2Decks = game.getPlayerTwoDecks();

		for (GameInput round : rounds) {

			StartGameInput roundInput = round.getStartGame();
			int seed = roundInput.getShuffleSeed();
			ArrayList<CardInput> deckP1 = p1Decks.getDecks().get(roundInput.getPlayerOneDeckIdx());
			ArrayList<CardInput> deckP2 = p2Decks.getDecks().get(roundInput.getPlayerTwoDeckIdx());
			Random rnd = new Random(seed);
			Collections.shuffle(deckP1, rnd);
			rnd = new Random(seed);
			Collections.shuffle(deckP2, rnd);

			ArrayList<CardInput> handP1 = new ArrayList<>();
			ArrayList<CardInput> handP2 = new ArrayList<>();
			int startPlayer = roundInput.getStartingPlayer() - 1;

			Player[] player = new Player[2];
			player[0] = new Player(0, deckP1, roundInput.getPlayerOneHero());
			player[1] = new Player(0, deckP2, roundInput.getPlayerTwoHero());
			CardInput hero1 = player[0].getHero();
			hero1.setHealth(30);
			player[0].setHero(hero1);
			hero1 = player[1].getHero();
			hero1.setHealth(30);
			player[1].setHero(hero1);
			int turn = 2, ok = 2;

			ProcessActions processActions = new ProcessActions();
			for (ActionsInput action : round.getActions()) {

				int currPlayer = (startPlayer + ok % 2) % 2;
				if (turn % 2 == 0 && ok == 2) {
					this.UpdatePlayersHands(player, turn / 2);
					ok = 0;
				}
				if (action.getCommand().equals("endPlayerTurn")) {
					++turn;
					++ok;
				}
				//process action
				processActions.action(player, currPlayer, action, output);
			}
		}
	}
}
