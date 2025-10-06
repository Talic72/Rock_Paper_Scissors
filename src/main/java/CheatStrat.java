public class CheatStrat implements StrategyMain {

    public String getMove(String playerChoice) {

        if (playerChoice == "R")
        {
            return "P";
        }
        else if (playerChoice == "P")
        {
            return "S";
        }
        else
        {
            return "R";
        }

    }

}
