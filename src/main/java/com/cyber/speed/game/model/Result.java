package com.cyber.speed.game.model;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Result {

  private List<List<String>> matrix;
  private double reward;
  private Map<String, List<String>> appliedWinningCombinations;
  private String appliedBonusSymbol;

}