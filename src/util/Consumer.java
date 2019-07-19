package util;

import java.util.concurrent.BlockingQueue;

/**
 * Created by Agony on 2018/7/9.
 */
public class Consumer implements Runnable {
    private BlockingQueue queue;
    private String name;

    public Consumer(BlockingQueue queue, String name) {
        this.queue = queue;
        this.name = name;
    }

    @Override
    public void run() {
        System.out.println(String.format("%s trying consumer", name));
        try {
            System.out.println(String.format("%s consumer %s", name, queue.take()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
