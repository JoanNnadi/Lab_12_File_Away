
import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static java.nio.file.StandardOpenOption.CREATE;

public class DataSaver {

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        ArrayList<String[]> records = new ArrayList<>();
        boolean more = true;

        System.out.println("=== Student Record Entry System ===");

        // -------------------------------------------------------
        // INPUT LOOP — Collect multiple student records
        // -------------------------------------------------------
        while (more) {

            String first = SafeInput.getNonZeroLenString(in, "Enter FIRST name");
            String last = SafeInput.getNonZeroLenString(in, "Enter LAST name");
            String id = SafeInput.getNonZeroLenString(in, "Enter ID number");

            String email = SafeInput.getNonZeroLenString(in, "Enter EMAIL");

            int yob = SafeInput.getRangedInt(in, "Enter YEAR OF BIRTH", 1900, 2025);

            records.add(new String[]{id, first, last, email, yob + ""});

            more = SafeInput.getYNConfirm(in, "Add another student?");
        }

        // -------------------------------------------------------
        // CHOOSE FILENAME + VALID EXTENSION
        // -------------------------------------------------------
        String baseName = SafeInput.getNonZeroLenString(in, "Enter output file name (no extension)");

        String extension = "";
        boolean valid = false;

        while (!valid) {
            extension = SafeInput.getNonZeroLenString(in,
                    "Enter file extension (.txt or .csv ONLY)");

            if (!extension.startsWith(".")) {
                extension = "." + extension;
            }

            if (extension.equalsIgnoreCase(".csv") || extension.equalsIgnoreCase(".txt")) {
                valid = true;
            } else {
                System.out.println("ERROR: Only .txt and .csv allowed!");
            }
        }

        String fullName = "src/" + baseName + extension;

        // -------------------------------------------------------
        // WRITE FILE
        // -------------------------------------------------------
        try (PrintWriter out = new PrintWriter(new FileWriter(fullName))) {

            if (extension.equalsIgnoreCase(".csv")) {
                out.println("ID,First Name,Last Name,Email,YOB");
            }

            for (String[] rec : records) {
                out.println(String.join(",", rec));
            }

            System.out.println("\nFile saved to: " + fullName);

        } catch (IOException e) {
            System.out.println("ERROR: Could not write file.");
            return;
        }

        // -------------------------------------------------------
        // READ FILE USING FILECHOOSER
        // -------------------------------------------------------
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

        System.out.println("\n--- Now choose the file to READ ---");

        ArrayList<String[]> loaded = new ArrayList<>();
        String[] header = {"ID", "First Name", "Last Name", "Email", "YOB"};

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

            File selected = chooser.getSelectedFile();
            Path file = selected.toPath();

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new BufferedInputStream(Files.newInputStream(file, CREATE))))) {

                String line;
                boolean skipHeader = true;

                while ((line = reader.readLine()) != null) {

                    // Skip CSV header row
                    if (skipHeader && extension.equalsIgnoreCase(".csv")) {
                        skipHeader = false;
                        continue;
                    }

                    loaded.add(line.split(","));
                }

            } catch (IOException e) {
                System.out.println("ERROR reading file.");
                return;
            }

            // -------------------------------------------------------
            // DISPLAY TABLE
            // -------------------------------------------------------
            printTable(header, loaded);

            // -------------------------------------------------------
            // EDIT A RECORD
            // -------------------------------------------------------
            if (SafeInput.getYNConfirm(in, "Would you like to EDIT a record?")) {

                int row = SafeInput.getRangedInt(in,
                        "Enter row number to edit", 1, loaded.size());

                String[] rec = loaded.get(row - 1);

                System.out.println("\nEditing Row " + row);
                System.out.println("Press ENTER to keep old value.\n");

                for (int i = 0; i < rec.length; i++) {
                    System.out.printf("%s (%s): ", header[i], rec[i]);
                    String newVal = in.nextLine().trim();

                    if (!newVal.isEmpty()) {
                        rec[i] = newVal;
                    }
                }

                System.out.println("Record updated!\n");
                printTable(header, loaded);
            }

        } else {
            System.out.println("No file selected.");
        }
    }


    // -------------------------------------------------------
    // TABLE PRINTING METHOD
    // -------------------------------------------------------
    private static void printTable(String[] header, List<String[]> rows) {

        int cols = header.length;
        int[] width = new int[cols];

        // Compute column widths
        for (int i = 0; i < cols; i++)
            width[i] = header[i].length();

        for (String[] r : rows)
            for (int i = 0; i < cols; i++)
                width[i] = Math.max(width[i], r[i].length());

        // Borders
        String border = buildBorder(width);

        System.out.println(border);
        System.out.println("| #  | " + buildRow(header, width) + " |");
        System.out.println(border);

        int index = 1;
        for (String[] r : rows) {
            System.out.printf("| %-2d | %s |\n", index++, buildRow(r, width));
        }

        System.out.println(border);
    }

    private static String buildBorder(int[] width) {
        StringBuilder sb = new StringBuilder("+----+");
        for (int w : width)
            sb.append("-".repeat(w + 2)).append("+");
        return sb.toString();
    }

    private static String buildRow(String[] row, int[] width) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < row.length; i++)
            sb.append(String.format(" %-" + width[i] + "s |", row[i]));
        return sb.toString();
    }
}

