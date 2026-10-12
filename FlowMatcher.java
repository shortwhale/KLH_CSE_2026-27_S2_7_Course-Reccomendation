
import java.util.*;

public class FlowMatcher {

    private static boolean findMatch(
            int interest, boolean[][] edges,
            boolean[] visited, int[] courseToInterest) {

        for (int course = 0; course < edges[interest].length; course++) {
            if (edges[interest][course] && !visited[course]) {
                visited[course] = true;

                if (courseToInterest[course] == -1 ||
                    findMatch(courseToInterest[course], edges,
                              visited, courseToInterest)) {
                    courseToInterest[course] = interest;
                    return true;
                }
            }
        }
        return false;
    }

    public static List<String> maximumMatching(
            String[] interests, String[] courses, boolean[][] edges) {

        int[] courseToInterest = new int[courses.length];
        Arrays.fill(courseToInterest, -1);

        int matchingSize = 0;

        for (int i = 0; i < interests.length; i++) {
            boolean[] visited = new boolean[courses.length];

            if (findMatch(i, edges, visited, courseToInterest)) {
                matchingSize++;
            }
        }

        List<String> pairs = new ArrayList<>();

        for (int c = 0; c < courses.length; c++) {
            if (courseToInterest[c] != -1) {
                pairs.add(
                    interests[courseToInterest[c]] + " -> " + courses[c]
                );
            }
        }

        System.out.println("Maximum matching size: " + matchingSize);
        return pairs;
    }

    public static void runDemo() {
        System.out.println("\n[1] Maximum Bipartite Matching");
        System.out.println("-".repeat(48));

        String[] interests = {
            "Coding", "Mathematics", "Design"
        };

        String[] courses = {
            "Computer Science", "Data Science", "Digital Design"
        };

        boolean[][] edges = {
            {true, true, false},
            {true, true, false},
            {false, false, true}
        };

        List<String> pairs = maximumMatching(interests, courses, edges);

        System.out.println("Interest-to-course assignments:");

        for (String pair : pairs) {
            System.out.println(pair);
        }
    }
}
