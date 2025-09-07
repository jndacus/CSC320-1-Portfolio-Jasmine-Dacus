package automobile.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AutomobileInventory {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            try {
                // 1) Construct via parameterized constructor
                Automobile auto = new Automobile("Honda Owner", "Honda", "Civic", "Blue", 2018, 42000);

                // 2) List values and print
                System.out.println("Initial Vehicle:");
                printLines(auto.listVehicleInfo());

                // 3) Remove vehicle
                String removeMsg = auto.removeVehicle("Honda", "Civic", "Blue", 2018);
                System.out.println(removeMsg);

                // 4) Add a new vehicle
                String addMsg = auto.addNewVehicle("Toyota", "Camry", "Red", 2020, 23500);
                System.out.println(addMsg);

                // 5) List and print new info
                System.out.println("After Add:");
                printLines(auto.listVehicleInfo());

                // 6) Update the vehicle
                String updateMsg = auto.updateVehicleAttributes("Toyota", "Camry SE", "Black", 2021, 19800);
                System.out.println(updateMsg);

                // 7) List and print updated info
                System.out.println("After Update:");
                String[] listing = auto.listVehicleInfo();
                printLines(listing);

                // 8) Ask to print to file
                System.out.print("Would you like to print the information to a file (Y or N)? ");
                String response = scanner.nextLine().trim();
                if (response.equalsIgnoreCase("Y")) {
                    // Prepare content
                    List<String> lines = new ArrayList<>();
                    lines.add("Automobile Inventory Listing");
                    for (String line : listing) lines.add(line);

                    // Platform-aware path (Windows: C:\\Temp\\Autos.txt; macOS/Linux: ~/Autos.txt)
                    Path out = outPathForPlatform();
                    ensureParentDirectory(out);
                    try {
                        Files.write(out, lines);
                        System.out.println("File printed to: " + out.toAbsolutePath());
                    } catch (IOException io) {
                        System.out.println("Failed to write file: " + io.getMessage());
                    }
                } else {
                    System.out.println("File will not be printed.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred in the inventory flow: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    private static void printLines(String[] arr) {
        for (String s : arr) System.out.println(s);
    }

    private static Path outPathForPlatform() {
        if (isWindows()) return Paths.get("C:\\Temp\\Autos.txt");
        return Path.of(System.getProperty("user.home"), "Autos.txt");
    }

    private static boolean isWindows() {
        String os = System.getProperty("os.name");
        return os != null && os.toLowerCase().contains("win");
    }

    private static void ensureParentDirectory(Path filePath) throws IOException {
        Path parent = filePath.getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }
    }
}
