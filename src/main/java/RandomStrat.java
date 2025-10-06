import java.util.Random;
public class Random implements StrategyMain {

    private Random rand = new Random();

    public String getMove(String PlayerChoice) {
        int choice = rand.nextInt(3);
        if (choice == 0)
        {
            return "R";
        }
        else if (choice == 1)
        {
            return "P";
        }
        else if (choice == 2)
        {
            return "S";
        }
    }
}
