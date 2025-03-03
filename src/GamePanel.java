import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    private boolean gameOverFlag = false;
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY = 75;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 6;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random rand;
    GameFrame parent;

    GamePanel(GameFrame parent) {
        this.parent = parent;
        rand = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        setupKeyBindings(); // Replace KeyAdapter with this
    }

    private void setupKeyBindings() {
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        // Define key actions
        addKeyBinding(inputMap, actionMap, KeyEvent.VK_LEFT, "left", 'L', 'R');
        addKeyBinding(inputMap, actionMap, KeyEvent.VK_RIGHT, "right", 'R', 'L');
        addKeyBinding(inputMap, actionMap, KeyEvent.VK_UP, "up", 'U', 'D');
        addKeyBinding(inputMap, actionMap, KeyEvent.VK_DOWN, "down", 'D', 'U');
    }

    private void addKeyBinding(InputMap inputMap, ActionMap actionMap, int keyCode, String actionId, char newDir, char oppositeDir) {
        KeyStroke keyStroke = KeyStroke.getKeyStroke(keyCode, 0);
        inputMap.put(keyStroke, actionId);
        actionMap.put(actionId, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (direction != oppositeDir) {
                    direction = newDir;
                }
            }
        });
    }

    public void startGame() {
        newApple();
        running = true;
        gameOverFlag = false;
        direction = 'R';
        timer = new Timer(DELAY, this);
        timer.start();
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    public void startNewGame() {
        if (running) return;

        // Reset game state
        bodyParts = 6;
        applesEaten = 0;
        direction = 'R';
        running = true;
        gameOverFlag = false;

        // Initialize snake position (center, horizontal)
        int startX = (SCREEN_WIDTH / 2);
        int startY = (SCREEN_HEIGHT / 2);
        for (int i = 0; i < bodyParts; i++) {
            x[i] = startX - i * UNIT_SIZE;
            y[i] = startY;
        }

        newApple();
        timer = new Timer(DELAY, this);
        timer.start();
        requestFocusInWindow();
    }

    public void draw(Graphics g) {
        if (running) {

            /*
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }
            */

            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.setColor(new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.GREEN);
            g.setFont(new Font("TimesRoman", Font.BOLD, 40));
            FontMetrics fm = getFontMetrics(g.getFont());
            g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - fm.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
        }
        else{
            GameOver();
        }
    }
    public void newApple() {
        appleX = rand.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = rand.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;

    }
    public void move(){
        for(int i = bodyParts; i > 0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        switch(direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
        }

    }
    public void checkApple(){
        if((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }
    public void checkCollision() {
        // Check body collision
        for (int i = bodyParts; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
                break;
            }
        }

        // Check border collisions
        if (x[0] < 0 || x[0] >= SCREEN_WIDTH || y[0] < 0 || y[0] >= SCREEN_HEIGHT) {
            running = false;
        }

        if (!running) {
            timer.stop();
            GameOver();
        }
    }

    public void GameOver() { // Fără Graphics g
        if (!gameOverFlag) {  // ✅ Verificăm dacă deja am intrat în GameOver
            gameOverFlag = true;
            running = false;
            timer.stop();
            JOptionPane.showMessageDialog(this, "Game Over!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
            parent.returnToMenu();
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        if(running) {
            move();
            checkApple();
            checkCollision();
        }
        repaint();
    }
}
