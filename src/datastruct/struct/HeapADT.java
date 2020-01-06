package datastruct.struct;

/**
 * 堆（特殊完全二叉树）
 */
public class HeapADT {

    private int[] array;//backup(big top heap)
    private int capacity;//bounded
    private int size;

    /**
     * index from 1
     * add array[size+1]
     * remove array[1]
     *                  root:array[1]
     *                  /          \
     * left:array[2*index]      right:array[2*index+1]
     */
    public HeapADT(int capacity) {
        array = new int[capacity + 1];
        this.capacity = capacity;
        size = 0;
    }

    public void insert(int data) {
        if (size >= capacity) return;
        array[++size] = data;
        heapifyDownToUp(size);
    }

    public void removeMax() {//remove root
        if (size == 0) return;
        array[1] = array[size--];
        heapifyUpToDown(size);
    }

    private void heapifyDownToUp(int index) {
        //heapify from down to up
        while (index / 2 > 0 && array[index] > array[index / 2]) {//compare with parent
            swap(array, index, index / 2);
            index = index / 2;
        }
    }

    private void heapifyUpToDown(int size) {
        //from up to down heapify
        int index = 1;
        while (true) {//compare with left and right
            int maxPos = index;
            if (2 * index <= size && array[index] < array[2 * index]) maxPos = 2 * index;//left
            if (2 * index + 1 <= size && array[maxPos] < array[2 * index + 1]) maxPos = 2 * index + 1;//right
            if (maxPos == index) break;//biggest
            swap(array, index, maxPos);
            index = maxPos;
        }
    }

    private void swap(int[] a, int i, int j) {
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }

    public void printHeap() {
        if (size == 0) return;
        int level = (int) (Math.log(capacity) / Math.log(2)) + 1;
        int index = 1;
        for (int high = level, deep = 1; high >= 1 || deep <= level; high--, deep++) {
            printSpace(high);
            for (int count = 0; count < Math.pow(2, deep - 1); count++) {
                System.out.print(array[index]);
                printSpace(high);
                index++;
                if (index == size + 1) {
                    return;
                }
            }
            System.out.println();
        }
    }

    private void printSpace(int n) {
        for (int i = 1; i <= n; i++) {
            System.out.print("\t");
        }
    }

    public static void main(String[] args) {
        HeapADT heap = new HeapADT(10);
        for (int i = 1; i <= heap.capacity; i++) {
            heap.insert(i);
        }
        heap.printHeap();
        System.out.println();
        heap.removeMax();
        System.out.println();
        heap.printHeap();
    }
}
