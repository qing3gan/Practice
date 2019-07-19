package util;

import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Set;

/**
 * Created by Agony on 2018/5/25.
 */
public class Test {
    public static void main(String[] args) throws Exception {
        /*Process process = Runtime.getRuntime().exec("ipconfig");
        process.waitFor();
        System.out.println();
        List<String> list = new ArrayList<>();
        for(String str : list){
            System.out.println();
        }
        System.out.println(LocalDate.now().plus(-1, ChronoUnit.DAYS));*/

        /*BlockingQueue<String> queue = new LinkedBlockingDeque<>(1);
        for (int i = 0; i < 3; i++) {
            new Thread(new Producer(queue, "producer" + i)).start();
        }

        for (int i = 0; i < 5; i++) {
            new Thread(new Consumer(queue, "consumer" + i)).start();
        }*/
        /*Stream<Integer> integerStream = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
        List<Integer> listInt = integerStream.parallel().collect(Collectors.toList());
        List<List<Integer>> listList = ListUtils.partition(listInt, 4);
        listList.parallelStream().forEach(System.out::println);
        System.out.println(listList);
        List<String> listStr = integerStream.parallel().map(x -> String.format("'%d'", x)).collect(Collectors.toList());
        System.out.println(listStr);
        Set<String> o = Sets.newHashSet("1", "2", "3");
        Set<String> n = Sets.newHashSet("2", "3", "4");
        System.out.println(CollectionUtils.removeAll(CollectionUtils.union(o, n), o));
        System.out.println(CollectionUtils.removeAll(CollectionUtils.union(o, n), n));*/
        /*String a = "a ";
        a.trim();
        System.out.println(a.substring(0,a.length()-1)+"b");*/
    }
}
