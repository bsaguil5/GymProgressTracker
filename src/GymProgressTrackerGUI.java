

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GymProgressTrackerGUI {

    private static final String FILE_PATH = "gym_prog.txt";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Gym Progress Tracker");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);
            frame.setLayout(new BorderLayout());

            // Load images from the classpath
            ImageIcon logoIcon = new ImageIcon("C:/mash-mashle.png");



            ImageIcon logWorkoutIcon = new ImageIcon("C:mash-mashle.png");
            ImageIcon viewWorkoutsIcon = new ImageIcon("C:/mash-mashle.png");
            ImageIcon logPRIcon = new ImageIcon("C:/mash-mashle.png");
            ImageIcon viewPRsIcon = new ImageIcon("C:/mash-mashle.png");

            // Create and set up components
            JTextArea textArea = new JTextArea();
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);

            JButton logWorkoutButton = new JButton("Log Workout", logWorkoutIcon);
            JButton viewWorkoutsButton = new JButton("View Workouts", viewWorkoutsIcon);
            JButton logPRButton = new JButton("Log PR", logPRIcon);
            JButton viewPRsButton = new JButton("View PRs", viewPRsIcon);

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(logWorkoutButton);
            buttonPanel.add(viewWorkoutsButton);
            buttonPanel.add(logPRButton);
            buttonPanel.add(viewPRsButton);

            // Add a logo at the top
            JLabel logoLabel = new JLabel(logoIcon);
            frame.add(logoLabel, BorderLayout.NORTH);

            frame.add(buttonPanel, BorderLayout.CENTER);
            frame.add(scrollPane, BorderLayout.SOUTH);

            logWorkoutButton.addActionListener(e -> logWorkout(textArea));
            viewWorkoutsButton.addActionListener(e -> viewWorkouts(textArea));
            logPRButton.addActionListener(e -> logPR(textArea));
            viewPRsButton.addActionListener(e -> viewPRs(textArea));

            frame.setVisible(true);
        });
    }

    private static void logWorkout(JTextArea textArea) {
        // Create input dialog
        String date = JOptionPane.showInputDialog("Enter the date (YYYY-MM-DD):");
        String exercise = JOptionPane.showInputDialog("Enter the exercise:");
        String setsStr = JOptionPane.showInputDialog("Enter the number of sets:");
        String repsStr = JOptionPane.showInputDialog("Enter the number of reps:");

        try {
            int sets = Integer.parseInt(setsStr);
            int reps = Integer.parseInt(repsStr);
            String entry = String.format("WORKOUT, %s, %s, %d sets of %d reps%n", date, exercise, sets, reps);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
                writer.write(entry);
            }

            textArea.append("Workout logged: " + entry);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Invalid number format.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error writing to file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void logPR(JTextArea textArea) {
        // Create input dialog
        String date = JOptionPane.showInputDialog("Enter the date (YYYY-MM-DD):");
        String exercise = JOptionPane.showInputDialog("Enter the exercise:");
        String pr = JOptionPane.showInputDialog("New PR (#, lbs):");

        String entry = String.format("PR, %s, %s, %s%n", date, exercise, pr);

        List<String> lines = new ArrayList<>();
        boolean updated = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("PR, ") && line.contains(exercise + ",")) {
                    lines.add(entry);
                    updated = true;
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading from file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        if (!updated) {
            lines.add(entry);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String line : lines) {
                writer.write(line);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error writing to file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        textArea.append("PR logged: " + entry);
    }

    private static void viewWorkouts(JTextArea textArea) {
        textArea.setText("");
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("WORKOUT, ")) {
                    textArea.append(line.substring("WORKOUT, ".length()) + "\n");
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading from file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void viewPRs(JTextArea textArea) {
        textArea.setText("");
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("PR, ")) {
                    textArea.append(line.substring("PR, ".length()) + "\n");
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading from file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
