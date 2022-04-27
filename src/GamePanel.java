import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;


public class GamePanel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 700;
    static final int SCREEN_HEIGHT = 700;
    static final int UNIT_SIZE = 20;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 35;
    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];
    int snakeSize = 3;
    int applesEaten;
    int appleX, appleY;
    char snakeDirection = 'R';
    boolean isRunning = false;
    Timer timer;
    Random random;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapted());
        startGame();
    }

    public void startGame() {
        newApple();
        isRunning = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (isRunning) {

            // GRID (optional)
            /*
            for (int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++) {
                g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
            }
             */

            for (int i = 0; i < snakeSize; i++) {
                if (i == 0) {
                    g.setColor(new Color(0, 144, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                else {
                    g.setColor(new Color(114, 251, 38));
                    g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }

            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            drawText(g, Color.red, new Font("Ink Free", Font.BOLD, 35), "Score: " + applesEaten, g.getFont().getSize() + 25);
        }
        else {
            gameOver(g);
        }
    }

    public void drawText(Graphics g, Color color, Font font, String text, int y) {
        g.setColor(color);
        g.setFont(font);
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString(text, (SCREEN_WIDTH - metrics.stringWidth(text)) / 2, y);
    }

    public void newApple() {
        appleX = random.nextInt((SCREEN_WIDTH/UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((SCREEN_HEIGHT/UNIT_SIZE)) * UNIT_SIZE;
    }

    public void move() {
        for (int i = snakeSize; i > 0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch (snakeDirection) {
            case 'U' -> y[0] = y[0] - UNIT_SIZE;
            case 'D' -> y[0] = y[0] + UNIT_SIZE;
            case 'L' -> x[0] = x[0] - UNIT_SIZE;
            case 'R' -> x[0] = x[0] + UNIT_SIZE;
        }
    }

    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            snakeSize++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollisions() {
        // CHECK IF HEAD COLLIDES WITH BODY
        for (int i = snakeSize; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                isRunning = false;
            }
        }
        // CHECK IF HEAD TOUCHES LEFT BORDER
        if (x[0] < 0) {
            isRunning = false;
        }
        // CHECK IF HEAD TOUCHES RIGHT BORDER
        if (x[0] > SCREEN_WIDTH - UNIT_SIZE) {
            isRunning = false;
        }
        // CHECK IF HEAD TOUCHES TOP BORDER
        if (y[0] < 0) {
            isRunning = false;
        }
        // CHECK IF HEAD TOUCHES BOTTOM BORDER
        if (y[0] > SCREEN_HEIGHT - UNIT_SIZE) {
            isRunning = false;
        }

        if (!isRunning) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {
        // SCORE
        drawText(g, Color.red, new Font("Ink Free", Font.BOLD, 35), "Score: " + applesEaten, g.getFont().getSize() + 25);
        // GAME OVER TEXT
        drawText(g, Color.red, new Font("Ink Free", Font.BOLD, 75), "Game Over", SCREEN_HEIGHT / 2);
        drawText(g, Color.yellow, new Font("Ink Free", Font.BOLD, 50), "Press R to restart", SCREEN_HEIGHT / 2 + 65);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isRunning) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    // INNER CLASS
    public class MyKeyAdapted extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    if (snakeDirection != 'D')
                        snakeDirection = 'U';
                    break;
                case KeyEvent.VK_DOWN:
                    if (snakeDirection != 'U')
                        snakeDirection = 'D';
                    break;
                case KeyEvent.VK_LEFT:
                    if (snakeDirection != 'R')
                        snakeDirection = 'L';
                    break;
                case KeyEvent.VK_RIGHT:
                    if (snakeDirection != 'L')
                        snakeDirection = 'R';
                    break;
                case KeyEvent.VK_R:
                    isRunning = false;
                    SnakeGameProcess.restartGame();
                    break;
            }
        }
    }

}
