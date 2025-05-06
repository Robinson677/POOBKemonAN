package presentation;

import domain.PoobKemonFight;
import javax.swing.*;
import java.awt.*;
import java.net.URL;


/**
 * Pantalla que permite al jugador seleccionar la modalidad de juego:
 * PvP (jugador vs jugador)
 * PvM (jugador vs maquina)
 * MvM (maquina vs maquina)
 */
public class PoobKemonModality extends JFrame {
    private final PoobKemonFight fight;

    /**
     * Constructor de PoobKemonModality
     * @param fight representa la preparacion para la batalla
     */
    public PoobKemonModality(PoobKemonFight fight) {
        this.fight = fight;
        final domain.Trainer player1 = fight.getTrainer1();
        final domain.Trainer player2 = fight.getTrainer2();
        setTitle("Modalidad de POOBKemon");
        setSize(930, 673);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);


        JPanel panel = new JPanel() {
            ImageIcon bg;

            {
                URL imgUrl = getClass().getResource("/pokemonPictures/PoobKemonModalidad.png");
                if (imgUrl != null) {
                    bg = new ImageIcon(imgUrl);
                } else {
                    System.err.println("No se encontró la imagen PoobKemonModalidad.png");
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
        //PvP
        JButton pvpButton = createTransparentButton(345, 106, 260, 67);
        pvpButton.addActionListener(e -> {
            dispose();
            new PoobKemonSelector(fight);
        });

        JButton pvmButton = createTransparentButton(345, 259, 270, 75);
        pvmButton.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Seleccionaste Player vs Machine"));

        JButton mvmButton = createTransparentButton(345, 420, 280, 72);
        mvmButton.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Seleccionaste Machine vs Machine"));

        JButton exitButton = createTransparentButton(680, 552, 165, 54);
        exitButton.addActionListener(e -> {
            dispose();
            new StartPoobKemon();
        });

        panel.add(pvpButton);
        panel.add(pvmButton);
        panel.add(mvmButton);
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