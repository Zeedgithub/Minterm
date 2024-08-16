package management;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class PM25Management extends JFrame {
    private static final int WIDTH = 20;
    private static final int HEIGHT = 10;
    private static final int POPULATION_PER_CELL = 5000;

    private int[][] pm25Levels = new int[HEIGHT][WIDTH];
    private Random random = new Random();
    private JButton[][] gridButtons = new JButton[HEIGHT][WIDTH];
    private JButton loadDataButton, naturalRainButton, calculateSickButton, resetButton;
    private JLabel statusLabel;

    public PM25Management() {
        setTitle("PM 2.5 Management");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel gridPanel = new JPanel(new GridLayout(HEIGHT, WIDTH));
        gridPanel.setBackground(Color.LIGHT_GRAY);
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                gridButtons[i][j] = new JButton();
                gridButtons[i][j].setPreferredSize(new Dimension(40, 40));
                gridButtons[i][j].setFont(new Font("Arial", Font.BOLD, 12));
                gridButtons[i][j].setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                final int x = j;
                final int y = i;
                gridButtons[i][j].addActionListener(e -> makeArtificialRain(x, y));
                gridPanel.add(gridButtons[i][j]);
            }
        }
        add(gridPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.LINE_AXIS));
        controlPanel.setBackground(Color.WHITE);

        loadDataButton = new JButton("Load Data");
        naturalRainButton = new JButton("Natural Rain");
        calculateSickButton = new JButton("Calculate Sick People");
        resetButton = new JButton("Reset Grid");

        loadDataButton.addActionListener(e -> loadPM25Data("pm2.5.txt"));
        naturalRainButton.addActionListener(e -> naturalRain());
        calculateSickButton.addActionListener(e -> calculateSickPeople());
        resetButton.addActionListener(e -> resetGrid());

        loadDataButton.setToolTipText("Load PM2.5 data from file");
        naturalRainButton.setToolTipText("Apply natural rain across all areas");
        calculateSickButton.setToolTipText("Calculate the number of sick people based on PM2.5 levels");
        resetButton.setToolTipText("Reset the grid to initial state");

        controlPanel.add(loadDataButton);
        controlPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        controlPanel.add(naturalRainButton);
        controlPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        controlPanel.add(calculateSickButton);
        controlPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        controlPanel.add(resetButton);
        add(controlPanel, BorderLayout.SOUTH);

        statusLabel = new JLabel("Welcome to PM 2.5 Management");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        statusLabel.setForeground(Color.BLUE);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(statusLabel, BorderLayout.NORTH);

        pack();
        setLocationRelativeTo(null);
    }

    private void loadPM25Data(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            for (int i = 0; i < HEIGHT; i++) {
                for (int j = 0; j < WIDTH; j++) {
                    if (scanner.hasNextInt()) {
                        pm25Levels[i][j] = scanner.nextInt();
                        updateButtonColor(i, j);
                    }
                }
            }
            statusLabel.setText("Data loaded successfully");
        } catch (FileNotFoundException e) {
            statusLabel.setText("File not found: " + filename);
        }
    }

    private void makeArtificialRain(int x, int y) {
        if (x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT) {
            pm25Levels[y][x] /= 2;
            updateButtonColor(y, x);

            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i == 0 && j == 0) continue;
                    int newY = y + i;
                    int newX = x + j;
                    if (newY >= 0 && newY < HEIGHT && newX >= 0 && newX < WIDTH) {
                        pm25Levels[newY][newX] = (int)(pm25Levels[newY][newX] * 0.7);
                        updateButtonColor(newY, newX);
                    }
                }
            }
            statusLabel.setText("Artificial rain made at (" + x + "," + y + ")");
        } else {
            statusLabel.setText("Invalid coordinates for artificial rain");
        }
    }

    private void naturalRain() {
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                pm25Levels[i][j] = Math.max(0, pm25Levels[i][j] - 50);
                updateButtonColor(i, j);
            }
        }
        statusLabel.setText("Natural rain occurred");
    }

    private void calculateSickPeople() {
        int totalSick = 0;
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                int pm25 = pm25Levels[i][j];
                double sickPercentage;
                if (pm25 <= 50) {
                    sickPercentage = random.nextDouble() * 0.09;
                } else if (pm25 <= 100) {
                    sickPercentage = 0.10 + random.nextDouble() * 0.09;
                } else if (pm25 <= 150) {
                    sickPercentage = 0.20 + random.nextDouble() * 0.09;
                } else {
                    sickPercentage = 0.30 + random.nextDouble() * 0.20;
                }
                totalSick += (int)(POPULATION_PER_CELL * sickPercentage);
            }
        }
        statusLabel.setText("Total sick people: " + totalSick);
    }

    private void resetGrid() {
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                pm25Levels[i][j] = 0;
                updateButtonColor(i, j);
            }
        }
        statusLabel.setText("Grid reset to initial state");
    }

    private void updateButtonColor(int i, int j) {
        int level = pm25Levels[i][j];
        Color color;
        if (level <= 50) {
            color = Color.GREEN;
        } else if (level <= 100) {
            color = Color.YELLOW;
        } else if (level <= 150) {
            color = Color.ORANGE;
        } else {
            color = Color.RED;
        }
        gridButtons[i][j].setBackground(color);
        gridButtons[i][j].setText(String.valueOf(level));
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new PM25Management().setVisible(true));
    }
}
