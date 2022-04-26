import javax.swing.*;

public class SnakeGameProcess {
    public static JFrame frame;
    public static void main(String[] args) {
        frame = new GameFrame();
    }
    public static void restartGame() {
        frame.dispose();
        frame = new GameFrame();
    }
}
