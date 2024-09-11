import java.io.BufferedReader; // reads text from file -- readLine()
import java.io.BufferedWriter; // write() ; newLine()
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GymProgressTracker {

    private static final String FILE_PATH = "gym_prog.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n\n");
            System.out.print("1. Log a new workout   ");
            System.out.print("2. View past workouts   ");
            System.out.print("3. Log PRs   ");
            System.out.print("4. View PRs   ");
            System.out.println("5. Exit   ");
            System.out.print("Choose an option: ");

            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    logWorkout(scanner);
                    break;
                case 2:
                    viewWorkouts();
                    break;
                case 3:
                    logPRs(scanner);
                    break;
                case 4:
                    viewPRs();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void logPRs(Scanner scanner) {
        System.out.print("Enter the date (YYYY-MM-DD): ");
        String date = scanner.nextLine();

        System.out.print("Enter the exercise: ");
        String exercise = scanner.nextLine();

        System.out.print("New PR (#, lbs): ");
        String pr = scanner.nextLine();

        String entry = String.format("PR, %s, %s, %s%n", date, exercise, pr);

        List<String> lines = new ArrayList<>();
        boolean updated = false;

        // Read existing file content and update PR if exercise already exists
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
            System.err.println("Error reading from file: " + e.getMessage());
        }

        // If PR was not updated, add new PR
        if (!updated) {
            lines.add(entry);
        }

        // Write all lines back to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
            System.out.println("PR is in the system!");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }


    private static void logWorkout(Scanner scanner) {
        System.out.print("Enter the date (YYYY-MM-DD): ");
        String date = scanner.nextLine();

        System.out.print("Enter the exercise: ");
        String exercise = scanner.nextLine();

        System.out.print("Enter the number of sets: ");
        int sets = scanner.nextInt();

        System.out.print("Enter the number of reps: ");
        int reps = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        String entry = String.format("WORKOUT, %s, %s, %d sets of %d reps%n", date, exercise, sets, reps);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(entry);
            System.out.println("Workout logged in the system!");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }


    private static void viewWorkouts() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            System.out.println("Past Workouts:");
            while ((line = reader.readLine()) != null) {
                // Display only lines starting with "WORKOUT"
                if (line.startsWith("WORKOUT, ")) {
                    // Remove the "WORKOUT, " prefix before displaying
                    System.out.println(line.substring("WORKOUT, ".length()));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }
    }


    private static void viewPRs() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            System.out.println("Personal Records (PRs):");
            while ((line = reader.readLine()) != null) {
                // Display only lines starting with "PR"
                if (line.startsWith("PR, ")) {
                    // Remove the "PR, " prefix before displaying
                    System.out.println(line.substring("PR, ".length()));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }
    }

}
