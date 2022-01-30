package com.vilmantas.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@ToString
public class Games {

    private List<Match> pokerMatchList;

}
