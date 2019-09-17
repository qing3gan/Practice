package datastruct.algorithm;

/**
 * Rabin-Karp
 * 哈希匹配，单模式串匹配
 */
public class RK {

    public static int match(String str, String pattern) {
        if (str == null || pattern == null || str.length() < pattern.length()) return -1;
        long patternHash = radixSumHash(pattern);
        String subStr = str.substring(0, pattern.length());
        long strHash = radixSumHash(subStr);
        if (strHash == patternHash && subStr.equals(pattern)) return 0;
        char[] _str = str.toCharArray();
        for (int n = 1; n < str.length() - pattern.length() + 1; n++) {//traverse str's pattern length substring
            subStr = str.substring(n, pattern.length() + n);
            //h[n] = h[n-1] - (s[n-1]-'a') + (s[n+m-1]-'a')
            strHash = strHash - (_str[n - 1] - 'a') + (_str[n + pattern.length() - 1] - 'a');
            if (strHash == patternHash && subStr.equals(pattern)) {
                return n;
            }
        }
        return -1;
    }

    private static long radixSumHash(String str) {//a-z(0-25) 26 radix sum hash
        char[] _str = str.toCharArray();
        long hashSum = 0;
        for (int i = 0; i < _str.length; i++) {
            hashSum += (_str[i] - 'a');
        }
        return hashSum;
    }

    public static void main(String[] args) {
        System.out.println(match("abcdefghijklmnopqrstuywxyz", "efghijklmnopqr"));
    }
}
