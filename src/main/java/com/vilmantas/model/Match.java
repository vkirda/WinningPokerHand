package com.vilmantas.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@ToString
public class Match {

    private List<Card> player1Cards;
    private List<Card> player2Cards;

}
