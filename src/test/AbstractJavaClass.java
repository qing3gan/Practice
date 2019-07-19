package test;

import static java.lang.System.out;

/**
 * Created by Agony on 2019/7/1
 */
public class AbstractJavaClass {
    public static class Companion {

    }

    public static void compute() {
        out.println("compute");
    }
}

class AbstractChildJavaClass extends AbstractJavaClass {
    public static void main(String[] args) {
        AbstractChildJavaClass.compute();
        new AbstractChildJavaClass.Companion();
    }
}
