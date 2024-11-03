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
				players[0].setMana(10, true);
				players[1].setMana(10, true);
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
		int p1wins = 0;
		int p2wins = 0;
		int gamesPlayed = 0;
		for (GameInput round : rounds) {

			gamesPlayed++;
			StartGameInput roundInput = round.getStartGame();
			int seed = roundInput.getShuffleSeed();

			//copy constructor?
			Deck deckP1 = new Deck(p1Decks.getDecks().get(roundInput.getPlayerOneDeckIdx()));
			Deck deckP2 = new Deck(p2Decks.getDecks().get(roundInput.getPlayerTwoDeckIdx()));

			Random rnd = new Random(seed);
			Collections.shuffle(deckP1.getDeck(), rnd);
			Random rnd2 = new Random(seed);
			Collections.shuffle(deckP2.getDeck(), rnd2);

			int startPlayer = roundInput.getStartingPlayer() - 1;

			Player[] player = new Player[2];
			player[0] = new Player(0, deckP1.getDeck(), roundInput.getPlayerOneHero(), p1wins);
			player[1] = new Player(0, deckP2.getDeck(), roundInput.getPlayerTwoHero(), p2wins);

			int turn = 2, ok = 2;
			ProcessActions processActions = new ProcessActions();
			for (ActionsInput action : round.getActions()) {

				int currPlayer = (startPlayer + ok % 2) % 2;
				if (turn % 2 == 0 && ok == 2) {
					this.UpdatePlayersHands(player, turn / 2);
					processActions.updateHerosAfterTurn(player);
					ok = 0;
				}
				if (action.getCommand().equals("endPlayerTurn")) {
					++turn;
					++ok;
					processActions.updateCardsAfterTurn(currPlayer);
				} else if (action.getCommand().equals("getTotalGamesPlayed")) {
					processActions.getTotalGamesPlayed(gamesPlayed, output);
				}
				//process action
				processActions.action(player, currPlayer, action, output);
			}
			p1wins = player[0].getWins();
			p2wins = player[1].getWins();
		}
	}
}
