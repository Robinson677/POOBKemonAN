package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Nos muestra la pantalla inicial del juego Poobkemon Emeralda
 * podemos ver la imagen de fondo y un boton invisible que al presionarse
 * lleva al usuario a la pantalla de ingreso de nombre.
 */
public class StartPoobKemon extends JFrame {
    static {
        try (InputStream is = StartPoobKemon.class
                .getResourceAsStream("/fonts/PressStart2P-Regular.ttf")) {
            Font pixel = Font.createFont(Font.TRUETYPE_FONT, is);
            GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .registerFont(pixel);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructor de StartScreen
     */
    public StartPoobKemon() {
        setTitle("Poobkemon Emerald");
        setSize(930, 673);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel() {
            ImageIcon bg;

            {
                URL imgUrl = getClass().getResource("/pokemonPictures/poobKemon.png");
                if (imgUrl != null) {
                    bg = new ImageIcon(imgUrl);
                } else {
                    System.err.println("No se encontró la imagen de fondo poobKemon.png ");
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

        JButton startButton = new JButton();
        startButton.setBounds(290, 420, 335, 30);
        startButton.setOpaque(false);
        startButton.setContentAreaFilled(false);
        startButton.setBorderPainted(false);
        startButton.setFocusPainted(false);
        startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        startButton.addActionListener(new ActionListener() {
            /**
             * pasa a la siguiente interfaz
             * @param e darle a press start
             */
            public void actionPerformed(ActionEvent e) {
                dispose();
                new PoobKemonName();
            }
        });

        panel.add(startButton);
        setContentPane(panel);
        setVisible(true);
    }

    /**
     * Para ejecutar POOBKemon
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(StartPoobKemon::new);
    }
}