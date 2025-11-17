//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

import java.io.File;
import java.util.Scanner;
import java.nio.file.Path;
import javax.swing.JFileChooser;
import java.io.IOException;
import java.io.FileNotFoundException;
import javax.swing.*;
import java.io.*;

public class FileInspector
{
    public static void main(String[] args)
    {
        JFileChooser chooser = new JFileChooser();
        Scanner inFile;
        String line;

        // Set starting directory to /src
        Path target = new File(System.getProperty("user.dir")).toPath();
        target = target.resolve("src");

        chooser.setCurrentDirectory(target.toFile());

        try
        {
            // Let the user choose a file
            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
            {
                target = chooser.getSelectedFile().toPath();

                // Create Scanner to read the file
                inFile = new Scanner(target.toFile());

                int lineCount = 0;
                int wordCount = 0;
                int charCount = 0;

                // Read file line by line
                while (inFile.hasNextLine())
                {
                    line = inFile.nextLine();
                    System.out.println(line);

                    lineCount++;

                    // Count words
                    if (!line.trim().isEmpty())
                        wordCount += line.trim().split("\\s+").length;

                    // Count characters
                    charCount += line.length();
                }

                inFile.close();

                // Print summary
                System.out.println("\n===== SUMMARY REPORT =====");
                System.out.println("File Name       : " + target.getFileName());
                System.out.println("Total Lines     : " + lineCount);
                System.out.println("Total Words     : " + wordCount);
                System.out.println("Total Characters: " + charCount);
                System.out.println("==========================");
            }
            else
            {
                System.out.println("Sorry, you must select a file. Terminating!");
                System.exit(0);
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("File Not Found Error");
            e.printStackTrace();

        }
    }
}

