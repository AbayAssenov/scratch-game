package com.cyber.speed.game;

import com.cyber.speed.game.model.Config;
import com.cyber.speed.game.model.Result;
import com.cyber.speed.game.service.MatrixGenerator;
import com.cyber.speed.game.service.RewardCalculator;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.List;

public class ScratchGame {

  public static void main(String[] args) throws Exception {
    if (args.length < 2) {
      System.err.println("Usage: java -jar <jarfile> <configPath> <betAmount>");
      System.exit(1);
    }

    String configPath = args[0];
    double betAmount = Double.parseDouble(args[1]);

    ObjectMapper objectMapper = new ObjectMapper();
    Config config = objectMapper.readValue(new File(configPath), Config.class);

    List<List<String>> matrix = MatrixGenerator.generateMatrix(config);

    RewardCalculator.CalculationResult calculationResult =
        RewardCalculator.calculateReward(matrix, config, betAmount);

    Result result = new Result(
        matrix,
        calculationResult.totalReward,
        calculationResult.appliedCombinations,
        calculationResult.appliedBonus
    );

    String jsonOutput = objectMapper
        .writerWithDefaultPrettyPrinter()
        .writeValueAsString(result);
    System.out.println(jsonOutput);
  }
}
