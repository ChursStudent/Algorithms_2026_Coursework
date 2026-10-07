package lab;

public class Edge {
    public int target;
    public int weight;
    public boolean isDAG;

    public Edge (int t, int w, boolean dag) {
        target = t;
        weight = w;
        isDAG = dag;
    }
}
