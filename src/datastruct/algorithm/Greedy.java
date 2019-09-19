package datastruct.algorithm;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * 贪婪（数据，限制值，期望值）
 */
public class Greedy {

    private static class Huffman {

        private class Node implements Comparable<Node> {
            char data;
            int weight;
            Node left;
            Node right;

            public Node(char data, int weight) {
                this.data = data;
                this.weight = weight;
            }

            public Node(char data, int weight, Node left, Node right) {
                this(data, weight);
                this.left = left;
                this.right = right;
            }

            @Override
            public int compareTo(Node o) {
                return Integer.compare(this.weight, o.weight);
            }
        }

        private Node root;//huffman node
        private BiMap<Character, String> dict = HashBiMap.create();//huffman code dict

        private boolean buildHuffmanTree(String text) {
            if (text == null || text.length() < 2) return false;
            Map<Character, Integer> frequency = new HashMap<>();
            //build frequency map
            for (char c : text.toCharArray()) {
                frequency.put(c, frequency.getOrDefault(c, 0) + 1);
            }
            //build weight priority queue
            Queue<Node> weight = new PriorityQueue<>();
            frequency.forEach((key, value) -> {
                Node node = new Node(key, value);
                weight.add(node);
            });
            //build huffman tree
            while (true) {
                Node left = weight.remove();
                Node right = weight.remove();
                Node parent = new Node(' ', left.weight + right.weight, left, right);
                weight.add(parent);
                if (weight.size() == 1) {
                    root = weight.remove();
                    break;
                }
            }
            //build huffman dict
            buildHuffmanDict(root);
            return true;
        }

        private void buildHuffmanDict(Node root) {
            travelHuffmanTree(root, "", dict);
        }

        private void travelHuffmanTree(Node node, String suffix, Map<Character, String> dict) {
            if (node != null) {
                if (node.left == null && node.right == null) {
                    dict.put(node.data, suffix);
                }
                travelHuffmanTree(node.left, suffix + "0", dict);
                travelHuffmanTree(node.right, suffix + "1", dict);
            }
        }

        public String encode(String text) {
            if (root == null) {
                if (!buildHuffmanTree(text)) return text;
            }
            StringBuilder sb = new StringBuilder();
            for (char c : text.toCharArray()) {
                sb.append(dict.get(c));
            }
            return sb.toString();
        }

        public String decode(String text) {
            if (root == null || text.length() < 2) return text;
            StringBuilder sb = new StringBuilder();
            int i = 0;
            int j = 1;
            do {
                Character code = dict.inverse().get(text.substring(i, j));
                if (code != null) {
                    i = j;
                    sb.append(code);
                }
                j++;
            } while (i < text.length());
            return sb.toString();
        }
    }

    public static void main(String[] args) {
        Huffman huffman = new Huffman();
        String code = huffman.encode("abbccc");
        System.out.println(code);
        System.out.println(huffman.decode(code));
    }
}
