package com.vilmantas;

import com.vilmantas.model.Card;
import com.vilmantas.model.Games;
import com.vilmantas.model.enums.Hands;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.vilmantas.helpers.DecisionMaker.decideWhoWins;
import static com.vilmantas.helpers.FileReader.getInputStream;
import static com.vilmantas.helpers.InputStreamToString.getRawGameData;
import static com.vilmantas.helpers.Mapper.mapGamesData;
import static com.vilmantas.helpers.DecisionMaker.determineWhatPlayerHas;
//import static com.vilmantas.helpers.DecisionMaker.ordinalStuff;
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

        String actualCard1OfPlayer1Match1 = games.getPokerMatchList().get(0).getPlayer1Cards().get(0).getRankAndSuite();
        String actualCard2OfPlayer1Match1 = games.getPokerMatchList().get(0).getPlayer1Cards().get(1).getRankAndSuite();
        String actualCard3OfPlayer1Match1 = games.getPokerMatchList().get(0).getPlayer1Cards().get(2).getRankAndSuite();
        String actualCard4OfPlayer1Match1 = games.getPokerMatchList().get(0).getPlayer1Cards().get(3).getRankAndSuite();
        String actualCard5OfPlayer1Match1 = games.getPokerMatchList().get(0).getPlayer1Cards().get(4).getRankAndSuite();

        String actualCard1OfPlayer2Match1 = games.getPokerMatchList().get(0).getPlayer2Cards().get(0).getRankAndSuite();
        String actualCard2OfPlayer2Match1 = games.getPokerMatchList().get(0).getPlayer2Cards().get(1).getRankAndSuite();
        String actualCard3OfPlayer2Match1 = games.getPokerMatchList().get(0).getPlayer2Cards().get(2).getRankAndSuite();
        String actualCard4OfPlayer2Match1 = games.getPokerMatchList().get(0).getPlayer2Cards().get(3).getRankAndSuite();
        String actualCard5OfPlayer2Match1 = games.getPokerMatchList().get(0).getPlayer2Cards().get(4).getRankAndSuite();

        assertEquals("8C", actualCard1OfPlayer1Match1);
        assertEquals("TS", actualCard2OfPlayer1Match1);
        assertEquals("KC", actualCard3OfPlayer1Match1);
        assertEquals("9H", actualCard4OfPlayer1Match1);
        assertEquals("4S", actualCard5OfPlayer1Match1);

        assertEquals("7D", actualCard1OfPlayer2Match1);
        assertEquals("2S", actualCard2OfPlayer2Match1);
        assertEquals("5D", actualCard3OfPlayer2Match1);
        assertEquals("3S", actualCard4OfPlayer2Match1);
        assertEquals("AC", actualCard5OfPlayer2Match1);
    }

    @Test
    void testMatch1000Cards() throws IOException {

        Games games = mapGamesData(inputStream);

        String actualCard1OfPlayer1Match1 = games.getPokerMatchList().get(999).getPlayer1Cards().get(0).getRankAndSuite();
        String actualCard2OfPlayer1Match1 = games.getPokerMatchList().get(999).getPlayer1Cards().get(1).getRankAndSuite();
        String actualCard3OfPlayer1Match1 = games.getPokerMatchList().get(999).getPlayer1Cards().get(2).getRankAndSuite();
        String actualCard4OfPlayer1Match1 = games.getPokerMatchList().get(999).getPlayer1Cards().get(3).getRankAndSuite();
        String actualCard5OfPlayer1Match1 = games.getPokerMatchList().get(999).getPlayer1Cards().get(4).getRankAndSuite();

        String actualCard1OfPlayer2Match1 = games.getPokerMatchList().get(999).getPlayer2Cards().get(0).getRankAndSuite();
        String actualCard2OfPlayer2Match1 = games.getPokerMatchList().get(999).getPlayer2Cards().get(1).getRankAndSuite();
        String actualCard3OfPlayer2Match1 = games.getPokerMatchList().get(999).getPlayer2Cards().get(2).getRankAndSuite();
        String actualCard4OfPlayer2Match1 = games.getPokerMatchList().get(999).getPlayer2Cards().get(3).getRankAndSuite();
        String actualCard5OfPlayer2Match1 = games.getPokerMatchList().get(999).getPlayer2Cards().get(4).getRankAndSuite();

        assertEquals("AS", actualCard1OfPlayer1Match1);
        assertEquals("KD", actualCard2OfPlayer1Match1);
        assertEquals("3D", actualCard3OfPlayer1Match1);
        assertEquals("JD", actualCard4OfPlayer1Match1);
        assertEquals("8H", actualCard5OfPlayer1Match1);

        assertEquals("7C", actualCard1OfPlayer2Match1);
        assertEquals("8C", actualCard2OfPlayer2Match1);
        assertEquals("5C", actualCard3OfPlayer2Match1);
        assertEquals("QD", actualCard4OfPlayer2Match1);
        assertEquals("6C", actualCard5OfPlayer2Match1);
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
}
