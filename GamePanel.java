import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    static final int CELL_SIZE = 28;
    static final int GRID_SIZE = 20;
    static final int SCREEN_SIZE = CELL_SIZE * GRID_SIZE;

    public GamePanel() {
        setPreferredSize(new Dimension(SCREEN_SIZE, SCREEN_SIZE));
        setBackground(new Color(18, 18, 18));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid(g);
    }

    private void drawGrid(Graphics g) {
        g.setColor(new Color(30, 30, 30));
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                g.drawRect(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
    }
}