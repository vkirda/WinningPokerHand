package com.vilmantas.helpers;

import com.vilmantas.model.Card;
import com.vilmantas.model.Games;
import com.vilmantas.model.Match;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.vilmantas.helpers.InputStreamToString.getRawGameData;

public class Mapper {

    private Mapper() {
    }

    /**
     * Some background:
     * Data are taken from .txt file. Each line in the file represents a new Match.
     * String separated by whitespace represents a Card. First 5 cards of the match goes for Player1,
     * rest of the 5 cards goes to Player2.
     *
     * What method does:
     * 1. Raw game data in String format are split at the new line. List of lines are formed representing list of matches.
     * 2. Looping through the list of raw match data and splitting by whitespace(" "). Raw data represents a card separated by space(" ").
     * 2.1. Looping through the list of cards. Assigning first 5 cards to Player1, last 5 cards to Player2.
     * 3. After cards assigned to players - players are assigned to list of matches.
     * 4. Matches are assigned to game and method returns Game object.
     * */

    public static Games mapGamesData(InputStream inputStream) throws IOException {

        String rawGameData = getRawGameData(inputStream);

        List<String> gamesDataRaw = List.of(rawGameData.split("\\R")); // Special JAVA 8 pattern to match new line for any os

        List<Match> matches = new ArrayList<>();

        assignPlayersToMatchesAndAssignCardsToPlayers(gamesDataRaw, matches);

        return Games.builder().pokerMatchList(matches).build();
    }

    private static void assignPlayersToMatchesAndAssignCardsToPlayers(List<String> gamesDataRaw, List<Match> matches) {

        List<String> cardsRawData;
        Match match;

        for (String gameRaw : gamesDataRaw) {

            cardsRawData = List.of(gameRaw.split(" "));

            List<Card> cardsOfPlayer1 = new ArrayList<>();
            List<Card> cardsOfPlayer2 = new ArrayList<>();

            assignCardsForPlayers(cardsRawData, cardsOfPlayer1, cardsOfPlayer2);

            match = Match.builder()
                    .player1Cards(cardsOfPlayer1)
                    .player2Cards(cardsOfPlayer2)
                    .build();

            matches.add(match);
        }
    }

    private static void assignCardsForPlayers(List<String> cardsRawData, List<Card> cardsOfPlayer1, List<Card> cardsOfPlayer2) {

        Card card;

        for (int i = 0; i < cardsRawData.size(); i++) {

            if (i < 5) {

                card = Card.builder().rankAndSuite(cardsRawData.get(i)).build();
                cardsOfPlayer1.add(card);

            } else {

                card = Card.builder().rankAndSuite(cardsRawData.get(i)).build();
                cardsOfPlayer2.add(card);
            }
        }
    }
}
