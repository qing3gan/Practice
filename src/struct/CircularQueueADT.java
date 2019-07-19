package struct;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 循环队列(FIFO)
 */
public class CircularQueueADT {

    private int[] items;
    private int n = 0;
    private int head = 0;
    private int tail = 0;

    public CircularQueueADT(int n) {
        this.n = n;
        this.items = new int[n];
    }

    public boolean enqueue(int data) {
        if ((tail + 1) % n == head) return false;
        items[tail] = data;
        tail = (tail + 1) % n;
        return true;
    }

    public int dequeue() {
        if (head == tail) return -1;
        int data = items[head];
        head = (head + 1) % n;
        return data;
    }

    public void printQueue() {
        if (n == 0) return;
        for (int i = head; i % n != tail; ++i) {
            System.out.print(items[i] + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) throws Exception {
        CircularQueueADT queue = new CircularQueueADT(100);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Future<CircularQueueADT> future = executorService.submit(() -> {
            for (int i = 0; i < 100; i++) {
                queue.enqueue(i);
            }
            return queue;
        });
        future.get().printQueue();
        System.out.println(queue.dequeue());
        System.out.println(queue.dequeue());
        executorService.shutdown();
    }
}
