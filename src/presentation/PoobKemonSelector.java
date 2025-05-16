package presentation;

import domain.AITrainer;
import domain.POOBKemonException;
import domain.PoobKemonFight;
import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;

/**
 * Encargada de que cada entrenador elija 6 pokemones de 36
 */
public class PoobKemonSelector extends JFrame {
    private PoobKemonFight fight;
    private int currentTurn;
    private int[] picksCount = { 0, 0 };
    private final int initialPickerIndex;
    private static final int MAX_PICKS = 6;
    private final List<String> selectedKeys = new ArrayList<>();


    private JLabel lblName;
    private JLabel imgPok;
    private JLabel lblType;
    private JTextArea txtDesc;
    private JTextArea txtStats;
    private JButton btnCombat;
    private JButton btnExit;

    private static final int BUTTON_WIDTH = 125;
    private static final int BUTTON_HEIGHT = 57;


    /**
     * Constructor de PoobKemonSelector
     * Prepara lo que se va a mostrar en pantalla junto los 36 botones para da pokemon
     * @param fight representa la preparacion para la batalla
     */
    public PoobKemonSelector(PoobKemonFight fight) {
        this.fight = fight;
        this.initialPickerIndex = fight.getInitialPickerIndex();
        this.currentTurn = initialPickerIndex;

        if (fight.getCurrentTrainer() instanceof AITrainer) {
            AITrainer ai = (AITrainer) fight.getCurrentTrainer();
            try {
                ai.autoSelectTeam(fight);
                picksCount[currentTurn] = 6;
            } catch (POOBKemonException ex) {
                ex.printStackTrace();
            }
            if (currentTurn == initialPickerIndex) {
                fight.nextTurn();
                currentTurn = 1 - currentTurn;
                String humanName = fight.getTrainerName(currentTurn);
                String humanTeam = fight.getTrainerTeam(currentTurn);
                showTurnDialog(humanName, humanTeam);
            } else {
                dispose();
                new PoobKemonCombat(fight).setVisible(true);
                return;
            }
        }

        setTitle("POOBKemon Selector");
        setSize(930, 673);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(null) {
            private final Image bg = loadImage("/pokemonPictures/PoobKemonSelector.png");
            /**
             * grafica la imagen y la deja completa
             * @param g
             */
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bg != null) {
                    g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        setContentPane(panel);

        int startX = 29;
        int startY = 14;
        double spacingX = 148.2;
        double spacingY = 74.6;
        String[] pokemonNames = {
                "GENGAR","BLASTOISE","GYARADOS","GROUDON","GARDEVOIR","GRANBULL",
                "DUSCLOPS","GOLDUCK","SWELLOW","DONPHAN","ALAKAZAM","TOGETIC",
                "RAICHU","SLAKING","MACHAMP","ARBOK","LATIOS","ABSOL",
                "MAGNETON","SNORLAX","HARIYAMA","NIDORINO","DRAGONITE","MIGHTYENA",
                "METAGROSS","HERACROSS","REGIROCK","VENUSAUR","RAPIDASH","DELIBIRD",
                "STEELIX","PINSIR","TYRANITAR","SCEPTILE","CHARIZARD","GLALIE"
        };
        int index = 0;
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                if (index >= pokemonNames.length) break;
                String key = pokemonNames[index++];
                JButton btn = new JButton();
                btn.setBounds((int)(startX + col * spacingX), (int)(startY + row * spacingY), BUTTON_WIDTH, BUTTON_HEIGHT);
                btn.setOpaque(false);
                btn.setContentAreaFilled(false);
                btn.setBorderPainted(false);
                btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                btn.addActionListener(e -> onPokemonClick(key));
                panel.add(btn);
            }
        }

        JPanel detail = new JPanel(null);
        detail.setOpaque(false);
        detail.setBounds(0, 420, 930, 293);
        panel.add(detail);

        int miniWidth = 220;
        int miniStartX = 0;
        int imgSize = 120;

        lblName = new JLabel("PoobKemon", SwingConstants.CENTER);
        lblName.setFont(new Font("Press Start 2P", Font.BOLD, 13));
        lblName.setForeground(Color.BLACK);
        lblName.setBounds(miniStartX + (miniWidth - 200)/2, 69, 200, 25);
        detail.add(lblName);

        imgPok = new JLabel();
        imgPok.setHorizontalAlignment(SwingConstants.CENTER);
        imgPok.setBounds(miniStartX + (miniWidth - imgSize)/2, 64, imgSize, imgSize);
        URL defaultGif = getClass().getResource("/pokemonPictures/Rayquaza.gif");
        if (defaultGif != null) imgPok.setIcon(new ImageIcon(defaultGif));
        detail.add(imgPok);

        lblType = new JLabel("Tipo: --", SwingConstants.CENTER);
        lblType.setFont(new Font("Press Start 2P", Font.BOLD, 8));
        lblType.setForeground(Color.BLACK);
        lblType.setBounds(miniStartX + (miniWidth - 180)/2, 160, 180, 20);
        detail.add(lblType);

        txtDesc = new JTextArea(
                "Cada Pokémon pertenece a un tipo que define sus fortalezas, " +
                        "debilidades e inmunidades. Además, cuentan con estadísticas clave (salud, ataque, defensa, " +
                        "ataque especial, defensa especial y velocidad) que afectan su rendimiento en combate. " +
                        "También existen ítems que pueden potenciar estas estadísticas para mejorar sus habilidades."
        );
        txtDesc.setFont(new Font("Serif", Font.PLAIN, 10));
        txtDesc.setLineWrap(true);
        txtDesc.setWrapStyleWord(true);
        txtDesc.setEditable(false);
        txtDesc.setOpaque(false);
        txtDesc.setForeground(new Color(30, 30, 30));
        txtDesc.setBorder(null);
        JScrollPane spDesc = new JScrollPane(txtDesc);
        spDesc.setBounds(190, 77, 300, 120);
        spDesc.setOpaque(false);
        spDesc.getViewport().setOpaque(false);
        spDesc.setBorder(BorderFactory.createEmptyBorder());
        detail.add(spDesc);

        txtStats = new JTextArea(
                "PS: --\n" +
                        "Ataque: --\n" +
                        "Defensa: --\n" +
                        "Velocidad: --\n" +
                        "Atq. Esp.: --\n" +
                        "Def. Esp.: --"
        );
        txtStats.setFont(new Font("Press Start 2P", Font.PLAIN, 12));
        txtStats.setEditable(false);
        txtStats.setOpaque(false);
        txtStats.setForeground(new Color(30, 30, 30));
        txtStats.setBorder(null);
        JScrollPane spStats = new JScrollPane(txtStats);
        spStats.setBounds(520, 80, 300, 100);
        spStats.setOpaque(false);
        spStats.getViewport().setOpaque(false);
        spStats.setBorder(BorderFactory.createEmptyBorder());
        detail.add(spStats);

        btnCombat = new JButton("Combate");
        btnCombat.setBounds(490, 163, 140, 25);
        btnCombat.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCombat.setForeground(Color.WHITE);
        btnCombat.setBackground(new Color(70, 130, 180));
        btnCombat.setFocusPainted(false);
        btnCombat.setBorder(BorderFactory.createLineBorder(new Color(60, 110, 160), 2));
        btnCombat.setCursor(new Cursor(Cursor.HAND_CURSOR));
        detail.add(btnCombat);
        btnCombat.addActionListener(e -> onCombatButton());

        btnExit = new JButton("Salir");
        btnExit.setBounds(640, 163, 160, 25);
        btnExit.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnExit.setForeground(Color.WHITE);
        btnExit.setBackground(new Color(220, 20, 60));
        btnExit.setFocusPainted(false);
        btnExit.setBorder(BorderFactory.createLineBorder(new Color(200, 0, 40), 2));
        btnExit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        detail.add(btnExit);
        btnExit.addActionListener(e -> onExitButton());

        setVisible(true);
        String name = fight.getTrainerName(currentTurn);
        String team = fight.getTrainerTeam(currentTurn);
        if (!fight.readyToCombat()) {
            showTurnDialog(name, team);
        }
    }

    /**
     * Se encarga de que los jugadores ya hayan escogido sus pokemones y esten listos para el combate
     * Cuando el jugador que gano la moneda elije pasa el turno al otro
     * Cuando los 2 elijan pasan al combate
     */
    private void onCombatButton() {
        if (picksCount[currentTurn] < MAX_PICKS) {
            JOptionPane.showMessageDialog(this,
                    "Aún te faltan " + (MAX_PICKS - picksCount[currentTurn]) +
                            " Pokémon por elegir.", "Faltan selecciones",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (currentTurn == initialPickerIndex) {
            fight.nextTurn();
            currentTurn = 1 - currentTurn;

            if (fight.getCurrentTrainer().isAI()) {
                AITrainer ai = (AITrainer) fight.getCurrentTrainer();
                try {
                    ai.autoSelectTeam(fight);
                    picksCount[currentTurn] = MAX_PICKS;
                } catch (POOBKemonException ex) {
                    ex.printStackTrace();
                }
                dispose();
                new PoobKemonCombat(fight).setVisible(true);
                return;
            }

            showTurnDialog(
                    fight.getTrainerName(currentTurn),
                    fight.getTrainerTeam(currentTurn)
            );
            return;
        }

        dispose();
        new PoobKemonCombat(fight).setVisible(true);
    }

        /**
         *  Se devuelve a la pantalla de inicial y resetea todo
         */
    private void onExitButton() {
        fight.resetSelection();
        SwingUtilities.invokeLater(() -> {
            dispose();
            new StartPoobKemon().setVisible(true);
        });
    }


    /**
     * Permite quitar un pokemon al hacer click
     */
    private void onRemoveButton() {
        if (fight.hasSelected(currentTurn)) {
            var trainer = fight.getCurrentTrainer();
            var p = fight.getCurrentPokemon(currentTurn);
            fight.removePokemon(trainer, p);
            JOptionPane.showMessageDialog(
                    this,
                    "Has eliminado a " + p.getName(),
                    "Eliminar Pokémon",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    /**
     * Maneja el evento de clic en un pokemon actualizando los detalles de pokemon
     * como su nombre, tipo, descripcion, estadisticas y el gift
     * Muestra un cuadro de dialogo donde se elije o elimina el pokemon
     * @param key del pokemon por su nombre
     */
    private void onPokemonClick(String key) {
        lblName.setText(fight.getDisplayName(key));
        lblType.setText("Tipo: " + fight.getTypeName(key));
        txtDesc.setText(fight.getDescription(key));
        txtStats.setText(fight.getStats(key));
        URL u = getClass().getResource("/pokemonPictures/" + fight.getResourceName(key));
        if (u != null) imgPok.setIcon(new ImageIcon(u));
        showSelectionDialog(key);
    }


    /**
     * Muestra un dialogo y implementa la logica de elegir o quitar un pokemon del equipo
     * @param key del pokemon por su nombre
     */
    private void showSelectionDialog(String key) {
        boolean blue = fight.getTrainerTeam(currentTurn).equalsIgnoreCase("azul");
        Color bgColor = blue ? Color.CYAN : Color.PINK;

        JDialog dialog = new JDialog(this, "Seleccionar", true);
        dialog.setSize(300, 350);
        dialog.setLocationRelativeTo(this);

        JPanel content = new JPanel(new BorderLayout(0, 10));
        content.setBackground(bgColor);
        content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel top = new JLabel("Elegir a " + fight.getDisplayName(key), SwingConstants.CENTER);
        top.setFont(new Font("Press Start 2P", Font.BOLD, 14));
        content.add(top, BorderLayout.NORTH);

        Image usosImg = loadImage("/pokemonPictures/PokebolaClose.png");
        if (usosImg != null) {
            Image scaled = usosImg.getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            JLabel icon = new JLabel(new ImageIcon(scaled), SwingConstants.CENTER);
            content.add(icon, BorderLayout.CENTER);
        }

        JPanel buttons = new JPanel(new GridLayout(2, 1, 0, 10));
        buttons.setOpaque(false);
        JButton choose = new JButton("Elegir");
        styleButton(choose, new Color(70,130,180), new Color(60,110,160));
        JButton remove = new JButton("Eliminar");
        styleButton(remove, new Color(220,20,60), new Color(200,0,40));
        buttons.add(choose);
        buttons.add(remove);
        content.add(buttons, BorderLayout.SOUTH);

        boolean available = fight.getPokemonKeys().contains(key);
        choose.setEnabled(available);
        remove.setEnabled(available && fight.hasSelected(currentTurn));

        choose.addActionListener(e -> {
            boolean ok = fight.selectPokemon(key);
            if (ok) {

                picksCount[currentTurn]++;
                selectedKeys.add(key);

                JOptionPane.showMessageDialog(this,
                        "Has elegido a " + fight.getDisplayName(key),
                        "Seleccionar Pokémon",
                        JOptionPane.INFORMATION_MESSAGE
                );
                dialog.dispose();
                repaint();
            }
        });


        remove.addActionListener(e -> {
            onRemoveButton();
            dialog.dispose();
        });

        dialog.setContentPane(content);
        dialog.setResizable(false);
        dialog.setVisible(true);
    }


    /**
     * Se encarga de los avisos y indica los turnos de los jugadores
     */
    private void showTurnDialog(String trainerName, String teamColor) {
        boolean blue = teamColor.equalsIgnoreCase("azul");
        Color bg = blue ? Color.CYAN : Color.PINK;

        JDialog d = new JDialog(this, "Aviso", true);
        d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        d.getContentPane().setBackground(bg);
        d.setLayout(new BorderLayout(10, 10));

        String mensaje = !fight.hasSelected(currentTurn)
                ? String.format("Elije un Pokémon %s del team %s", trainerName, teamColor)
                : String.format("Turno de %s", trainerName);
        JLabel txt = new JLabel(mensaje, SwingConstants.CENTER);
        txt.setFont(new Font("Press Start 2P", Font.PLAIN, 8));
        txt.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        d.add(txt, BorderLayout.NORTH);

        URL url = getClass().getResource("/pokemonPictures/Usos.png");
        if (url != null) {
            ImageIcon raw = new ImageIcon(url);
            Image scaled = raw.getImage()
                    .getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            JLabel imgLabel = new JLabel(new ImageIcon(scaled), SwingConstants.CENTER);
            d.add(imgLabel, BorderLayout.CENTER);
        } else {
            System.err.println("No se encontró /pokemonPictures/Usos.png en el classpath");
        }

        JButton ok = new JButton("OK");
        ok.setFont(new Font("Press Start 2P", Font.PLAIN, 10));
        ok.addActionListener(e -> d.dispose());
        JPanel pnl = new JPanel();
        pnl.setOpaque(false);
        pnl.add(ok);
        d.add(pnl, BorderLayout.SOUTH);

        d.pack();
        d.setSize(320, 260);
        d.setResizable(false);
        d.setLocationRelativeTo(this);
        d.setVisible(true);
    }


    /**
     * Decora el boton
     * @param b el boton
     * @param bg el color de fondo
     * @param border del color del borde
     */
    private void styleButton(JButton b, Color bg, Color border) {
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setBorder(BorderFactory.createLineBorder(border, 2));
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
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

}
