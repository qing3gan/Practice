package struct;

import java.util.Random;

/**
 * 跳表(NODE[])
 */
public class SkipListADT {

    static class Node {
        int data;//node value
        Node[] next = new Node[MAX_LEVEL];//node index pointer
        int maxLevel = 0;//node index level
    }

    static final int MAX_LEVEL = 16;//max index level

    static Node head = new Node();//LinkedList with head

    static int levelCount = 1;//insert delete index level

    static Random random = new Random();//keep balance

    static void printAll() {
        Node p = head;
        while (p.next[0] != null) {
            System.out.print(p.next[0].data + " ");
            p = p.next[0];
        }
        System.out.println();
    }

    static void find(int data) {
        Node p = head;
        //skip
        for (int i = levelCount - 1; i >= 0; i--) {
            //next
            while (p.next[i] != null && p.next[i].data < data) {
                p = p.next[i];
            }
        }
        //origin linkedList
        if (p.next[0] != null && p.next[0].data == data) System.out.println(p.next[0].data);
    }

    static int randomLevel() {
        //index level
        int level = 1;
        for (int i = 1; i <= MAX_LEVEL; i++) {
            if (random.nextInt() % 2 == 1) level++;//odd even balance
        }
        return level;
    }

    static void insert(int data) {
        //insert new node
        int level = randomLevel();
        Node newNode = new Node();
        newNode.data = data;
        newNode.maxLevel = level;
        Node[] update = new Node[level];
        //init
        for (int i = 0; i < level; i++) {
            update[i] = head;
        }
        Node p = head;
        //skip
        for (int i = level - 1; i >= 0; i--) {
            //next
            while (p.next[i] != null && p.next[i].data < data) {
                p = p.next[i];
            }
            //prev
            update[i] = p;
        }
        //insert link
        for (int i = 0; i < level; i++) {
            newNode.next[i] = update[i].next[i];
            update[i].next[i] = newNode;
        }
        //insert index
        if (levelCount < level) levelCount = level;
    }

    static void delete(int data) {
        Node[] update = new Node[levelCount];//delete index
        Node p = head;
        //skip
        for (int i = levelCount - 1; i >= 0; i--) {
            //next
            while (p.next[i] != null && p.next[i].data < data) {
                p = p.next[i];
            }
            //prev
            update[i] = p;
        }
        //exist
        if (p.next[0] != null && p.next[0].data == data) {
            //skip
            for (int i = levelCount - 1; i >= 0; i--) {
                //link
                if (update[i].next[i] != null && update[i].next[i].data == data)
                    update[i].next[i] = update[i].next[i].next[i];
            }
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 64; i += 2) {
            insert(i);
        }
        printAll();
        find(2);
        insert(1);
        printAll();
        delete(4);
        printAll();
    }
}
