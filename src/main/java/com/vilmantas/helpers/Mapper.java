package com.vilmantas.helpers;

import com.vilmantas.model.Card;
import com.vilmantas.model.Games;
import com.vilmantas.model.Match;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import static com.vilmantas.helpers.InputStreamToString.getRawGameData;

public class Mapper {

    private Mapper() {
    }

    /**
     * Some background:
     * Data are taken from .txt file. Each line in the file represents a new Match.
     * String separated by whitespace represents a Card. First 5 cards of the match goes for Player1,
     * rest of the 5 cards goes to Player2.
     * <p>
     * What method does:
     * 1. Raw game data in String format are split at the new line. List of lines are formed representing list of matches.
     * 2. Looping through the list of raw match data and splitting by whitespace(" "). Raw data represents a card separated by space(" ").
     * 2.1. Looping through the list of cards. Assigning first 5 cards to Player1, last 5 cards to Player2.
     * 3. After cards assigned to players - players are assigned to list of matches.
     * 4. Matches are assigned to game and method returns Game object.
     */

    public static Games mapGamesData(InputStream inputStream) throws IOException {

        String rawGameData = getRawGameData(inputStream);

        List<String> gamesDataRaw = List.of(rawGameData.split("\\R")); // Special JAVA 8 pattern to match new line for any os

        List<Match> matches = assignPlayersToMatchesAndAssignCardsToPlayers(gamesDataRaw);

        return Games.builder().pokerMatchList(matches).build();
    }

    private static List<Match> assignPlayersToMatchesAndAssignCardsToPlayers(List<String> gamesDataRaw) {

        return gamesDataRaw.stream()
                .map(gameRaw -> {

                    List<String> cardsRawData = List.of(gameRaw.split(" "));

                    List<Card> cardsOfPlayer1 = assignFirst5Cards(cardsRawData);
                    List<Card> cardsOfPlayer2 = assignLast5Cards(cardsRawData);

                    return Match.builder()
                            .player1Cards(cardsOfPlayer1)
                            .player2Cards(cardsOfPlayer2)
                            .build();
                })
                .collect(Collectors.toList());
    }

    private static List<Card> assignFirst5Cards(List<String> cardsRawData) {

        return cardsRawData.stream()
                .map(card -> Card.builder().rankAndSuite(card).build())
                .limit(5)
                .collect(Collectors.toList());
    }

    private static List<Card> assignLast5Cards(List<String> cardsRawData) {

        return cardsRawData.stream()
                .map(card -> Card.builder().rankAndSuite(card).build())
                .skip(5)
                .collect(Collectors.toList());
    }
}
