package com.vilmantas;

import com.vilmantas.model.Card;
import com.vilmantas.model.Games;

import java.util.List;
import java.util.stream.Collectors;

public class TestCommons {


    List<Card> makeCards(List<String> cardsString) {

        return cardsString.stream()
                .map(
                        card -> Card.builder()
                                .rankAndSuite(card)
                                .build()
                )
                .collect(Collectors.toList());
    }

    List<String> getPlayer1CardsFromFile(Games game, int matchNumber) {

        return game.getPokerMatchList()
                .get(matchNumber)
                .getPlayer1Cards()
                .stream()
                .map(Card::getRankAndSuite)
                .collect(Collectors.toList());
    }

    List<String> getPlayer2CardsFromFile(Games game, int matchNumber) {

        return game.getPokerMatchList()
                .get(matchNumber)
                .getPlayer2Cards()
                .stream()
                .map(Card::getRankAndSuite)
                .collect(Collectors.toList());
    }
}
