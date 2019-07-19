package test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Agony on 2019/3/28
 */
public class Test {
    private static Test test;

    private Test() {
        System.out.println("1");
    }

    static {
        try {
            System.out.println(1/0);
            test = new Test();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Test getTest() {
        if(null == test){
            return new Test();
        }
        return test;
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executorService.submit(() -> System.out.println(getTest()));
        }
        System.out.println("2");
        executorService.shutdown();
    }
}
