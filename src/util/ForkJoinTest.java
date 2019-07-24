package util;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ForkJoinTest {

    static class ForkJoinSum extends RecursiveTask<Integer> {

        private static final int THRESHOLD = 1000;
        private final int start;
        private final int end;

        public ForkJoinSum(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected Integer compute() {
            if (end - start < THRESHOLD) {
                System.out.println(start + "-" + end);
                int sum = 0;
                for (int i = start; i <= end; i++) {
                    sum += i;
                }
                return sum;
            }
            int mid = (start + end) >>> 1;
            ForkJoinSum sum1 = new ForkJoinSum(start, mid);
            ForkJoinSum sum2 = new ForkJoinSum(mid + 1, end);
            sum1.fork();
            sum2.fork();
            return sum1.join() + sum2.join();
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ForkJoinSum forkJoinSum = new ForkJoinSum(1, 10000);
        forkJoinPool.submit(forkJoinSum);
        System.out.println(forkJoinSum.get());
    }
}
