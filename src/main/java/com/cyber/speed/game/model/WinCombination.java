package com.cyber.speed.game.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WinCombination {

  @JsonProperty("reward_multiplier")
  private double rewardMultiplier;
  @JsonProperty("when")
  private String when;
  @JsonProperty("count")
  private int count;
  @JsonProperty("group")
  private String group;
  @JsonProperty("covered_areas")
  private List<List<String>> coveredAreas;
}