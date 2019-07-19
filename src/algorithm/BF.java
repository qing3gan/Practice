package algorithm;

/**
 * Brute Force
 * 暴力匹配，单模式串匹配
 */
public class BF {

    public static int match(String str, String pattern) {
        if (str == null || pattern == null || str.length() < pattern.length()) return -1;
        //O(n*m)
        char[] _str = str.toCharArray();
        char[] _pattern = pattern.toCharArray();
        for (int n = 0; n < _str.length - _pattern.length + 1; n++) {
            for (int m = 0; m < _pattern.length; m++) {
                if (_pattern[m] != _str[n + m]) break;//move
                if (m == _pattern.length - 1) return n;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        System.out.println(match("abcdefghijklmnopqrstuywxyz", "zz"));
    }
}
