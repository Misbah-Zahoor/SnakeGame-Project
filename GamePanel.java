import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    static final int CELL_SIZE = 28;
    static final int GRID_SIZE = 20;
    static final int SCREEN_SIZE = CELL_SIZE * GRID_SIZE;

    private Queue<Character> directionQueue;
    private Snake snake;
    private ScoreBoard scoreBoard;
    private Point food;
    private Random random;
    private Timer gameTimer;

    private char currentDir = 'R';
    private boolean gameOver = false;
    private int score = 0;
    private int delay = 300;

    public GamePanel() {
        setPreferredSize(new Dimension(SCREEN_SIZE, SCREEN_SIZE));
        setBackground(new Color(18, 18, 18));
        setFocusable(true);
        addKeyListener(this);

        snake = new Snake();
        scoreBoard = new ScoreBoard();
        directionQueue = new LinkedList<>();
        random = new Random();

        spawnFood();

        gameTimer = new Timer(delay, this);
        gameTimer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            processDirection();
            moveSnake();
            repaint();
        }
    }

    private void processDirection() {
        if (!directionQueue.isEmpty()) {
            char next = directionQueue.poll();
            if (next == 'L' && currentDir == 'R') return;
            if (next == 'R' && currentDir == 'L') return;
            if (next == 'U' && currentDir == 'D') return;
            if (next == 'D' && currentDir == 'U') return;
            currentDir = next;
        }
    }

    private void moveSnake() {
        Point head = snake.getHead();
        Point newHead;

        switch (currentDir) {
            case 'R': newHead = new Point(head.x + 1, head.y); break;
            case 'L': newHead = new Point(head.x - 1, head.y); break;
            case 'U': newHead = new Point(head.x, head.y - 1); break;
            default:  newHead = new Point(head.x, head.y + 1); break;
        }

        if (newHead.x < 0 || newHead.x >= GRID_SIZE ||
                newHead.y < 0 || newHead.y >= GRID_SIZE) {
            endGame();
            return;
        }

        if (snake.collidesWith(newHead)) {
            endGame();
            return;
        }

        if (newHead.equals(food)) {
            snake.grow(newHead);
            score += 10;
            spawnFood();
            speedUp();
        } else {
            snake.move(newHead);
        }
    }

    private void spawnFood() {
        do {
            food = new Point(random.nextInt(GRID_SIZE), random.nextInt(GRID_SIZE));
        } while (snake.collidesWith(food));
    }

    private void speedUp() {
        if (score % 50 == 0 && delay > 60) {
            delay -= 10;
            gameTimer.setDelay(delay);
        }
    }

    private void endGame() {
        gameOver = true;
        gameTimer.stop();
        scoreBoard.addScore(score);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid(g);
        drawFood(g);
        drawSnake(g);
        drawScore(g);
        if (gameOver) drawGameOver(g);
    }

    private void drawGrid(Graphics g) {
        g.setColor(new Color(30, 30, 30));
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                g.drawRect(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
    }

    private void drawSnake(Graphics g) {
        LinkedList<Point> body = snake.getBody();
        for (int i = 0; i < body.size(); i++) {
            Point p = body.get(i);
            if (i == 0) {
                g.setColor(new Color(100, 220, 100));
            } else {
                g.setColor(new Color(50, 160, 50));
            }
            g.fillRoundRect(
                    p.x * CELL_SIZE + 2,
                    p.y * CELL_SIZE + 2,
                    CELL_SIZE - 4,
                    CELL_SIZE - 4,
                    8, 8
            );
        }
    }

    private void drawFood(Graphics g) {
        g.setColor(new Color(220, 60, 60));
        g.fillOval(
                food.x * CELL_SIZE + 4,
                food.y * CELL_SIZE + 4,
                CELL_SIZE - 8,
                CELL_SIZE - 8
        );
    }

    private void drawScore(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Score: " + score, 8, 18);
    }

    private void drawGameOver(Graphics g) {
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, SCREEN_SIZE, SCREEN_SIZE);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 36));
        g.drawString("GAME OVER", SCREEN_SIZE / 2 - 100, SCREEN_SIZE / 2 - 60);

        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString("Score: " + score, SCREEN_SIZE / 2 - 35, SCREEN_SIZE / 2 - 25);

        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Top Scores:", SCREEN_SIZE / 2 - 45, SCREEN_SIZE / 2 + 15);
        g.setFont(new Font("Arial", Font.PLAIN, 15));
        ArrayList<Integer> scores = scoreBoard.getScores();
        for (int i = 0; i < scores.size(); i++) {
            g.drawString((i + 1) + ".  " + scores.get(i),
                    SCREEN_SIZE / 2 - 30,
                    SCREEN_SIZE / 2 + 38 + (i * 22));
        }

        g.setColor(new Color(150, 220, 150));
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Press R to restart", SCREEN_SIZE / 2 - 60, SCREEN_SIZE / 2 + 150);
    }

    private void restartGame() {
        snake = new Snake();
        directionQueue.clear();
        currentDir = 'R';
        score = 0;
        delay = 300;
        gameOver = false;
        spawnFood();
        gameTimer.setDelay(delay);
        gameTimer.start();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:  directionQueue.offer('L'); break;
            case KeyEvent.VK_RIGHT: directionQueue.offer('R'); break;
            case KeyEvent.VK_UP:    directionQueue.offer('U'); break;
            case KeyEvent.VK_DOWN:  directionQueue.offer('D'); break;
            case KeyEvent.VK_R:     if (gameOver) restartGame(); break;
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}