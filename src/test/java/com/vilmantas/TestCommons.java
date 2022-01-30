package com.vilmantas;

import com.vilmantas.model.Card;

import java.util.ArrayList;
import java.util.List;

public class TestCommons {


    public List<Card> makeCards(List<String> cardsString) {

        List<Card> cards = new ArrayList<>();

        cardsString.forEach(card -> cards.add(Card.builder().rankAndSuite(card).build()));

        return cards;
    }

    List<Card> player1cards = new ArrayList<>();
    List<Card> player2cards = new ArrayList<>();
}
