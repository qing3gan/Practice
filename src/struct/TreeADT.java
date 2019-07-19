package struct;

import java.util.*;

/**
 * 树
 */
public class TreeADT {

    static class Node {
        int data;
        Node left;
        Node right;

        public Node(int data) {
            this.data = data;
        }
    }

    /**
     * print(root) -> print(root.left) -> print(root.right)
     */
    static void preOrder(Node node) {
        if (node == null) return;
        System.out.print(node.data + " ");
        preOrder(node.left);
        preOrder(node.right);
    }

    /**
     * print(root.left) -> print(root) -> print(root.right)
     */
    static void inOrder(Node node) {
        if (node == null) return;
        inOrder(node.left);
        System.out.print(node.data + " ");
        inOrder(node.right);
    }

    /**
     * print(root.left) -> print(root.right) -> print(root)
     */
    static void postOrder(Node node) {
        if (node == null) return;
        postOrder(node.left);
        postOrder(node.right);
        System.out.print(node.data + " ");
    }

    /**
     * breadth first search
     * offer(root) -> poll(root) -> offer(root.left) -> offer(root.right)
     * -> poll(root.left) -> offer(root.left.left) -> offer(root.left.right)...
     */
    static void bfs(Node node) {
        List<Integer> list = new ArrayList<>();
        Queue<Node> queue = new LinkedList<>();
        queue.offer(node);
        while (!queue.isEmpty()) {
            Node n = queue.poll();
            if (n != null) {
                if (n.left != null) {
                    queue.offer(n.left);
                }
                if (n.right != null) {
                    queue.offer(n.right);
                }
                list.add(n.data);
            }
        }
        System.out.println(list);
    }

    /**
     * depth first search
     * push(root) -> pop(root) -> push(root.right) -> push(root.left)
     * -> pop(root.left) -> push(root.left.right) -> push(root.left.left)
     */
    static void dfs(Node node) {
        List<Integer> list = new ArrayList<>();
        Stack<Node> stack = new Stack<>();
        stack.push(node);
        while (!stack.isEmpty()) {
            Node n = stack.pop();
            if (n != null) {
                if (n.right != null) {
                    stack.push(n.right);
                }
                if (n.left != null) {
                    stack.push(n.left);
                }
                list.add(n.data);
            }
        }
        System.out.println(list);
    }

    /**
     * BinarySearchTree(left < parent < right)
     */
    static int find(Node tree, int data) {
        if (tree.data == data) return tree.data;
        Node p = tree;//pointer
        while (p != null) {//search
            if (data < p.data) {//left(<)
                p = p.left;
            } else if (data > p.data) {//right(>)
                p = p.right;
            } else {//(=)
                return p.data;
            }
        }
        return -1;
    }

    /**
     * BinarySearchTree(find -> insert)
     */
    static void insert(Node tree, int data) {
        if (tree == null) tree = new Node(data);
        Node p = tree;//pointer
        while (p != null) {//search
            if (data < p.data) {//left(<)
                if (p.left == null) {
                    p.left = new Node(data);
                    return;
                }
                p = p.left;
            } else {//right(>=)
                if (p.right == null) {
                    p.right = new Node(data);
                    return;
                }
                p = p.right;
            }
        }
    }

    /**
     * BinarySearchTree(find -> delete)
     */
    static void delete(Node tree, int data) {
        //search
        Node p = tree;//deleted node
        Node pp = null;//deleted parent
        while (p != null && p.data != data) {
            pp = p;
            if (data < p.data) p = p.left;
            else p = p.right;
        }
        if (p == null) return;//not find
        //find
        //deleted node has left and right
        if (p.left != null && p.right != null) {//update deleted node and deleted parent(swap with deleted node right tree left min node)
            Node minP = p.right;//min node
            Node minPP = p;//min parent
            while (minP.left != null) {//search min
                minPP = minP;
                minP = minP.left;
            }
            //find and swap
            p.data = minP.data;
            //update and use strategy below
            p = minP;
            pp = minPP;
        }
        //deleted node only have left or right
        //deleted node haven't child
        Node child = null;//deleted child
        if (p.left != null) child = p.left;
        else if (p.right != null) child = p.right;
        else child = null;
        //delete
        if (pp == null) tree = child;
        else if (pp.left == p) pp.left = child;
        else if (pp.right == p) pp.right = child;
    }

    /**
     * BinarySearchTree(find left tree)
     */
    static int findMin(Node tree) {
        Node p = tree;//node
        while (p.left != null) {//search
            p = p.left;
        }
        return p.data;
    }

    /**
     * BinarySearchTree(find right tree)
     */
    static int findMax(Node tree) {
        Node p = tree;//node
        while (p.right != null) {//search
            p = p.right;
        }
        return p.data;
    }

    /**
     * RecursionTree[printA(capacity,capacity) = capacity's printA(capacity,capacity-1) = capacity(capacity-1)...(capacity-k+1)'s printA(capacity,k)]
     */
    static void printAlignment(int[] a, int k) {
        //recursive condition
        if (k == 1) {//printA(capacity,1) -> a[0...capacity-1] was aligned, just print by order
            for (int i = 0; i < a.length; i++) {
                System.out.print(a[i] + " ");
            }
            System.out.println();
        }
        //recursive method
        for (int i = 0; i < k; i++) {//swap with last for align
            //swap for align
            int tmp = a[i];
            a[i] = a[k - 1];
            a[k - 1] = tmp;
            printAlignment(a, k - 1);//print alignment
            //swap for realign
            tmp = a[i];
            a[i] = a[k - 1];
            a[k - 1] = tmp;
        }
    }

    public static void main(String[] args) {
        /**
         *         root(4)
         *     a(2)       b(6)
         * c(1) d(3)  e(5) f(7)
         */
        Node root = new Node(4);
        Node a = new Node(2);
        Node b = new Node(6);
        Node c = new Node(1);
        Node d = new Node(3);
        Node e = new Node(5);
        Node f = new Node(7);
        root.left = a;
        root.right = b;
        a.left = c;
        a.right = d;
        b.left = e;
        b.right = f;
        /*preOrder(root);
        System.out.println();
        inOrder(root);
        System.out.println();
        postOrder(root);*/
        /*System.out.println(find(root, 4));
        System.out.println(find(root, 8));*/
        /*insert(root, 8);
        preOrder(root);
        System.out.println();
        inOrder(root);
        System.out.println();
        postOrder(root);*/
        /*delete(root, 4);
        preOrder(root);
        System.out.println();
        inOrder(root);
        System.out.println();
        postOrder(root);*/
        /*System.out.println(findMin(root));
        System.out.println(findMax(root));*/
//        printAlignment(new int[]{1, 2, 3, 4}, 4);
//        bfs(root);
//        dfs(root);
    }
}
