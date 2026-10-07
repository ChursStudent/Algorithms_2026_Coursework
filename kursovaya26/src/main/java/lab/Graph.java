package lab;


import java.util.*;

import static java.lang.Math.random;
import static lab.BicliqueFind.getBiclique;
import static lab.Clique.getClique;
import static lab.probability.prob;

public class Graph {
    public Map<Integer, List<Edge>> edgeList = new HashMap<>();
    public int Size = 0;

    public Graph(int[][] M1) {
        for (int i = 0; i < M1.length; i++) {
            for (int j = 0; j < M1[0].length; j++) {
                if (M1[i][j] != 0) {
                    addEdge(i, j);
                }
            }
        }
        Size = M1.length;
    }

    public Graph() {    }

    public void addEdge (int u, int v) {
        edgeList.computeIfAbsent(u, k -> new ArrayList<>()).add(new Edge(v, 1, false));
    }

    public void addEdge (int u, int v, int w) {
        edgeList.computeIfAbsent(u, k -> new ArrayList<>()).add(new Edge(v, w, false));
    }

    public void deleteEdge (int u, int v) {
        for (Edge out : edgeList.getOrDefault(u, Collections.emptyList())) {
            if (out.target == v) {
                edgeList.get(u).remove(out);
            }
        }
    }

    public boolean has (int u) {
        for (int vertex : edgeList.keySet()) {
            if (vertex == u) {
                return true;
            }
            for (Edge out : edgeList.getOrDefault(vertex, Collections.emptyList())) {
                if (out.target == u) {
                    return true;
                }
            }
        }
        return false;
    }

    public void clearGraph () {
        edgeList = new HashMap<>();
        Size = 0;
    }

    public int[][] getMatrix() {
        int[][] M = new int[Size][Size];
        for (int v : edgeList.keySet()) {
            for (Edge out : edgeList.getOrDefault(v, Collections.emptyList())) {
                M[v][out.target] = 1;
            }
        }
        return M;
    }

    public void generate (int size, char type, int maxWeight) {
        double koeff;
        int random;
        clearGraph();
        if (type == 'r') {
            koeff = 1.0 / size;
        }
        else {
            koeff = 1.0 / 2.0;
        }
        for (int u = 0; u < size; u++) {
            for (int v = 0; v < size; v++) {
                if (prob(koeff)) {
                    if (maxWeight < 1) {
                        addEdge(u, v);
                    }
                    else {
                        addEdge(u, v, (int)(1.0/maxWeight+random()*(maxWeight - 1)/maxWeight)*maxWeight);
                    }
                }
            }
        }
        for (int u = 0; u < size; u++) {
            if (!has(u)) {
                do {
                    random = (int) (random() * (size - 1));
                }
                while (random == u);

                if (prob(0.5)) {
                    addEdge (u, random);
                }
                else{
                    addEdge(random, u);
                }
            }
        }
        Size = size;
    }

    public static List<List<Integer>> getMicroClusters(int[][] M) {
        List<List<Integer>> clusters = new ArrayList<>();
        List<Integer> clique;
        List<Integer> loopVertices = new ArrayList<>();
        for (int v = 0; v < M.length; v++) {
            if (M[v][v] == 1) {
                loopVertices.add(v);
            }
        }
        int[][] M2 = new int[loopVertices.size()][loopVertices.size()];
        int newV = 0;
        int newT;
        boolean foundLoop;
        for (int v = 0; v < M.length; v++) {
            foundLoop = false;
            newT = 0;
            for (int t = 0; t < M[0].length; t++) {
                if (M[v][v] == 1 && M[t][t] == 1) {
                    M2[newV][newT] = M[v][t];
                    newT++;
                    foundLoop = true;
                }
            }
            if (foundLoop) {
                newV++;
            }
        }

        while (true) {
            clique = getClique(M2);
            if (clique.size() <= 1){
                break;
            }
            else {
                clusters.add(new ArrayList<>(clique));
                for (int v : clique) {
                    for (int t : clique) {
                        M2[v][t] = 0;
                    }
                }
            }
        }
        List<List<Integer>> origClusters = new ArrayList<>();
        List<Integer> origCluster;
        for (List<Integer> c : clusters) {
            origCluster = new ArrayList<>();
            for (int v : c) {
                origCluster.add(loopVertices.get(v));
            }
            origClusters.add(origCluster);
        }
        return origClusters;
    }

    public record Pair<V, T>(V verticesA, T verticesB) {
    }

    public DAGraph DAGCompression () {
        DAGraph CompressedG = new DAGraph();
        int newVertex;
        CompressedG.Size = Size;
        int[][] G = getMatrix();
        List<List<Integer>> microClusters = getMicroClusters(G);
        for (List<Integer> cluster : microClusters) {
            for (int v : cluster) {
                for (int t : cluster) {
                    G[v][t] = 0;
                }
            }
            newVertex = CompressedG.insertDagVertex(cluster);
            CompressedG.addEdge(newVertex, newVertex);
            CompressedG.Clusters.put(newVertex, cluster);
        }
        Pair<Set<Integer>, Set<Integer>> bilobate = getBiclique(G);
        int A;
        int B;
        while (bilobate != null) {
            A = CompressedG.insertDagVertex(new ArrayList<>(bilobate.verticesA));
            B = CompressedG.insertDagVertex(new ArrayList<>(bilobate.verticesB));
            CompressedG.addEdge(A, B);
            for (int v : bilobate.verticesA) {
                for (int t : bilobate.verticesB) {
                    G[v][t] = 0;
                }
            }
            bilobate = getBiclique(G);
        }
        for (int v = 0; v < Size; v++) {
            for (int t = 0; t < Size; t++) {
                if (G[v][t] != 0) {
                    CompressedG.addEdge(v, t);
                }
            }
        }
        return CompressedG;
    }

    public static void main(String[] args) {
        int[][] M2 = {
                {0, 1, 0, 0, 0, 1, 0, 0, 0, 0},
                {1, 0, 1, 1, 1, 0, 0, 0, 1, 0},
                {0, 1, 0, 1, 0, 0, 0, 1, 0, 1},
                {0, 1, 1, 1, 1, 1, 0, 0, 1, 0},
                {0, 0, 0, 1, 0, 1, 0, 0, 0, 1},
                {1, 1, 1, 0, 0, 0, 0, 1, 0, 0},
                {1, 0, 0, 1, 0, 1, 0, 0, 1, 0},
                {0, 0, 1, 1, 1, 1, 1, 0, 1, 1},
                {0, 0, 0, 1, 1, 1, 1, 1, 0, 1},
                {0, 0, 1, 1, 0, 0, 1, 0, 1, 0},
        };
        Graph G2 = new Graph(M2);
        DAGraph Dag2 = G2.DAGCompression();
        Dag2.printGraph();
        SwitchingGraph Sw2 = new SwitchingGraph(Dag2);
        Sw2.printGraph();
        System.out.println("Sw2 size = " + Sw2.Size);
        Sw2.DFS(1);
        Sw2.BFS(1);
        Map<Integer, Integer> dijkstraDistances = Sw2.dijkstra(2);
        System.out.println("Расстояния, найденные алгоритмом Дейкстры  от вершины " + 2);
        for (int t = 0; t < Sw2.Size; t++) {
            System.out.println(t + 1 + ": " + dijkstraDistances.get(t));
        }
        int[][] M1 = {
                { 0, 0, 0, 1, 0, 0, 0, 0, 0},
                { 0, 0, 0, 1, 1, 0, 0, 0, 0},
                { 0, 0, 0, 1, 1, 0, 0, 0, 0},
                { 0, 0, 0, 0, 0, 0, 0, 0, 0},
                { 0, 0, 0, 0, 0, 0, 0, 1, 1},
                { 0, 0, 0, 0, 0, 0, 0, 1, 1},
                { 0, 0, 0, 1, 1, 1, 0, 0, 0},
                { 0, 0, 0, 0, 0, 0, 0, 0, 0},
                { 0, 0, 0, 0, 0, 0, 0, 0, 0},
        };
        Graph G1 = new Graph(M1);
        DAGraph Dag1 = G1.DAGCompression();
        Dag1.printGraph();
        SwitchingGraph Sw1 = new SwitchingGraph(Dag1);
        Sw1.printGraph();
        System.out.println("Sw1 size = " + Sw1.Size);
        Sw1.DFS(7);
        Sw1.BFS(7);
    }
}
