import java.io.*;
import java.nio.file.*;
import java.util.*;

public class CourseRecommender {

    private static final String[] FIELDS = {
            "Category",
            "Description",
            "Interests",
            "Preferred School Subjects",
            "Skills Needed",
            "Prerequisites",
            "Entrance Exams",
            "Duration",
            "Career Options",
            "Future Scope",
            "Why Recommended"
    };

    public static void recommend(String studentInput) {

        if (studentInput == null || studentInput.trim().isEmpty()) {
            System.out.println("Please enter your interests.");
            return;
        }

        List<String> studentInterests = extractKeywords(studentInput);

        if (studentInterests.isEmpty()) {
            System.out.println("Please enter valid interests.");
            return;
        }

        File corpusFolder = new File("Corpus");

        if (!corpusFolder.exists() || !corpusFolder.isDirectory()) {
            System.out.println("Corpus folder not found!");
            return;
        }

        File[] files = corpusFolder.listFiles();

        if (files == null || files.length == 0) {
            System.out.println("No corpus files found!");
            return;
        }

        List<Result> results = new ArrayList<>();

        for (File file : files) {

            if (!file.isFile()) {
                continue;
            }

            if (!file.getName().toLowerCase().endsWith(".txt")) {
                continue;
            }

            if (file.getName().equalsIgnoreCase("INDEX.txt")) {
                continue;
            }

            try {

                String content = Files.readString(file.toPath());

                CourseInfo course = parseCourse(content);

                if (course.specialization.isEmpty()) {
                    continue;
                }

                Result result = calculateRelevance(
                        course,
                        studentInterests
                );

                results.add(result);

            } catch (IOException e) {

                System.out.println(
                        "Error reading: " + file.getName()
                );
            }
        }

        results.sort(
                (a, b) -> Integer.compare(
                        b.score,
                        a.score
                )
        );

        displayResults(results, studentInterests);
    }


    private static List<String> extractKeywords(String input) {

        Set<String> keywords = new LinkedHashSet<>();

        String cleanedInput = input
                .toLowerCase()
                .replaceAll("[^a-zA-Z0-9+#.\\- ]", " ");

        String[] parts = cleanedInput.split("[,;]+");

        Set<String> stopWords = new HashSet<>(
                Arrays.asList(
                        "i",
                        "am",
                        "interested",
                        "interest",
                        "in",
                        "and",
                        "or",
                        "the",
                        "a",
                        "an",
                        "my",
                        "is",
                        "are",
                        "to",
                        "want",
                        "like",
                        "love",
                        "enjoy",
                        "enjoying",
                        "study",
                        "studying",
                        "learn",
                        "learning",
                        "course",
                        "courses",
                        "career",
                        "careers",
                        "field",
                        "future"
                )
        );

        for (String part : parts) {

            part = part.trim();

            if (part.isEmpty()) {
                continue;
            }

            if (!stopWords.contains(part)
                    && part.length() >= 2) {

                keywords.add(part);
            }

            String[] words = part.split("\\s+");

            for (String word : words) {

                word = word.trim();

                if (word.length() >= 3
                        && !stopWords.contains(word)) {

                    keywords.add(word);
                }
            }
        }

        return new ArrayList<>(keywords);
    }


    private static CourseInfo parseCourse(String content) {

        CourseInfo course = new CourseInfo();

        String[] lines = content.split("\\R");

        for (int i = 0; i < lines.length; i++) {

            String line = lines[i].trim();

            if (line.startsWith("Specialization:")) {

                course.specialization =
                        line.substring(
                                "Specialization:".length()
                        ).trim();

            } else {

                for (String field : FIELDS) {

                    String header = field + ":";

                    if (line.equalsIgnoreCase(header)) {

                        StringBuilder value =
                                new StringBuilder();

                        i++;

                        while (i < lines.length) {

                            String next =
                                    lines[i].trim();

                            if (next.startsWith("====")
                                    || next.startsWith("####")
                                    || isFieldHeader(next)) {

                                i--;
                                break;
                            }

                            if (!next.isEmpty()) {

                                if (value.length() > 0) {
                                    value.append(" ");
                                }

                                value.append(next);
                            }

                            i++;
                        }

                        setField(
                                course,
                                field,
                                value.toString()
                        );

                        break;
                    }
                }
            }
        }

        return course;
    }


    private static boolean isFieldHeader(String line) {

        for (String field : FIELDS) {

            if (line.equalsIgnoreCase(field + ":")) {
                return true;
            }
        }

        return false;
    }


    private static void setField(
            CourseInfo course,
            String field,
            String value) {

        switch (field) {

            case "Category":
                course.category = value;
                break;

            case "Description":
                course.description = value;
                break;

            case "Interests":
                course.interests = value;
                break;

            case "Preferred School Subjects":
                course.subjects = value;
                break;

            case "Skills Needed":
                course.skills = value;
                break;

            case "Prerequisites":
                course.prerequisites = value;
                break;

            case "Entrance Exams":
                course.entranceExams = value;
                break;

            case "Duration":
                course.duration = value;
                break;

            case "Career Options":
                course.careerOptions = value;
                break;

            case "Future Scope":
                course.futureScope = value;
                break;

            case "Why Recommended":
                course.whyRecommended = value;
                break;
        }
    }


    private static Result calculateRelevance(
            CourseInfo course,
            List<String> studentInterests) {

        int score = 0;

        int exactMatches = 0;
        int kmpMatches = 0;
        int zMatches = 0;
        int approximateMatches = 0;

        List<String> matchedKeywords =
                new ArrayList<>();

        List<String> kmpMatchedKeywords =
                new ArrayList<>();

        List<String> zMatchedKeywords =
                new ArrayList<>();

        List<String> editMatches =
                new ArrayList<>();

        String specialization =
                course.specialization.toLowerCase();

        String interests =
                course.interests.toLowerCase();

        String subjects =
                course.subjects.toLowerCase();

        String skills =
                course.skills.toLowerCase();

        String description =
                course.description.toLowerCase();

        String whyRecommended =
                course.whyRecommended.toLowerCase();

        String careers =
                course.careerOptions.toLowerCase();


        for (String keyword : studentInterests) {

            boolean exactMatch = false;
            boolean matchedByKMP = false;
            boolean matchedByZ = false;


            /*
             * KMP EXACT MATCHING
             *
             * KMP is used for important course fields:
             * Specialization
             * Interests
             * Skills
             */

            if (KMP.search(specialization, keyword)) {

                score += 30;
                exactMatch = true;
                matchedByKMP = true;

            } else if (KMP.search(interests, keyword)) {

                score += 25;
                exactMatch = true;
                matchedByKMP = true;

            } else if (KMP.search(skills, keyword)) {

                score += 10;
                exactMatch = true;
                matchedByKMP = true;
            }


            /*
             * Z ALGORITHM EXACT MATCHING
             *
             * Z Algorithm is used for:
             * Why Recommended
             * Subjects
             * Description
             * Career Options
             */

            if (!exactMatch) {

                if (ZAlgorithm.search(whyRecommended, keyword)) {

                    score += 15;
                    exactMatch = true;
                    matchedByZ = true;

                } else if (ZAlgorithm.search(subjects, keyword)) {

                    score += 10;
                    exactMatch = true;
                    matchedByZ = true;

                } else if (ZAlgorithm.search(description, keyword)) {

                    score += 5;
                    exactMatch = true;
                    matchedByZ = true;

                } else if (ZAlgorithm.search(careers, keyword)) {

                    score += 5;
                    exactMatch = true;
                    matchedByZ = true;
                }
            }


            if (exactMatch) {

                exactMatches++;

                matchedKeywords.add(keyword);

                if (matchedByKMP) {

                    kmpMatches++;

                    kmpMatchedKeywords.add(keyword);

                }

                if (matchedByZ) {

                    zMatches++;

                    zMatchedKeywords.add(keyword);
                }

                continue;
            }


            /*
             * EDIT DISTANCE APPROXIMATE MATCHING
             */

            String closestWord = "";
            int closestDistance = Integer.MAX_VALUE;

            List<String> courseWords =
                    getImportantWords(course);

            for (String courseWord : courseWords) {

                if (courseWord.length() < 3) {
                    continue;
                }

                int distance =
                        EditDistance.calculate(
                                keyword,
                                courseWord
                        );

                if (distance < closestDistance) {

                    closestDistance = distance;
                    closestWord = courseWord;
                }
            }


            int threshold;

            if (keyword.length() <= 4) {

                threshold = 1;

            } else if (keyword.length() <= 7) {

                threshold = 2;

            } else {

                threshold = 3;
            }


            if (closestDistance <= threshold) {

                approximateMatches++;

                if (closestDistance == 1) {

                    score += 12;

                } else if (closestDistance == 2) {

                    score += 8;

                } else {

                    score += 5;
                }

                editMatches.add(
                        keyword
                                + " -> "
                                + closestWord
                                + " (distance "
                                + closestDistance
                                + ")"
                );
            }
        }


        /*
         * BONUS FOR MULTIPLE MATCHING INTERESTS
         */

        if (exactMatches >= 2) {
            score += 10;
        }

        if (exactMatches >= 3) {
            score += 10;
        }

        if (approximateMatches >= 2) {
            score += 5;
        }


        score = Math.min(score, 100);


        return new Result(
                course,
                score,
                exactMatches,
                kmpMatches,
                zMatches,
                approximateMatches,
                matchedKeywords,
                kmpMatchedKeywords,
                zMatchedKeywords,
                editMatches
        );
    }


    private static List<String> getImportantWords(
            CourseInfo course) {

        List<String> words =
                new ArrayList<>();

        addWords(words, course.specialization);
        addWords(words, course.interests);
        addWords(words, course.subjects);
        addWords(words, course.skills);
        addWords(words, course.careerOptions);
        addWords(words, course.description);

        return words;
    }


    private static void addWords(
            List<String> words,
            String text) {

        String cleaned =
                text.toLowerCase()
                        .replaceAll(
                                "[^a-zA-Z0-9+#.\\- ]",
                                " "
                        );

        String[] tokens =
                cleaned.split("\\s+");

        for (String token : tokens) {

            token = token.trim();

            if (token.length() >= 3) {
                words.add(token);
            }
        }
    }


    private static void displayResults(
            List<Result> results,
            List<String> studentInterests) {

        System.out.println();

        System.out.println(
                "=================================================="
        );

        System.out.println(
                "           PERSONALIZED COURSE RECOMMENDATIONS"
        );

        System.out.println(
                "=================================================="
        );

        System.out.println(
                "\nStudent Interests: "
                        + String.join(
                                ", ",
                                studentInterests
                        )
        );


        int displayed = 0;


        for (Result result : results) {

            if (result.score < 10) {
                continue;
            }

            displayed++;

            System.out.println();

            System.out.println(
                    "--------------------------------------------------"
            );

            System.out.println(
                    displayed
                            + ". "
                            + result.course.specialization
            );

            System.out.println(
                    "--------------------------------------------------"
            );

            System.out.println(
                    "Category: "
                            + result.course.category
            );

            System.out.println(
                    "Relevance Score: "
                            + result.score
                            + "/100"
            );

            System.out.println(
                    "Total Exact Matches: "
                            + result.exactMatches
            );

            System.out.println(
                    "KMP Exact Matches: "
                            + result.kmpMatches
            );

            System.out.println(
                    "Z Algorithm Exact Matches: "
                            + result.zMatches
            );

            System.out.println(
                    "Edit Distance Matches: "
                            + result.approximateMatches
            );


            if (!result.kmpMatchedKeywords.isEmpty()) {

                System.out.println(
                        "\nKMP Matched Interests:"
                );

                for (String keyword :
                        result.kmpMatchedKeywords) {

                    System.out.println(
                            "  [KMP] "
                                    + keyword
                    );
                }
            }


            if (!result.zMatchedKeywords.isEmpty()) {

                System.out.println(
                        "\nZ Algorithm Matched Interests:"
                );

                for (String keyword :
                        result.zMatchedKeywords) {

                    System.out.println(
                            "  [Z Algorithm] "
                                    + keyword
                    );
                }
            }


            if (!result.editMatches.isEmpty()) {

                System.out.println(
                        "\nApproximate Matches:"
                );

                for (String match :
                        result.editMatches) {

                    System.out.println(
                            "  [Edit Distance] "
                                    + match
                    );
                }
            }


            printField(
                    "Description",
                    result.course.description
            );

            printField(
                    "Preferred School Subjects",
                    result.course.subjects
            );

            printField(
                    "Skills Needed",
                    result.course.skills
            );

            printField(
                    "Prerequisites",
                    result.course.prerequisites
            );

            printField(
                    "Entrance Exams",
                    result.course.entranceExams
            );

            printField(
                    "Duration",
                    result.course.duration
            );

            printField(
                    "Career Options",
                    result.course.careerOptions
            );

            printField(
                    "Future Scope",
                    result.course.futureScope
            );


            /*
             * WHY RECOMMENDED IS SHORTENED
             */

            printField(
                    "Why Recommended",
                    shortenWhyRecommended(
                            result.course.whyRecommended
                    )
            );


            if (displayed == 5) {
                break;
            }
        }


        if (displayed == 0) {

            System.out.println(
                    "\nNo strong course matches were found."
            );

            System.out.println(
                    "Try entering interests such as:"
            );

            System.out.println(
                    "computers, coding, mathematics"
            );
        }


        System.out.println();

        System.out.println(
                "=================================================="
        );

        System.out.println(
                "TECHNICAL MATCHING USED"
        );

        System.out.println(
                "=================================================="
        );

        System.out.println(
                "KMP: Used for exact keyword matching "
                        + "in specialization, interests and skills."
        );

        System.out.println(
                "Z Algorithm: Used for exact keyword matching "
                        + "in subjects, descriptions, careers and recommendations."
        );

        System.out.println(
                "Edit Distance: Used to identify "
                        + "approximately matching interests."
        );

        System.out.println(
                "KMP Complexity: O(n + m)"
        );

        System.out.println(
                "Z Algorithm Complexity: O(n + m)"
        );

        System.out.println(
                "Edit Distance Complexity: O(n x m)"
        );

        System.out.println(
                "=================================================="
        );
    }


    private static String shortenWhyRecommended(
            String text) {

        if (text == null || text.trim().isEmpty()) {
            return "";
        }

        String[] sentences =
                text.split("(?<=[.!?])\\s+");

        if (sentences.length == 0) {
            return text;
        }

        return sentences[0];
    }


    private static void printField(
            String name,
            String value) {

        if (value == null || value.trim().isEmpty()) {
            return;
        }

        System.out.println(
                "\n" + name + ":"
        );

        System.out.println(
                "  " + value
        );
    }


    private static class CourseInfo {

        String specialization = "";
        String category = "";
        String description = "";
        String interests = "";
        String subjects = "";
        String skills = "";
        String prerequisites = "";
        String entranceExams = "";
        String duration = "";
        String careerOptions = "";
        String futureScope = "";
        String whyRecommended = "";
    }


    private static class Result {

        CourseInfo course;

        int score;
        int exactMatches;
        int kmpMatches;
        int zMatches;
        int approximateMatches;

        List<String> matchedKeywords;
        List<String> kmpMatchedKeywords;
        List<String> zMatchedKeywords;
        List<String> editMatches;


        Result(
                CourseInfo course,
                int score,
                int exactMatches,
                int kmpMatches,
                int zMatches,
                int approximateMatches,
                List<String> matchedKeywords,
                List<String> kmpMatchedKeywords,
                List<String> zMatchedKeywords,
                List<String> editMatches) {

            this.course = course;
            this.score = score;
            this.exactMatches = exactMatches;
            this.kmpMatches = kmpMatches;
            this.zMatches = zMatches;
            this.approximateMatches = approximateMatches;

            this.matchedKeywords =
                    matchedKeywords;

            this.kmpMatchedKeywords =
                    kmpMatchedKeywords;

            this.zMatchedKeywords =
                    zMatchedKeywords;

            this.editMatches =
                    editMatches;
        }
    }
}
