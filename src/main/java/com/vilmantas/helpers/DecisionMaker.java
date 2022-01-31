package com.vilmantas.helpers;

import com.vilmantas.model.Card;
import com.vilmantas.model.enums.Hands;
import com.vilmantas.model.enums.Rank;
import com.vilmantas.model.enums.Suit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import static com.vilmantas.model.enums.Hands.*;


/**
 * Helper class with logic that determines what hand player has, and who wins.
 */

public class DecisionMaker {

    private DecisionMaker() {
    }

    // Sorted by default and no duplicates
    private static final EnumSet<Suit> SUITS_UNIQUE_SORTED = EnumSet.noneOf(Suit.class);
    private static final EnumSet<Rank> RANKS_UNIQUE_SORTED = EnumSet.noneOf(Rank.class);

    // Need duplicates
    private static final List<Rank> RANKS = new ArrayList<>();

    private static final String PLAYER_1_WINS = "Player 1 wins!";
    private static final String PLAYER_2_WINS = "Player 2 wins!";

    public static String decideWhoWins(List<Card> player1Cards, List<Card> player2Cards) {

        Hands player1Hand = determineWhatPlayerHas(player1Cards);
        Hands player2Hand = determineWhatPlayerHas(player2Cards);

        int compareHands = player1Hand.compareTo(player2Hand);

        if (compareHands < 0) {
            return PLAYER_2_WINS;
        }

        if (compareHands == 0) {

            return tieBreaker(player1Cards, player2Cards, player1Hand);
        }

        return PLAYER_1_WINS;
    }

    private static String tieBreaker(List<Card> player1Cards, List<Card> player2Cards, Hands tiedHands) {

        List<Rank> player1Ranks = player1Cards.stream()
                .map(Card::getRank)
                .collect(Collectors.toList());

        List<Rank> player2Ranks = player2Cards.stream()
                .map(Card::getRank)
                .collect(Collectors.toList());

        int compareRanks;

        if (tiedHands == ROYAL_FLUSH) {

            return "It's a tie!";
        }

        if (tiedHands == STRAIGHT_FLUSH || tiedHands == FLUSH || tiedHands == STRAIGHT || tiedHands == HIGH_CARD) {

            Collections.reverse(player1Ranks);
            Collections.reverse(player2Ranks);

            for (int i = 0; i < player1Ranks.size(); i++) {

                compareRanks = player1Ranks.get(i).compareTo(player2Ranks.get(i));

                if (compareRanks > 0) {
                    return PLAYER_1_WINS;
                }
                if (compareRanks < 0) {
                    return PLAYER_2_WINS;
                }
            }
        }

        if (tiedHands == FOUR_OF_A_KIND) {

            return getWinnerByFrequency(player1Ranks, player2Ranks, 4);
        }

        if (tiedHands == FULL_HOUSE || tiedHands == THREE_OF_A_KIND) {

            return getWinnerByFrequency(player1Ranks, player2Ranks, 3);
        }

        if (tiedHands == TWO_PAIR) {

            Collections.sort(player1Ranks);
            Collections.sort(player2Ranks);

            Rank player1NotPairedRank = getRankByFrequency(player1Ranks, 1);
            Rank player2NotPairedRank = getRankByFrequency(player2Ranks, 1);

            player1Ranks.remove(player1NotPairedRank);
            player2Ranks.remove(player2NotPairedRank);

            int highestPairIndex = player1Ranks.size() - 1;
            int lowestPairIndex = 0;

            compareRanks = player1Ranks.get(highestPairIndex).compareTo(player2Ranks.get(highestPairIndex));

            if (compareRanks > 0) {
                return PLAYER_1_WINS;
            }
            if (compareRanks < 0) {
                return PLAYER_2_WINS;
            }

            compareRanks = player1Ranks.get(lowestPairIndex).compareTo(player2Ranks.get(lowestPairIndex));

            if (compareRanks > 0) {
                return PLAYER_1_WINS;
            }
            if (compareRanks < 0) {
                return PLAYER_2_WINS;
            }

            compareRanks = player1NotPairedRank.compareTo(player2NotPairedRank);

            if (compareRanks > 0) {
                return PLAYER_1_WINS;
            }
            if (compareRanks < 0) {
                return PLAYER_2_WINS;
            }
        }

        if (tiedHands == PAIR) {

            Collections.reverse(player1Ranks);
            Collections.reverse(player2Ranks);

            Rank player1Rank = getRankByFrequency(player1Ranks, 2);
            Rank player2Rank = getRankByFrequency(player2Ranks, 2);

            compareRanks = player1Rank.compareTo(player2Rank);

            if (compareRanks > 0) {
                return PLAYER_1_WINS;
            }

            if (compareRanks < 0) {
                return PLAYER_2_WINS;
            }

            player1Ranks.removeAll(Collections.singleton(player1Rank));
            player2Ranks.removeAll(Collections.singleton(player2Rank));

            for (int i = 0; i < player1Ranks.size(); i++) {

                compareRanks = player1Ranks.get(i).compareTo(player2Ranks.get(i));

                if (compareRanks > 0) {
                    return PLAYER_1_WINS;
                }
                if (compareRanks < 0) {
                    return PLAYER_2_WINS;
                }
            }

        }

        return "It's a tie!";
    }

    private static String getWinnerByFrequency(List<Rank> player1Ranks, List<Rank> player2Ranks, int sameCardsAmount) {

        int compareRanks;
        Rank player1Rank = getRankByFrequency(player1Ranks, sameCardsAmount);
        Rank player2Rank = getRankByFrequency(player2Ranks, sameCardsAmount);

        compareRanks = player1Rank.compareTo(player2Rank);

        if (compareRanks > 0) {

            return PLAYER_1_WINS;
        }

        if (compareRanks < 0) {

            return PLAYER_2_WINS;
        }

        throw new IllegalArgumentException("Unexpected arguments does not fit in the logic of the method. " + player1Ranks + ", " + player2Ranks);
    }

    private static Rank getRankByFrequency(List<Rank> ranks, int sameCardsAmount) {

        return ranks.stream()
                .filter(rank -> Collections.frequency(ranks, rank) == sameCardsAmount)
                .findFirst()
                .get();
    }

    public static Hands determineWhatPlayerHas(List<Card> playerHand) {

        loadListsAndSets(playerHand);

        if (isRoyalFlush()) {
            tearDown();
            return ROYAL_FLUSH;
        }
        if (isStraightFlush()) {
            tearDown();
            return STRAIGHT_FLUSH;
        }
        if (isFourOfAKind()) {
            tearDown();
            return FOUR_OF_A_KIND;
        }
        if (isFullHouse()) {
            tearDown();
            return FULL_HOUSE;
        }
        if (isFlush()) {
            tearDown();
            return FLUSH;
        }
        if (isStraight()) {
            tearDown();
            return STRAIGHT;
        }
        if (isThreeOfAKind()) {
            tearDown();
            return THREE_OF_A_KIND;
        }
        if (isTwoPair()) {
            tearDown();
            return TWO_PAIR;
        }
        if (isPair()) {
            tearDown();
            return PAIR;
        }

        tearDown();
        return HIGH_CARD;
    }

    private static void loadListsAndSets(List<Card> playerHand) {

        playerHand.forEach(card -> {
            RANKS_UNIQUE_SORTED.add(card.getRank());
            SUITS_UNIQUE_SORTED.add(card.getSuit());
            RANKS.add(card.getRank());
        });

        Collections.sort(RANKS);
    }

    private static void tearDown() {

        RANKS_UNIQUE_SORTED.clear();
        SUITS_UNIQUE_SORTED.clear();
        RANKS.clear();
    }

    private static boolean isRoyalFlush() {

        return RANKS_UNIQUE_SORTED.contains(Rank.ACE) && RANKS_UNIQUE_SORTED.contains(Rank.KING) && isFlush() && isStraight();
    }

    private static boolean isStraightFlush() {

        return (!RANKS_UNIQUE_SORTED.contains(Rank.ACE) || !RANKS_UNIQUE_SORTED.contains(Rank.KING)) && isFlush() && isStraight();
    }

    private static boolean isFourOfAKind() {

        return isRanksSameAtIndex(0, 3) || isRanksSameAtIndex(1, 4);
    }

    private static boolean isFullHouse() {

        return (isRanksSameAtIndex(0, 2) && isRanksSameAtIndex(3, 4))
                || (isRanksSameAtIndex(0, 1) && isRanksSameAtIndex(2, 4));
    }

    private static boolean isFlush() {

        return RANKS_UNIQUE_SORTED.size() == 5 && SUITS_UNIQUE_SORTED.size() == 1;
    }

    private static boolean isStraight() {

        if (RANKS_UNIQUE_SORTED.size() != 5) {
            return false;
        }

        if (RANKS_UNIQUE_SORTED.contains(Rank.ACE) && RANKS_UNIQUE_SORTED.contains(Rank.TWO) && RANKS_UNIQUE_SORTED.contains(Rank.THREE)
                && RANKS_UNIQUE_SORTED.contains(Rank.FOUR) && RANKS_UNIQUE_SORTED.contains(Rank.FIVE)) {
            return true;
        }

        // Taking first card in the hand and to its value adding 1 - that means second
        // card value has to increase by one. If not - that's not straight
        int nextCard = RANKS.get(0).ordinal() + 1;
        for (int i = 1; i < RANKS.size(); i++) {
            if (RANKS.get(i).ordinal() == nextCard) {
                nextCard++;
            } else {
                return false;
            }
        }
        return true;
    }

    private static boolean isThreeOfAKind() {

        return (isRanksSameAtIndex(0, 2) && isRanksNotSameAtIndex(3, 4)) ||
                (isRanksSameAtIndex(1, 3) && isRanksNotSameAtIndex(0, 4)) ||
                (isRanksSameAtIndex(2, 4) && isRanksNotSameAtIndex(0, 1));
    }

    private static boolean isTwoPair() {

        if (RANKS_UNIQUE_SORTED.size() != 3) {
            return false;
        }

        return (isRanksSameAtIndex(0, 1) && isRanksSameAtIndex(2, 3)) ||
                (isRanksSameAtIndex(0, 1) && isRanksSameAtIndex(3, 4)) ||
                (isRanksSameAtIndex(1, 2) && isRanksSameAtIndex(3, 4));
    }

    private static boolean isPair() {

        return RANKS_UNIQUE_SORTED.size() == 4;
    }

    private static boolean isRanksSameAtIndex(int sortedCardIndex1, int sortedCardIndex2) {
        return RANKS.get(sortedCardIndex1) == RANKS.get(sortedCardIndex2);
    }

    private static boolean isRanksNotSameAtIndex(int sortedCardIndex1, int sortedCardIndex2) {
        return RANKS.get(sortedCardIndex1) != RANKS.get(sortedCardIndex2);
    }
}
