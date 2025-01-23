package com.cyber.speed.game.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProbabilityConfig {

  @JsonProperty("standard_symbols")
  private List<StandardSymbolDistribution> standardSymbols;

  @JsonProperty("bonus_symbols")
  private BonusSymbolsDistribution bonusSymbols;

  public static class BonusSymbolsDistribution {

    @JsonProperty("symbols")
    private Map<String, Double> symbols;

    public Map<String, Double> getSymbols() {
      return symbols;
    }

    public void setSymbols(Map<String, Double> symbols) {
      this.symbols = symbols;
    }
  }
}
