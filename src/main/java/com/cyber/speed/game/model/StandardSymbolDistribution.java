package com.cyber.speed.game.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StandardSymbolDistribution {

  @JsonProperty("row")
  private int row;

  @JsonProperty("column")
  private int column;

  @JsonProperty("symbols")
  private Map<String, Double> symbols;}
