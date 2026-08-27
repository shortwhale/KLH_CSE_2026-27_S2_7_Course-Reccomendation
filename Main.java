import java.io.*;
import java.util.*;

public class Main {

    // KMP LPS array
    public static int[] computeLPS(String pattern) {
        int[] lps = new int[pattern.length()];
        int length = 0;
        int i = 1;

        while (i < pattern.length()) {
            if (Character.toLowerCase(pattern.charAt(i)) ==
                Character.toLowerCase(pattern.charAt(length))) {

                length++;
                lps[i] = length;
                i++;

            } else {
                if (length != 0) {
                    length = lps[length - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }

        return lps;
    }

    // KMP String Matching
    public static boolean KMP(String text, String pattern) {

        if (pattern.length() == 0)
            return true;

        pattern = pattern.toLowerCase();
        text = text.toLowerCase();

        int[] lps = computeLPS(pattern);

        int i = 0;
        int j = 0;

        while (i < text.length()) {

            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;

                if (j == pattern.length()) {
                    return true;
                }

            } else {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }

        return false;
    }

    public static void main(String[] args) {

        String fileName = "DSA Corpus File.txt";

        try {
            BufferedReader reader =
                    new BufferedReader(new FileReader(fileName));

            ArrayList<String> specializations = new ArrayList<>();
            ArrayList<String> categories = new ArrayList<>();
            ArrayList<String> interests = new ArrayList<>();
            ArrayList<String> careers = new ArrayList<>();
            ArrayList<String> futureScope = new ArrayList<>();

            String line;

            String specialization = "";
            String category = "";
            String interest = "";
            String career = "";
            String future = "";

            while ((line = reader.readLine()) != null) {

                line = line.trim();

                if (line.startsWith("Specialization:")) {

                    // Save previous specialization
                    if (!specialization.equals("")) {
                        specializations.add(specialization);
                        categories.add(category);
                        interests.add(interest);
                        careers.add(career);
                        futureScope.add(future);
                    }

                    specialization =
                            line.substring("Specialization:".length()).trim();

                    category = "";
                    interest = "";
                    career = "";
                    future = "";
                }

                else if (line.startsWith("Category:")) {
                    category =
                            line.substring("Category:".length()).trim();
                }

                else if (line.startsWith("Interests:")) {
                    interest =
                            line.substring("Interests:".length()).trim();
                }

                else if (line.startsWith("Career Options:")) {
                    career =
                            line.substring("Career Options:".length()).trim();
                }

                else if (line.startsWith("Future Scope:")) {
                    future =
                            line.substring("Future Scope:".length()).trim();
                }
            }

            // Add last specialization
            if (!specialization.equals("")) {
                specializations.add(specialization);
                categories.add(category);
                interests.add(interest);
                careers.add(career);
                futureScope.add(future);
            }

            reader.close();

            Scanner scanner = new Scanner(System.in);

            System.out.println("=================================");
            System.out.println("   COURSE RECOMMENDATION SYSTEM");
            System.out.println("=================================");

            System.out.print("\nEnter your course interest: ");
            String keyword = scanner.nextLine().trim();

            boolean found = false;

            System.out.println("\n=================================");
            System.out.println("       RECOMMENDED COURSES");
            System.out.println("=================================");

            for (int i = 0; i < specializations.size(); i++) {

                if (KMP(interests.get(i), keyword)) {

                    found = true;

                    System.out.println("\nCourse: "
                            + specializations.get(i));

                    System.out.println("Category: "
                            + categories.get(i));

                    System.out.println("Interests: "
                            + interests.get(i));

                    System.out.println("Career Options: "
                            + careers.get(i));

                    System.out.println("Future Scope: "
                            + futureScope.get(i));

                    System.out.println("---------------------------------");
                }
            }

            if (!found) {
                System.out.println("\nNo matching course found.");
                System.out.println("Try another interest.");
            }

            scanner.close();

        } catch (FileNotFoundException e) {

            System.out.println("Corpus file not found!");
            System.out.println(
                    "Make sure 'DSA Corpus File.txt' is in the project folder."
            );

        } catch (IOException e) {

            System.out.println("Error reading corpus file.");
        }
    }
}