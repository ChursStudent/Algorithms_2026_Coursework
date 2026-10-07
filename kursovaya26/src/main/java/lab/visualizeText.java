package lab;

import java.awt.*;

public class visualizeText implements DrawableShape {
    private int x;
    private int y;
    private String text;
    private Color col;
    visualizeText (String Text, double X, double Y, Color color) {
        x = ((int) X);
        y = ((int) Y) - 5;
        text = Text;
        col = color;
    }
    @Override
    public void draw (Graphics2D g2d) {
        g2d.setFont(new Font("Serif", Font.BOLD, 16));
        g2d.setColor(col);
        g2d.drawString(text, x, y);
    }
}
