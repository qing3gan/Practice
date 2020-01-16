package datastruct.algorithm;

public class Sort {

    /**
     * from bottom to top, compare with sibling and swap
     */
    static void bubbleSort(int a[]) {
        for (int i = 0; i < a.length; i++) {//all(unordered bottom, ordered top)
            boolean bSwap = false;//reach the ordered top
            for (int j = 0; j < a.length - i - 1; j++) {//unordered(bottom)
                if (a[j] > a[j + 1]) {//compare with sibling
                    //swap with sibling
                    swap(a, j + 1, j);
                    bSwap = true;//top pos
                }
            }
            if (!bSwap) break;//top pos
        }
    }

    /**
     * from left to right, compare with ordered area and move
     */
    static void insertSort(int a[]) {
        for (int i = 1; i < a.length; i++) {//all(expect index 0, default ordered, ordered left, unordered right)
            int value = a[i];//unordered(left)
            int j = i - 1;//ordered(right)
            for (; j >= 0; j--) {//ordered(right -> left)
                //compare with ordered
                if (value < a[j]) a[j + 1] = a[j];//move right
                else break;//insert pos
            }
            a[j + 1] = value;//insert(left)
        }
    }

    /**
     * from left to right, find the minimum one by one
     */
    static void selectSort(int a[]) {
        for (int i = 0; i < a.length; i++) {//all(ordered left, unordered right)
            int value = a[i];//unordered(guard)
            int pos = i;//minimum pos
            for (int j = i + 1; j < a.length; j++) {//unordered(left -> right)
                if (a[j] < value) {//compare with unordered to find minimum
                    value = a[j];//minimum guard
                    pos = j;
                }
            }
            //swap with minimum
            swap(a, i, pos);
        }
    }

    /**
     * from p<r to p>=r, recurse split util can not split anymore, then merge by order
     */
    static void mergeSort(int a[]) {
        mergeSortSplit(a, 0, a.length - 1);
    }

    static void mergeSortSplit(int a[], int p, int r) {
        if (p >= r) return;//can not split anymore
        int q = (p + r) / 2;//middle
        mergeSortSplit(a, p, q);//recurse split
        mergeSortSplit(a, q + 1, r);//recurse split
        mergeSortMerge(a, p, q, r);//merge
    }

    static void mergeSortMerge(int a[], int p, int q, int r) {
        int[] tmp = new int[r - p + 1];//temp array for merge
        int i = p, j = q + 1, k = 0;//multi index(array left index, array right index, temp array start index) for merge
        while (i <= q && j <= r) {//merge by order
            if (a[i] < a[j]) tmp[k++] = a[i++];
            else tmp[k++] = a[j++];
        }
        int start = i, end = q;//merge the remain array(default array left)
        if (j <= r) {//array right remain
            start = j;
            end = r;
        }
        while (start <= end) {//merge remain
            tmp[k++] = a[start++];
        }
        System.arraycopy(tmp, 0, a, p, r - p + 1);
    }

    /**
     * from p<r to p>=r, recurse partition util can not partition anymore
     */
    static void quickSort(int a[]) {
        quickSortSplit(a, 0, a.length - 1);
    }

    static void quickSortSplit(int a[], int p, int r) {
        if (p >= r) return;//can not partition anymore
        int q = quickSortPartition(a, p, r);//partition pivot index
        quickSortSplit(a, p, q - 1);//partition
        quickSortSplit(a, q + 1, r);//partition
    }

    static int quickSortPartition(int a[], int p, int r) {
        int pivot = a[r];//pivot
        //just like select sort(handled left, unhandled right)
        int i = p;
        for (int j = p; j <= r - 1; j++) {
            if (a[j] < pivot) {//handled(swap)
                swap(a, i, j);
                i++;
            }
        }
        //swap with pivot
        swap(a, i, r);
        return i;
    }

    /**
     * range to bucket(space to time)
     */
    static void bucketSort(int a[]) {
        //find the max
        int max = a[0];
        for (int i = 0; i < a.length; i++) {
            if (max < a[i]) {
                max = a[i];
            }
        }
        //put bucket
        int[] bucket = new int[max + 1];
        for (int i = 0; i < a.length; i++) {
            bucket[a[i]] = a[i];
        }
        int k = 0;
        for (int j = 0; j < bucket.length; j++) {
            if (bucket[j] != 0) {
                a[k++] = bucket[j];
            }
        }
        /*int m = 2;//bucket
        int[] m1 = new int[a.length];//capacity
        int[] m2 = new int[a.length];//capacity
        int pivot = 5;//(min+max)/2
        int j = 0, k = 0;//bucket index
        for (int i = 0; i < a.length; i++) {//put bucket
            if (a[i] < pivot) m1[j++] = a[i];
            else m2[k++] = a[i];
        }
        //mergeSort
        mergeSort(m1);
        mergeSort(m2);
        //get bucket
        int r = 0;
        for (int p = 0; p < a.length; p++) {
            if (m1[p] != 0) {
                a[r++] = m1[p];
            }
        }
        for (int q = 0; q < a.length; q++) {
            if (m2[q] != 0) {
                a[r++] = m2[q];
            }
        }*/
    }

    /**
     * counting to bucket(space to time)
     */
    static void countSort(int a[]) {
        //find the max
        int max = a[0];
        for (int i = 0; i < a.length; i++) {
            if (max < a[i]) {
                max = a[i];
            }
        }
        //size array
        int[] c = new int[max + 1];
        for (int j = 0; j < a.length; j++) {
            c[a[j]]++;
        }
        //size sum
        for (int k = 1; k <= max; k++) {
            c[k] += c[k - 1];
        }
        //size sort
        int[] r = new int[a.length];
        for (int p = a.length - 1; p >= 0; p--) {
            int index = c[a[p]] - 1;//a's value, c's index
            r[index] = a[p];
            c[a[p]]--;
        }
        //done
        for (int q = 0; q < a.length; q++) {
            a[q] = r[q];
        }
    }

    /**
     * radix to bucket(space to time)
     */
    static void radixSort(int a[]) {
        int radix = 2;
        for (int i = 1; i <= radix; i++) {
            int[] r = new int[a.length];//result
            int[] c = new int[10];//0-9's bucket
            //size sort
            for (int j = 0; j < a.length; j++) {
                int digit = a[j] % (int) Math.pow(10, i) / (int) Math.pow(10, i - 1);
                c[digit]++;
            }
            for (int k = 1; k < c.length; k++) {
                c[k] += c[k - 1];
            }
            for (int m = a.length - 1; m >= 0; m--) {
                int digit = a[m] % (int) Math.pow(10, i) / (int) Math.pow(10, i - 1);
                int index = c[digit] - 1;
                r[index] = a[m];
                c[digit]--;
            }
            for (int n = 0; n < a.length; n++) {
                a[n] = r[n];
            }
        }
    }

    static void printArray(int a[]) {
        for (int anA : a) {
            System.out.print(anA + " ");
        }
        System.out.println();
    }

    /**
     * order, static, array
     */
    static int binarySearch(int a[], int value) {
        /*int low = 0;
        int high = a.length - 1;
        int mid = low + (high - low) >> 1;//low+high/2 -> (low+low-low+high)/2 -> low+(high-low)/2, prevent math overflow
        while (low <= high) {//low>high end while
            if (value == a[mid]) {//mid
                System.out.println(mid);
                return mid;
            } else if (value < a[mid]) {//left
                high = mid - 1;
            } else {//right
                low = mid + 1;
            }
            mid = low + (high - low) >> 1;
        }
        System.out.println(mid);
        return mid;*/
        return binarySearch(a, 0, a.length - 1, value);
    }

    static int binarySearch(int a[], int low, int high, int value) {
        if (low > high) return -1;
        int mid = low + (high - low) >> 1;
        if (value == a[mid]) {
            System.out.println(mid);
            return mid;
        } else if (value < a[mid]) {
            binarySearch(a, low, mid - 1, value);
        } else {
            binarySearch(a, mid + 1, high, value);
        }
        return mid;
    }

    static void sqrt(double value) {
        double low = 0.0;
        double high = value;
        double mid = (low + high) / 2;
        while (Math.abs(value - mid * mid) > 0.000001) {
            if (mid * mid < value) {
                low = mid;
            } else {
                high = mid;
            }
            mid = (low + high) / 2;
        }
        System.out.println(mid);
    }

    static void heapSort(int[] a, int size) {//last leaf node swap with root and heapify(just like remove max)
        buildHeap(a, size);//build
        //sort
        while (size > 1) {
            swap(a, 1, size);//swap with root
            size--;//just like remove max
            heapify(a, size, 1);
        }
    }

    static void buildHeap(int[] a, int size) {//from non-leaf node to top heapify
        for (int index = size / 2; index >= 1; index--) {//[1..n/2] is non-leaf node
            heapify(a, size, index);
        }
    }

    static void heapify(int[] a, int size, int index) {
        //from up to down heapify
        while (true) {//compare with left and right
            int maxPos = index;
            if (2 * index <= size && a[index] < a[2 * index]) maxPos = 2 * index;//left
            if (2 * index + 1 <= size && a[maxPos] < a[2 * index + 1]) maxPos = 2 * index + 1;//right
            if (maxPos == index) break;//biggest
            swap(a, index, maxPos);
            index = maxPos;
        }
    }

    static void swap(int[] a, int i, int j) {
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }

    public static void main(String[] args) {
        int a[] = {0, 10, 90, 88, 72, 64, 59, 42, 38, 26, 12};
//        bubbleSort(a);
//        insertSort(a);
//        selectSort(a);
//        mergeSort(a);
//        quickSort(a);
//        bucketSort(a);
//        countSort(a);
//        radixSort(a);
//        binarySearch(a, 12);
//        sqrt(3);
        heapSort(a, a.length - 1);
        printArray(a);
    }
}
