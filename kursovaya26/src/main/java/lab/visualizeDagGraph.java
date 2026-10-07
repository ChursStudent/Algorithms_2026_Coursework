package lab;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class visualizeDagGraph extends JPanel {
    private List<DrawableShape> shapes = new ArrayList<>();

    public visualizeDagGraph(DAGraph G, int W_width, int W_height) {
        int cx = W_width / 2;
        int cy = W_height / 2;
        int x, y;
        int nDag = G.Clusters.size();
        int nReal = G.Size;
        int radiusDag = cy/3;
        int radiusReal = cy*2/3;
        Map<Integer, int[]> CoordsDag = new HashMap<>();
        Map<Integer, int[]> CoordsReal = new HashMap<>();
        double angle = 0;
        double angleDagStep = Math.toRadians(360.0 / nDag);
        double angleRealStep = Math.toRadians(360.0 / nReal);
        for (int v : G.Clusters.keySet()) {
            int[] XY = new int[2];
            x = (int)(cx + radiusDag*Math.cos(angle));
            y = (int)(cy - radiusDag*Math.sin(angle));
            XY[0] = x;
            XY[1] = y;
            CoordsDag.put(v, XY);
            angle += angleDagStep;
        }
        for (int v : G.Clusters.keySet()) {
            for (int t : G.Clusters.get(v)) {
                if (!CoordsReal.containsKey(t) && !G.Clusters.containsKey(t)) {
                    int[] XY = new int[2];
                    x = (int) (cx + radiusReal * Math.cos(angle));
                    y = (int) (cy - radiusReal * Math.sin(angle));
                    XY[0] = x;
                    XY[1] = y;
                    CoordsReal.put(t, XY);
                    angle += angleRealStep;
                }
            }
        }
        if (CoordsReal.size() < nReal) {
            System.out.println("есть вершины вне кластеров");
            for (int i = 0; i < nReal; i++) {
                if (!CoordsReal.containsKey(i)) {
                    int[] XY = new int[2];
                    x = (int) (cx + radiusReal * Math.cos(angle));
                    y = (int) (cy - radiusReal * Math.sin(angle));
                    XY[0] = x;
                    XY[1] = y;
                    CoordsReal.put(i, XY);
                    angle += angleRealStep;
                }
            }
        }
        for (int v : CoordsDag.keySet()) {
            for (int t : G.Clusters.get(v)) {
                if (t >= nReal) {
                    shapes.add(new visualizeArrow(CoordsDag.get(v)[0], CoordsDag.get(v)[1], CoordsDag.get(t)[0], CoordsDag.get(t)[1], Color.PINK));
                }
                else {
                    shapes.add(new visualizeArrow(CoordsDag.get(v)[0], CoordsDag.get(v)[1], CoordsReal.get(t)[0], CoordsReal.get(t)[1], Color.PINK));
                }
            }
        }
        int vX, vY, tX, tY;
        for (int v : G.TransitionEdges.keySet()) {
            for (Edge e : G.TransitionEdges.get(v)) {
                if (v >= nReal) {
                    vX = CoordsDag.get(v)[0];
                    vY = CoordsDag.get(v)[1];
                }
                else {
                    vX = CoordsReal.get(v)[0];
                    vY = CoordsReal.get(v)[1];
                }
                if (e.target >= nReal) {
                    tX = CoordsDag.get(e.target)[0];
                    tY = CoordsDag.get(e.target)[1];
                }
                else {
                    tX = CoordsReal.get(e.target)[0];
                    tY = CoordsReal.get(e.target)[1];
                }
                if (e.target == v) {
                    shapes.add(new visualizeLoop(vX, vY, Color.CYAN));
                }
                else {
                    shapes.add(new visualizeArrow(vX, vY, tX, tY, Color.CYAN));
                }
            }
        }
        for (int v : CoordsReal.keySet()) {
            shapes.add(new visualizeCircle(CoordsReal.get(v)[0], CoordsReal.get(v)[1], Color.BLUE));
            shapes.add(new visualizeText("" + v, CoordsReal.get(v)[0], CoordsReal.get(v)[1], Color.CYAN));
        }
        for (int v : CoordsDag.keySet()) {
            shapes.add(new visualizeCircle(CoordsDag.get(v)[0], CoordsDag.get(v)[1], Color.RED));
            shapes.add(new visualizeText("" + v, CoordsDag.get(v)[0], CoordsDag.get(v)[1], Color.ORANGE));
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
