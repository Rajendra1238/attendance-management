import java.io.*;
import java.util.*;

class AttendanceSystem {
    private static final String FILE_NAME = "attendance.txt";
    private static Map<String, Set<String>> attendanceData = new HashMap<>();

    public static void main(String[] args) {
        loadAttendanceData();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nAttendance Management System");
            System.out.println("1. Mark Attendance");
            System.out.println("2. View Attendance");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    markAttendance(scanner);
                    break;
                case 2:
                    viewAttendance();
                    break;
                case 3:
                    saveAttendanceData();
                    System.out.println("Exiting the system. Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void loadAttendanceData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                String date = parts[0];
                Set<String> presentStudents = new HashSet<>(Arrays.asList(parts[1].split(",")));
                attendanceData.put(date, presentStudents);
            }
        } catch (IOException e) {
            System.out.println("Error loading attendance data. Starting with an empty system.");
        }
    }

    private static void saveAttendanceData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Map.Entry<String, Set<String>> entry : attendanceData.entrySet()) {
                String date = entry.getKey();
                String presentStudents = String.join(",", entry.getValue());
                writer.write(date + ":" + presentStudents);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving attendance data.");
        }
    }

    private static void markAttendance(Scanner scanner) {
        System.out.print("Enter the date (YYYY-MM-DD): ");
        String date = scanner.nextLine();
        System.out.print("Enter the student ID (comma-separated for multiple students): ");
        String studentIds = scanner.nextLine();

        attendanceData.computeIfAbsent(date, k -> new HashSet<>());
        attendanceData.get(date).addAll(Arrays.asList(studentIds.split(",")));

        System.out.println("Attendance marked successfully.");
    }

    private static void viewAttendance() {
        System.out.print("Enter the date to view attendance (YYYY-MM-DD): ");
        String date = new Scanner(System.in).nextLine();

        Set<String> presentStudents = attendanceData.getOrDefault(date, new HashSet<>());

        System.out.println("\nAttendance for " + date + ":");
        for (String studentId : presentStudents) {
            System.out.println("Student ID: " + studentId);
        }
    }
}
