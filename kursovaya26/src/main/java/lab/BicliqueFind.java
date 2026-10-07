package lab;

import java.util.HashSet;
import java.util.Set;

public class BicliqueFind {

    public static class Biclique {
        public Set<Integer> U = new HashSet<>();
        public Set<Integer> V = new HashSet<>();
        public int edgeCount = 0;

        public Biclique() {}

        public Biclique(Set<Integer> U, Set<Integer> V) {
            this.U = new HashSet<>(U);
            this.V = new HashSet<>(V);
            this.edgeCount = U.size() * V.size();
        }

        @Override
        public String toString() {
            return "Максимальный полный двудольный подграф:\n" +
                    "Доля U (Источники): " + U + "\n" +
                    "Доля V (Стоки):    " + V + "\n" +
                    "Количество рёбер:   " + edgeCount;
        }
    }

    private final int[][] adjMatrix;
    private final int numVertices;
    private Biclique bestBiclique;

    public BicliqueFind(int[][] adjMatrix) {
        this.adjMatrix = adjMatrix;
        this.numVertices = adjMatrix.length;
        this.bestBiclique = new Biclique();
    }

    public Biclique findMaxBiclique() {
        Set<Integer> currentU = new HashSet<>();
        Set<Integer> initialCandidatesV = new HashSet<>();
        for (int i = 0; i < numVertices; i++) {
            initialCandidatesV.add(i);
        }
        backtrack(0, currentU, initialCandidatesV);
        return bestBiclique;
    }

    private void backtrack(int uIndex, Set<Integer> currentU, Set<Integer> candidatesV) {
        int remainingVertices = numVertices - uIndex;
        int maxPossibleEdges = (currentU.size() + remainingVertices) * candidatesV.size();
        if (maxPossibleEdges <= bestBiclique.edgeCount) {
            return;
        }
        if (uIndex == numVertices) {
            int currentEdges = currentU.size() * candidatesV.size();
            if (currentEdges > bestBiclique.edgeCount) {
                bestBiclique = new Biclique(currentU, candidatesV);
            }
            return;
        }
        Set<Integer> nextCandidatesV = new HashSet<>();
        for (int v : candidatesV) {
            if (adjMatrix[uIndex][v] != 0) {
                nextCandidatesV.add(v);
            }
        }
        if (!nextCandidatesV.isEmpty() || currentU.isEmpty()) {
            currentU.add(uIndex);
            backtrack(uIndex + 1, currentU, nextCandidatesV);
            currentU.remove(uIndex);
        }
        backtrack(uIndex + 1, currentU, candidatesV);
    }

    public static Graph.Pair<Set<Integer>, Set<Integer>> getBiclique(int[][] M) {
        BicliqueFind finder = new BicliqueFind(M);
        Biclique result = finder.findMaxBiclique();
        Set<Integer> A = result.U;
        Set<Integer> B = result.V;
        Graph.Pair<Set<Integer>,Set<Integer>> BestBiclique = new Graph.Pair<>(A, B);
        if (A.size() > 1 || B.size() > 1) {
            return BestBiclique;
        }
        else {
            return null;
        }
    }
}
