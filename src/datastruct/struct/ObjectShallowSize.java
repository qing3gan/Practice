package datastruct.struct;

import java.lang.instrument.Instrumentation;

/**
 * 这种方法得到的是Shallow Size，即遇到引用时，只计算引用的长度，不计算所引用的对象的实际大小。
 * 如果要计算所引用对象的实际大小，必须通过递归的方式去计算。
 * <p>
 * Created by Agony on 2020/1/16
 */
public class ObjectShallowSize {

    private static Instrumentation inst;

    public static void premain(String agentArgs, Instrumentation instP) {
        inst = instP;
    }

    public static long sizeOf(Object obj) {
        return inst.getObjectSize(obj);
    }
}
