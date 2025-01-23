package com.cyber.speed.game.service;

import com.cyber.speed.game.model.Config;
import com.cyber.speed.game.model.StandardSymbolDistribution;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class MatrixGenerator {

  public static List<List<String>> generateMatrix(Config config) {
    int rows = config.getRows();
    int columns = config.getColumns();

    List<StandardSymbolDistribution> standardDistList = config.getProbabilities().getStandardSymbols();
    Map<String, Double> bonusMap = config.getProbabilities().getBonusSymbols().getSymbols();

    Map<String, Map<String, Double>> standardDistByCell = new HashMap<>();
    for (StandardSymbolDistribution ssd : standardDistList) {
      String key = ssd.getRow() + ":" + ssd.getColumn();
      standardDistByCell.put(key, ssd.getSymbols());
    }

    Random random = new Random();
    List<List<String>> matrix = new ArrayList<>();

    for (int r = 0; r < rows; r++) {
      List<String> rowSymbols = new ArrayList<>();
      for (int c = 0; c < columns; c++) {

        String cellKey = r + ":" + c;
        Map<String, Double> standardMap = null;
        if (standardDistByCell.containsKey(cellKey)) {
          standardMap = standardDistByCell.get(cellKey);
        } else {
          standardMap = standardDistByCell.get("0:0");
          if (standardMap == null) {
            throw new RuntimeException("Не найдена конфигурация вероятностей для (0:0)");
          }
        }

        Map<String, Double> combinedMap = new HashMap<>();
        for (Map.Entry<String, Double> e : standardMap.entrySet()) {
          combinedMap.put(e.getKey(), e.getValue());
        }
        for (Map.Entry<String, Double> e : bonusMap.entrySet()) {
          combinedMap.put(e.getKey(), e.getValue());
        }

        double totalWeight = combinedMap.values().stream().mapToDouble(Double::doubleValue).sum();
        double randVal = random.nextDouble() * totalWeight;

        String chosenSymbol = null;
        double cumulative = 0.0;
        for (Map.Entry<String, Double> entry : combinedMap.entrySet()) {
          cumulative += entry.getValue();
          if (randVal <= cumulative) {
            chosenSymbol = entry.getKey();
            break;
          }
        }

        rowSymbols.add(chosenSymbol);
      }
      matrix.add(rowSymbols);
    }

    return matrix;
  }
}
