package presentation;

import domain.PoobKemonFight;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Pantalla que se encarga de que los jugadores ingresen sus nombres y equipos
 */
public class PoobKemonName extends JFrame implements KeyListener {

    private PoobKemonFight fight;
    private int[] pickOrder = new int[2];
    private int picksDone = 0;
    private static final String CHARS = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZ ";
    private static final int COLS = 8, ROWS = 4, TOTAL = COLS * ROWS, MAX_NAME = 25;

    private final List<JLabel> gridLabels = new ArrayList<>();
    private final JLabel       nameLabel;
    private final StringBuilder playerName = new StringBuilder();

    private boolean uppercase  = true;
    private boolean teamChosen = false;
    private boolean isTeamAzul = true;
    private int     selectedIndex = 0;

    /**
     * Constructor de PoobKemonName
     */
    public PoobKemonName() {
        super();
        setTitle("POOBKemon Entrenadores");
        setSize(930, 673);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        addKeyListener(this);
        setFocusable(true);

        JPanel content = new JPanel(null) {
            private final Image bg = loadImage("/pokemonPictures/Nombre.jpg");
            /**
             * grafica la imagen y la deja completa
             * @param g
             */
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bg != null) g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
            }
        };
        setContentPane(content);

        nameLabel = new JLabel("", SwingConstants.LEFT);
        nameLabel.setFont(new Font("Monospaced", Font.BOLD, 26));
        nameLabel.setBounds(360, 205, 430, 40);
        content.add(nameLabel);

        JPanel grid = new JPanel(new GridLayout(ROWS, COLS, 5, 5));
        grid.setOpaque(false);
        grid.setBounds(140, 380, 480, 160);
        content.add(grid);
        for (int i = 0; i < CHARS.length(); i++) {
            JLabel lbl = makeCell(" " + CHARS.charAt(i) + " ");
            gridLabels.add(lbl);
            grid.add(lbl);
        }
        gridLabels.add(makeCell("–"));  grid.add(gridLabels.get(28));
        gridLabels.add(makeCell("Fin"));grid.add(gridLabels.get(29));
        gridLabels.add(makeCell("Team"));grid.add(gridLabels.get(30));
        gridLabels.add(makeCell("Team"));grid.add(gridLabels.get(31));
        highlight();
        setVisible(true);

        showCoinTossAtStart();
        pickOrder[0] = coinWinnerIndex;
        pickOrder[1] = 1 - coinWinnerIndex;

        fight = new PoobKemonFight(
                new domain.Trainer("",""),
                new domain.Trainer("",""),
                coinWinnerIndex + 1
        );

        showTurnDialog(pickOrder[picksDone]);

    }

    /**
     * Crea una celda que quede bien con el juego
     * @param text el texto que se muestra en la celda
     * @return lbl el JLabel
     */
    private JLabel makeCell(String text) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setFont(new Font("Monospaced", Font.BOLD, 20));
        lbl.setOpaque(true);
        lbl.setBackground(Color.WHITE);
        return lbl;
    }

    /**
     * Carga una imagen desde la carpeta de pokemonPictures
     * @param path la ruta a pokemonPictures
     * @return Image
     */
    private Image loadImage(String path) {
        URL u = getClass().getResource(path);
        return u != null ? new ImageIcon(u).getImage() : null;
    }


    /**
     * Resalta la celda seleccionada en la cuadro cambiando su color de fondo segun que
     * Amarillo para caracteres normales
     * Cyan para el Team Azul
     * Rojo para el Team Rojo
     */
    private void highlight() {
        for (int i = 0; i < TOTAL; i++) {
            JLabel lbl = gridLabels.get(i);
            if (i == selectedIndex) {
                lbl.setBackground(i == 30 ? Color.CYAN : i == 31 ? Color.RED : Color.YELLOW);
            } else {
                lbl.setBackground(Color.WHITE);
            }
        }
    }

    /***
     * La cuadricula con caracteres y el espaciado
     * @param e el evento de que pase las teclas con usando las flechitas
     */
    @Override public void keyPressed(KeyEvent e) {
        int r = selectedIndex / COLS, c = selectedIndex % COLS;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:  c = (c > 0 ? c - 1 : COLS - 1); break;
            case KeyEvent.VK_RIGHT: c = (c < COLS - 1 ? c + 1 : 0); break;
            case KeyEvent.VK_UP:    r = (r > 0 ? r - 1 : ROWS - 1); break;
            case KeyEvent.VK_DOWN:  r = (r < ROWS - 1 ? r + 1 : 0); break;
            case KeyEvent.VK_A:     selectCurrent(); break;
            case KeyEvent.VK_B:     deleteChar();    break;
            case KeyEvent.VK_ENTER:
                if (selectedIndex == 29) finishAction();
                break;
        }
        selectedIndex = r * COLS + c;
        highlight();
    }

    /**
     * Se encarga de las ultimos 4 cuadros de 32
     * el 29 para el mayusculas y minusculas
     * el 30 para finalizar y pasar turno
     * el 31 para que el jugador elija el team Azul
     * el 32 para que el jugador elija el team  Rojo
     */
    private void selectCurrent() {
        if (selectedIndex < CHARS.length()) {
            if (playerName.length() < MAX_NAME) {
                char c = CHARS.charAt(selectedIndex);
                playerName.append(uppercase ? c : Character.toLowerCase(c));
                nameLabel.setText(playerName.toString());
            }
        } else if (selectedIndex == 28) {
            uppercase = !uppercase;
            for (int i = 0; i < CHARS.length(); i++) {
                char c = CHARS.charAt(i);
                gridLabels.get(i).setText(" " + (uppercase ? c : Character.toLowerCase(c)) + " ");
            }
        } else if (selectedIndex == 29) {
            finishAction();
        } else if (selectedIndex == 30) {
            teamChosen = true; isTeamAzul = true;
        } else if (selectedIndex == 31) {
            teamChosen = true; isTeamAzul = false;
        }
    }

    /**
     * Elimina el ultimo caracter del nombre del jugador
     */
    private void deleteChar() {
        if (playerName.length() > 0) {
            playerName.deleteCharAt(playerName.length() - 1);
            nameLabel.setText(playerName.toString());
        }
    }

    /**
     * Finaliza la accion se valida si el jugador puso su nombre y eligio su team
     * Si el jugador que gano la moneda ya valido le pasa el turno al otro
     *  Cuando ambos hayan validado pasa a elegir el modo
     */
    private void finishAction() {
        if (playerName.length() == 0 || !teamChosen) {
            String msg = playerName.length() == 0 ? "¡Debes ingresar un nombre!" : "¡Debes elegir un equipo!";
            showWarning(msg);
            return;
        }
        String color = isTeamAzul ? "Azul" : "Rojo";
        if (picksDone == 1 && color.equals(fight.getCurrentTrainer().getColorTeam())) {
            showWarning("¡Ese equipo ya lo eligió el Jugador " + (pickOrder[0] + 1) + "!");
            return;
        }
        int slot = pickOrder[picksDone];
        fight.addTrainer(new domain.Trainer(playerName.toString(), color), slot + 1);
        picksDone++;
        if (picksDone < 2) {
            playerName.setLength(0);
            nameLabel.setText("");
            teamChosen = false; isTeamAzul = true; selectedIndex = 0;
            highlight();
            showTurnDialog(pickOrder[picksDone]);
            return;
        }
        dispose();
        new PoobKemonMode(fight);
    }

    /**
     * Muestra una advertencia con un mensaje personalizado
     * @param msg Mensaje de advertencia a mostrar.
     */
    private void showWarning(String msg) {
        URL imgUrl = getClass().getResource("/pokemonPictures/Usos.png");
        ImageIcon icon = null;
        if (imgUrl != null) {
            Image img = new ImageIcon(imgUrl).getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            icon = new ImageIcon(img);
        }
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(icon), BorderLayout.CENTER);
        JLabel lbl = new JLabel(msg, SwingConstants.CENTER);
        lbl.setFont(new Font("Monospaced", Font.BOLD, 16));
        panel.add(lbl, BorderLayout.SOUTH);
        JOptionPane.showMessageDialog(this, panel, "Advertencia", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Muestra un dialogo indicando a quien le toca el turno
     */
    private void showTurnDialog(int playerIndex) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(playerIndex == pickOrder[0] ? Color.CYAN : Color.PINK);

        URL imgUrl = getClass().getResource("/pokemonPictures/Usos.png");
        if (imgUrl != null) {
            Image img = new ImageIcon(imgUrl).getImage()
                    .getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            panel.add(new JLabel(new ImageIcon(img), SwingConstants.CENTER), BorderLayout.CENTER);
        }

        JLabel text = new JLabel(
                "Turno de Jugador " + (playerIndex + 1),
                SwingConstants.CENTER
        );
        text.setFont(new Font("Monospaced", Font.BOLD, 16));
        panel.add(text, BorderLayout.SOUTH);

        JOptionPane.showMessageDialog(
                this,
                panel,
                "Turno",
                JOptionPane.PLAIN_MESSAGE
        );
    }


    private int coinWinnerIndex;

    /**
     * Simula un lanzamiento de moneda al inicio para decidir qué jugador comienza
     * Muestra el resultado de una imagen de una moneda cara o sello
     */
    private void showCoinTossAtStart() {
        boolean heads = Math.random() < 0.5;
        coinWinnerIndex = heads ? 0 : 1;
        JDialog dialog = new JDialog(this, "Lanzamiento de Moneda", true);
        dialog.setSize(250, 300);
        dialog.setLocationRelativeTo(this);
        JPanel p = new JPanel(new BorderLayout()); p.setBackground(Color.WHITE);
        String path = heads ? "/pokemonPictures/Cara.jpg" : "/pokemonPictures/Sello.jpg";
        URL url = getClass().getResource(path);
        if (url != null) {
            Image img = new ImageIcon(url).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            p.add(new JLabel(new ImageIcon(img), SwingConstants.CENTER), BorderLayout.CENTER);
        }
        JLabel txt = new JLabel("Jugador " + (heads ? "1" : "2") + " comienza", SwingConstants.CENTER);
        txt.setFont(new Font("Monospaced", Font.BOLD, 16)); p.add(txt, BorderLayout.NORTH);
        JButton btn = new JButton("Continuar"); btn.setFont(new Font("Monospaced", Font.BOLD, 14));
        btn.addActionListener(e -> dialog.dispose()); JPanel bp = new JPanel(); bp.setBackground(Color.WHITE); bp.add(btn); p.add(bp, BorderLayout.SOUTH);
        dialog.setContentPane(p);
        dialog.setVisible(true);
        getContentPane().setBackground(heads ? Color.CYAN : Color.PINK);
    }

    /**
     * Los eventos al precionar las teclas
     * @param e the event to be processed
     */
    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped  (KeyEvent e) {}
}

