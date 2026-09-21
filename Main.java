import java.io.*;
import java.util.*;

public class Main {

    static class Career {
        String specialization = "";
        String category = "";
        String description = "";
        String interests = "";
        String preferredSubjects = "";
        String skillsNeeded = "";
        String prerequisites = "";
        String entranceExams = "";
        String duration = "";
        String careerOptions = "";
        String futureScope = "";
        String whyRecommended = "";
    }

    static class Match {
        Career career;
        String interest;
        int distance;

        Match(Career career, String interest, int distance) {
            this.career = career;
            this.interest = interest;
            this.distance = distance;
        }
    }

    public static ArrayList<Career> loadCorpus(String folderPath) {
        ArrayList<Career> careers = new ArrayList<>();

        File folder = new File(folderPath);

        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Corpus folder not found: " + folderPath);
            return careers;
        }

        File[] files = folder.listFiles(
                (dir, name) -> name.toLowerCase().endsWith(".txt")
        );

        if (files == null || files.length == 0) {
            System.out.println("No corpus files found.");
            return careers;
        }

        for (File file : files) {
            parseFile(file, careers);
        }

        return careers;
    }

    public static void parseFile(File file, ArrayList<Career> careers) {
        try {
            BufferedReader reader =
                    new BufferedReader(new FileReader(file));

            String line;
            Career currentCareer = null;
            String currentField = "";

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }

                if (line.startsWith("Specialization:")) {

                    if (currentCareer != null) {
                        careers.add(currentCareer);
                    }

                    currentCareer = new Career();
                    currentCareer.specialization = getValue(line);
                    currentField = "specialization";

                } else if (line.equals("Category:")) {
                    currentField = "category";

                } else if (line.equals("Description:")) {
                    currentField = "description";

                } else if (line.equals("Interests:")) {
                    currentField = "interests";

                } else if (line.equals("Preferred School Subjects:")) {
                    currentField = "preferredSubjects";

                } else if (line.equals("Skills Needed:")) {
                    currentField = "skillsNeeded";

                } else if (line.equals("Prerequisites:")) {
                    currentField = "prerequisites";

                } else if (line.equals("Entrance Exams:")) {
                    currentField = "entranceExams";

                } else if (line.equals("Duration:")) {
                    currentField = "duration";

                } else if (line.equals("Career Options:")) {
                    currentField = "careerOptions";

                } else if (line.equals("Future Scope:")) {
                    currentField = "futureScope";

                } else if (line.equals("Why Recommended:")) {
                    currentField = "whyRecommended";

                } else if (currentCareer != null) {
                    addFieldContent(currentCareer, currentField, line);
                }
            }

            if (currentCareer != null) {
                careers.add(currentCareer);
            }

            reader.close();

        } catch (IOException e) {
            System.out.println("Error reading file: " + file.getName());
        }
    }

    public static String getValue(String line) {
        int index = line.indexOf(":");

        if (index == -1) {
            return "";
        }

        return line.substring(index + 1).trim();
    }

    public static void addFieldContent(
            Career career,
            String field,
            String value) {

        switch (field) {
            case "category":
                career.category += value + " ";
                break;

            case "description":
                career.description += value + " ";
                break;

            case "interests":
                career.interests += value + " ";
                break;

            case "preferredSubjects":
                career.preferredSubjects += value + " ";
                break;

            case "skillsNeeded":
                career.skillsNeeded += value + " ";
                break;

            case "prerequisites":
                career.prerequisites += value + " ";
                break;

            case "entranceExams":
                career.entranceExams += value + " ";
                break;

            case "duration":
                career.duration += value + " ";
                break;

            case "careerOptions":
                career.careerOptions += value + " ";
                break;

            case "futureScope":
                career.futureScope += value + " ";
                break;

            case "whyRecommended":
                career.whyRecommended += value + " ";
                break;
        }
    }

    public static ArrayList<Match> findKMPMatches(
            ArrayList<Career> careers,
            String keyword) {

        ArrayList<Match> matches = new ArrayList<>();

        for (Career career : careers) {
            String[] interests = career.interests.split(",");

            for (String interest : interests) {
                interest = interest.trim();

                if (KMP.search(interest, keyword)) {
                    matches.add(new Match(career, interest, 0));
                    break;
                }
            }
        }

        return matches;
    }

    public static ArrayList<Match> findEditDistanceMatches(
            ArrayList<Career> careers,
            String keyword) {

        ArrayList<Match> matches = new ArrayList<>();

        for (Career career : careers) {

            String bestInterest = "";
            int bestDistance = Integer.MAX_VALUE;

            String[] interests = career.interests.split(",");

            for (String interest : interests) {

                interest = interest.trim();

                int distance =
                        EditDistance.calculate(keyword, interest);

                if (distance < bestDistance) {
                    bestDistance = distance;
                    bestInterest = interest;
                }
            }

            matches.add(
                    new Match(
                            career,
                            bestInterest,
                            bestDistance
                    )
            );
        }

        matches.sort(
                Comparator.comparingInt(match -> match.distance)
        );

        return matches;
    }

    public static String findBestInterest(
            Career career,
            String keyword) {

        String bestInterest = "";
        int bestDistance = Integer.MAX_VALUE;

        String[] interests = career.interests.split(",");

        for (String interest : interests) {

            interest = interest.trim();

            int distance =
                    EditDistance.calculate(keyword, interest);

            if (distance < bestDistance) {
                bestDistance = distance;
                bestInterest = interest;
            }
        }

        return bestInterest;
    }

    public static void displayCareer(
            Career career,
            String keyword,
            boolean editDistanceMode) {

        System.out.println(
                "\n=============================================="
        );

        System.out.println(
                "RECOMMENDED SPECIALIZATION"
        );

        System.out.println(
                "=============================================="
        );

        System.out.println(
                "Specialization: " +
                career.specialization
        );

        System.out.println(
                "Category: " +
                career.category.trim()
        );

        if (editDistanceMode) {

            String closestInterest =
                    findBestInterest(
                            career,
                            keyword
                    );

            int distance =
                    EditDistance.calculate(
                            keyword,
                            closestInterest
                    );

            System.out.println(
                    "Closest Interest: " +
                    closestInterest
            );

            System.out.println(
                    "Edit Distance: " +
                    distance
            );
        }

        System.out.println(
                "\nDescription:"
        );

        System.out.println(
                career.description.trim()
        );

        System.out.println(
                "\nInterests:"
        );

        System.out.println(
                career.interests.trim()
        );

        System.out.println(
                "\nPreferred School Subjects:"
        );

        System.out.println(
                career.preferredSubjects.trim()
        );

        System.out.println(
                "\nSkills Needed:"
        );

        System.out.println(
                career.skillsNeeded.trim()
        );

        System.out.println(
                "\nPrerequisites:"
        );

        System.out.println(
                career.prerequisites.trim()
        );

        System.out.println(
                "\nEntrance Exams:"
        );

        System.out.println(
                career.entranceExams.trim()
        );

        System.out.println(
                "\nDuration:"
        );

        System.out.println(
                career.duration.trim()
        );

        System.out.println(
                "\nCareer Options:"
        );

        System.out.println(
                career.careerOptions.trim()
        );

        System.out.println(
                "\nFuture Scope:"
        );

        System.out.println(
                career.futureScope.trim()
        );

        System.out.println(
                "\nWhy Recommended:"
        );

        System.out.println(
                career.whyRecommended.trim()
        );
    }

    public static void main(String[] args) {

        String corpusFolder = "corpus";

        ArrayList<Career> careers =
                loadCorpus(corpusFolder);

        if (careers.isEmpty()) {
            System.out.println(
                    "No career information found."
            );
            return;
        }

        Scanner scanner =
                new Scanner(System.in);

        System.out.println(
                "=============================================="
        );

        System.out.println(
                "       CAREER RECOMMENDATION SYSTEM"
        );

        System.out.println(
                "=============================================="
        );

        System.out.println(
                "\nCareer records loaded: " +
                careers.size()
        );

        System.out.print(
                "\nEnter your interest: "
        );

        String keyword =
                scanner.nextLine().trim();

        if (keyword.isEmpty()) {
            System.out.println(
                    "Please enter a valid interest."
            );
            scanner.close();
            return;
        }

        ArrayList<Match> kmpMatches =
                findKMPMatches(
                        careers,
                        keyword
                );

        if (!kmpMatches.isEmpty()) {

            System.out.println(
                    "\n✓ Exact interest match found!"
            );

            System.out.println(
                    "Matching Algorithm: KMP"
            );

            int count =
                    Math.min(3, kmpMatches.size());

            System.out.println(
                    "\nTop " +
                    count +
                    " Recommendations:"
            );

            for (int i = 0; i < count; i++) {

                Match match =
                        kmpMatches.get(i);

                System.out.println(
                        "\n" +
                        (i + 1) +
                        ". " +
                        match.career.specialization
                );

                System.out.println(
                        "   Category: " +
                        match.career.category.trim()
                );

                System.out.println(
                        "   Matching Interest: " +
                        match.interest
                );
            }

            displayCareer(
                    kmpMatches.get(0).career,
                    keyword,
                    false
            );

        } else {

            System.out.println(
                    "\n✗ No exact KMP match found."
            );

            System.out.println(
                    "Using Edit Distance to find the closest matches..."
            );

            ArrayList<Match> editMatches =
                    findEditDistanceMatches(
                            careers,
                            keyword
                    );

            int count =
                    Math.min(3, editMatches.size());

            System.out.println(
                    "\nClosest Recommendations:"
            );

            for (int i = 0; i < count; i++) {

                Match match =
                        editMatches.get(i);

                System.out.println(
                        "\n" +
                        (i + 1) +
                        ". " +
                        match.career.specialization
                );

                System.out.println(
                        "   Category: " +
                        match.career.category.trim()
                );

                System.out.println(
                        "   Closest Interest: " +
                        match.interest
                );

                System.out.println(
                        "   Edit Distance: " +
                        match.distance
                );
            }

            displayCareer(
                    editMatches.get(0).career,
                    keyword,
                    true
            );
        }

        scanner.close();
    }
}