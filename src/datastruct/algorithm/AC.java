package datastruct.algorithm;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Aho-Corasick
 * 最长可匹配好前后缀，多模式串匹配
 */
public class AC {

    static class ACNode {
        char data;
        ACNode[] child = new ACNode[26];//a-z
        boolean isEndingChar = false;
        int length;//ending char pattern length
        ACNode fail;//next

        ACNode(char data) {
            this.data = data;
        }
    }

    static ACNode root = new ACNode('/');

    /**
     * build a trie
     */
    static void insert(String text) {
        ACNode p = root;
        char[] cText = text.toCharArray();
        for (char c : cText) {
            int index = c - 'a';
            if (p.child[index] == null) {
                ACNode n = new ACNode(c);
                p.child[index] = n;
            }
            p = p.child[index];
        }
        p.isEndingChar = true;
        p.length = text.length();
    }

    /**
     * build fail pointer
     */
    static void buildFailPointer() {
        //traverse trie with level order
        Queue<ACNode> queue = new LinkedList<>();
        root.fail = null;//except 1 char
        queue.add(root);
        while (!queue.isEmpty()) {
            ACNode p = queue.remove();
            //traverse trie
            for (int i = 0; i < 26; i++) {
                ACNode pc = p.child[i];
                if (pc == null) continue;
                if (p == root) {
                    pc.fail = root;//root child fail to root
                } else {
                    //good prefix rule
                    ACNode q = p.fail;
                    while (q != null) {
                        ACNode qc = q.child[pc.data - 'a'];
                        if (qc != null) {//to fail
                            pc.fail = qc;
                            break;
                        }
                        //fail path
                        q = q.fail;
                    }
                    if (q == null) {
                        pc.fail = root;//to root
                    }
                }
                queue.add(pc);
            }
        }
    }

    static void match(String str) {
        System.out.println(str);
        char[] cText = str.toCharArray();
        ACNode p = root;
        //match from begin to end
        for (int i = 0; i < str.length(); i++) {
            int index = cText[i] - 'a';
            while (p.child[index] == null && p != root) {//bad char
                p = p.fail;//next
            }
            p = p.child[index];//good char
            if (p == null) p = root;//move
            //pattern from root to fail to leaf
            ACNode tmp = p;
            while (tmp != root) {
                if (tmp.isEndingChar) {//match
                    int pos = i - tmp.length + 1;
                    System.out.println(pos + "," + tmp.length);
                }
                //good prefix max match substring
                tmp = tmp.fail;
            }
        }
    }

    public static void main(String[] args) {
        String[] strings = new String[]{"fuck", "bitch", "shit"};
        for (String string : strings) {
            insert(string);
        }
        buildFailPointer();
        match("justfucktestbitchandshit");
    }
}
