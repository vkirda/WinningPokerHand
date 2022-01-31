package com.vilmantas;

import com.vilmantas.model.Card;
import com.vilmantas.model.Games;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.vilmantas.helpers.DecisionMaker.decideWhoWins;
import static com.vilmantas.helpers.FileReader.getInputStream;
import static com.vilmantas.helpers.Mapper.mapGamesData;

public class Application {

    public static void main(String[] args) throws IOException {

        InputStream inputStream = getInputStream("p054_poker.txt");

        Games games = mapGamesData(inputStream);

        long count = games.getPokerMatchList().stream()
                .filter(match -> {
                    List<Card> player1Cards = match.getPlayer1Cards();
                    List<Card> player2Cards = match.getPlayer2Cards();
                    return decideWhoWins(player1Cards, player2Cards).equalsIgnoreCase("Player 1 wins!");
                })
                .count();

        System.out.println("Player 1 wins " + count + " times!");
    }
}
