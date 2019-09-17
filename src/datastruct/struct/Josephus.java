package datastruct.struct;

/**
 * 约瑟夫问题
 */
public class Josephus {

    public static void getSafePos(int n, int k) {
        int s = 0;
        for (int i = 2; i <= n; i++) {
            s = (s + k) % i;
        }
        System.out.println(s);
    }

    static class Node {
        int data;
        Node next;

        Node(int v) {
            data = v;
        }
    }

    public static void simulateJosephus(int n, int k) {
        Node head = new Node(0);
        Node tail = head;
        Node iterator = head;
        for (int i = 1; i < n; i++) {
            tail = (tail.next = new Node(i));
        }
        tail.next = head;
        while (iterator.next != head) {
            System.out.print(iterator.data + ",");
            iterator = iterator.next;
        }
        System.out.print(iterator.data);
        System.out.println();
        Node curr = tail;
        while (curr != curr.next) {
            for (int i = 1; i < k; i++) {
                curr = curr.next;
            }
            System.out.println("No." + curr.next.data + " out.");
            curr.next = curr.next.next;
        }
        System.out.println("No." + curr.data + " left.");
    }

    public static void main(String[] args) {
        getSafePos(5, 3);
        simulateJosephus(5, 3);
    }
}
