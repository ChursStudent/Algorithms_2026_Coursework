package lab;

import javax.swing.*;

public class ShowGraph extends JFrame {
    public ShowGraph (Graph G) {
        JFrame frame = new JFrame("Graph");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        visualizeGraph drawings = new visualizeGraph(G, 600, 600);
        frame.add(drawings);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    public ShowGraph (DAGraph G) {
        JFrame frame = new JFrame("DagCompressedGraph");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        visualizeDagGraph drawings = new visualizeDagGraph(G, 600, 600);
        frame.add(drawings);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    public static void showG(Graph G) {
        SwingUtilities.invokeLater(() -> new ShowGraph(G));
    }
    public static void showDAG(DAGraph G) {
        SwingUtilities.invokeLater(() -> new ShowGraph(G));
    }
}
