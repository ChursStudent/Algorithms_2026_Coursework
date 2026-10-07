package lab;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class visualizeCircle implements DrawableShape {
    private int x;
    private int y;
    private Color col;
    visualizeCircle (double X, double Y, Color c) {
        x = ((int) X) - 3;
        y = ((int) Y) - 3;
        col = c;
    }
    @Override
    public void draw (Graphics2D g2d) {
        g2d.setColor(col);
        g2d.fill(new Ellipse2D.Double(x, y, 6, 6));
    }
}
