package lab;

import java.util.ArrayList;
import java.util.List;

public class Clique {
    public static List<Integer> getClique (int[][] M) {
        List<Integer> Clique = findMaximumClique(M);
        return Clique;
    }

    private static List<Integer> maxClique = new ArrayList<>();

    public static List<Integer> findMaximumClique(int[][] matrix) {
        int n = matrix.length;
        maxClique.clear();
        boolean[][] adj = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    if (matrix[i][i] == 1 && matrix[j][j] == 1) {
                        adj[i][j] = (matrix[i][j] == 1 && matrix[j][i] == 1);
                    }
                }
            }
        }
        List<Integer> currentClique = new ArrayList<>();
        backtrack(0, currentClique, adj, n);
        return maxClique;
    }

    private static void backtrack(int startVertex, List<Integer> currentClique, boolean[][] adj, int n) {
        if (currentClique.size() + (n - startVertex) <= maxClique.size()) {
            return;
        }
        if (currentClique.size() > maxClique.size()) {
            maxClique = new ArrayList<>(currentClique);
        }
        for (int i = startVertex; i < n; i++) {
            if (isValidCandidate(i, currentClique, adj)) {
                currentClique.add(i);
                backtrack(i + 1, currentClique, adj, n);
                currentClique.remove(currentClique.size() - 1);
            }
        }
    }

    private static boolean isValidCandidate(int vertex, List<Integer> currentClique, boolean[][] adj) {
        for (int u : currentClique) {
            if (!adj[vertex][u]) {
                return false;
            }
        }
        return true;
    }
}
