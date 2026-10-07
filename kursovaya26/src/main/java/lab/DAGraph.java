package lab;

import java.util.*;
import java.util.stream.Collectors;

public class DAGraph {
    Map<Integer, List<Integer>> Clusters = new HashMap<>();
    Map<Integer, List<Edge>> TransitionEdges = new HashMap<>();
    int Size = 0;

    public void clearGraph () {
        Clusters = new HashMap<>();
        TransitionEdges = new HashMap<>();
        Size = 0;
    }

    public void printGraph() {
        System.out.println("Clusters:");
        for (int v : Clusters.keySet()) {
            System.out.print(v + ": ");
            for (int t : Clusters.get(v)) {
                System.out.print(t + " ");
            }
            System.out.print("\n");
        }
        System.out.println("TransitionEdges:");
        for (int v : TransitionEdges.keySet()) {
            System.out.print(v + ": ");
            for (Edge t : TransitionEdges.get(v)) {
                System.out.print(t.target + " ");
            }
            System.out.print("\n");
        }
    }

    public int insertDagVertex(List<Integer> newC) {
        if (newC.size() == 1) {
            return newC.getFirst();
        }
        List<Integer> cluster;
        Map<Integer, List<Integer>> matching = new HashMap<>() ;
        Map<Integer, List<Integer>> matching2 = new HashMap<>() ;
        int matched = 0;
        int matchedCount;
        for (int num : Clusters.keySet()) {
            cluster = Clusters.get(num) ;
            matchedCount = 0;
            for (int v : newC) {
                if (cluster.contains(v)) {
                    matchedCount++;
                }
                else {
                    break;
                }
            }
            if (matchedCount == newC.size()) {
                matching.put(num, cluster);
                if (cluster.size() == newC.size()) {
                    matched = num;
                    return matched;
                }
            }
            else if (matchedCount == cluster.size()) {
                matching2.put(num, cluster);
            }
        }
        int newVertex;
        if (!matching.isEmpty()) {
            boolean isFirst = true;
            for (int num : matching.keySet()) {
                if (isFirst) {
                    matched = num;
                    isFirst = false;
                }
                else if (Clusters.get(num).size() < Clusters.get(matched).size()) {
                    matched = num;
                }
            }
            for (int v : newC) {
                Clusters.get(matched).remove((Integer) v);
            }
            newVertex = Size+Clusters.size();
            Clusters.get(matched).add(newVertex);
            Clusters.put(newVertex, newC);
            return newVertex;
        }
        else if (!matching2.isEmpty()) {
            List<Integer> newC2 = new ArrayList<>();
            List<Integer> sortedMatch2 = matching2.entrySet().stream()
                    .sorted(Comparator.comparingInt(entry -> entry.getValue().size()))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            boolean dagVisHelpful;
            for (int dagV : sortedMatch2) {
                if (newC.isEmpty()) {
                    break;
                }
                dagVisHelpful = false;
                for (int t : matching2.get(dagV)) {
                    if (newC.contains(t)) {
                        newC.remove((Integer) t);
                        dagVisHelpful = true;
                    }
                }
                if (dagVisHelpful) {
                    newC2.add(dagV);
                }
            }
            if (!newC.isEmpty()) {
                newC2.addAll(newC);
                newC.clear();
            }
            newVertex = Size+Clusters.size();
            Clusters.put(newVertex, newC2);
            return newVertex;
        }
        else {
            newVertex = Size+Clusters.size();
            Clusters.put(newVertex, newC);
            return newVertex;
        }
    }

    public void addEdge (int u, int v) {
        TransitionEdges.computeIfAbsent(u, k -> new ArrayList<>()).add(new Edge(v, 1, false));
    }

}
