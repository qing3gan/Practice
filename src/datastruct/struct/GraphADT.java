package datastruct.struct;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 图（顶点，边：邻接表）
 */
public class GraphADT {

    /**
     * 无权图
     */
    private int v;//vertex number
    private LinkedList<Integer>[] adj;//adjacency list

    public GraphADT(int v) {
        this.v = v;
        adj = new LinkedList[v];
        for (int i = 0; i < v; i++) {
            adj[i] = new LinkedList<>();
        }
        eadj = new LinkedList[v];
        for (int i = 0; i < v; i++) {
            eadj[i] = new LinkedList<>();
        }
    }

    public void addDirectEdge(int s, int t) {//direct graph(start,terminal)
        adj[s].add(t);
    }

    public void addNonDirectEdge(int s, int t) {//non-direct graph(start,terminal)
        adj[s].add(t);
        adj[t].add(s);
    }

    public void bfs(int s, int t) {//breadth-first-search(start,terminal)
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
        int[] prev = new int[v];//preview vertex(s -> t reverse path, prev[t]=s)
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

    /**
     * 有权图
     */
    private class Edge {
        public int sid;//start
        public int tid;//terminal
        public int w;//weight

        public Edge(int sid, int tid, int w) {
            this.sid = sid;
            this.tid = tid;
            this.w = w;
        }
    }

    private class Vertex {//for dijkstra
        public int id;//vertex
        public int dist;//distance

        public Vertex(int id, int dist) {
            this.id = id;
            this.dist = dist;
        }
    }

    private LinkedList<Edge>[] eadj;

    public void addEdge(int s, int t, int w) {//direct weight graph(start,terminal,weight)
        this.eadj[s].add(new Edge(s, t, w));
    }

    private class PriorityUpdateQueue {
        private Vertex[] nodes;//vertex.dist small top heap
        private int capacity;//bounded
        private int size;

        public PriorityUpdateQueue(int capacity) {
            nodes = new Vertex[capacity + 1];
            this.capacity = capacity;
            size = 0;
        }

        public void add(Vertex vertex) {
            if (size >= capacity) return;
            nodes[++size] = vertex;
            heapifyDownToUp(size);
        }

        public Vertex poll() {
            if (size == 0) return null;
            Vertex vertex = nodes[1];
            nodes[1] = nodes[size--];
            heapifyUpToDown(size);
            return vertex;
        }

        public void update(Vertex vertex) {
            for (int i = 1; i <= size; i++) {
                if (nodes[i].id == vertex.id) {
                    nodes[i].dist = vertex.dist;
                    heapifyDownToUp(i);
                    break;
                }
            }
        }

        public boolean isEmpty() {
            return size == 0;
        }

        private void heapifyUpToDown(int size) {
            //from up to down heapify
            int index = 1;
            while (true) {//compare with left and right
                int minPos = index;
                if (2 * index <= size && nodes[2 * index] != null && nodes[index].dist > nodes[2 * index].dist)
                    minPos = 2 * index;//left
                if (2 * index + 1 <= size && nodes[2 * index + 1] != null && nodes[index].dist > nodes[2 * index + 1].dist)
                    minPos = 2 * index + 1;//right
                if (minPos == index) break;//smallest
                swap(nodes, index, minPos);
                index = minPos;
            }
        }

        private void heapifyDownToUp(int size) {
            //heapify from down to up
            while (size / 2 > 0 && nodes[size].dist < nodes[size / 2].dist) {//compare with parent
                swap(nodes, size, size / 2);
                size = size / 2;
            }
        }

        private void swap(Vertex[] a, int i, int j) {
            Vertex tmp = a[i];
            a[i] = a[j];
            a[j] = tmp;
        }
    }

    public void dijkstra(int s, int t) {//from s to t shortest path
        int[] prev = new int[v];//preview vertex(s -> t reverse path, prev[t]=s)
        Vertex[] vertexes = new Vertex[v];//update s -> i shortest distance
        for (int i = 0; i < v; i++) {
            vertexes[i] = new Vertex(i, Integer.MAX_VALUE);//init s -> i distance
        }
        PriorityUpdateQueue queue = new PriorityUpdateQueue(v);//dynamic programing
        boolean[] inqueue = new boolean[v];//avoid requeue
        vertexes[s].dist = 0;//init s -> s distance
        queue.add(vertexes[s]);
        inqueue[s] = true;
        while (!queue.isEmpty()) {//traverse vertex
            Vertex minVertex = queue.poll();
            if (minVertex.id == t) break;
            for (int i = 0; i < eadj[minVertex.id].size(); i++) {
                //minVertex -> edge -> nextVertex
                Edge edge = eadj[minVertex.id].get(i);
                Vertex nextVertex = vertexes[edge.tid];
                //traverse edge
                if (minVertex.dist + edge.w < nextVertex.dist) {
                    nextVertex.dist = minVertex.dist + edge.w;
                    prev[nextVertex.id] = minVertex.id;
                    if (inqueue[nextVertex.id]) {
                        queue.update(nextVertex);
                    } else {
                        queue.add(nextVertex);
                        inqueue[nextVertex.id] = true;
                    }
                }
            }
        }
        printPath(prev, s, t);
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
        /*graph.addDirectEdge(0, 2);
        graph.addDirectEdge(2, 1);
        graph.addDirectEdge(2, 4);
        graph.addDirectEdge(3, 2);
        graph.addDirectEdge(5, 4);
        graph.topoSortByKahn();
        System.out.println();
        graph.topoSortByDfs();*/
        graph.addEdge(0, 1, 4);
        graph.addEdge(0, 2, 2);
        graph.addEdge(1, 3, 1);
        graph.addEdge(2, 1, 1);
        graph.addEdge(2, 3, 2);
        graph.addEdge(3, 4, 1);
        graph.addEdge(3, 5, 3);
        graph.addEdge(4, 5, 3);
        graph.dijkstra(0, 5);
    }
}
