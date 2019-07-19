package struct;

/**
 * 链表(NODE)
 */
public class LinkedListADT {

    static class Node {
        int data;
        Node next;

        Node(int data) {
            this.data = data;
        }
    }

    static void printList(Node list) {
        while (list != null) {
            System.out.print(list.data + " ");
            list = list.next;
        }
        System.out.println();
    }

    static Node reverseList(Node list) {
        Node head = null;
        Node prev = null;
        Node curr = list;
        while (curr != null) {
            Node next = curr.next;
            if (next == null) head = curr;
            curr.next = prev;
            prev = curr;
            curr = next;
        }
        return head;
    }

    static boolean checkCircleList(Node list) {
        if (list == null) return false;
        Node fast = list;
        Node slow = list;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
            if (fast == slow) return true;
        }
        return false;
    }

    static Node mergeSortedList(Node la, Node lb) {
        if (la == null) return lb;
        if (lb == null) return la;
        Node p = la;
        Node q = lb;
        Node head;
        if (p.data < q.data) {
            head = p;
            p = p.next;
        } else {
            head = q;
            q = q.next;
        }
        Node r = head;
        while (p != null && q != null) {
            if (p.data < q.data) {
                r.next = p;
                p = p.next;
            } else {
                r.next = q;
                q = q.next;
            }
            r = r.next;
        }
        if (p != null) {
            r.next = p;
        } else {
            r.next = q;
        }
        return head;
    }

    static Node deleteLastKth(Node list, int k) {
        Node fast = list;
        int i = 1;
        while (fast != null && i < k) {
            fast = fast.next;
            i++;
        }
        if (fast == null) return list;
        Node slow = list;
        Node prev = null;
        while (fast.next != null) {
            fast = fast.next;
            prev = slow;
            slow = slow.next;
        }
        if (prev == null) list = list.next;
        else prev.next = prev.next.next;
        return list;
    }

    static Node findMiddleNode(Node list) {
        if (list == null) return null;
        Node fast = list;
        Node slow = list;
        while (fast.next != null && fast.next.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        return slow;
    }

    public static void main(String[] args) {
        Node list1 = new Node(1);
        Node node1 = list1;
        for (int i = 3; i <= 10; i += 2) {
            node1 = (node1.next = new Node(i));
        }
        Node list2 = new Node(2);
        Node node2 = list2;
        for (int i = 4; i <= 10; i += 2) {
            node2 = (node2.next = new Node(i));
        }
//        node.next = list1;
        printList(list1);
        printList(list2);
//        printList(mergeSortedList(list1, list2));
//        printList(deleteLastKth(list1, 1));
//        printList(deleteLastKth(list2, 5));
        System.out.println(findMiddleNode(list1).data);
        System.out.println(findMiddleNode(list2).data);
//        printList(reverseList(list1));
//        System.out.println(checkCircleList(list1));
    }
}
