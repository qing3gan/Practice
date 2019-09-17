package datastruct.struct;

/**
 * 字典树
 */
public class TrieADT {

    static class TreeNode {
        char data;
        TreeNode[] child = new TreeNode[26];//a-z
        boolean isEndingChar = false;

        TreeNode(char data) {
            this.data = data;
        }
    }

    static TreeNode root = new TreeNode('/');

    /**
     * build a trie
     */
    static void insert(String text) {
        TreeNode p = root;
        char[] cText = text.toCharArray();
        for (char c : cText) {
            int index = c - 'a';
            if (p.child[index] == null) {
                TreeNode n = new TreeNode(c);
                p.child[index] = n;
            }
            p = p.child[index];
        }
        p.isEndingChar = true;
    }

    /**
     * match text
     */
    static boolean find(String text) {
        TreeNode p = root;
        char[] cText = text.toCharArray();
        for (char c : cText) {
            int index = c - 'a';
            if (p.child[index] == null) return false;
            p = p.child[index];
        }
        return p.isEndingChar;
    }

    public static void main(String[] args) {
        String[] strings = new String[]{"hello", "hi", "how", "he"};
        for (String string : strings) {
            insert(string);
        }
        System.out.println(find("he"));
    }
}
