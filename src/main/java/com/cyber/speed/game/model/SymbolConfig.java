package com.cyber.speed.game.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SymbolConfig {

  @JsonProperty("reward_multiplier")
  private double rewardMultiplier;

  private String type;
  private String impact;
  @JsonProperty("extra")
  private double extra;
}