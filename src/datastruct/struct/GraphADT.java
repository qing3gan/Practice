package datastruct.struct;

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

    public void addDirectEdge(int s, int t) {
        adj[s].add(t);
    }

    public void addNonDirectEdge(int s, int t) {//non-direct graph(start,end)
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

    /**
     * 有向无环图，拓扑排序
     * <p>
     * 至少有一个入度为0的顶点
     */
    public void topoSortByKahn() {
        int[] inDegree = new int[v];
        //in degree+
        for (int i = 0; i < v; i++) {
            for (int j = 0; j < adj[i].size(); j++) {
                int w = adj[i].get(j);//i->w
                inDegree[w]++;
            }
        }
        //0 in degree
        LinkedList<Integer> queue = new LinkedList<>();
        for (int i = 0; i < v; i++) {
            if (inDegree[i] == 0) {
                queue.add(i);
            }
        }
        //in degree-
        while (!queue.isEmpty()) {
            int i = queue.remove();
            System.out.print("->" + i);
            for (int j = 0; j < adj[i].size(); j++) {
                int w = adj[i].get(j);
                inDegree[w]--;
                if (inDegree[w] == 0) {
                    queue.add(w);
                }
            }
        }
    }

    /**
     * 逆邻接表
     */
    public void topoSortByDfs() {
        //inverse adjacency list
        LinkedList<Integer>[] inverseAdj = new LinkedList[v];
        for (int i = 0; i < v; i++) {
            inverseAdj[i] = new LinkedList<>();
        }
        for (int i = 0; i < v; i++) {
            for (int j = 0; j < adj[i].size(); j++) {
                int w = adj[i].get(j);//i->w
                inverseAdj[w].add(i);//w->i
            }
        }
        //dfs
        boolean[] visited = new boolean[v];
        for (int i = 0; i < v; i++) {
            if (!visited[i]) {
                visited[i] = true;
                topoDfs(i, inverseAdj, visited);
            }
        }
    }

    private void topoDfs(int vertex, LinkedList<Integer>[] inverse, boolean[] visited) {
        for (int i = 0; i < inverse[vertex].size(); i++) {
            int w = inverse[vertex].get(i);
            if (visited[w]) {
                continue;
            }
            visited[w] = true;
            topoDfs(w, inverse, visited);
        }
        System.out.print("->" + vertex);
    }

    public static void main(String[] args) {
        GraphADT graph = new GraphADT(6);
        /*graph.addNonDirectEdge(0, 1);
        graph.addNonDirectEdge(0, 2);
        graph.addNonDirectEdge(1, 3);
        graph.addNonDirectEdge(2, 3);
        graph.addNonDirectEdge(1, 5);
        graph.addNonDirectEdge(2, 4);
        graph.bfs(0, 5);
        System.out.println();
        graph.dfs(0, 4);*/
        graph.addDirectEdge(0, 2);
        graph.addDirectEdge(2, 1);
        graph.addDirectEdge(2, 4);
        graph.addDirectEdge(3, 2);
        graph.addDirectEdge(5, 4);
        graph.topoSortByKahn();
        System.out.println();
        graph.topoSortByDfs();
    }
}
