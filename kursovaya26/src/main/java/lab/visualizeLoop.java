package lab;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class visualizeLoop implements DrawableShape{
    private int x;
    private int y;
    private Color col;
    public visualizeLoop(double X, double Y, Color c) {
        x = (int)X;
        y = (int)Y;
        col = c;
    }
    @Override
    public void draw (Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(1.0f));
        g2d.setColor(col);
        g2d.draw(new Ellipse2D.Double(x - 10, y - 20, 20, 20));
        g2d.drawLine(x, y, x - 5, y - 5);
        g2d.drawLine(x, y, x - 7, y + 2);
    }
}
