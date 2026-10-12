
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class RandomizedCourseIndex {

    private static final int MOD = 1000000007;
    private static final int BASE = 31 + new Random().nextInt(1000);

    private static int hash(String text) {
        long value = 0;

        for (int i = 0; i < text.length(); i++) {
            value = (value * BASE + text.charAt(i)) % MOD;
        }

        return (int) value;
    }

    public static void searchCorpus(String query) {
        System.out.println("\n[3] Randomized Hash Keyword Search");
        System.out.println("-".repeat(48));

        File folder = new File("Corpus");
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Corpus folder not found.");
            return;
        }

        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            System.out.println("No corpus files found.");
            return;
        }

        Map<Integer, Set<String>> index = new HashMap<>();
        for (File file : files) {
            if (!file.isFile()
                    || !file.getName().toLowerCase().endsWith(".txt")
                    || file.getName().equalsIgnoreCase("INDEX.txt")) {
                continue;
            }
            try {
                String content = Files.readString(file.toPath()).toLowerCase();
                String[] words = content.split("[^a-zA-Z0-9+#.\\-]+");
                for (String word : words) {
                    if (word.length() >= 2) {
                        int key = hash(word);
                        index.computeIfAbsent(key, k -> new HashSet<>())
                             .add(word + "|" + file.getName());
                    }
                }
            } catch (IOException e) {
                System.out.println("Could not read: " + file.getName());
            }
        }

        String[] queryWords = query.toLowerCase().split("[^a-zA-Z0-9+#.\\-]+");
        boolean found = false;
        Set<String> uniqueQueryWords = new LinkedHashSet<>(Arrays.asList(queryWords));
        System.out.println("Query: " + query);

        for (String word : uniqueQueryWords) {
            if (word.length() < 2) continue;
            Set<String> candidates = index.get(hash(word));
            if (candidates == null) continue;

            List<String> matchedFiles = new ArrayList<>();
            for (String entry : candidates) {
                String[] parts = entry.split("\\|", 2);
                if (parts[0].equals(word)) matchedFiles.add(parts[1]);
            }
            if (!matchedFiles.isEmpty()) {
                found = true;
                Collections.sort(matchedFiles);
                int total = matchedFiles.size();
                int limit = Math.min(4, total);
                System.out.println("  " + word + " -> " + total + " course file(s)");
                for (int i = 0; i < limit; i++) {
                    System.out.println("     - " + matchedFiles.get(i));
                }
                if (total > limit) {
                    System.out.println("     ... and " + (total - limit) + " more");
                }
            }
        }

        if (!found) System.out.println("No matching keywords found.");
    }
}
