package datastruct.algorithm;

/**
 * Boyer Moore
 * 坏字符，好前缀，单模式串匹配
 */
public class BM {

    public static int match(String str, String pattern) {
        if (str == null || pattern == null || str.length() < pattern.length()) return -1;
        char[] strs = str.toCharArray();
        char[] patterns = pattern.toCharArray();
        //bad char rule
        int[] asciiTable = new int[256];
        generateAsciiTableForBadCharRule(patterns, asciiTable);
        //good suffix rule
        int[] suffix = new int[pattern.length()];
        boolean[] prefix = new boolean[pattern.length()];
        generateSuffixPrefixForGoodSuffixRule(patterns, suffix, prefix);
        int i = 0;//str matching position
        while (i <= str.length() - pattern.length()) {
            //match from end to begin
            int j;//pattern matching position
            for (j = pattern.length() - 1; j >= 0; j--) {
                if (strs[i + j] != patterns[j]) break;//bad char
            }
            //match
            if (j < 0) return i;
            //not match
            //bad char rule
            int si = j;//pattern bad char position
            int xi = asciiTable[strs[i + j]];//str bad char in pattern position
            int x = si - xi;
            //good suffix rule
            int y = 0;
            if (j < patterns.length - 1) {//good suffix
                y = moveByGoodSuffixRule(j, patterns.length, suffix, prefix);
            }
            //move
//            i += si - xi;
//            i += moveByGoodSuffixRule(j, patterns.length, suffix, prefix);
            i += Math.max(x, y);
        }
        return -1;
    }

    private static int moveByGoodSuffixRule(int j, int m, int[] suffix, boolean[] prefix) {
        int k = m - 1 - j;//good suffix length
        if (suffix[k] != -1) return j - suffix[k] + 1;//suffix(+1 j position)
        for (int r = j + 2; r <= m - 1; r++) {//prefix(+2 good suffix suffix)
            if (prefix[m - r]) return r;//good prefix position
        }
        return m;//pattern length
    }

    private static void generateSuffixPrefixForGoodSuffixRule(char[] patterns, int[] suffix, boolean[] prefix) {
        for (int i = 0; i < patterns.length; i++) {//good suffix rule suffix/prefix table(use the biggest position when string duplicated)
            suffix[i] = -1;//array index is suffix length, array value is matched substring begin index
            prefix[i] = false;//array index is suffix length, array value is suffix == prefix
        }
        for (int i = 0; i < patterns.length - 1; i++) {//pattern pretreatment(substring)
            int j = i;//substring index, pattern[0, i]
            int k = 0;//suffix length(pattern[0, pattens.length - 1])
            while (j >= 0 && patterns[j] == patterns[patterns.length - 1 - k]) {//find suffix common substring
                //match from end to begin
                j--;
                k++;
                suffix[k] = j + 1;//suffix common substring
            }
            //find prefix
            if (j < 0) prefix[k] = true;//suffix == prefix
        }
    }

    private static void generateAsciiTableForBadCharRule(char[] patterns, int[] asciiPos) {
        for (int i = 0; i < 256; i++) {//bad char rule ascii table(use the biggest position when char duplicated)
            asciiPos[i] = -1;
        }
        for (int i = 0; i < patterns.length; i++) {//pattern pretreatment
            int ascii = patterns[i];//array index is char ascii
            asciiPos[ascii] = i;//array value is char position
        }
    }

    public static void main(String[] args) {
        System.out.println(match("aacdefgabcdefg", "abcd"));
    }

}
