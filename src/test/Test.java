package test;

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
        /*ConcurrentHashMap<String, Object> set = new ConcurrentHashMap<>();
        set.put("1", new Object());
        set.put("2", new Object());
        System.out.println(StringUtils.join(set.keySet().iterator(), ","));*/
        /*List<String> list = Arrays.asList("1","2","3","4");
        Iterator<String> a = list.iterator();
        Iterator<String> b = list.iterator();
        System.out.println(a.next());
        System.out.println(a.next());
        System.out.println(b.next());*/
    }
}
