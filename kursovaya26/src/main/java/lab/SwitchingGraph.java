package lab;

import java.util.*;

public class SwitchingGraph {
    public Map<Integer, List<Edge>> edgeList = new HashMap<>();
    public int Size;

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

    public void changeEdgeWeight (int v, int t, int w) {
        if (edgeList.containsKey(v)) {
            for (int i = 0; i < edgeList.get(v).size(); i++) {
                if (edgeList.get(v).get(i).target == t && !edgeList.get(v).get(i).isDAG) {
                    edgeList.get(v).get(i).weight = w;
                    return;
                }
            }
        }

        System.out.println("Не удалось изменить вес ребра");
    }

    public void addSwitchingEdge (int u, int v) {
        edgeList.computeIfAbsent(u, k -> new ArrayList<>()).add(new Edge(v, 0, true));
    }

    public void clearGraph () {
        edgeList = new HashMap<>();
        Size = 0;
    }

    public void printGraph() {
        System.out.println("Edges:");
        for (int v : edgeList.keySet()) {
            System.out.print(v + ": ");
            for (Edge t : edgeList.get(v)) {
                if (t.isDAG) {
                    System.out.print("not real " + t.target + ", ");
                }
                else {
                    System.out.print("real " + t.target + " (" + t.weight + "), ");
                }
            }
            System.out.print("\n");
        }
    }

    public SwitchingGraph(DAGraph dagraph) {
        Size = dagraph.Size;

        for (int v : dagraph.Clusters.keySet()) {
            for (int t : dagraph.Clusters.get(v)) {
                addSwitchingEdge(v, t);
            }
        }
        int newV;
        int newT;
        for (int v : dagraph.Clusters.keySet()) {
            if (v >= Size) {
                newV = -v;
            }
            else {
                newV = v;
            }
            for (int t : dagraph.Clusters.get(v)) {
                if (t >= Size) {
                    newT = -t;
                }
                else {
                    newT = t;
                }
                addSwitchingEdge(newT, newV);
            }
        }
        for (int v : dagraph.TransitionEdges.keySet()) {
            if (v >= Size) {
                newV = -v;
            }
            else {
                newV = v;
            }
            for (Edge t : dagraph.TransitionEdges.get(v)) {
                addEdge(newV, t.target, t.weight);
            }
        }
    }

    public void DFS (int start) {
        System.out.println("DFS");
        Set<Integer> visited = new HashSet<>();
        dfsRecursion (start, visited);
        System.out.print("\n");
    }

    public void dfsRecursion (int current, Set<Integer> visited) {
        visited.add(current);
        if (Math.abs(current) < Size) {
            System.out.print(current + " ");
        }
        for (Edge out : edgeList.getOrDefault(current, Collections.emptyList())) {
            if (!visited.contains(out.target)) {
                dfsRecursion(out.target, visited);
            }
        }
    }

    public void BFS (int start) {
        System.out.println("BFS");
        Set<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();
        int current;
        visited.add(start);
        queue.add(start);
        while (!queue.isEmpty()) {
            current = queue.poll();
            if (Math.abs(current) < Size) {
                System.out.print(current + " ");
            }
            for (Edge out : edgeList.getOrDefault(current, Collections.emptyList())) {
                if (!visited.contains(out.target)) {
                    visited.add(out.target);
                    queue.add(out.target);
                }
            }
        }
        System.out.print("\n");
    }

    public int findMaxVertex() {
        int max = 0;
        for (int v : edgeList.keySet()) {
            if (v > max) {
                max = v;
            }
            for (Edge e : edgeList.get(v)) {
                if (e.target > max) {
                    max = e.target;
                }
            }
        }
        return max;
    }

    public static class NodeDistance implements Comparable<NodeDistance> {
        int node;
        int distance;

        public NodeDistance (int n, int d) {
            node = n;
            distance = d;
        }

        @Override
        public int compareTo (NodeDistance other) {
            return Integer.compare(distance, other.distance);
        }
    }
    public Map<Integer, Integer> dijkstra (int source) {
        Map<Integer, Integer> distances = new HashMap<>();
        NodeDistance current;
        int u;
        int v;
        int w;
        int maxVertex = findMaxVertex();
        for (int i = -maxVertex; i <= -Size; i++) {
            distances.put(i, Integer.MAX_VALUE);
        }
        for (int i = 0; i <= maxVertex; i++) {
            distances.put(i, Integer.MAX_VALUE);
        }
        distances.replace(source, 0);
        PriorityQueue<NodeDistance> pQ = new PriorityQueue<>();
        pQ.add(new NodeDistance(source, 0));
        while (!pQ.isEmpty()) {
            current = pQ.poll();
            u = current.node;
            if (current.distance > distances.get(u)) {
                continue;
            }
            if (edgeList.containsKey(u)) {
                for (Edge e : edgeList.get(u)) {
                    v = e.target;
                    w = e.weight;
                    if (distances.get(u) != Integer.MAX_VALUE && distances.get(u) + w < distances.get(v)) {
                        distances.replace(v, distances.get(u) + w);
                        pQ.add(new NodeDistance(v, distances.get(v)));
                    }
                }
            }
        }
        return distances;
    }

}
