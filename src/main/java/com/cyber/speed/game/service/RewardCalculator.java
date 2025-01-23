package com.cyber.speed.game.service;

import com.cyber.speed.game.model.Config;
import com.cyber.speed.game.model.SymbolConfig;
import com.cyber.speed.game.model.WinCombination;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RewardCalculator {

  public static class CalculationResult {

    public double totalReward;
    public Map<String, List<String>> appliedCombinations = new HashMap<>();
    public String appliedBonus;
  }

  public static CalculationResult calculateReward(List<List<String>> matrix,
      Config config,
      double betAmount) {
    CalculationResult result = new CalculationResult();

    Map<String, Integer> symbolCounts = new HashMap<>();
    List<String> bonusSymbolsFound = new ArrayList<>();

    for (int r = 0; r < matrix.size(); r++) {
      for (int c = 0; c < matrix.get(r).size(); c++) {
        String symbol = matrix.get(r).get(c);
        SymbolConfig sc = config.getSymbols().get(symbol);
        if (sc == null) {
          continue;
        }
        if ("standard".equalsIgnoreCase(sc.getType())) {
          symbolCounts.put(symbol, symbolCounts.getOrDefault(symbol, 0) + 1);
        } else if ("bonus".equalsIgnoreCase(sc.getType())) {
          bonusSymbolsFound.add(symbol);
        }
      }
    }

    List<Map.Entry<String, WinCombination>> sameSymbolCombinations = new ArrayList<>();
    List<Map.Entry<String, WinCombination>> linearCombinations = new ArrayList<>();

    for (Map.Entry<String, WinCombination> entry : config.getWinCombinations().entrySet()) {
      WinCombination comb = entry.getValue();
      if ("same_symbols".equalsIgnoreCase(comb.getWhen())) {
        sameSymbolCombinations.add(entry);
      } else if ("linear_symbols".equalsIgnoreCase(comb.getWhen())) {
        linearCombinations.add(entry);
      }
    }

    double total = 0.0;
    Map<String, List<String>> appliedCombinationsMap = new HashMap<>();

    for (String symbol : symbolCounts.keySet()) {
      SymbolConfig sc = config.getSymbols().get(symbol);
      if (sc == null) {
        continue;
      }

      double baseReward = betAmount * sc.getRewardMultiplier();
      int count = symbolCounts.get(symbol);

      Map<String, Double> groupToMultiplier = new HashMap<>();
      Map<String, String> groupToCombinationName = new HashMap<>();

      for (Map.Entry<String, WinCombination> entry : sameSymbolCombinations) {
        String comboName = entry.getKey();
        WinCombination combo = entry.getValue();
        if (count >= combo.getCount()) {
          double currentComboMultiplier = combo.getRewardMultiplier();
          String group = combo.getGroup();

          if (!groupToMultiplier.containsKey(group)
              || currentComboMultiplier > groupToMultiplier.get(group)) {
            groupToMultiplier.put(group, currentComboMultiplier);
            groupToCombinationName.put(group, comboName);
          }
        }
      }

      for (Map.Entry<String, WinCombination> entry : linearCombinations) {
        String comboName = entry.getKey();
        WinCombination combo = entry.getValue();
        List<List<String>> coveredAreas = combo.getCoveredAreas();
        if (coveredAreas == null) {
          continue;
        }

        boolean matched = false;
        for (List<String> area : coveredAreas) {
          boolean allMatch = true;
          for (String coord : area) {
            String[] parts = coord.split(":");
            int rr = Integer.parseInt(parts[0]);
            int cc = Integer.parseInt(parts[1]);
            if (rr < 0 || rr >= matrix.size()
                || cc < 0 || cc >= matrix.get(rr).size()) {
              allMatch = false;
              break;
            }
            String cellSymbol = matrix.get(rr).get(cc);

            if (!cellSymbol.equals(symbol)) {
              allMatch = false;
              break;
            }
          }
          if (allMatch) {
            matched = true;
            break;
          }
        }

        if (matched) {
          double currentComboMultiplier = combo.getRewardMultiplier();
          String group = combo.getGroup();
          if (!groupToMultiplier.containsKey(group)
              || currentComboMultiplier > groupToMultiplier.get(group)) {
            groupToMultiplier.put(group, currentComboMultiplier);
            groupToCombinationName.put(group, comboName);
          }
        }
      }

      double finalMultiplier = 1.0;
      List<String> combosUsedForSymbol = new ArrayList<>();
      for (Map.Entry<String, Double> e : groupToMultiplier.entrySet()) {
        finalMultiplier *= e.getValue();
        combosUsedForSymbol.add(groupToCombinationName.get(e.getKey()));
      }

      if (!combosUsedForSymbol.isEmpty()) {
        double symbolReward = baseReward * finalMultiplier;
        total += symbolReward;
        appliedCombinationsMap.put(symbol, combosUsedForSymbol);
      }
    }

    String appliedBonusSymbol = null;
    if (total > 0 && !bonusSymbolsFound.isEmpty()) {
      appliedBonusSymbol = bonusSymbolsFound.get(0);
      SymbolConfig bonusConfig = config.getSymbols().get(appliedBonusSymbol);
      if (bonusConfig != null) {
        switch (bonusConfig.getImpact()) {
          case "multiply_reward":
            total *= bonusConfig.getRewardMultiplier();
            break;
          case "extra_bonus":
            total += bonusConfig.getExtra();
            break;
          case "miss":
          default:
            break;
        }
      }
    }

    result.totalReward = total;
    result.appliedCombinations = appliedCombinationsMap;
    result.appliedBonus = appliedBonusSymbol;

    return result;
  }
}
