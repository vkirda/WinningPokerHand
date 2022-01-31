package com.vilmantas;

import com.vilmantas.model.Card;
import com.vilmantas.model.Games;
import com.vilmantas.model.enums.Hands;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static com.vilmantas.helpers.DecisionMaker.decideWhoWins;
import static com.vilmantas.helpers.DecisionMaker.determineWhatPlayerHas;
import static com.vilmantas.helpers.FileReader.getInputStream;
import static com.vilmantas.helpers.InputStreamToString.getRawGameData;
import static com.vilmantas.helpers.Mapper.mapGamesData;
import static com.vilmantas.model.enums.Hands.*;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UnitTests extends TestCommons {

    InputStream inputStream = getInputStream("p054_poker.txt");

    @Test
    void testFileReading() {

        assertNotNull(inputStream);
    }

    @Test
    void testInputStreamConversionToString() throws IOException {

        String rawGameData = getRawGameData(inputStream);
        assertNotNull(rawGameData);
    }

    @Test
    void testMatch1Cards() throws IOException {

        Games games = mapGamesData(inputStream);

        List<String> actualCardsOfPlayer1Match1 = getPlayer1CardsFromFile(games, 0);
        List<String> actualCardsOfPlayer2Match1 = getPlayer2CardsFromFile(games, 0);

        assertEquals(Arrays.asList("8C", "TS", "KC", "9H", "4S"), actualCardsOfPlayer1Match1);
        assertEquals(Arrays.asList("7D", "2S", "5D", "3S", "AC"), actualCardsOfPlayer2Match1);
    }

    @Test
    void testMatch1000Cards() throws IOException {

        Games games = mapGamesData(inputStream);

        List<String> actualCardsOfPlayer1Match1000 = getPlayer1CardsFromFile(games, 999);
        List<String> actualCardsOfPlayer2Match1000 = getPlayer2CardsFromFile(games, 999);

        assertEquals(Arrays.asList("AS", "KD", "3D", "JD", "8H"), actualCardsOfPlayer1Match1000);
        assertEquals(Arrays.asList("7C", "8C", "5C", "QD", "6C"), actualCardsOfPlayer2Match1000);
    }

    @Test
    void testCardObject() throws IOException {

        Games games = mapGamesData(inputStream);

        Card cardExpected = Card.builder()
                .rankAndSuite("8C")
                .build();

        Card cardActual = games.getPokerMatchList()
                .get(0)
                .getPlayer1Cards()
                .get(0);

        assertEquals(cardExpected, cardActual);
    }

    @Test
    void testHighCardLogic() throws IOException {

        Games games = mapGamesData(inputStream);
        List<Card> cards = games.getPokerMatchList().get(0).getPlayer1Cards();

        Hands actualHand = determineWhatPlayerHas(cards);

        assertEquals(HIGH_CARD, actualHand);
    }

    @Test
    void testPairLogic() {

        List<Card> cards = makeCards(asList("6C", "AD", "AS", "JC", "TC"));

        Hands actualHand = determineWhatPlayerHas(cards);

        assertEquals(PAIR, actualHand);
    }

    @Test
    void testTwoPairLogic() throws IOException {

        Games games = mapGamesData(inputStream);
        List<Card> cards = games.getPokerMatchList().get(1).getPlayer1Cards();

        Hands actualHand = determineWhatPlayerHas(cards);

        assertEquals(TWO_PAIR, actualHand);
    }

    @Test
    void testThreeOfAKindLogic() {

        List<Card> cards = makeCards(asList("AC", "AD", "AS", "JC", "TC"));

        Hands actualHand = determineWhatPlayerHas(cards);

        assertEquals(THREE_OF_A_KIND, actualHand);
    }

    @Test
    void testStraightLow() {

        List<Card> cards = makeCards(asList("AC", "2D", "3S", "4C", "5C"));

        Hands actualHand = determineWhatPlayerHas(cards);

        assertEquals(STRAIGHT, actualHand);
    }

    @Test
    void testStraightHigh() {

        List<Card> cards = makeCards(asList("KC", "QD", "JS", "TC", "9C"));

        Hands actualHand = determineWhatPlayerHas(cards);

        assertEquals(STRAIGHT, actualHand);
    }

    @Test
    void testFlush() {

        List<Card> cards = makeCards(asList("KD", "QD", "JD", "TD", "2D"));

        Hands actualHand = determineWhatPlayerHas(cards);

        assertEquals(FLUSH, actualHand);
    }

    @Test
    void testFullHouse() {

        List<Card> cards = makeCards(asList("JD", "JS", "JC", "2D", "2S"));

        Hands actualHand = determineWhatPlayerHas(cards);

        assertEquals(FULL_HOUSE, actualHand);
    }

    @Test
    void testFourOfAKind() {

        List<Card> cards = makeCards(asList("JD", "2S", "2C", "2D", "2H"));

        Hands actualHand = determineWhatPlayerHas(cards);

        assertEquals(FOUR_OF_A_KIND, actualHand);
    }

    @Test
    void testStraightFlush() {

        List<Card> cards = makeCards(asList("5H", "4H", "3H", "2H", "AH"));

        Hands actualHand = determineWhatPlayerHas(cards);

        assertEquals(STRAIGHT_FLUSH, actualHand);
    }

    @Test
    void testRoyal() {

        List<Card> cards = makeCards(asList("KH", "QH", "JH", "TH", "AH"));

        Hands actualHand = determineWhatPlayerHas(cards);

        assertEquals(ROYAL_FLUSH, actualHand);
    }

    @Test
    void testPlayer1Wins() {

        List<Card> player1cards = makeCards(asList("3H", "QH", "JH", "TH", "2H"));
        List<Card> player2cards = makeCards(asList("KH", "KC", "JH", "TH", "3S"));

        String announceAWinner = decideWhoWins(player1cards, player2cards);

        assertEquals("Player 1 wins!", announceAWinner);
    }

    @Test
    void testPlayer2Wins() {

        List<Card> player1cards = makeCards(asList("KH", "KC", "JH", "TH", "3S"));
        List<Card> player2cards = makeCards(asList("3H", "QH", "JH", "TH", "2H"));

        String announceAWinner = decideWhoWins(player1cards, player2cards);

        assertEquals("Player 2 wins!", announceAWinner);
    }

    @Test
    void testTieRoyalFlush() {

        List<Card> player1cards = makeCards(asList("AH", "KH", "QH", "JH", "TH"));
        List<Card> player2cards = makeCards(asList("TC", "JC", "QC", "KC", "AC"));

        String announceAWinner = decideWhoWins(player1cards, player2cards);

        assertEquals("It's a tie!", announceAWinner);
    }

    @Test
    void testTieStraightFlush() {

        List<Card> player1cards = makeCards(asList("KH", "QH", "JH", "TH", "9H"));
        List<Card> player2cards = makeCards(asList("KC", "QC", "JC", "TC", "9C"));

        String announceAWinner = decideWhoWins(player1cards, player2cards);

        assertEquals("It's a tie!", announceAWinner);
    }

    @Test
    void testTieBreakerStraightFlush() {

        List<Card> player1cards = makeCards(asList("KH", "QH", "JH", "TH", "9H"));
        List<Card> player2cards = makeCards(asList("QC", "JC", "TC", "9C", "8C"));

        String announceAWinner = decideWhoWins(player1cards, player2cards);

        assertEquals("Player 1 wins!", announceAWinner);
    }

    @Test
    void testTieBreakerFourOfAKindLow() {

        List<Card> player1cards = makeCards(asList("4H", "4S", "4D", "4C", "5H"));
        List<Card> player2cards = makeCards(asList("3C", "3D", "3S", "3H", "2C"));

        String announceAWinner = decideWhoWins(player1cards, player2cards);

        assertEquals("Player 1 wins!", announceAWinner);
    }

    @Test
    void testTieBreakerFullHouse() {

        List<Card> player1cards = makeCards(asList("2H", "2S", "4D", "4C", "4H"));
        List<Card> player2cards = makeCards(asList("3C", "3D", "3S", "5H", "5C"));

        String announceAWinner = decideWhoWins(player1cards, player2cards);

        assertEquals("Player 1 wins!", announceAWinner);
    }

    @Test
    void testTieBreakerFlush() {

        List<Card> player1cards = makeCards(asList("KH", "QH", "JH", "TH", "6H"));
        List<Card> player2cards = makeCards(asList("KC", "QC", "JC", "TC", "5C"));

        String announceAWinner = decideWhoWins(player1cards, player2cards);

        assertEquals("Player 1 wins!", announceAWinner);
    }

    @Test
    void testTieBreakerStraight() {

        List<Card> player1cards = makeCards(asList("KH", "QH", "JH", "9C", "TC"));
        List<Card> player2cards = makeCards(asList("QC", "JC", "TC", "9D", "8D"));

        String announceAWinner = decideWhoWins(player1cards, player2cards);

        assertEquals("Player 1 wins!", announceAWinner);
    }

    @Test
    void testTieBreakerThreeOfAKind() {

        List<Card> player1cards = makeCards(asList("KD", "7H", "7S", "9C", "7C"));
        List<Card> player2cards = makeCards(asList("6H", "JC", "6D", "6S", "8D"));

        String announceAWinner = decideWhoWins(player1cards, player2cards);

        assertEquals("Player 1 wins!", announceAWinner);
    }

    @Test
    void testTieBreakerTwoPairHigh() {

        List<Card> player1cards = makeCards(asList("JH", "JC", "6D", "6S", "8D"));
        List<Card> player2cards = makeCards(asList("9D", "7H", "7S", "5C", "9H"));

        String announceAWinner = decideWhoWins(player1cards, player2cards);

        assertEquals("Player 1 wins!", announceAWinner);
    }

    @Test
    void testTieBreakerTwoPairLow() {

        List<Card> player1cards = makeCards(asList("JH", "JC", "8D", "8S", "3D"));
        List<Card> player2cards = makeCards(asList("JD", "JS", "7S", "7C", "9H"));

        String announceAWinner = decideWhoWins(player1cards, player2cards);

        assertEquals("Player 1 wins!", announceAWinner);
    }

    @Test
    void testTieBreakerTwoPairNotPairedCard() {

        List<Card> player1cards = makeCards(asList("JH", "JC", "8D", "8S", "5D"));
        List<Card> player2cards = makeCards(asList("JD", "JS", "8H", "8C", "4H"));

        String announceAWinner = decideWhoWins(player1cards, player2cards);

        assertEquals("Player 1 wins!", announceAWinner);
    }

    @Test
    void testTieBreakerHigherPair() {

        List<Card> player1cards = makeCards(asList("QH", "QC", "8D", "7S", "5D"));
        List<Card> player2cards = makeCards(asList("JD", "JS", "9H", "7C", "4H"));

        String announceAWinner = decideWhoWins(player1cards, player2cards);

        assertEquals("Player 1 wins!", announceAWinner);
    }

    @Test
    void testTieBreakerPairWithSecondCard() {

        List<Card> player1cards = makeCards(asList("JH", "JC", "8D", "7S", "5D"));
        List<Card> player2cards = makeCards(asList("JS", "JD", "8H", "7C", "4D"));

        String announceAWinner = decideWhoWins(player1cards, player2cards);

        assertEquals("Player 1 wins!", announceAWinner);
    }

    @Test
    void testTieBreakerHighCard() {

        List<Card> player1cards = makeCards(asList("4H", "6C", "8D", "TS", "KD"));
        List<Card> player2cards = makeCards(asList("4H", "6C", "8D", "9S", "KD"));

        String announceAWinner = decideWhoWins(player1cards, player2cards);

        assertEquals("Player 1 wins!", announceAWinner);
    }

    @Test
    void testTieBreakerHighCardLast() {

        List<Card> player1cards = makeCards(asList("5H", "6C", "8D", "TS", "KD"));
        List<Card> player2cards = makeCards(asList("4C", "6H", "8H", "TH", "KH"));

        String announceAWinner = decideWhoWins(player1cards, player2cards);

        assertEquals("Player 1 wins!", announceAWinner);
    }

}
