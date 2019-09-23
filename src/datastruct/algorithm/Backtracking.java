package datastruct.algorithm;

/**
 * 回溯（枚举）
 */
public class Backtracking {

    /**
     * 8x8的棋盘，希望往里放8个棋子（皇后），每个棋子所在的行、列、对角线都不能有另一个棋子
     */
    private static class EightQueues {
        private int[] result = new int[8];//result[row]=column

        public void cal8Queues(int row) {//cal8Queues(0)
            //recurse and(backtracking)
            if (row == 8) {
                print8Queues(result);
                return;
            }
            //recurse loop(from [0,0] to [7,7])
            for (int column = 0; column < 8; column++) {
                if (isOk(row, column)) {
                    result[row] = column;
                    cal8Queues(row + 1);
                }
            }
        }

        private boolean isOk(int row, int column) {
            int leftup = column - 1, rightup = column + 1;
            //from row-1 to top
            for (int i = row - 1; i >= 0; i--) {
                if (result[i] == column) return false;
                if (leftup >= 0 && result[i] == leftup) return false;
                if (rightup < 8 && result[i] == rightup) return false;
                leftup--;
                rightup++;
            }
            return true;
        }

        private void print8Queues(int[] result) {
            for (int row = 0; row < 8; row++) {
                for (int column = 0; column < 8; column++) {
                    if (result[row] == column) System.out.print("* ");
                    else System.out.print("- ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    /**
     * 一个背包总的承载重量是Wkg，现有n个物品，每个物品的重量不等且不可分割，在不超重的前提下，让背包中的物品之总重量最大
     */
    private static class ZeroOne {
        private int maxWeight = Integer.MAX_VALUE;//stop flag

        public void f(int i, int sumWeight, int[] items, int n, int weight) {
            //recurse end(backtracking)
            if (sumWeight == weight || i == n) {
                if (sumWeight > maxWeight) maxWeight = sumWeight;
                System.out.println("maxWeight: " + sumWeight);
                return;
            }
            //recurse loop(from 0 to 2^n with cut)
            f(i + 1, sumWeight, items, n, weight);//0
            if (sumWeight + items[i] <= weight) {//1(cut)
                f(i + 1, sumWeight + items[i], items, n, weight);
            }
        }
    }

    /**
     * 正则匹配
     */
    private static class Pattern {
        private boolean matched = false;
        private char[] pattern;
        private int plen;

        public Pattern(String pattern) {
            this.pattern = pattern.toCharArray();
            this.plen = pattern.length();
        }

        public boolean match(String text) {
            if (text == null) return false;
            matched = false;
            rmatch(0, 0, text.toCharArray(), text.length());
            return matched;
        }

        private void rmatch(int ti, int pj, char[] text, int tlen) {
            //recurse end(backtracking)
            if (matched) return;
            if (pj == plen) {
                if (ti == tlen) matched = true;
                return;
            }
            //recurse loop(from pattern[0,length] to text[0,length])
            if (pattern[pj] == '*') {
                for (int k = 0; k <= tlen - ti; k++) {
                    rmatch(ti + k, pj + 1, text, tlen);
                }
            } else if (pattern[pj] == '?') {
                rmatch(ti, pj + 1, text, tlen);
                rmatch(ti + 1, pj + 1, text, tlen);
            } else if (ti < tlen && pattern[pj] == text[ti]) {
                rmatch(ti + 1, pj + 1, text, tlen);
            }
        }
    }

    public static void main(String[] args) {
//        EightQueues eightQueues = new EightQueues();
//        eightQueues.cal8Queues(0);
//        ZeroOne zeroOne = new ZeroOne();
//        int[] items = new int[]{1, 3, 6};
//        zeroOne.f(0, 0, items, 3, 8);
        Pattern pattern = new Pattern("?bcd");
        System.out.println(pattern.match("abcd"));
        System.out.println(pattern.match("bbcd"));
    }
}
