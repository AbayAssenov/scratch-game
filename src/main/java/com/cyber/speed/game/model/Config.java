package com.cyber.speed.game.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.Data;

@Data
public class Config {

  private int columns;
  private int rows;
  private Map<String, SymbolConfig> symbols;
  private ProbabilityConfig probabilities;
  @JsonProperty("win_combinations")
  private Map<String, WinCombination> winCombinations;

}
