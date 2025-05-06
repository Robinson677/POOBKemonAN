package presentation;

import domain.PoobKemonFight;
import domain.Trainer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

/**
 * Batalla POOBKemon entre dos entrenadores, cada uno con 6 pokemones de diferentes tipos
 * y movimientos
 */
public class PoobKemonCombat extends JFrame implements KeyListener {
    private final PoobKemonFight fight;
    private int currentTurn;
    private final boolean[] pokemonChosen = {false, false};
    private boolean battleStarted = false;

    private static final Point[] CURSOR_POS = {
            new Point(488, 477),
            new Point(690, 477),
            new Point(488, 540),
            new Point(690, 540)
    };

    private enum State {
        MENU, MOVE_SELECT, BATTLE,  COMBAT , BACKPACK, POKEMON_SELECT
    }

    private State state = State.MENU;
    private int moveSelection;
    private int moveCount;

    private int selection = 0;
    private JLabel cursorLabel;
    private BackgroundPanel contentPanel;
    private PoobKemonBackPack backpackPanel;
    private PokemonSelectionPanel pokemonPanel;

    private Image[] combatFrames;
    private int currentFrame = 0;
    private Timer animationTimer;

    /**
     * Contructor de PoobKemonCombat
     * @param fight objeto que contiene la logica del combate, entrenadores, pokemones, movimientos, items y entrenadores
     */
    public PoobKemonCombat(PoobKemonFight fight) {
        super("Tablero Combate");
        this.fight = fight;
        fight.resetTurnToInitial();
        this.currentTurn = fight.getInitialPickerIndex();
        setSize(930, 673);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        contentPanel = new BackgroundPanel("/pokemonPictures/PoobKemonTablero.png");
        contentPanel.setLayout(null);
        setContentPane(contentPanel);

        cursorLabel = new JLabel("▶");
        cursorLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 35));
        cursorLabel.setSize(35, 35);
        contentPanel.add(cursorLabel);
        updateCursor();

        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();

        setVisible(true);
        String firstName = fight.getTrainerName(currentTurn);
        String firstTeam = fight.getTrainerTeam(currentTurn);
        showTurnDialog(firstName, firstTeam,
                String.format("Turno de %s del team %s", firstName, firstTeam));
    }


    /**
     * Actualiza la posicion del cursor segun o que elija el jugador
     */
    private void updateCursor() {
        if (state == State.MENU) {
            Point p = CURSOR_POS[selection];
            cursorLabel.setLocation(p.x, p.y);
            cursorLabel.setVisible(true);
        }
        repaint();
    }

    /**
     * Opciones del tablero principal de la batalla
     * @param e representa el evento de que escoge un jugador
     */
    @Override
    public void keyPressed(KeyEvent e) {

        if (state == State.BATTLE && e.getKeyCode() == KeyEvent.VK_ENTER) {
            enterMoveSelect();
            return;
        }

        if (state == State.MOVE_SELECT) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    moveSelection = (moveSelection + moveCount - 1) % moveCount;
                    break;
                case KeyEvent.VK_DOWN:
                    moveSelection = (moveSelection + 1) % moveCount;
                    break;
                case KeyEvent.VK_ENTER:
                    if (fight.useMove(moveSelection)) {
                        nextCombatTurn();
                        state = State.MENU;
                    }
                    break;
                case KeyEvent.VK_ESCAPE:
                    state = State.MENU;
                    updateCursor();
                    break;
            }
            repaint();
            return;
        }

        if (state == State.POKEMON_SELECT) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                exitPokemonSelect();
            } else {
                pokemonPanel.processKey(e);
            }
            return;
        }

        if (state == State.COMBAT) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                showBattleScreen();
            }
            return;
        }
        if (state == State.BACKPACK) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                if (!backpackPanel.isshowingOptions()) {
                    exitBackpack();
                } else {
                    backpackPanel.ocultarOptions();
                }
            } else {
                backpackPanel.processKey(e);
            }
            return;
        }

        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                selection = (selection % 2 == 0) ? selection + 1 : selection - 1;
                break;
            case KeyEvent.VK_RIGHT:
                selection = (selection % 2 == 1) ? selection - 1 : selection + 1;
                break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_DOWN:
                selection = (selection < 2) ? selection + 2 : selection - 2;
                break;
            case KeyEvent.VK_ENTER:
                executeMenuSelection();
                break;
        }
        updateCursor();
    }

    /**
     * Ejecuta la accion que selecciono el jugador en 4 opciones:
     * lucha, mochila, pokemon y huida
     */
    private void executeMenuSelection() {
        switch (selection) {
            case 0:
                if (!pokemonChosen[currentTurn]) {
                    String name = fight.getTrainerName(currentTurn);
                    String team = fight.getTrainerTeam(currentTurn);
                    showTurnDialog(name, team,
                            String.format("Elije un Pokémon %s del team %s", name, team));

                } else if (!battleStarted && pokemonChosen[0] && pokemonChosen[1]) {
                    battleStarted = true;
                    enterCombat();

                } else if (battleStarted) {
                    enterMoveSelect();
                }
                break;

            case 1:
                if (!pokemonChosen[currentTurn]) {
                    String name = fight.getTrainerName(currentTurn);
                    String team = fight.getTrainerTeam(currentTurn);
                    showTurnDialog(name, team,
                            String.format("Elije un Pokémon %s del team %s", name, team));
                } else if (!battleStarted) {
                    String name = fight.getTrainerName(currentTurn);
                    String team = fight.getTrainerTeam(currentTurn);
                    showTurnDialog(name, team,
                            "Debes pulsar Combate para iniciar la batalla");
                } else {
                    enterBackpack();
                    String name = fight.getTrainerName(currentTurn);
                    String team = fight.getTrainerTeam(currentTurn);
                    showTurnDialog(name, team, "Habilitado en Combate");
                }
                break;

            case 2:
                enterPokemonSelect();
                break;

            case 3: {
                String name = fight.getTrainerName(currentTurn);
                String team = fight.getTrainerTeam(currentTurn);
                showTurnDialog(
                        name,
                        team,
                        String.format("Combate finalizado por %s del team %s", name, team)
                );
                fight.resetSelection();
                dispose();
                new StartPoobKemon().setVisible(true);
                break;
            }
        }
    }

    private void enterMoveSelect() {
        state = State.MOVE_SELECT;
        cursorLabel.setVisible(false);
        moveSelection = 0;
        moveCount = fight.getMoveCount();
        repaint();
    }

    /**
     * Avanza el turno tanto en el dominio como en la GUI,
     * actualiza el cursor y muestra el diálogo de turno.
     */
    private void nextCombatTurn() {
        fight.nextTurn();
        currentTurn = 1 - currentTurn;
        updateCursor();
        String name = fight.getTrainerName(currentTurn);
        String team = fight.getTrainerTeam(currentTurn);
        showTurnDialog(
                name,
                team,
                String.format("Turno de %s del team %s", name, team)
        );
    }



    /**
     * Muestra un aviso centrado, con fondo del color del equipo, indicando el turno
     */
    private void showTurnDialog(String trainerName, String teamColor, String mensaje) {
        boolean blue = teamColor.equalsIgnoreCase("azul");
        Color bg = blue ? Color.CYAN : Color.PINK;

        JDialog d = new JDialog(this, "Aviso", true);
        d.getContentPane().setBackground(bg);
        d.setLayout(new BorderLayout(10, 10));
        d.setSize(360, 240);
        d.setResizable(false);
        d.setLocationRelativeTo(this);

        JLabel lbl = new JLabel(mensaje, SwingConstants.CENTER);
        lbl.setFont(new Font("Press Start 2P", Font.PLAIN, 12));
        lbl.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        d.add(lbl, BorderLayout.NORTH);

        URL url = getClass().getResource("/pokemonPictures/Usos.png");
        if (url != null) {
            JLabel img = new JLabel(new ImageIcon(
                    new ImageIcon(url).getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH)
            ), SwingConstants.CENTER);
            d.add(img, BorderLayout.CENTER);
        }

        JButton ok = new JButton("OK");
        ok.setFont(new Font("Press Start 2P", Font.PLAIN, 10));
        ok.addActionListener(e -> d.dispose());
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.add(ok);
        d.add(p, BorderLayout.SOUTH);

        d.setVisible(true);
    }

    /**
     * Inicia el estado del combate y muestra una animacion
     */
    private void enterCombat() {
        state = State.COMBAT;
        contentPanel.setBackgroundImage("/pokemonPictures/PoobKemonLucha.png");
        cursorLabel.setVisible(false);
        battleStarted = true;
        showBattleScreen();
        state = State.BATTLE;
    }

    /**
     * Animacion entre los 2 entrenadores que se van a enfrentar
     */
    private void startCombatAnimation() {
        if (combatFrames == null) {
            combatFrames = new Image[39];
            for (int i = 0; i < 39; i++) {
                String path = "/animation/Pantalla" + i + ".png";
                URL url = getClass().getResource(path);
                if (url != null) {
                    combatFrames[i] = new ImageIcon(url).getImage();
                } else {
                    System.err.println("No se encontró el recurso: " + path);
                }
            }
        }
        currentFrame = 0;
        animationTimer = new Timer(100, ev -> {
            if (currentFrame < combatFrames.length && combatFrames[currentFrame] != null) {
                contentPanel.setCustomImage(combatFrames[currentFrame]);
            }
            currentFrame++;
            if (currentFrame >= combatFrames.length) {
                animationTimer.stop();
                SwingUtilities.invokeLater(this::showBattleScreen);
            }
        });
        animationTimer.start();
    }

    /**
     * Detiene la animacion del combate
     */
    private void stopCombatAnimation() {
        if (animationTimer != null && animationTimer.isRunning()) animationTimer.stop();
    }

    /**
     * Prepaara la batalla obteniendo informacion de los entrenadores y sus pokemones
     */
    private void showBattleScreen() {
        fight.resetTurnToInitial();
        currentTurn = fight.getInitialPickerIndex();

        String name1    = fight.getPokemonName(0);
        String name2    = fight.getPokemonName(1);
        String backFile = fight.getBackImageName(0);
        String frontFile= fight.getFrontImageName(1);

        int hp1    = fight.getPokemonCurrentHp(0);
        int maxHp1 = fight.getPokemonMaxHp(0);
        int hp2    = fight.getPokemonCurrentHp(1);
        int maxHp2 = fight.getPokemonMaxHp(1);
        String team1 = fight.getTrainerTeam(0);
        String team2 = fight.getTrainerTeam(1);

        BattlePanel bp = new BattlePanel(
                name1, name2,
                backFile, frontFile,
                hp1, maxHp1, hp2, maxHp2,
                team1, team2
        );
        setContentPane(bp);
        revalidate();
        repaint();
    }

    /**
     * Se abre la mochila muestra los items que disponibles
     */
    private void enterBackpack() {
        state = State.BACKPACK;
        contentPanel.setBackgroundImage("/pokemonPictures/PoobKemonMochila.png");
        cursorLabel.setVisible(false);
        if (backpackPanel == null) {
            backpackPanel = new PoobKemonBackPack(this);
            contentPanel.add(backpackPanel);
        }
        backpackPanel.setVisible(true);
        backpackPanel.requestFocusInWindow();
    }

    /**
     * Para salir de la mochila
     */
    private void exitBackpack() {
        state = State.MENU;
        backpackPanel.setVisible(false);
        contentPanel.setBackgroundImage("/pokemonPictures/PoobKemonTablero.png");
        updateCursor();
    }


    /**
     *  Notifica si se salio de la mochila
     */
    public void notifyExitBackpack() {
        exitBackpack();
    }

    /**
     * Permite entrar en la seleccion de pokemones que eligio el entrenador
     */
    private void enterPokemonSelect() {
        state = State.POKEMON_SELECT;
        cursorLabel.setVisible(false);

        if (pokemonPanel != null) {
            contentPanel.remove(pokemonPanel);
        }
        pokemonPanel = new PokemonSelectionPanel(this);
        contentPanel.add(pokemonPanel);
        pokemonPanel.setBounds(0,0, contentPanel.getWidth(), contentPanel.getHeight());
        pokemonPanel.setVisible(true);
        pokemonPanel.requestFocusInWindow();
        contentPanel.repaint();
    }

    /**
     * Sale de la seleccion de pokemones y marca que ya un jugador eligio a su pokemon
     */
    private void exitPokemonSelect() {
        state = State.MENU;
        pokemonChosen[currentTurn] = true;
        if (!(pokemonChosen[0] && pokemonChosen[1])) {
            nextCombatTurn();
        }
        if (pokemonPanel != null) pokemonPanel.setVisible(false);
        contentPanel.setBackgroundImage("/pokemonPictures/PoobKemonTablero.png");
        selection = 0;
        updateCursor();
        requestFocusInWindow();
    }


    /**
     * Para cuando se libera una tecla
     * @param e
     */
    @Override
    public void keyReleased(KeyEvent e) {
    }

    /**
     * Cuando la tecla es oprimida
     * @param e
     */
    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Se encarga de mostrarnos la batalla entre los entrenadores
     */
    private class BattlePanel extends JPanel {
        private final Image backImg, frontImg, bgImage;
        private final String name1, name2;
        private final int hp1, maxHp1, hp2, maxHp2;
        private final String team1, team2;

        /**
         * Construcotor de BattlePanel
         * @param name1 nombre entredandor 1
         * @param name2 nombre entrenador 2
         * @param backFile imagen del pokemon a espaldas
         * @param frontFile imagem del pokemon al frente
         * @param hp1 puntos de salud actuales de pokemon 1
         * @param maxHp1 maxima vida del pokemon 1
         * @param hp2 puntos de salud actuales de pokemon 2
         * @param maxHp2 maxima vida del pokemon 2
         * @param team1 representa el color del team rojo o azul
         * @param team2 representa el color del team rojo o azul
         */
        public BattlePanel(String name1,
                           String name2,
                           String backFile,
                           String frontFile,
                           int hp1, int maxHp1,
                           int hp2, int maxHp2,
                           String team1, String team2) {
            this.name1 = name1;
            this.name2 = name2;
            this.hp1 = hp1;
            this.maxHp1 = maxHp1;
            this.hp2 = hp2;
            this.maxHp2 = maxHp2;
            this.team1 = team1;
            this.team2 = team2;

            Image tmp = null;
            URL u = getClass().getResource("/pokemonPictures/" + backFile);
            backImg = (u != null) ? new ImageIcon(u).getImage() : null;

            u = getClass().getResource("/pokemonPictures/" + frontFile);
            frontImg = (u != null) ? new ImageIcon(u).getImage() : null;

            u = getClass().getResource("/pokemonPictures/PoobKemonLucha.png");
            bgImage = (u != null) ? new ImageIcon(u).getImage() : null;

            setLayout(null);
        }

        private Color teamColor(String team) {
            return "azul".equalsIgnoreCase(team) ? Color.CYAN : Color.PINK;
        }

        /**
         * Se encarga de mostrar los pokemones, su nombre y PS
         * @param g
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int w = getWidth(), h = getHeight();

            if (bgImage != null) {
                g.drawImage(bgImage, 0, 0, w, h, this);
            }

            Font fontName = new Font("Press Start 2P", Font.BOLD, 23);
            Font fontPlain = new Font("Press Start 2P", Font.PLAIN, 14);
            Font fontBold = new Font("Press Start 2P", Font.BOLD, 14);

            g.setFont(fontName);
            FontMetrics fmName = g.getFontMetrics(fontName);
            int name2X = 70;
            int name2Y = 105;
            g.setColor(Color.BLACK);
            g.drawString(name2, name2X, name2Y);

            int name2Width = fmName.stringWidth(name2) + 10;
            String hpText2 = hp2 + "/" + maxHp2;
            g.setFont(fontPlain);
            g.setColor(Color.BLACK);
            g.drawString(hpText2, name2X + name2Width, name2Y);

            int hpText2Width = g.getFontMetrics().stringWidth(hpText2) + 5;
            g.setFont(fontBold);
            g.setColor(teamColor(team2));
            g.drawString("PC", name2X + name2Width + hpText2Width, name2Y);

            g.setFont(fontName);
            g.setColor(Color.BLACK);
            g.drawString(name1, 540, h - 310);

            int barW  = 200, barH = 20;
            int barX2 = 70,  barY2 = 110;
            int barX1 = 540, barY1 = h - 295;

            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(barX2, barY2, barW, barH);
            g.setColor(teamColor(team2));
            int filled2 = (int)((hp2 / (double)maxHp2) * barW);
            g.fillRect(barX2, barY2, filled2, barH);
            g.setColor(Color.BLACK);
            g.drawRect(barX2, barY2, barW, barH);

            int backW  = 215, backH  = 200;
            int frontW = 250, frontH = 250;
            int xBack  = 150, yBack  = 222;
            int xFront = 545, yFront = 42;

            if (backImg != null) {
                g.drawImage(backImg, xBack, yBack, backW, backH, this);
            }
            if (frontImg != null) {
                g.drawImage(frontImg, xFront, yFront, frontW, frontH, this);
            }

            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(barX1, barY1, barW, barH);
            g.setColor(teamColor(team1));
            int filled1 = (int)((hp1 / (double)maxHp1) * barW);
            g.fillRect(barX1, barY1, filled1, barH);
            g.setColor(Color.BLACK);
            g.drawRect(barX1, barY1, barW, barH);

            int textY1 = barY1 + barH + 20;
            g.setFont(fontPlain);
            g.setColor(Color.BLACK);
            g.drawString(hp1 + " / " + maxHp1, barX1 + 4, textY1);
            g.setFont(fontBold);
            g.setColor(teamColor(team1));
            g.drawString("PC", barX1 + barW - 40, textY1);

            if (state == State.MOVE_SELECT) {
                int panelY   = getHeight() - 180;
                int leftW    = 300,    leftH    = 160;
                int leftX    = 50,     leftY   = panelY;
                int rightW   = 200,    rightH   = 160;
                int rightX   = leftX + leftW + 10, rightY = panelY;
                Font fontMove = new Font("Press Start 2P", Font.PLAIN, 14);

                g.setColor(new Color(0,0,0,120));
                g.fillRoundRect(leftX, leftY, leftW, leftH, 16,16);
                g.fillRoundRect(rightX, rightY, rightW, rightH, 16,16);

                FontMetrics fm = g.getFontMetrics(fontMove);
                int lineH = fm.getHeight() + 8;
                for (int i = 0; i < fight.getMoveCount(); i++) {
                    int col = (i < 2) ? 0 : 1;
                    int row = i % 2;
                    int x = leftX + 10 + col * (leftW/2);
                    int y = leftY + 20 + row * lineH;

                    if (i == moveSelection) {
                        g.setFont(fontMove.deriveFont(Font.BOLD, 16f));
                        g.setColor(teamColor(fight.getTrainerTeam(currentTurn)));
                        g.drawString("▶", leftX + 4, y);
                    }


                    g.setFont(fontMove);
                    g.setColor(i == moveSelection ? Color.BLACK : Color.LIGHT_GRAY);
                    g.drawString(fight.getMoveName(i), x, y);
                }

                String ppInfo   = String.format("PP %d/%d", fight.getMovePPCurrent(moveSelection), fight.getMovePPMax(moveSelection));
                String typeInfo = "TIPO/" + fight.getMoveType(moveSelection).name();

                FontMetrics fmPlain = g.getFontMetrics(fontMove);
                int textYY1 = rightY + 20 + fmPlain.getAscent();
                int textY2 = textYY1 + lineH;

                g.setFont(fontMove);
                g.setColor(Color.LIGHT_GRAY);
                g.drawString("PP", rightX + 10, textYY1);
                g.setColor(Color.BLACK);
                g.drawString(ppInfo, rightX + 60, textYY1);

                g.setFont(fontMove);
                g.setColor(Color.LIGHT_GRAY);
                g.drawString("TIPO", rightX + 10, textY2);
                g.setColor(Color.BLACK);
                g.drawString(typeInfo, rightX + 60, textY2);
            }
        }

    }


    /**
     * Para la seleccion de los pokemones permitiendo elegir entre 6 de ellos
     * y salir
     */
    private class PokemonSelectionPanel extends JPanel {
        private boolean inInfo = false;
        private boolean inExit = false;
        private int selected = 0;

        private static final Point INFO_POS = new Point(80, 120);
        private static final Point[] SLOT_POS = {
                new Point(370, 82),
                new Point(370, 182),
                new Point(370, 282),
                new Point(370, 382),
                new Point(370, 481)
        };
        private static final Point EXIT_POS = new Point(715, 576);

        private static final int ICON_SIZE_CLOSED = 82;
        private static final int ICON_SIZE_OPENED = 85;

        private final Image bgImage;
        private final Image closeImage;
        private final Image openImage;
        private final Image pokemonMini;

        /**
         * Constructor de PokemonSelectionPanel
         * @param parent referencia a la ventana de combate
         */
        public PokemonSelectionPanel(PoobKemonCombat parent) {
            setOpaque(false);
            setFocusable(true);
            setPreferredSize(new Dimension(930, 673));

            URL bgUrl = getClass().getResource("/pokemonPictures/PoobKemonSeleccion2.png");
            bgImage = (bgUrl != null) ? new ImageIcon(bgUrl).getImage() : null;

            ImageIcon rawClose = new ImageIcon(getClass().getResource("/pokemonPictures/PokebolaClose.png"));
            closeImage = rawClose.getImage()
                    .getScaledInstance(ICON_SIZE_CLOSED, ICON_SIZE_CLOSED, Image.SCALE_SMOOTH);

            ImageIcon rawOpen = new ImageIcon(getClass().getResource("/pokemonPictures/PokebolaOpen.png"));
            openImage = rawOpen.getImage()
                    .getScaledInstance(ICON_SIZE_OPENED, ICON_SIZE_OPENED, Image.SCALE_SMOOTH);

            String miniName = PoobKemonCombat.this.fight.getMiniGifName(PoobKemonCombat.this.currentTurn);
            Image tmpMini = null;
            if (miniName != null) {
                URL miniUrl = getClass().getResource("/pokemonPictures/" + miniName);
                if (miniUrl != null) {
                    tmpMini = new ImageIcon(miniUrl).getImage();
                } else {
                    System.err.println("No se encontró mini gif: " + miniName);
                }
            }
            pokemonMini = tmpMini;

            addKeyListener(new KeyAdapter() {
                /**
                 * procesa las teclas oprimidas
                 * @param e el evento de oprimir una tecla
                 */
                @Override
                public void keyPressed(KeyEvent e) {
                    processKey(e);
                }
            });
            SwingUtilities.invokeLater(this::requestFocusInWindow);
        }

        /**
         * Muestra 6 pokebolas con sus mini pokemones adentro
         * depende del usario al mover las flechas si ls pokebola abre o cierra
         * @param g  graficador
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (bgImage != null) {
                g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            }

            if (!inInfo) {
                g.drawImage(closeImage,
                        INFO_POS.x - ICON_SIZE_CLOSED / 2,
                        INFO_POS.y - ICON_SIZE_CLOSED / 2,
                        ICON_SIZE_CLOSED, ICON_SIZE_CLOSED, this);
            }

            for (int i = 0; i < SLOT_POS.length; i++) {
                if (!inInfo && !inExit && i == selected) continue;
                Point slot = SLOT_POS[i];
                g.drawImage(closeImage,
                        slot.x - ICON_SIZE_CLOSED / 2,
                        slot.y - ICON_SIZE_CLOSED / 2,
                        ICON_SIZE_CLOSED, ICON_SIZE_CLOSED, this);
            }

            if (!inExit) {
                g.drawImage(closeImage,
                        EXIT_POS.x - ICON_SIZE_CLOSED / 2,
                        EXIT_POS.y - ICON_SIZE_CLOSED / 2,
                        ICON_SIZE_CLOSED, ICON_SIZE_CLOSED, this);
            }

            if (inInfo) {
                g.drawImage(openImage,
                        INFO_POS.x - ICON_SIZE_OPENED / 2,
                        INFO_POS.y - ICON_SIZE_OPENED / 2,
                        ICON_SIZE_OPENED, ICON_SIZE_OPENED, this);
            } else if (inExit) {
                g.drawImage(openImage,
                        EXIT_POS.x - ICON_SIZE_OPENED / 2,
                        EXIT_POS.y - ICON_SIZE_OPENED / 2,
                        ICON_SIZE_OPENED, ICON_SIZE_OPENED, this);
            } else {
                Point sel = SLOT_POS[selected];
                g.drawImage(openImage,
                        sel.x - ICON_SIZE_OPENED / 2,
                        sel.y - ICON_SIZE_OPENED / 2,
                        ICON_SIZE_OPENED, ICON_SIZE_OPENED, this);
            }

            if (pokemonMini != null) {
                int size = 80;
                int x = INFO_POS.x - size / 2 + 10;
                int y = INFO_POS.y - size / 2 - 3;
                g.drawImage(pokemonMini, x, y, size, size, this);
            }

        }

        /**
         * Las teclase que se puede oprimir
         * 5 en columa pokebolas de seleccion
         * 1 a la izquieda otra pokebola de seleccion principal
         * 1 a la derecha para salir
         * @param e el evento
         */
        private void processKey(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (inExit) {
                        inExit = false;
                    } else {
                        inInfo = true;
                    }
                    break;

                case KeyEvent.VK_RIGHT:
                    if (inInfo) {
                        inInfo = false;
                    } else {
                        inExit = true;
                    }
                    break;

                case KeyEvent.VK_DOWN:
                    if (inInfo) {
                        inInfo = false;
                        selected = 0;
                    } else if (!inExit) {
                        selected = (selected + 1) % SLOT_POS.length;
                    }
                    break;

                case KeyEvent.VK_UP:
                    if (inExit) {
                        inExit = false;
                        selected = SLOT_POS.length - 1;
                    } else if (!inInfo) {
                        selected = (selected - 1 + SLOT_POS.length) % SLOT_POS.length;
                    }
                    break;

                case KeyEvent.VK_ESCAPE:
                case KeyEvent.VK_ENTER:
                    exitPokemonSelect();
                    return;

                default:
                    return;
            }
            repaint();
        }
    }


    /**
     * Representa la mochila del entrenador con los items disponibles y
     * permite seleccionar y usar diferentes items durante el combate
     */
    private class PoobKemonBackPack extends JPanel {
        private PoobKemonCombat parent;
        private static final Rectangle DESC_BOUNDS = new Rectangle(50, 420, 330, 150);
        private static final Rectangle IMG_BOUNDS = new Rectangle(15, 260, 120, 120);
        private String[] items = {"Poción", "Superpoción", "Hiperpoción", "Revivir"};
        private int seleccion = 0;
        private JTextArea description;
        private JLabel itemImageLabel;
        private JLabel cursor;
        private boolean showingOptions = false;
        private String[] Options = {"USAR", "SALIR"};
        private int opcionSelected = 0;
        private JPanel panelItems, panelDesc, panelOptions;
        private JLabel[] etiquetasItems, etiquetasOptions;
        private JLabel cursorOptions;

        /**
         * Constructor de PoobKemonBackPack
         * @param parent referencia a la ventana de combate
         */
        public PoobKemonBackPack(PoobKemonCombat parent) {
            this.parent = parent;
            setLayout(null);
            setBounds(0, 0, 930, 673);
            setOpaque(false);
            setFocusable(true);
            addKeyListener(new KeyAdapter() {
                /**
                 * procesa las teclas precionadas
                 * @param e evento de proceso
                 */
                @Override
                public void keyPressed(KeyEvent e) {
                    processKey(e);
                }
            });

            setupItemsPanel();
            itemImageLabel = new JLabel();
            itemImageLabel.setBounds(IMG_BOUNDS);
            add(itemImageLabel);
            panelDesc = createDescPanel();
            add(panelDesc);
            updateCursor();
        }

        /**
         * Posiciones de los objetos que se pueden seleccionar
         * pocion, superpocion y hiperpocion
         */
        private void setupItemsPanel() {
            panelItems = new JPanel(null);
            panelItems.setBounds(430, 80, 450, 400);
            panelItems.setOpaque(false);
            add(panelItems);
            Font f = new Font("Press Start 2P", Font.PLAIN, 24);
            etiquetasItems = new JLabel[items.length];
            for (int i = 0; i < items.length; i++) {
                etiquetasItems[i] = new JLabel(items[i] + " x" + (i < 3 ? "2" : "1"));
                etiquetasItems[i].setFont(f);
                etiquetasItems[i].setBounds(25, 20 + i * 90, 380, 40);
                panelItems.add(etiquetasItems[i]);
            }
            cursor = new JLabel("▶");
            cursor.setFont(f);
            cursor.setBounds(0, 20, 30, 40);
            cursor.setVisible(false);
            panelItems.add(cursor);
        }

        /**
         * Decribe cada item que el entrenador puede usar
         * @return p la descripcion
         */
        private JPanel createDescPanel() {
            JPanel p = new JPanel(new BorderLayout());
            p.setBounds(DESC_BOUNDS);
            p.setOpaque(false);

            description = new JTextArea();
            description.setLineWrap(true);
            description.setWrapStyleWord(true);
            description.setOpaque(false);
            description.setEditable(false);
            description.setForeground(Color.BLACK);
            description.setFont(new Font("Press Start 2P", Font.PLAIN, 15));

            JScrollPane scrollPane = new JScrollPane(description);
            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(false);
            scrollPane.setBorder(null);

            p.add(scrollPane, BorderLayout.CENTER);
            return p;
        }

        /**
         * Panel para ver si el jugador usa o no un item
         */
        public void ocultarOptions() {
            if (panelOptions != null) {
                remove(panelOptions);
                panelOptions = null;
            }
            showingOptions = false;
            opcionSelected = 0;
            updateCursor();
            requestFocusInWindow();
            repaint();
        }

        /**
         * Se oculta el panel segun lo que elija el entrenador
         * @return
         */
        public boolean isshowingOptions() {
            return showingOptions;
        }

        /**
         * Si se da escape sale de la vetentda
         * @param e de oprimir esc
         */
        public void processKey(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                if (showingOptions) {
                    ocultarOptions();
                } else {
                    parent.notifyExitBackpack();
                }
                return;
            }

            if (!showingOptions) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_DOWN:
                        seleccion = (seleccion + 1) % items.length;
                        break;
                    case KeyEvent.VK_UP:
                        seleccion = (seleccion - 1 + items.length) % items.length;
                        break;
                    case KeyEvent.VK_ENTER:
                        mostrarOptions();
                        return;
                }
            } else {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_DOWN:
                        opcionSelected = (opcionSelected + 1) % Options.length;
                        updateCursorOptions();
                        break;
                    case KeyEvent.VK_UP:
                        opcionSelected = (opcionSelected - 1 + Options.length) % Options.length;
                        updateCursorOptions();
                        break;
                    case KeyEvent.VK_ENTER:
                        executeOption(opcionSelected);
                        return;
                }
            }
            updateCursor();
        }

        /**
         * Actualiza el cursor
         */
        private void updateCursor() {
            cursor.setVisible(true);
            cursor.setLocation(0, 20 + seleccion * 90);
            mostrardescription();
            repaint();
        }

        /**
         * Muestra la descripcion en un panel de abajo a la izquierda
         */
        private void mostrardescription() {
            description.setFont(new Font("Press Start 2P", Font.PLAIN, 14));
            String it = items[seleccion];
            description.setText("");
            itemImageLabel.setIcon(null);

            if (it.equals("Poción")) {
                description.setText("""
                        Cura 20 PS de tu Pokémon.
                        
                        Es una poción básica que 
                        puedes usar en combate.
                        """);
                setItemImage("/pokemonPictures/Pocion.png");

            } else if (it.equals("Superpoción")) {
                description.setText("""
                        Cura 50 PS de tu Pokémon.
                        
                        No sirve si está debilitado.
                        """);
                setItemImage("/pokemonPictures/SuperPocion.png");

            } else if (it.equals("Hiperpoción")) {
                description.setText("""
                        Cura 200 PS de tu Pokémon.
                        
                        No se puede usar en un 
                        Pokémon con 0 PS.
                        """);
                setItemImage("/pokemonPictures/HiperPocion.png");

            } else if (it.equals("Revivir")) {
                description.setText("""
                        Revive a un Pokémon con
                        la mitad de sus PS.
                        
                        Solo funciona si está KO.
                        """);
                setItemImage("/pokemonPictures/Revivir.png");
            }
        }

        /**
         * para las imagenes de los items de pokemonPictures
         * @param path pokemonPictures
         */
        private void setItemImage(String path) {
            Image img = loadImage(path);
            if (img != null)
                itemImageLabel.setIcon(new ImageIcon(img.getScaledInstance(IMG_BOUNDS.width, IMG_BOUNDS.height, Image.SCALE_SMOOTH)));
        }

        /**
         * Muestra las opciones que puede elegir  el entrenador
         * usar y salir
         */
        private void mostrarOptions() {
            if (panelOptions != null) remove(panelOptions);
            showingOptions = true;
            panelOptions = new JPanel(null) {
                /**
                 * Imagen pequeña
                 * @param g grafica la imagen
                 */
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Image bg = loadImage("/pokemonPictures/Usos.png");
                    if (bg != null) g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
                }
            };
            panelOptions.setBounds(690, 500, 240, 140);
            panelOptions.setOpaque(false);
            add(panelOptions);

            Font f = new Font("Press Start 2P", Font.PLAIN, 14);
            etiquetasOptions = new JLabel[Options.length];
            for (int i = 0; i < Options.length; i++) {
                etiquetasOptions[i] = new JLabel(Options[i]);
                etiquetasOptions[i].setFont(f);
                etiquetasOptions[i].setBounds(50, 30 + i * 40, 150, 25);
                panelOptions.add(etiquetasOptions[i]);
            }

            cursorOptions = new JLabel("▶");
            cursorOptions.setFont(f);
            cursorOptions.setBounds(35, 30, 30, 25);
            panelOptions.add(cursorOptions);

            repaint();
            updateCursorOptions();
            requestFocusInWindow();
        }

        /**
         * Actualiza el cursos del mini panel
         */
        private void updateCursorOptions() {
            if (cursorOptions != null) {
                cursorOptions.setLocation(35, 30 + opcionSelected * 40);
                panelOptions.repaint();
            }
        }

        /**
         * Si elije usar item que sera aplicado en el pokemon
         * o salir y elejir otro item
         * @param op opciones
         */
        private void executeOption(int op) {
            if (Options[op].equals("USAR")) {
                description.setText("Usaste " + items[selection] + "!");
            } else {
                remove(panelOptions);
                revalidate();
                repaint();
                showingOptions = false;
                opcionSelected = 0;
                updateCursor();
                requestFocusInWindow();
            }
            repaint();
        }


        /**
         * carga la imagen de pokemonPictures
         * @param path pokemonPictures
         * @return la imagen
         */
        private Image loadImage(String path) {
            URL url = getClass().getResource(path);

            if (url != null) {
                ImageIcon icon = new ImageIcon(url);
                return icon.getImage();
            } else {
                return null;
            }
        }

    }



    private static class BackgroundPanel extends JPanel {
        private Image background;

        public BackgroundPanel(String path) {
            load(path);
        }

        public void setBackgroundImage(String path) {
            load(path);
        }

        public void setCustomImage(Image img) {
            this.background = img;
            repaint();
        }

        private void load(String path) {
            URL url = getClass().getResource(path);
            if (url != null) {
                ImageIcon icon = new ImageIcon(url);
                background = icon.getImage();
            } else {
                background = null;
                System.err.println("No se pudo cargar la imagen de fondo: " + path);
            }
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (background != null) {
                g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
            }
        }

    }

    public static void main(String[] args) {
        Trainer t1 = new Trainer("Ash", "azul");
        Trainer t2 = new Trainer("Misty", "rojo");

        PoobKemonFight fight = new PoobKemonFight(t1, t2, 1);
        fight.selectPokemon("RAICHU");
        fight.nextTurn();
        fight.selectPokemon("GENGAR");
        fight.resetTurnToInitial();

        SwingUtilities.invokeLater(() -> {
            PoobKemonCombat window = new PoobKemonCombat(fight);
            window.setVisible(true);
        });
    }
}