package game;

import com.fasterxml.jackson.databind.node.ArrayNode;

import fileio.ActionsInput;
import fileio.DecksInput;
import fileio.GameInput;
import fileio.Input;
import fileio.StartGameInput;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Manages the gameplay, including updating player states,
 * controlling the flow of game rounds.
 */
public class Gameplay {

    /**
     * Updates the players' hands and mana based on the current round.
     *
     * @param players An array of Player objects representing the players in the game.
     * @param round The current round number of the game.
     */
    private void updatePlayersHands(final Player[] players, final int round) {
        //update player hand + mana
        final int maxMana = 10;
        if (round < maxMana) {
            players[0].setMana(round, Boolean.TRUE);
            players[1].setMana(round, Boolean.TRUE);
        } else {
            players[0].setMana(maxMana, true);
            players[1].setMana(maxMana, true);
        }
        if (!players[0].getDeck().isEmpty() && !players[1].getDeck().isEmpty()) {
            players[0].getHand().add(players[0].getDeck().get(0));
            players[0].getDeck().remove(0);
            players[1].getHand().add(players[1].getDeck().get(0));
            players[1].getDeck().remove(0);
        }
    }

    /**
     * Initiates and controls the flow of the game rounds based on the input game data.
     *
     * @param game The complete set of game inputs including player decks and game rounds.
     * @param output The node used to store the output of the game actions and results.
     */
    public void startGame(final Input game, final ArrayNode output) {

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
            Deck deckP1 = new Deck(p1Decks.getDecks()
                    .get(roundInput.getPlayerOneDeckIdx()));
            Deck deckP2 = new Deck(p2Decks.getDecks()
                    .get(roundInput.getPlayerTwoDeckIdx()));

            Random rnd = new Random(seed);
            Collections.shuffle(deckP1.getDeck(), rnd);
            Random rnd2 = new Random(seed);
            Collections.shuffle(deckP2.getDeck(), rnd2);

            int startPlayer = roundInput.getStartingPlayer() - 1;

            Player[] player = new Player[2];
            player[0] = new Player(0, deckP1.getDeck(),
                    roundInput.getPlayerOneHero(), p1wins);
            player[1] = new Player(0, deckP2.getDeck(),
                    roundInput.getPlayerTwoHero(), p2wins);

            int turn = 2, ok = 2;
            ProcessActions processActions = new ProcessActions();
            for (ActionsInput action : round.getActions()) {

                int currPlayer = (startPlayer + ok % 2) % 2;
                if (turn % 2 == 0 && ok == 2) {
                    this.updatePlayersHands(player, turn / 2);
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
