import java.util.*;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("=================================");
        System.out.println("   COURSE RECOMMENDATION SYSTEM");
        System.out.println("=================================");

        System.out.print("\nEnter your interests: ");

        String interests = scanner.nextLine();

        CourseRecommender.recommend(interests);

        scanner.close();
    }
}
