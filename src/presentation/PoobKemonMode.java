package presentation;

import domain.PoobKemonFight;
import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * Pantalla que permite al usuario seleccionar entre 2 modos de juego disponibles:
 * Normal o Supervivencia
 */
public class PoobKemonMode extends JFrame {
    private final PoobKemonFight fight;
    private final boolean survivalMode;


    /**
     * @param fight  representa la preparacion para la batalla
     * @param survivalMode preparacion para la batalla extrema
     */
    public PoobKemonMode(PoobKemonFight fight, boolean survivalMode) {
        this.fight = fight;
        this.survivalMode = survivalMode;
        final domain.Trainer player1 = fight.getTrainer1();
        final domain.Trainer player2 = fight.getTrainer2();
        setTitle("Modo de Juego de POOBKemon");
        setSize(930, 673);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel() {
            ImageIcon bg;

            {
                URL imgUrl = getClass().getResource("/pokemonPictures/PoobKemonModos.png");
                if (imgUrl != null) {
                    bg = new ImageIcon(imgUrl);
                } else {
                    System.err.println("No se encontró la imagen PoobKemonModos.png");
                }
            }
            /**
             * grafica la imagen y la deja completa
             * @param g
             */
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bg != null) {
                    g.drawImage(bg.getImage(), 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        panel.setLayout(null);

        //Batalla Normal
        JButton normalButton = createTransparentButton(225, 140, 527, 62);
        normalButton.addActionListener(e -> {
            dispose();
            new PoobKemonModality(fight, false).setVisible(true);
        });

        //Batalla Superviviencia
        JButton survivalButton = createTransparentButton(185, 300, 662, 57);
        survivalButton.addActionListener(e -> {
            dispose();
            new PoobKemonModality(fight, true).setVisible(true);
        });

        JButton exitButton = createTransparentButton(665, 495, 200, 53);
        exitButton.addActionListener(e -> {
            dispose();
            new StartPoobKemon();
        });

        panel.add(normalButton);
        panel.add(survivalButton);
        panel.add(exitButton);

        setContentPane(panel);
        setVisible(true);
    }

    /**
     * creamos un boton invisible
     * @param x coordenada horizontal
     * @param y coordenada vertical
     * @param width ancho del boton
     * @param height alto del boton
     * @return JButton transparente
     */
    private JButton createTransparentButton(int x, int y, int width, int height) {
        JButton button = new JButton();
        button.setBounds(x, y, width, height);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
}