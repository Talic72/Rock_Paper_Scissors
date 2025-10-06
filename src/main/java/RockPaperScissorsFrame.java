import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;


public class RockPaperScissorsFrame extends JFrame {

    private JTextArea resultFrame;
    private JTextField playerWins;
    private JTextField computerWins;
    private JTextField tiesArea;
    private int playerWon = 0;
    private int computerWon = 0;
    private int ties = 0;
    private int playerRockCount = 0;
    private int playerPaperCount = 0;
    private int playerScissorCount = 0;
    private String playerLast = "";

    //Strats
    private StrategyMain randomStrat = new RandomStrat();
    private StrategyMain cheatStrat = new CheatStrat();


    //Main Framework
    public RockPaperScissorsFrame() {

        setSize(1200, 600);
        setLayout(new BorderLayout());

        ControlPanel();
        StatsPanel();
        ResultsPanel();

        setVisible(true);



    }

    private ImageIcon scaleIcon(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage();
        Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    private void ControlPanel() {

        JPanel controlPanel = new JPanel();
        controlPanel.setBorder(BorderFactory.createTitledBorder("Choose an Option Below"));

        ImageIcon rockIcon = scaleIcon("src/rock.jpg", 100, 100);
        ImageIcon paperIcon = scaleIcon("src/paper_1.jpg" ,100, 100);
        ImageIcon scissorsIcon = scaleIcon("src/scissors.jpg" ,100, 100);

        JButton rockBtn = new JButton("Rock", rockIcon);
        JButton paperBtn = new JButton("Paper", paperIcon);
        JButton scissorsBtn = new JButton("Scissors", scissorsIcon);
        JButton quitBtn = new JButton("Quit");

        rockBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        rockBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        paperBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        paperBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        scissorsBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        scissorsBtn.setVerticalTextPosition(SwingConstants.BOTTOM);

        ActionListener moveFetcher = e -> handlePlayerMove(e.getActionCommand());
        rockBtn.addActionListener(moveFetcher);
        paperBtn.addActionListener(moveFetcher);
        scissorsBtn.addActionListener(moveFetcher);
        quitBtn.addActionListener(moveFetcher);

        controlPanel.add(rockBtn);
        controlPanel.add(paperBtn);
        controlPanel.add(scissorsBtn);
        controlPanel.add(quitBtn);

        add(controlPanel, BorderLayout.NORTH);
    }

    private void StatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(1, 6, 5, 5));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Stat Board"));
        statsPanel.add(new JLabel("Player Wins:"));
        playerWins = new JTextField("0");
        playerWins.setEditable(false);
        statsPanel.add(playerWins);

        statsPanel.add(new JLabel("Computer Wins:"));
        computerWins = new JTextField("0");
        computerWins.setEditable(false);
        statsPanel.add(computerWins);

        statsPanel.add(new JLabel("Ties:"));
        tiesArea = new JTextField("0");
        tiesArea.setEditable(false);
        statsPanel.add(tiesArea);

        add(statsPanel, BorderLayout.SOUTH);
    }

    private void ResultsPanel() {
        resultFrame = new JTextArea();
        resultFrame.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultFrame);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Game Results"));
        add(scrollPane, BorderLayout.CENTER);
    }

    private void handlePlayerMove(String playerChoice) {
        String playerMove = "";

        if (playerChoice.equals("Rock")) {
            playerRockCount++;
            playerMove = "R";
            playerLast = "R";
        } else if (playerChoice.equals("Paper")) {
            playerPaperCount++;
            playerMove = "P";
            playerLast = "P";
        } else if (playerChoice.equals("Scissors")) {
            playerScissorCount++;
            playerMove = "S";
            playerLast = "S";
        } else {
            System.exit(0);
        }

        StrategyMain computerStrategy = selectStrategy();
        String computerMove = computerStrategy.getMove(playerLast);


        String result = determineWinner(playerMove, computerMove, computerStrategy);

        resultFrame.append(result + "\n");
        playerWins.setText(String.valueOf(playerWon));
        computerWins.setText(String.valueOf(computerWon));
        tiesArea.setText(String.valueOf(ties));
    }

    private StrategyMain selectStrategy() {
        Random rand = new Random();
        int p = rand.nextInt(1000) + 1; // 1-1000

        if (p <= 100) return cheatStrat;
        else if (p >= 325 && p <=549) return new LeastUsed();
        else if (p >= 550 && p <= 774) return new MostUsed();
        else if (p >= 775 && p <= 999) return new LastUsed();
        else return randomStrat;
    }

    private String determineWinner(String player, String computer, StrategyMain strategy) {
        String winnerText;
        if (player.equals(computer)) {
            ties++;
            winnerText = translate(player) + " ties with " + translate(computer) + " (Tie!)";
        } else if ((player.equals("R") && computer.equals("S")) ||
                (player.equals("P") && computer.equals("R")) ||
                (player.equals("S") && computer.equals("P"))) {
            playerWon++;
            winnerText = translate(player) + " beats " + translate(computer) + " (Player wins!)";
        } else {
            computerWon++;
            winnerText = translate(computer) + " beats " + translate(player) + " (Computer wins!)";
        }
        winnerText += " (Strategy: " + strategy.getClass().getSimpleName() + ")";
        return winnerText;
    }

    private String translate(String move) {
        if (move.equals("R"))
        {
            return "Rock";
        }
        else if (move.equals("P"))
        {
            return "Paper";
        }
        else
        {
            return "Scissors";
        }
    }

    private class LeastUsed implements StrategyMain {
        @Override
        public String getMove(String playerMove) {
            int minCount = Math.min(playerRockCount, Math.min(playerPaperCount, playerScissorCount));
            if (minCount == playerRockCount) return "P";
            else if (minCount == playerPaperCount) return "S";
            else return "R";
        }
    }


    private class MostUsed implements StrategyMain {
        @Override
        public String getMove(String playerMove) {
            int maxCount = Math.max(playerRockCount, Math.max(playerPaperCount, playerScissorCount));

            if (maxCount == playerRockCount)
            {
                return "P";
            } else if (maxCount == playerPaperCount)
            {
                return "S";
            }
            else
            {
                return "R";
            }
        }
    }


    /** Last used strategy (Inner class) */
    private class LastUsed implements StrategyMain {
        @Override
        public String getMove(String playerMove) {
            if (playerMove.isEmpty()) {
                return randomStrat.getMove(playerMove);
            }
            if (playerMove.equals("R"))
            {
                return "P";
            }
            else if (playerMove.equals("P"))
            {
                return "S";
            }
            else
            {
                return "R";
            }

        }
    }
    }







