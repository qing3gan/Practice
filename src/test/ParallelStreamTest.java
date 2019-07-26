package test;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Agony on 2019/7/24
 */
public class ParallelStreamTest {
    public static void main(String[] args) throws Exception {
        System.out.println("availableProcessors: " + Runtime.getRuntime().availableProcessors());
        List<Integer> list = IntStream.range(1, 10000).boxed().collect(Collectors.toList());
        Set<Thread> threadSet = new CopyOnWriteArraySet<>();
        list.parallelStream().forEach(x -> threadSet.add(Thread.currentThread()));
        System.out.println("threadSet: " + threadSet.toString());
        List<Integer> list1 = IntStream.range(1, 10000).boxed().collect(Collectors.toList());
        List<Integer> list2 = IntStream.range(1, 10000).boxed().collect(Collectors.toList());
        Set<Thread> threadSet2 = new CopyOnWriteArraySet<>();
        CountDownLatch countDownLatch = new CountDownLatch(2);
        Thread threadA = new Thread(() -> {
            list1.parallelStream().forEach(integer -> threadSet2.add(Thread.currentThread()));
            countDownLatch.countDown();
        });
        Thread threadB = new Thread(() -> {
            list2.parallelStream().forEach(integer -> threadSet2.add(Thread.currentThread()));
            countDownLatch.countDown();
        });
        threadA.setName("ThreadA");
        threadB.setName("ThreadB");
        threadA.start();
        threadB.start();
        countDownLatch.await();
        System.out.println("threadSet2: " + threadSet2.toString());
    }
}
