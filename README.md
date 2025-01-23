# Scratch Game

Scratch Game is a Java application that implements a scratch card game mechanic. Users place a bet, and a symbol matrix (generated based on probabilities) determines whether they win or lose.

## Features
- Generate game matrices of arbitrary sizes (e.g., 3x3, 4x4).
- Support for two types of symbols:
    - **Standard Symbols** — determine winnings based on combinations.
    - **Bonus Symbols** — applied only if a win is achieved.
- Calculate rewards based on combinations and bonuses.
- Fully configurable through a JSON file.

## Tech Stack
- **Java 17** — main programming language.
- **Maven** — dependency and build management.
- **Jackson** — JSON handling.
- **JUnit 5** — testing framework.
- **Lombok** — for simplifying model classes.

## Requirements
- Java 17 or higher.
- Maven (for building the project).

## Configuration
Example `config.json` file (see full details in the project):
```json
{
  "columns": 3,
  "rows": 3,
  "symbols": {
    "A": {
      "reward_multiplier": 5,
      "type": "standard"
    },
    "10x": {
      "reward_multiplier": 10,
      "type": "bonus",
      "impact": "multiply_reward"
    }
  },
  "probabilities": {
    "standard_symbols": [
      {
        "row": 0,
        "column": 0,
        "symbols": {
          "A": 1,
          "B": 2
        }
      }
    ],
    "bonus_symbols": {
      "symbols": {
        "10x": 1
      }
    }
  },
  "win_combinations": {
    "same_symbol_3_times": {
      "reward_multiplier": 2,
      "when": "same_symbols",
      "count": 3
    }
  }
}
````

### How to Run

To build the project, run:

```bash
mvn clean package
```
#### Place the Config File
Ensure the `config.json` file is located in the root directory of the project, as shown below:
```
scratch-game/
├── src/
├── target/
├── config.json
├── pom.xml
└── README.md
```

#### Run the Game
Execute the following command, providing the path to the `config.json` file and the betting amount:
```bash
java -jar target/scratch-game-1.0.0-jar-with-dependencies.jar config.json 100
```


The game will output a JSON result containing the following:
- The generated matrix.
- Total reward.
- Applied winning combinations and bonuses.

Example output:

```json
{
  "matrix": [
    ["A", "A", "A"],
    ["10x", "B", "C"],
    ["D", "E", "F"]
  ],
  "reward": 10000,
  "applied_winning_combinations": {
    "A": ["same_symbol_3_times"]
  },
  "applied_bonus_symbol": "10x"
}
```

### Testing

To run tests:

```bash
mvn test
```

### Contact

For questions or suggestions, feel free to contact the developer at [nofrant1@gmail.com](mailto:nofrant1@gmail.com).
