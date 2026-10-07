package lab;

import javax.swing.*;
import java.awt.*;

public class visualizeArrow implements DrawableShape {
    private int x1;
    private int y1;
    private int x2;
    private int y2;
    private Color col;
    public visualizeArrow(double X1, double Y1, double X2, double Y2, Color c) {
        x1 = (int)X1;
        y1 = (int)Y1;
        x2 = (int)X2;
        y2 = (int)Y2;
        col = c;
    }
    @Override
    public void draw (Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(1.0f));
        g2d.setColor(col);
        g2d.drawLine(x1, y1, x2, y2);
        double dx = x2 - x1;
        double dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        double arrowRad = Math.toRadians(20);
        int arrowSize = (int)Math.min(Math.sqrt(Math.pow(dx, 2)+ Math.pow(dy, 2)) / 3, 10);
        int x3 = (int) (x2 - arrowSize * Math.cos(angle - arrowRad));
        int y3 = (int) (y2 - arrowSize * Math.sin(angle - arrowRad));
        int x4 = (int) (x2 - arrowSize * Math.cos(angle + arrowRad));
        int y4 = (int) (y2 - arrowSize * Math.sin(angle + arrowRad));
        g2d.drawLine(x2,y2,x3,y3);
        g2d.drawLine(x2,y2,x4,y4);
    }
}
