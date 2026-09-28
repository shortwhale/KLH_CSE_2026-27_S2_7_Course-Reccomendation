public class ZAlgorithm {

    public static int[] computeZ(String str) {
        int n = str.length();
        int[] z = new int[n];

        int left = 0;
        int right = 0;

        for (int i = 1; i < n; i++) {

            if (i <= right) {
                z[i] = Math.min(right - i + 1, z[i - left]);
            }

            while (i + z[i] < n &&
                   str.charAt(z[i]) == str.charAt(i + z[i])) {
                z[i]++;
            }

            if (i + z[i] - 1 > right) {
                left = i;
                right = i + z[i] - 1;
            }
        }

        return z;
    }

    public static boolean search(String text, String pattern) {

        if (pattern == null || pattern.length() == 0) {
            return true;
        }

        if (text == null || text.length() == 0) {
            return false;
        }

        text = text.toLowerCase();
        pattern = pattern.toLowerCase();

        String combined = pattern + "\u0000" + text;

        int[] z = computeZ(combined);

        int patternLength = pattern.length();

        for (int i = patternLength + 1; i < combined.length(); i++) {

            if (z[i] >= patternLength) {
                return true;
            }
        }

        return false;
    }
}