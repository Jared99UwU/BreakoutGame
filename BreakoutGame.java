import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class BreakoutGame extends JPanel implements ActionListener {
    private Timer timer;
    private int paddleX = 310; // Posición inicial de la paleta
    private int ballPosX = 120, ballPosY = 350; // Posición inicial de la pelota
    private int ballDirX = -1, ballDirY = -2; // Dirección inicial de la pelota
    private int bricks[][]; // Matriz para los bloques
    private final int brickRows = 3, brickCols = 7; // Configuración de bloques

    public BreakoutGame() {
        initGame();
        setFocusable(true);
        addKeyListener(new PaddleController());
    }

    // Inicializa el juego
    public void initGame() {
        bricks = new int[brickRows][brickCols];
        for (int i = 0; i < brickRows; i++) {
            for (int j = 0; j < brickCols; j++) {
                bricks[i][j] = 1; // 1 representa que el bloque está presente
            }
        }
        timer = new Timer(8, this); // Velocidad de actualización del juego
        timer.start();
    }

    @Override
    public void paint(Graphics g) {
        // Fondo
        g.setColor(Color.black);
        g.fillRect(1, 1, 692, 592);

        // Paleta
        g.setColor(Color.green);
        g.fillRect(paddleX, 550, 100, 8);

        // Pelota
        g.setColor(Color.yellow);
        g.fillOval(ballPosX, ballPosY, 20, 20);

        // Bloques
        for (int i = 0; i < brickRows; i++) {
            for (int j = 0; j < brickCols; j++) {
                if (bricks[i][j] > 0) {
                    g.setColor(Color.red);
                    g.fillRect(j * 100 + 80, i * 30 + 50, 80, 20);
                }
            }
        }

        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();

        // Detectar colisiones con la paleta
        if (new Rectangle(ballPosX, ballPosY, 20, 20).intersects(new Rectangle(paddleX, 550, 100, 8))) {
            ballDirY = -ballDirY;
        }

        // Colisiones con los bloques
        for (int i = 0; i < brickRows; i++) {
            for (int j = 0; j < brickCols; j++) {
                if (bricks[i][j] > 0) {
                    int brickX = j * 100 + 80;
                    int brickY = i * 30 + 50;
                    int brickWidth = 80;
                    int brickHeight = 20;

                    Rectangle brickRect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                    Rectangle ballRect = new Rectangle(ballPosX, ballPosY, 20, 20);

                    if (ballRect.intersects(brickRect)) {
                        bricks[i][j] = 0;
                        ballDirY = -ballDirY;
                    }
                }
            }
        }

        // Movimiento de la pelota
        ballPosX += ballDirX;
        ballPosY += ballDirY;

        // Detectar colisiones con los bordes de la ventana
        if (ballPosX < 0 || ballPosX > 670) {
            ballDirX = -ballDirX;
        }
        if (ballPosY < 0) {
            ballDirY = -ballDirY;
        }
        if (ballPosY > 570) {
            // Reiniciar juego si la pelota cae
            ballPosX = 120;
            ballPosY = 350;
            ballDirX = -1;
            ballDirY = -2;
            paddleX = 310;
            initGame();
        }

        repaint();
    }

    private class PaddleController extends KeyAdapter implements KeyListener {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                if (paddleX >= 10) {
                    paddleX -= 20;
                }
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                if (paddleX <= 590) {
                    paddleX += 20;
                }
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Breakout Game");
        BreakoutGame game = new BreakoutGame();
        frame.setBounds(10, 10, 700, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(game);
        frame.setVisible(true);
    }
}
