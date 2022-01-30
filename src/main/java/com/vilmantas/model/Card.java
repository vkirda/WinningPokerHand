package com.vilmantas.model;

import com.vilmantas.model.enums.Rank;
import com.vilmantas.model.enums.Suit;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class Card {

    private Rank rank;
    private Suit suit;
    private String rankAndSuite;

    public Card(Rank rank, Suit suit, String rankAndSuite) {

        this.rankAndSuite = rankAndSuite;

        if (rankAndSuite.contains("C")) {
            this.suit = Suit.CLUBS;
        }
        if (rankAndSuite.contains("H")) {
            this.suit = Suit.HEARTS;
        }
        if (rankAndSuite.contains("S")) {
            this.suit = Suit.SPADES;
        }
        if (rankAndSuite.contains("D")) {
            this.suit = Suit.DIAMONDS;
        }

        if (rankAndSuite.contains("2")) {
            this.rank = Rank.TWO;
        }
        if (rankAndSuite.contains("3")) {
            this.rank = Rank.THREE;
        }
        if (rankAndSuite.contains("4")) {
            this.rank = Rank.FOUR;
        }
        if (rankAndSuite.contains("5")) {
            this.rank = Rank.FIVE;
        }
        if (rankAndSuite.contains("6")) {
            this.rank = Rank.SIX;
        }
        if (rankAndSuite.contains("7")) {
            this.rank = Rank.SEVEN;
        }
        if (rankAndSuite.contains("8")) {
            this.rank = Rank.EIGHT;
        }
        if (rankAndSuite.contains("9")) {
            this.rank = Rank.NINE;
        }
        if (rankAndSuite.contains("T")) {
            this.rank = Rank.TEN;
        }
        if (rankAndSuite.contains("J")) {
            this.rank = Rank.JACK;
        }
        if (rankAndSuite.contains("Q")) {
            this.rank = Rank.QUEEN;
        }
        if (rankAndSuite.contains("K")) {
            this.rank = Rank.KING;
        }
        if (rankAndSuite.contains("A")) {
            this.rank = Rank.ACE;
        }
    }
}
