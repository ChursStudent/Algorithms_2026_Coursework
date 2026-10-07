package lab;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class visualizeGraph extends JPanel {
    private List<DrawableShape> shapes = new ArrayList<>();

    public visualizeGraph(Graph G, int W_width, int W_height) {
        int cx = W_width / 2;
        int cy = W_height / 2;
        int x, y;
        int n = G.Size;
        double rStep = (double) (W_width - 20) / 2 / n;
        double aStep = Math.toRadians(75);
        double radius = 0;
        double angle = 0;
        List<int[]> Coords = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int[] XY = new int[2];
            x = (int)(cx + radius*Math.cos(angle));
            y = (int)(cy - radius*Math.sin(angle));
            XY[0] = x;
            XY[1] = y;
            Coords.add(XY);
            radius += rStep;
            angle += aStep;
        }
        for (int i = 0; i < n; i++) {
            for (Edge e : G.edgeList.getOrDefault(i, Collections.emptyList())) {
                if (e.target == i) {
                    shapes.add(new visualizeLoop(Coords.get(i)[0], Coords.get(i)[1], Color.CYAN));
                } else {
                    shapes.add(new visualizeArrow(Coords.get(i)[0], Coords.get(i)[1], Coords.get(e.target)[0], Coords.get(e.target)[1], Color.CYAN));
                }
            }
        }
        for (int i = 0; i < n; i++) {
            shapes.add(new visualizeCircle(Coords.get(i)[0], Coords.get(i)[1], Color.BLACK));
            shapes.add(new visualizeText("" + i, Coords.get(i)[0], Coords.get(i)[1], Color.BLACK));
        }
        setPreferredSize(new Dimension(600, 600));
        setBackground(Color.GRAY);
    }
    @Override
    protected void paintComponent (Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        for (DrawableShape shape : shapes) {
            shape.draw(g2d);
        }
    }
}
