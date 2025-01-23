package com.cyber.speed.game.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.cyber.speed.game.model.Config;
import com.cyber.speed.game.model.SymbolConfig;
import com.cyber.speed.game.model.WinCombination;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class RewardCalculatorTest {

  @Test
  public void testCalculateReward_SameSymbolsAndBonus() {
    Config config = new Config();
    config.setSymbols(createSymbolsAAnd10x());
    config.setWinCombinations(createWinCombos_sameSymbol3Times(2.0));

    List<List<String>> matrix = Arrays.asList(
        Arrays.asList("A", "A", "A"),
        Arrays.asList("10x", "B", "C"),
        Arrays.asList("D", "E", "F")
    );
    RewardCalculator.CalculationResult result =
        RewardCalculator.calculateReward(matrix, config, 100.0);

    assertEquals(10000, result.totalReward, 1e-6);
    assertEquals("10x", result.appliedBonus);
  }

  @Test
  public void testCalculateReward_NoWin_NoReward() {
    Config config = new Config();
    config.setSymbols(createSymbolsAAnd10x());
    config.setWinCombinations(createWinCombos_sameSymbol3Times(2.0));

    List<List<String>> matrix = Arrays.asList(
        Arrays.asList("A", "B", "C"),
        Arrays.asList("10x", "B", "C"),
        Arrays.asList("D", "E", "F")
    );

    RewardCalculator.CalculationResult result =
        RewardCalculator.calculateReward(matrix, config, 100.0);

    assertEquals(0.0, result.totalReward, 1e-6);
    assertNull(result.appliedBonus);
  }

  @Test
  public void testCalculateReward_SameSymbols_ChooseMaxInGroup() {
    Config config = new Config();
    config.setSymbols(createSymbolsAAnd10x());

    Map<String, WinCombination> combos = new HashMap<>();

    WinCombination same3 = new WinCombination();
    same3.setWhen("same_symbols");
    same3.setCount(3);
    same3.setRewardMultiplier(2);
    same3.setGroup("same_symbols");
    combos.put("same_symbol_3_times", same3);

    WinCombination same4 = new WinCombination();
    same4.setWhen("same_symbols");
    same4.setCount(4);
    same4.setRewardMultiplier(3);
    same4.setGroup("same_symbols");
    combos.put("same_symbol_4_times", same4);

    config.setWinCombinations(combos);

    List<List<String>> matrix = Arrays.asList(
        Arrays.asList("A", "A", "A", "A"),
        Arrays.asList("B", "C", "D", "F"),
        Arrays.asList("B", "C", "D", "F")
    );

    RewardCalculator.CalculationResult result =
        RewardCalculator.calculateReward(matrix, config, 100.0);

    assertEquals(1500.0, result.totalReward, 1e-6);
  }

  private Map<String, SymbolConfig> createSymbolsAAnd10x() {
    Map<String, SymbolConfig> symbols = new HashMap<>();

    SymbolConfig aConfig = new SymbolConfig();
    aConfig.setType("standard");
    aConfig.setRewardMultiplier(5);
    symbols.put("A", aConfig);

    SymbolConfig bonusConfig = new SymbolConfig();
    bonusConfig.setType("bonus");
    bonusConfig.setImpact("multiply_reward");
    bonusConfig.setRewardMultiplier(10);
    symbols.put("10x", bonusConfig);

    return symbols;
  }

  private Map<String, WinCombination> createWinCombos_sameSymbol3Times(double multiplier) {
    Map<String, WinCombination> combos = new HashMap<>();
    WinCombination same3 = new WinCombination();
    same3.setWhen("same_symbols");
    same3.setCount(3);
    same3.setRewardMultiplier(multiplier);
    same3.setGroup("same_symbols");
    combos.put("same_symbol_3_times", same3);
    return combos;
  }
}
