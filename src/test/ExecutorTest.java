package test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Agony on 2019/3/28
 */
public class ExecutorTest {
    private static ExecutorTest executorTest;

    private ExecutorTest() {
        System.out.println("1");
    }

    static {
        try {
            System.out.println(1/0);
            executorTest = new ExecutorTest();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ExecutorTest getExecutorTest() {
        if(null == executorTest){
            return new ExecutorTest();
        }
        return executorTest;
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executorService.submit(() -> System.out.println(getExecutorTest()));
        }
        System.out.println("2");
        executorService.shutdown();
    }
}
