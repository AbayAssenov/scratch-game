package com.cyber.speed.game.service;

import com.cyber.speed.game.model.Config;
import com.cyber.speed.game.model.ProbabilityConfig;
import com.cyber.speed.game.model.StandardSymbolDistribution;
import com.cyber.speed.game.model.SymbolConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class MatrixGeneratorTest {


  @Test
  public void testGenerateMatrixSize_4x4() {
    Config config = createMinimalConfig(4, 4);

    List<List<String>> matrix = MatrixGenerator.generateMatrix(config);

    Assertions.assertNotNull(matrix, "Matrix is null");
    Assertions.assertEquals(4, matrix.size(), "Matrix row count should be 4");
    for (List<String> row : matrix) {
      Assertions.assertEquals(4, row.size(), "Matrix column count should be 4");
    }
  }


  @Test
  public void testGenerateMatrixSize_3x3() {
    Config config = createMinimalConfig(3, 3);

    List<List<String>> matrix = MatrixGenerator.generateMatrix(config);

    Assertions.assertNotNull(matrix, "Matrix is null");
    Assertions.assertEquals(3, matrix.size(), "Matrix row count should be 3");
    for (List<String> row : matrix) {
      Assertions.assertEquals(3, row.size(), "Matrix column count should be 3");
    }
  }

  private Config createMinimalConfig(int rows, int columns) {
    Config config = new Config();
    config.setRows(rows);
    config.setColumns(columns);

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

    config.setSymbols(symbols);

    ProbabilityConfig probabilityConfig = new ProbabilityConfig();
    StandardSymbolDistribution dist = new StandardSymbolDistribution();
    dist.setRow(0);
    dist.setColumn(0);
    dist.setSymbols(Collections.singletonMap("A", 1.0));
    probabilityConfig.setStandardSymbols(Collections.singletonList(dist));

    ProbabilityConfig.BonusSymbolsDistribution bonusDist = new ProbabilityConfig.BonusSymbolsDistribution();
    bonusDist.setSymbols(Collections.singletonMap("10x", 1.0));
    probabilityConfig.setBonusSymbols(bonusDist);

    config.setProbabilities(probabilityConfig);

    return config;
  }
}
