package struct;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 图（邻接矩阵，邻接表）
 */
public class GraphADT {

    private int v;//vertex number
    private LinkedList<Integer>[] adj;//adjacency list

    public GraphADT(int v) {
        this.v = v;
        adj = new LinkedList[v];
        for (int i = 0; i < v; i++) {
            adj[i] = new LinkedList<>();
        }
    }

    public void addEdge(int s, int t) {//non-direct graph(start,end)
        adj[s].add(t);
        adj[t].add(s);
    }

    public void bfs(int s, int t) {//breadth-first-search(start,end)
        if (s == t) return;//find
        //init
        boolean[] visited = new boolean[v];//visited vertex
        visited[s] = true;//avoid duplicate access
        Queue<Integer> queue = new LinkedList<>();//record walk
        queue.add(s);
        int[] prev = new int[v];//preview vertex(s -> t reverse path)
        for (int i = 0; i < v; i++) {
            prev[i] = -1;
        }
        //search
        while (queue.size() != 0) {
            int w = queue.poll();//walk
            for (int i = 0; i < adj[w].size(); i++) {
                int q = adj[w].get(i);//breadth
                if (!visited[q]) {
                    prev[q] = w;//reverse path
                    if (q == t) {//find
                        printPath(prev, s, t);
                        return;
                    }
                    visited[q] = true;
                    queue.add(q);
                }
                //do nothing
            }
        }
    }

    private boolean found = false;//depth found

    public void dfs(int s, int t) {//depth-first-search
        if (s == t) return;//find
        //init
        found = false;
        boolean[] visited = new boolean[v];//visited vertex
        int[] prev = new int[v];//preview vertex(s -> t reverse path)
        for (int i = 0; i < v; i++) {
            prev[i] = -1;
        }
        //search
        recurDfs(s, t, visited, prev);
        printPath(prev, s, t);
    }

    private void recurDfs(int w, int t, boolean[] visited, int[] prev) {
        if (found) return;
        visited[w] = true;//avoid duplicate access
        if (w == t) {//find
            found = true;
            return;
        }
        for (int i = 0; i < adj[w].size(); i++) {
            int q = adj[w].get(i);//depth
            if (!visited[q]) {
                prev[q] = w;//reverse path
                recurDfs(q, t, visited, prev);//recurse search
            }
        }
    }

    private void printPath(int[] prev, int s, int t) {
        if (prev[t] != -1 && t != s) {
            printPath(prev, s, prev[t]);
        }
        System.out.print(t + "->");
    }

    public static void main(String[] args) {
        GraphADT graph = new GraphADT(6);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 3);
        graph.addEdge(1, 5);
        graph.addEdge(2, 4);
        graph.bfs(0, 5);
        System.out.println();
        graph.dfs(0, 4);
    }
}
