package algorithm;

/**
 * Knuth Morris Pratt
 * 最长可匹配好前后缀，单模式串匹配
 */
public class KMP {

    public static int match(String str, String pattern) {
        if (str == null || pattern == null || str.length() < pattern.length()) return -1;
        char[] strs = str.toCharArray();
        char[] patterns = pattern.toCharArray();
        //good prefix rule
        int[] next = generateNext(patterns);
        int j = 0;//pattern matching position
        for (int i = 0; i < str.length(); i++) {//str matching position
            //match from begin to end
            while (j > 0 && strs[i] != patterns[j]) {//bad char(except 1 char)
                j = next[j - 1] + 1;//move(good prefix max match substring length)
            }
            if (strs[i] == patterns[j]) {//good char
                j++;
            }
            if (j == patterns.length) {//match
                return i - patterns.length + 1;
            }
        }
        return -1;
    }

    private static int[] generateNext(char[] pattern) {
        int[] next = new int[pattern.length];
        next[0] = -1;//expect 1 char
        int k = -1;//good prefix max match substring ending char position
        for (int i = 1; i < pattern.length; i++) {
            //pattern[0, k] is pattern[0, i - 1]'s good prefix max match substring
            //pattern[k + 1] != pattern[0, i]
            while (k != -1 && pattern[k + 1] != pattern[i]) {//find pattern[0, i - 1]'s good prefix max match substring(dynamic programing)
                k = next[k];//pattern[0, next[k]] is pattern[0, i - 1]'s good prefix max match substring
            }
            //pattern[k + 1] == pattern[i]
            if (pattern[k + 1] == pattern[i]) {//find pattern[0, i]'s good prefix max match substring(dynamic programing)
                k++;
            }
            next[i] = k;//good prefix max match substring ending char position
        }
        return next;
    }

    public static void main(String[] args) {
        System.out.println(match("abcdeggabcdfgg", "abcdf"));
    }
}
