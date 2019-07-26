package test;

import java.util.concurrent.BlockingQueue;

/**
 * Created by Agony on 2018/7/9.
 */
public class Producer implements Runnable {
    private BlockingQueue<String> queue;
    private String name;

    public Producer(BlockingQueue<String> queue, String name) {
        this.queue = queue;
        this.name = name;
    }

    @Override
    public void run() {
        System.out.println(String.format("%s producing product", name));
        try {
            queue.put(name);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
