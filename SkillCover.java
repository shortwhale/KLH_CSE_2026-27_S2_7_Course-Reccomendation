
import java.util.*;

public class SkillCover {

    public static List<String> approximateVertexCover(
            String[] skills, int[][] edges) {

        boolean[] coveredVertex = new boolean[skills.length];
        List<String> cover = new ArrayList<>();

        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];

            if (!coveredVertex[u] && !coveredVertex[v]) {
                coveredVertex[u] = true;
                coveredVertex[v] = true;

                cover.add(skills[u]);
                cover.add(skills[v]);
            }
        }

        return cover;
    }

    public static void runDemo() {
        System.out.println("\n[2] Approximate Vertex Cover");
        System.out.println("-".repeat(48));

        String[] skills = {
            "Programming", "Mathematics",
            "Statistics", "Visualization"
        };

        int[][] edges = {
            {0, 1},
            {1, 2},
            {0, 3},
            {2, 3}
        };

        List<String> cover = approximateVertexCover(skills, edges);

        System.out.println("Skill relationships:");

        for (int[] edge : edges) {
            System.out.println(
                skills[edge[0]] + " -- " + skills[edge[1]]
            );
        }

        System.out.println("Approximate vertex cover: " + cover);
        System.out.println("Cover size: " + cover.size());
        System.out.println(
            "Approximation guarantee: cover size <= 2 * optimal size"
        );
    }
}
