package datastruct.algorithm;

/**
 * 动态规划（最优解）
 */
public class DynamicPrograming {

    /**
     * 一个背包总的承载重量是weight，现有n个物品，每个物品的重量不等且不可分割，在不超重的前提下，让背包中的物品之总重量最大(<=weight)[weight+1]
     * 购物车现有n个商品，商品总价满value则达到满减条件，在最大程度接近满减条件的情况下选出商品(>=value)[2*value+1]
     */
    public static class ZeroOne {

        public int f(int items[], int n, int weight) {
            //states[n][weight]
            boolean[][] states = new boolean[n][weight + 1];
            //first
            states[0][0] = true;
            if (items[0] < weight) {
                states[0][items[0]] = true;
            }
            //dynamic programing(from 1 to n)
            for (int i = 1; i < n; i++) {
                for (int j = 0; j <= weight; j++) {//0
                    if (states[i - 1][j]) states[i][j] = true;
                }
                for (int j = 0; j <= weight - items[i]; j++) {//1
                    if (states[i - 1][j]) states[i][j + items[i]] = true;
                }
            }
            //max
            for (int i = weight; i >= 0; i--) {
                if (states[n - 1][i]) return i;
            }
            return 0;
        }

        public int f2(int items[], int n, int weight) {
            //states[weight]
            boolean[] states = new boolean[weight + 1];
            //first
            states[0] = true;
            if (items[0] < weight) {
                states[items[0]] = true;
            }
            //dynamic programing(from 1 to n)
            for (int i = 1; i < n; i++) {
                for (int j = weight - items[i]; j >= 0; j--) {//1
                    if (states[j]) states[j + items[i]] = true;
                }
            }
            //max
            for (int i = weight; i >= 0; i--) {
                if (states[i]) return i;
            }
            return 0;
        }

        public int b(int items[], int values[], int n, int weight) {
            //states[n][weight]=value
            int[][] states = new int[n][weight + 1];
            //init
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < weight + 1; j++) {
                    states[i][j] = -1;
                }
            }
            //first
            states[0][0] = 0;
            if (items[0] <= weight) {
                states[0][items[0]] = values[0];
            }
            //dynamic programing(from 1 to n)
            for (int i = 1; i < n; i++) {
                for (int j = 0; j <= weight; j++) {//0
                    if (states[i - 1][j] >= 0) states[i][j] = states[i - 1][j];
                }
                for (int j = 0; j <= weight - items[i]; j++) {//1
                    if (states[i - 1][j] >= 0) {
                        int value = states[i - 1][j] + values[i];
                        if (value > states[i][j + items[i]]) {
                            states[i][j + items[i]] = value;
                        }
                    }
                }
            }
            //max
            int maxValue = -1;
            for (int i = 0; i <= weight; i++) {
                if (states[n - 1][i] > maxValue) maxValue = states[n - 1][i];
            }
            return maxValue;
        }

        public int b2(int items[], int values[], int n, int weight) {
            //states[weight]=value
            int[] states = new int[weight + 1];
            //init
            for (int i = 0; i < n; i++) {
                states[i] = -1;
            }
            //first
            states[0] = 0;
            if (items[0] <= weight) {
                states[items[0]] = values[0];
            }
            //dynamic programing(from 1 to n)
            for (int i = 1; i < n; i++) {
                for (int j = weight - items[i]; j >= 0; j--) {//1
                    if (states[j] >= 0) {
                        int value = states[j] + values[i];
                        if (value > states[j + items[i]]) {
                            states[j + items[i]] = value;
                        }
                    }
                }
            }
            //max
            int maxValue = -1;
            for (int i = 0; i <= weight; i++) {
                if (states[i] > maxValue) maxValue = states[i];
            }
            return maxValue;
        }

        public void buybox(int items[], int n, int price) {
            //states[n][price]
            boolean[][] states = new boolean[n][2 * price + 1];
            //first
            states[0][0] = true;
            if (items[0] < 2 * price) {
                states[0][items[0]] = true;
            }
            //dynamic programing(from 1 to n)
            for (int i = 1; i < n; i++) {
                for (int j = 0; j <= 2 * price; j++) {//0
                    if (states[i - 1][j]) states[i][j] = true;
                }
                for (int j = 0; j <= 2 * price - items[i]; j++) {//1
                    if (states[i - 1][j]) states[i][j + items[i]] = true;
                }
            }
            //min
            int j;
            for (j = price; j < 2 * price + 1; j++) {
                if (states[n - 1][j]) break;
            }
            if (j == 2 * price + 1) return;//none
            //find
            for (int i = n - 1; i >= 1; i--) {
                if (j - items[i] > 0 && states[i - 1][j - items[i]]) {//1
                    System.out.print(items[i] + " ");
                    j = j - items[i];
                }
            }
            if (j != 0) System.out.print(items[0]);
        }
    }

    public static void main(String[] args) {
        ZeroOne zeroOne = new ZeroOne();
//        int[] items = new int[]{2, 2, 4, 6, 3};
//        int[] values = new int[]{3, 4, 8, 9, 6};
//        int n = 5;
//        int weight = 9;
//        System.out.println(zeroOne.f(items, n, weight));
//        System.out.println(zeroOne.f2(items, n, weight));
//        System.out.println(zeroOne.b(items, values, n, weight));
//        System.out.println(zeroOne.b2(items, values, n, weight));
        int[] items = new int[]{10, 50, 99, 68, 12, 54, 7, 62, 23, 134, 6, 45, 234, 2, 65, 2, 36, 2, 3461, 1, 31, 2, 35};
        zeroOne.buybox(items, items.length, 200);
    }
}
