import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class GameFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private GamePanel gamePanel;

    public GameFrame() {
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Create and add panels
        mainPanel.add(createMenuPanel(), "Menu");
        gamePanel = new GamePanel(this); // Initialize once
        mainPanel.add(gamePanel, "Game");

        this.add(mainPanel);
        this.setSize(600, 600);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        cardLayout.show(mainPanel, "Menu");
    }
    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BorderLayout());

        JButton playButton = new JButton("Play");
        playButton.addActionListener(e -> {startGame();});

        menuPanel.add(playButton, BorderLayout.CENTER);
        return menuPanel;
    }

    public void startGame() {
        gamePanel.startNewGame();
        // Force focus to the game panel
        SwingUtilities.invokeLater(() -> {
            gamePanel.requestFocusInWindow();
        });
        cardLayout.show(mainPanel, "Game");
    }

    public void returnToMenu() {
        cardLayout.show(mainPanel, "Menu");
    }

}
