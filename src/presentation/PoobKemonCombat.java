package presentation;

import domain.*;
import domain.PoobKemonFight.UIState;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.io.File;
import java.io.IOException;

/**
 * Batalla POOBKemon entre dos entrenadores, cada uno con 6 pokemones de diferentes tipos
 * y movimientos
 */
public class PoobKemonCombat extends JFrame implements KeyListener {
    private final PoobKemonFight fight;
    private int currentTurn;
    private final boolean bothAI;
    private final boolean[] pokemonChosen = {false, false};
    private boolean battleStarted = false;
    private boolean inBattleSelect = false;
    private List<String> logMessages = new ArrayList<>();
    private Timer turnTimer;

    //Cursor del tablero incial
    private static final Point[] CURSOR_POS = {
            new Point(488, 477),
            new Point(690, 477),
            new Point(488, 540),
            new Point(690, 540)
    };

    //Cursor de las opciones de movimientos
    private static final Point[] MOVE_CURSOR_POS = {
            new Point(70, 500),
            new Point(470, 500),
            new Point(70, 560),
            new Point(470, 560)
    };

    private enum State {
        MENU, BATTLE, MOVE_SELECT, COMBAT, BACKPACK, POKEMON_SELECT, ITEM_SELECT, LOG, PAUSED
    }

    private State state = State.MENU;
    private int moveSelection;
    private int moveCount;

    private int selection = 0;
    private JLabel cursorLabel;
    private BackgroundPanel contentPanel;
    private PoobKemonBackPack backpackPanel;
    private PokemonSelectionPanel pokemonPanel;

    private String pendingItem;
    private Image[] combatFrames;
    private int currentFrame = 0;
    private Timer animationTimer;

    /**
     * Contructor de PoobKemonCombat
     *
     * @param fight objeto que contiene la logica del combate, entrenadores, pokemones, movimientos, items y entrenadores
     */
    public PoobKemonCombat(PoobKemonFight fight) {
        super("Tablero Combate");
        this.fight = fight;
        this.bothAI = fight.getTrainer1().isAI() && fight.getTrainer2().isAI();
        fight.resetTurnToInitial();
        this.currentTurn = fight.getInitialPickerIndex();
        if (fight.getTrainer1().isAI() ^ fight.getTrainer2().isAI()) {
            if (fight.getCurrentTrainer().isAI()) {
                fight.nextTurn();
                this.currentTurn = 1 - this.currentTurn;
            }
        }
        setJMenuBar(createMenuBar());
        int menuBarHeight = getJMenuBar().getPreferredSize().height;
        setSize(930, 673 + menuBarHeight);
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

        if (bothAI) {
            for (int i = 0; i < 2; i++) {
                AITrainer ai = (AITrainer) (i == 0 ? fight.getTrainer1() : fight.getTrainer2());
                int switchIdx = ai.autoChooseSwitch(fight);
                String key = ai.getTeam().get(switchIdx).getName().toUpperCase();
                fight.switchActivePokemon(key);
                pokemonChosen[i] = true;
                fight.nextTurn();
                currentTurn = 1 - currentTurn;
            }
            battleStarted = true;
            enterCombat();
        } else {
            String firstName = fight.getTrainerName(currentTurn);
            String firstTeam = fight.getTrainerTeam(currentTurn);
            if (!fight.getCurrentTrainer().isAI()) {
                showTurnDialog(firstName, firstTeam,
                        String.format("Turno de %s del team %s", firstName, firstTeam));
            } else {
                enterPokemonSelect();
            }
        }
    }

    /**
     * Constructor para reanudar un estado de combate previamente guardado.
     * Simplemente muestra el tablero en estado MENU.
     */
    public PoobKemonCombat(PoobKemonFight fight, boolean resume) {
        super("Tablero Combate");
        this.fight = fight;
        this.bothAI = fight.getTrainer1().isAI() && fight.getTrainer2().isAI();
        this.currentTurn = fight.getInitialTurn();

        setJMenuBar(createMenuBar());
        int menuH = getJMenuBar().getPreferredSize().height;
        setSize(930, 673 + menuH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        for (PoobKemon p : fight.getTrainer1().getTeam()) {
            p.setFightReference(fight);
            for (MovePoobKemon m : p.getMovements()) {
                m.setFightReference(fight);
            }
        }
        for (PoobKemon p : fight.getTrainer2().getTeam()) {
            p.setFightReference(fight);
            for (MovePoobKemon m : p.getMovements()) {
                m.setFightReference(fight);
            }
        }

        contentPanel = new BackgroundPanel("/pokemonPictures/PoobKemonTablero.png");
        contentPanel.setLayout(null);
        setContentPane(contentPanel);

        cursorLabel = new JLabel("▶");
        cursorLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 35));
        cursorLabel.setSize(35, 35);
        contentPanel.add(cursorLabel);

        pokemonChosen[0] = true;
        pokemonChosen[1] = true;
        battleStarted = true;

        UIState last = fight.getUiState();
        switch (last) {
            case MENU:
                state = State.MENU;
                showBoardScreen();
                updateCursor();
                break;
            case BACKPACK:
                state = State.BACKPACK;
                showBoardScreen();
                enterBackpack();
                break;
            case POKEMON_SELECT:
                state = State.POKEMON_SELECT;
                showBoardScreen();
                enterPokemonSelect();
                break;
            case ITEM_SELECT:
                state = State.ITEM_SELECT;
                showBoardScreen();
                enterItemSelect(fight.getLastPendingItem());
                break;
            case MOVE_SELECT:
                state = State.MOVE_SELECT;
                showBoardScreen();
                enterMoveSelect();
                break;
            case LOG:
                state = State.LOG;
                showBoardScreen();
                displayLog(fight.drainLog());
                break;
            default:
                state = State.MENU;
                showBoardScreen();
                updateCursor();
        }

        addKeyListener(this);
        setFocusable(true);
        setVisible(true);
    }



    /**
     * Crea el menú superior con las opciones Guardar/Cargar.
     */
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("Archivo");
        menuBar.add(fileMenu);

        JMenuItem save = new JMenuItem("Guardar");
        save.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                try {
                    fight.saveToFile(file.getAbsolutePath());
                    JOptionPane.showMessageDialog(this,
                            "Partida guardada en:\n" + file.getAbsolutePath(),
                            "Guardado exitoso",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Error al guardar:\n" + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        fileMenu.add(save);

        JMenuItem load = new JMenuItem("Cargar");
        load.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                try {
                    PoobKemonFight loaded = PoobKemonFight.loadFromFile(file.getAbsolutePath());
                    this.dispose();
                    PoobKemonCombat ventana = new PoobKemonCombat(loaded, true);
                    ventana.setVisible(true);
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this,
                            "Error al cargar:\n" + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                System.out.println("DEBUG: showOpenDialog no se aprobo :(");
            }
        });
        fileMenu.add(load);

        return menuBar;
    }

    /**
     * Para los mensajes de quitar daño de los movimientos de los pokemones
     * @param messages en lista de los movimientos
     */
    private void displayLog(List<String> messages) {
        this.logMessages = new ArrayList<>(messages);
        showBoardScreen();
        state = State.LOG;
        fight.setUiState(UIState.LOG);
        cursorLabel.setVisible(false);
        repaint();

        if (bothAI) {
            new Timer(2500, ev -> {
                ((Timer)ev.getSource()).stop();
                handleLogAdvance();
               }).start();
        }
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
     *
     * @param e representa el evento de que escoge un jugador
     */
    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_P) {
            if (state != State.PAUSED) {
                State prevState = state;
                state = State.PAUSED;
                fight.setUiState(UIState.PAUSED);
                if (turnTimer != null && turnTimer.isRunning()) turnTimer.stop();
                if (animationTimer != null && animationTimer.isRunning()) animationTimer.stop();

                JOptionPane.showMessageDialog(
                        this,
                        "POOBKemon en pausa.\nPulsa Aceptar para reanudar.",
                        "PAUSA",
                        JOptionPane.INFORMATION_MESSAGE
                );

                state = prevState;
                if (turnTimer != null) turnTimer.start();
                if (animationTimer != null) animationTimer.start();
            }
            return;
        }

        if (state == State.ITEM_SELECT) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_ENTER:
                    applyPendingItemOnPokemon();
                    return;
                case KeyEvent.VK_ESCAPE:
                    state = State.BACKPACK;
                    fight.setUiState(UIState.BACKPACK);
                    enterBackpack();
                    return;
                default:
                    pokemonPanel.processKey(e);
                    return;
            }
        }

        if (state == State.MOVE_SELECT) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (moveSelection % 2 == 1) moveSelection--;
                    break;
                case KeyEvent.VK_RIGHT:
                    if (moveSelection % 2 == 0 && moveSelection + 1 < moveCount) moveSelection++;
                    break;
                case KeyEvent.VK_UP:
                    if (moveSelection >= 2) moveSelection -= 2;
                    break;
                case KeyEvent.VK_DOWN:
                    if (moveSelection + 2 < moveCount) moveSelection += 2;
                    break;
                case KeyEvent.VK_ENTER:
                    boolean anyPP = false;
                    for (int i = 0; i < fight.getMoveCount(); i++) {
                        if (fight.getMovePPCurrent(i) > 0) {
                            anyPP = true;
                            break;
                        }
                    }
                    if (anyPP) {
                        fight.useMove(moveSelection);
                        List<String> log = fight.drainLog();
                        displayLog(log);
                    } else {
                        fight.useStruggle();
                        List<String> log = fight.drainLog();
                        displayLog(log);
                    }
                    break;
                case KeyEvent.VK_ESCAPE:
                    state = State.MENU;
                    fight.setUiState(UIState.MENU);
                    updateCursor();
                    return;
            }

            repaint();
            return;
        }

        if (state == State.LOG) {
            if (!fight.getCurrentTrainer().isAI()) {
                if (!(e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_ESCAPE)) {
                    return;
                }
            }

            boolean p1Defeated = fight.isTrainerDefeated(0);
            boolean p2Defeated = fight.isTrainerDefeated(1);

            if (p1Defeated || p2Defeated) {
                int winnerIdx = p1Defeated ? 1 : 0;
                String winnerName = fight.getTrainerName(winnerIdx);
                String winnerTeam = fight.getTrainerTeam(winnerIdx);
                String message = String.format("¡%s del team %s ha ganado la batalla!", winnerName, winnerTeam);

                Color bg = "azul".equalsIgnoreCase(winnerTeam) ? Color.CYAN : Color.PINK;
                ImageIcon trophyIcon = null;
                URL trophyUrl = getClass().getResource("/pokemonPictures/trofeo.png");
                if (trophyUrl != null) trophyIcon = new ImageIcon(trophyUrl);

                JPanel panel = new JPanel(new BorderLayout());
                panel.setBackground(bg);
                JLabel lbl = new JLabel(message, SwingConstants.CENTER);
                lbl.setFont(new Font("Press Start 2P", Font.PLAIN, 14));
                panel.add(lbl, BorderLayout.NORTH);
                if (trophyIcon != null) {
                    JLabel iconLbl = new JLabel(trophyIcon, SwingConstants.CENTER);
                    panel.add(iconLbl, BorderLayout.CENTER);
                }
                JOptionPane.showMessageDialog(this, panel, "Fin del juego", JOptionPane.PLAIN_MESSAGE);

                fight.resetSelection();
                dispose();
                new StartPoobKemon().setVisible(true);
                return;
            }

            fight.nextTurn();
            currentTurn = 1 - currentTurn;

            if (fight.getCurrentTrainer().isAI() && fight.getPokemonCurrentHp(currentTurn) == 0) {
                AITrainer ai = (AITrainer) fight.getCurrentTrainer();
                int switchIdx = ai.autoChooseSwitch(fight);
                String key = fight.getCurrentTrainer()
                        .getTeam()
                        .get(switchIdx)
                        .getName()
                        .toUpperCase();
                fight.switchActivePokemon(key);
                fight.nextTurn();
                currentTurn = 1 - currentTurn;
            }

            showBoardScreen();
            if (fight.getCurrentTrainer().isAI()) {
                enterMoveSelect();
            }
            else if (fight.getPokemonCurrentHp(currentTurn) == 0) {
                inBattleSelect = true;
                setContentPane(contentPanel);
                contentPanel.setBackgroundImage("/pokemonPictures/PoobKemonTablero.png");
                contentPanel.revalidate();
                contentPanel.repaint();
                enterPokemonSelect();
            }

            else {
                state = State.MENU;
                fight.setUiState(UIState.MENU);
                selection = 0;
                updateCursor();
            }
            return;
        }



        if (state == State.POKEMON_SELECT) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                cancelPokemonSelect();
            } else {
                pokemonPanel.processKey(e);
            }
            return;
        }

        if (state == State.COMBAT) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE && animationTimer != null && animationTimer.isRunning()) {
                animationTimer.stop();
                fight.resetTurnToInitial();
                currentTurn = fight.getInitialPickerIndex();
                showBattleScreen();
                enterMoveSelect();
            }
            return;
        }

        if (state == State.BACKPACK) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                if (!backpackPanel.isShowingOptions()) {
                    cancelBackpack();
                } else {
                    backpackPanel.hideOptions();
                }
            } else {
                backpackPanel.processKey(e);
            }
            return;
        }


        if (state == State.MENU) {
            int prev = selection;
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
                    return;
            }

            if (e.getKeyCode() != KeyEvent.VK_ENTER
                    && !battleStarted
                    && pokemonChosen[0]
                    && pokemonChosen[1]
                    && selection == 2) {
                selection = prev;
            }

            updateCursor();
            return;
        }
    }


    /**
     * Ejecuta la accion que selecciono el jugador en 4 opciones:
     * lucha, mochila, pokemon y huida
     */
    private void executeMenuSelection() {
        switch (selection) {
            case 0: //Combate

                if (!pokemonChosen[currentTurn]) {
                    if (fight.getCurrentTrainer().isAI()) {
                        AITrainer ai = (AITrainer) fight.getCurrentTrainer();
                        int switchIdx = ai.autoChooseSwitch(fight);
                        String key = fight.getCurrentTrainer()
                                .getTeam()
                                .get(switchIdx)
                                .getName()
                                .toUpperCase();
                        fight.switchActivePokemon(key);
                        pokemonChosen[currentTurn] = true;

                        if (pokemonChosen[0] && pokemonChosen[1]) {
                            battleStarted = true;
                            enterCombat();
                        } else {

                            fight.nextTurn();
                            currentTurn = 1 - currentTurn;
                            showTurnDialog(
                                    fight.getTrainerName(currentTurn),
                                    fight.getTrainerTeam(currentTurn),
                                    String.format("Turno de %s del team %s",
                                            fight.getTrainerName(currentTurn),
                                            fight.getTrainerTeam(currentTurn)
                                    )
                            );
                        }
                    } else {
                        showTurnDialog(
                                fight.getTrainerName(currentTurn),
                                fight.getTrainerTeam(currentTurn),
                                String.format("Elige un Pokémon %s team %s",
                                        fight.getTrainerName(currentTurn),
                                        fight.getTrainerTeam(currentTurn)
                                )
                        );
                    }
                }
                else if (!battleStarted && pokemonChosen[0] && pokemonChosen[1]) {
                    battleStarted = true;
                    enterCombat();
                }
                else if (battleStarted) {
                    showBattleScreen();
                    enterMoveSelect();
                }
                break;

            case 1: //Mochila
                if (!battleStarted) {
                    showTurnDialog(fight.getTrainerName(currentTurn),
                            fight.getTrainerTeam(currentTurn),
                            "Debes pulsar Combate para iniciar la batalla");
                } else {
                    enterBackpack();
                }
                break;

            case 2: //Pokemon
                if (!battleStarted) {

                    if (!pokemonChosen[currentTurn]) {
                        enterPokemonSelect();
                    } else {
                        showTurnDialog(
                                fight.getTrainerName(currentTurn),
                                fight.getTrainerTeam(currentTurn),
                                "Debes pulsar Combate para iniciar la batalla"
                        );
                    }
                } else {
                    setContentPane(contentPanel);
                    contentPanel.setBackgroundImage("/pokemonPictures/PoobKemonTablero.png");
                    contentPanel.revalidate();
                    contentPanel.repaint();

                    inBattleSelect = true;
                    enterPokemonSelect();
                }
                break;

            case 3: //HUIDA
                if (!battleStarted) {
                    showTurnDialog(fight.getTrainerName(currentTurn),
                            fight.getTrainerTeam(currentTurn),
                            "Debes pulsar Combate para iniciar la batalla");
                } else {
                    showTurnDialog(fight.getTrainerName(currentTurn),
                            fight.getTrainerTeam(currentTurn),
                            String.format("Combate finalizado por %s del team %s",
                                    fight.getTrainerName(currentTurn),
                                    fight.getTrainerTeam(currentTurn)));
                    fight.resetSelection();
                    dispose();
                    new StartPoobKemon().setVisible(true);
                }
                break;
        }
    }


    private void enterMoveSelect() {
        state = State.MOVE_SELECT;
        fight.setUiState(UIState.MOVE_SELECT);
        moveSelection = 0;
        moveCount = fight.getMoveCount();
        Point p = MOVE_CURSOR_POS[moveSelection];
        cursorLabel.setLocation(p.x, p.y);
        cursorLabel.setVisible(true);

        if (!bothAI) {
            startTurnTimer();
        }
        repaint();

        if (fight.getCurrentTrainer().isAI()) {
            AITrainer ai = (AITrainer) fight.getCurrentTrainer();
            int idx = ai.autoChooseMove(fight);
            fight.useMove(idx);
            List<String> log = fight.drainLog();
            displayLog(log);
        }
    }


    /**
     * Muestra un aviso centrado, con fondo del color del equipo, indicando el turno
     */
    private void showTurnDialog(String trainerName, String teamColor, String message) {
        boolean blue = teamColor.equalsIgnoreCase("azul");
        Color bg = blue ? Color.CYAN : Color.PINK;

        JDialog d = new JDialog(this, "Aviso", true);
        d.getContentPane().setBackground(bg);
        d.setLayout(new BorderLayout(10, 10));
        d.setSize(360, 240);
        d.setResizable(false);
        d.setLocationRelativeTo(this);

        JLabel lbl = new JLabel(message, SwingConstants.CENTER);
        lbl.setFont(new Font("Press Start 2P", Font.PLAIN, 10));
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
        fight.setUiState(UIState.COMBAT);
        contentPanel.setBackgroundImage("/pokemonPictures/PoobKemonLucha.png");
        cursorLabel.setVisible(false);
        battleStarted = true;
        startCombatAnimation();
    }

    /**
     * Animacion entre los 2 entrenadores que se van a enfrentar
     */
    private void startCombatAnimation() {
        if (combatFrames == null) {
            combatFrames = new Image[38];
            for (int i = 0; i < 38; i++) {
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
                SwingUtilities.invokeLater(() -> {
                    fight.resetTurnToInitial();
                    currentTurn = fight.getInitialPickerIndex();
                    showBattleScreen();
                    enterMoveSelect();
                });
            }
        });
        animationTimer.start();
    }

    /**
     * Prepaara la batalla obteniendo informacion de los entrenadores y sus pokemones
     */
    private void showBattleScreen() {

        String name1 = fight.getPokemonName(0);
        String name2 = fight.getPokemonName(1);
        String backFile = fight.getBackImageName(0);
        String frontFile = fight.getFrontImageName(1);

        int hp1 = fight.getPokemonCurrentHp(0);
        int maxHp1 = fight.getPokemonMaxHp(0);
        int hp2 = fight.getPokemonCurrentHp(1);
        int maxHp2 = fight.getPokemonMaxHp(1);
        String team1 = fight.getTrainerTeam(0);
        String team2 = fight.getTrainerTeam(1);

        BattlePanel bp = new BattlePanel(
                name1, name2,
                backFile, frontFile,
                hp1, maxHp1, hp2, maxHp2,
                team1, team2,
                "/pokemonPictures/PoobKemonLucha.png"
        );
        setContentPane(bp);
        revalidate();
        repaint();
        state = State.BATTLE;
        fight.setUiState(UIState.BATTLE);
    }

    /**
     * Muestra el tablero con los pokémons, sus nombres y PS,
     * pero sin la animación de Lucha.
     */
    private void showBoardScreen() {

        String name1 = fight.getPokemonName(0);
        String name2 = fight.getPokemonName(1);
        String backFile = fight.getBackImageName(0);
        String frontFile = fight.getFrontImageName(1);

        int hp1 = fight.getPokemonCurrentHp(0);
        int maxHp1 = fight.getPokemonMaxHp(0);
        int hp2 = fight.getPokemonCurrentHp(1);
        int maxHp2 = fight.getPokemonMaxHp(1);

        String team1 = fight.getTrainerTeam(0);
        String team2 = fight.getTrainerTeam(1);

        BattlePanel bp = new BattlePanel(
                name1, name2,
                backFile, frontFile,
                hp1, maxHp1, hp2, maxHp2,
                team1, team2,
                "/pokemonPictures/PoobKemonTablero.png"
        );

        bp.setLayout(null);
        bp.add(cursorLabel);
        cursorLabel.setVisible(true);

        setContentPane(bp);
        revalidate();
        repaint();
        if (!bothAI && !fight.getCurrentTrainer().isAI()) {
            startTurnTimer();
        }
    }


    /**
     * Se abre la mochila muestra los items que disponibles
     */
    private void enterBackpack() {
        if (turnTimer != null) turnTimer.stop();
        state = State.BACKPACK;
        fight.setUiState(UIState.BACKPACK);
        setContentPane(contentPanel);
        revalidate();

        contentPanel.setBackgroundImage("/pokemonPictures/PoobKemonMochila.png");
        cursorLabel.setVisible(false);

        if (backpackPanel == null) {
            backpackPanel = new PoobKemonBackPack(this);
            contentPanel.add(backpackPanel);
        }
        backpackPanel.setVisible(true);
        backpackPanel.requestFocusInWindow();
        contentPanel.repaint();
    }


    /**
     * Cancela la mochila y vuelve al u sin pasar el turno
     */
    public void cancelBackpack() {
        state = State.MENU;
        fight.setUiState(UIState.MENU);
        if (backpackPanel != null) {
            backpackPanel.setVisible(false);
        }
        showBoardScreen();
        cursorLabel.setVisible(true);
        contentPanel.revalidate();
        contentPanel.repaint();
        requestFocusInWindow();
    }


    /**
     * Notifica si se salio de la mochila
     */
    public void notifyExitBackpack() {
        cancelBackpack();
    }

    /**
     * Comienza el flujo al usar un item
     */
    public void enterItemSelect(String item) {
        fight.setLastPendingItem(item);
        this.pendingItem = item;
        this.state = State.ITEM_SELECT;
        fight.setUiState(UIState.ITEM_SELECT);

        if (backpackPanel != null) {
            backpackPanel.setVisible(false);
        }

        setContentPane(contentPanel);
        contentPanel.revalidate();

        if (pokemonPanel != null) {
            contentPanel.remove(pokemonPanel);
        }
        List<String> teamKeys = fight.getCurrentTrainerPokemonKeys();
        pokemonPanel = new PokemonSelectionPanel(this, teamKeys);
        contentPanel.add(pokemonPanel);
        pokemonPanel.setBounds(0, 0, contentPanel.getWidth(), contentPanel.getHeight());
        pokemonPanel.setVisible(true);
        pokemonPanel.requestFocusInWindow();
        contentPanel.repaint();
    }

    /**
     * Si el jugador usa un item actualiza el log
     */
    public void applyPendingItemOnPokemon() {
        fight.useItem(pendingItem);
        List<String> log = fight.drainLog();
        pendingItem = null;

        if (pokemonPanel != null) {
            pokemonPanel.setVisible(false);
        }
        if (backpackPanel != null) {
            backpackPanel.setVisible(false);
        }

        showBoardScreen();
        displayLog(log);
        state = State.LOG;
        fight.setUiState(UIState.LOG);
        contentPanel.revalidate();
        contentPanel.repaint();
    }


    /**
     * Permite entrar en la seleccion de pokemones que eligio el entrenador
     */
    private void enterPokemonSelect() {
        if (turnTimer != null) turnTimer.stop();
        state = State.POKEMON_SELECT;
        fight.setUiState(UIState.POKEMON_SELECT);
        cursorLabel.setVisible(false);

        List<String> teamKeys = fight.getCurrentTrainerPokemonKeys();
        if (pokemonPanel != null) {
            contentPanel.remove(pokemonPanel);
        }

        pokemonPanel = new PokemonSelectionPanel(this, teamKeys);
        contentPanel.add(pokemonPanel);
        pokemonPanel.setBounds(0, 0, contentPanel.getWidth(), contentPanel.getHeight());
        pokemonPanel.setVisible(true);
        pokemonPanel.requestFocusInWindow();
        contentPanel.repaint();
    }

    /**
     * Sale de la seleccion de pokemones y marca que ya un jugador eligio a su pokemon
     */
    private void exitPokemonSelect() {

        if (fight.getCurrentTrainer().isAI() && battleStarted) {
            AITrainer ai = (AITrainer) fight.getCurrentTrainer();
            int switchIdx = ai.autoChooseSwitch(fight);
            String key = fight.getCurrentTrainer()
                    .getTeam()
                    .get(switchIdx)
                    .getName()
                    .toUpperCase();
            fight.switchActivePokemon(key);

            showBoardScreen();
            enterMoveSelect();
            return;
        }

        contentPanel.remove(pokemonPanel);
        pokemonPanel.setVisible(false);

        if (inBattleSelect) {
            inBattleSelect = false;
            fight.nextTurn();
            currentTurn = 1 - currentTurn;
            state = State.MENU;
            fight.setUiState(UIState.MENU);

            showBoardScreen();
            selection = 0;
            updateCursor();
            return;
        }

        pokemonChosen[currentTurn] = true;
        contentPanel.setBackgroundImage("/pokemonPictures/PoobKemonTablero.png");
        state = State.MENU;
        fight.setUiState(UIState.MENU);
        selection = 0;
        updateCursor();
        requestFocusInWindow();

        if (pokemonChosen[0] && pokemonChosen[1]) {
            battleStarted = true;
            enterCombat();
            return;
        }

        fight.nextTurn();
        currentTurn = 1 - currentTurn;

        if (fight.getCurrentTrainer().isAI() && !battleStarted) {
            AITrainer ai = (AITrainer) fight.getCurrentTrainer();
            int switchIdx = ai.autoChooseSwitch(fight);
            String key = fight.getCurrentTrainer()
                    .getTeam()
                    .get(switchIdx)
                    .getName()
                    .toUpperCase();
            fight.switchActivePokemon(key);

            pokemonChosen[currentTurn] = true;
            battleStarted = true;
            enterCombat();
            return;
        }

        showTurnDialog(
                fight.getTrainerName(currentTurn),
                fight.getTrainerTeam(currentTurn),
                String.format("Turno de %s del team %s",
                        fight.getTrainerName(currentTurn),
                        fight.getTrainerTeam(currentTurn)
                )
        );
    }


    /**
     * Cancela la seleccion del pokemon si aun no se esta en battalla
     */
    private void cancelPokemonSelect() {
        inBattleSelect = false;
        state = State.MENU;
        fight.setUiState(UIState.MENU);
        showBoardScreen();
        selection = 0;
        updateCursor();
        requestFocusInWindow();
    }


    /**
     * Avanza el estado desde el log cambiando el turno y maneja el KO
     */
    private void handleLogAdvance() {
        boolean p1Defeated = fight.isTrainerDefeated(0);
        boolean p2Defeated = fight.isTrainerDefeated(1);
        if (p1Defeated || p2Defeated) {
            int winnerIdx = p1Defeated ? 1 : 0;
            String winnerName = fight.getTrainerName(winnerIdx);
            String winnerTeam = fight.getTrainerTeam(winnerIdx);

            Color bg = "azul".equalsIgnoreCase(winnerTeam) ? Color.CYAN : Color.PINK;

            ImageIcon trophyIcon = null;

            URL trophyUrl = getClass().getResource("/pokemonPictures/trofeo.png");
            if (trophyUrl != null) {
                trophyIcon = new ImageIcon(trophyUrl);
            }

            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(bg);
            JLabel lbl = new JLabel(
                    String.format("¡%s del team %s ha ganado la batalla!", winnerName, winnerTeam),
                    SwingConstants.CENTER
            );

            lbl.setFont(new Font("Press Start 2P", Font.PLAIN, 14));
            panel.add(lbl, BorderLayout.NORTH);
            if (trophyIcon != null) {
                JLabel iconLbl = new JLabel(trophyIcon, SwingConstants.CENTER);
                panel.add(iconLbl, BorderLayout.CENTER);
            }

            JOptionPane.showMessageDialog(this, panel, "Fin del juego", JOptionPane.PLAIN_MESSAGE);
            fight.resetSelection();
            dispose();
            new StartPoobKemon().setVisible(true);
            return;
        }

        fight.nextTurn();
        currentTurn = 1 - currentTurn;

        if (fight.getCurrentTrainer().isAI() && fight.getPokemonCurrentHp(currentTurn) == 0) {
            AITrainer ai = (AITrainer) fight.getCurrentTrainer();
            int switchIdx = ai.autoChooseSwitch(fight);
            String key = ai.getTeam().get(switchIdx).getName().toUpperCase();
            fight.switchActivePokemon(key);
            fight.nextTurn();
            currentTurn = 1 - currentTurn;
        }

        showBoardScreen();
        if (fight.getCurrentTrainer().isAI()) {
            enterMoveSelect();
        } else if (fight.getPokemonCurrentHp(currentTurn) == 0) {
            inBattleSelect = true;
            setContentPane(contentPanel);
            contentPanel.setBackgroundImage("/pokemonPictures/PoobKemonTablero.png");
            contentPanel.revalidate();
            contentPanel.repaint();
            enterPokemonSelect();
        } else {
            state = State.MENU;
            fight.setUiState(UIState.MENU);
            selection = 0;
            updateCursor();
        }
    }


    /**
     * Inicia o reinicia el tiempo de 20 segundos en cad turno
     */
    private void startTurnTimer() {
        if (turnTimer != null && turnTimer.isRunning()) {
            turnTimer.stop();
        }
        turnTimer = new Timer(20_000, e -> onTurnTimeOut());
        turnTimer.setRepeats(false);
        turnTimer.start();
    }


    /**
     * Penaliza al jugador que no hizo su jugada en 20 segundos==
     */
    private void onTurnTimeOut() {
        fight.penalizeNoAction();

        List<String> penaltyLog = fight.drainLog();
        JOptionPane.showMessageDialog(
                this,
                String.join("\n", penaltyLog),
                "Tiempo agotado",
                JOptionPane.WARNING_MESSAGE
        );

        if (state == State.MOVE_SELECT) {
            showBattleScreen();
            enterMoveSelect();
        } else {
            showBoardScreen();
            state = State.MENU;
            fight.setUiState(UIState.MENU);
            selection = 0;
            updateCursor();
        }
    }

    /**
     * Para cuando se libera una tecla
     *
     * @param e
     */
    @Override
    public void keyReleased(KeyEvent e) {
    }

    /**
     * Cuando la tecla es oprimida
     *
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
         * Constructor de BattlePanel
         *
         * @param name1     nombre entredandor 1
         * @param name2     nombre entrenador 2
         * @param backFile  imagen del pokemon a espaldas
         * @param frontFile imagen del pokemon al frente
         * @param hp1       puntos de salud actuales de pokemon 1
         * @param maxHp1    maxima vida del pokemon 1
         * @param hp2       puntos de salud actuales de pokemon 2
         * @param maxHp2    maxima vida del pokemon 2
         * @param team1     representa el color del team rojo o azul
         * @param team2     representa el color del team rojo o azul
         * @param bgPath    nos permite cambiar el fondo a PoobKemonTablero
         */
        public BattlePanel(String name1,
                           String name2,
                           String backFile,
                           String frontFile,
                           int hp1, int maxHp1,
                           int hp2, int maxHp2,
                           String team1, String team2,
                           String bgPath) {
            this.name1 = name1;
            this.name2 = name2;
            this.hp1 = hp1;
            this.maxHp1 = maxHp1;
            this.hp2 = hp2;
            this.maxHp2 = maxHp2;
            this.team1 = team1;
            this.team2 = team2;

            Image tmp = null;
            URL backUrl = getClass().getResource("/pokemonPictures/" + backFile);
            backImg = (backUrl != null) ? new ImageIcon(backUrl).getImage() : null;

            URL frontUrl = getClass().getResource("/pokemonPictures/" + frontFile);
            frontImg = (frontUrl != null) ? new ImageIcon(frontUrl).getImage() : null;

            URL bgUrl = getClass().getResource(bgPath);
            bgImage = (bgUrl != null) ? new ImageIcon(bgUrl).getImage() : null;
            setLayout(null);
        }

        private Color teamColor(String team) {
            return "azul".equalsIgnoreCase(team) ? Color.CYAN : Color.PINK;
        }

        /**
         * Se encarga de mostrar los pokemones, su nombre y PS
         *
         * @param g grafica
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

            //Pokemon de frente
            g.setFont(fontName);
            FontMetrics fmName = g.getFontMetrics(fontName);
            int name2X = 70;
            int name2Y = 105;
            g.setColor(Color.BLACK);
            g.drawString(name2, name2X, name2Y);

            //hp del pokemon del frente
            int name2Width = fmName.stringWidth(name2) + 10;
            String hpText2 = hp2 + "/" + maxHp2;
            g.setFont(fontPlain);
            g.setColor(Color.BLACK);
            g.drawString(hpText2, name2X + name2Width, name2Y);

            //PS coloreado segun el team
            int hpText2Width = g.getFontMetrics().stringWidth(hpText2) + 5;
            g.setFont(fontBold);
            g.setColor(teamColor(team2));
            g.drawString("PS", name2X + name2Width + hpText2Width, name2Y);

            //Pokemon de espaldas
            g.setFont(fontName);
            g.setColor(Color.BLACK);
            g.drawString(name1, 540, h - 310);

            //Barras de PS
            int barW = 200, barH = 20;

            //La del pokemon de frente
            int barX2 = 70, barY2 = 110;
            int barX1 = 540, barY1 = h - 295;


            g.setColor(Color.LIGHT_GRAY);
            //La del pokemon de frente
            g.fillRect(barX2, barY2, barW, barH);
            g.setColor(teamColor(team2));
            int filled2 = (int) ((hp2 / (double) maxHp2) * barW);
            g.fillRect(barX2, barY2, filled2, barH);
            g.setColor(Color.BLACK);
            g.drawRect(barX2, barY2, barW, barH);

            //Alto y ancho imagenes pokemones
            int backW = 215, backH = 200;
            int frontW = 250, frontH = 250;
            //Posiciones de las imagenes
            int xBack = 150, yBack = 222;
            int xFront = 545, yFront = 40;

            if (backImg != null) {
                g.drawImage(backImg, xBack, yBack, backW, backH, this);
            }
            if (frontImg != null) {
                g.drawImage(frontImg, xFront, yFront, frontW, frontH, this);
            }

            //La del pokemon a espaldas
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(barX1, barY1, barW, barH);
            g.setColor(teamColor(team1));
            int filled1 = (int) ((hp1 / (double) maxHp1) * barW);
            g.fillRect(barX1, barY1, filled1, barH);
            g.setColor(Color.BLACK);
            g.drawRect(barX1, barY1, barW, barH);

            //El PC y el numero de vida al lado de la barra
            int textY1 = barY1 + barH + 20;
            g.setFont(fontPlain);
            g.setColor(Color.BLACK);
            g.drawString(hp1 + " / " + maxHp1, barX1 + 4, textY1);
            g.setFont(fontBold);
            g.setColor(teamColor(team1));
            g.drawString("PS", barX1 + barW - 40, textY1);

            if (state == State.LOG) {
                Font fontLog = new Font("Press Start 2P", Font.PLAIN, 14);
                g.setFont(fontLog);
                g.setColor(Color.BLACK);
                FontMetrics fm = g.getFontMetrics();

                //Para ajustar el texto de los dialogos
                int boxX = 35;
                int boxY = h - 170;
                int boxWidth = 370;
                int textY = boxY + 30;

                for (String msg : logMessages) {
                    List<String> lines = wrapText(msg, fm, boxWidth);
                    for (String line : lines) {
                        g.drawString(line, boxX + 10, textY);
                        textY += fm.getHeight() + 6;
                    }
                }
                return;
            }

            if (state == State.MOVE_SELECT) {
                int panelY = h - 180;
                int leftX = 50, leftY = panelY, leftW = 300, leftH = 160;
                int rightX = leftX + leftW + 10, rightY = panelY, rightW = 200, rightH = 160;

                int paddingX = 20;
                int columnSpacing = 240;
                int paddingY = 60;

                Font fontMove = new Font("Press Start 2P", Font.PLAIN, 17);
                Font fontMoveBold = fontMove.deriveFont(Font.BOLD, 16f);
                FontMetrics fm = g.getFontMetrics(fontMove);
                int lineH = fm.getHeight() + 17;

                g.setFont(fontMove);
                g.setColor(teamColor(fight.getTrainerTeam(currentTurn)));
                String header = fight.getTrainerName(currentTurn)
                        + " (" + fight.getTrainerTeam(currentTurn) + ")";
                g.drawString(header, leftX + 10, leftY + 20);

                for (int i = 0; i < fight.getMoveCount(); i++) {
                    int row = i / 2;
                    int col = i % 2;
                    int x = leftX + paddingX + col * columnSpacing;
                    int y = leftY + paddingY + row * lineH;

                    boolean hasPP = fight.getMovePPCurrent(i) > 0;

                    if (i == moveSelection && hasPP) {
                        g.setFont(fontMoveBold);
                        g.setColor(teamColor(fight.getTrainerTeam(currentTurn)));
                        g.drawString("▶", x - 20, y);
                    }

                    g.setFont(fontMove);
                    if (!hasPP) {
                        g.setColor(Color.DARK_GRAY);
                    } else if (i == moveSelection) {
                        g.setColor(Color.BLACK);
                    } else {
                        g.setColor(Color.LIGHT_GRAY);
                    }
                    g.drawString(fight.getMoveName(i), x, y);
                }

                String ppInfo = String.format("PP %d/%d",
                        fight.getMovePPCurrent(moveSelection),
                        fight.getMovePPMax(moveSelection));
                String typeInfo = "TIPO/" + fight.getMoveType(moveSelection).name();
                g.setFont(fontMove);
                g.setColor(Color.BLACK);

                int infoX = rightX + 285;
                int infoY = rightY + (rightH / 2) - (fm.getHeight() / 2) - 12;
                g.drawString(ppInfo, infoX, infoY);
                g.drawString(typeInfo, infoX, infoY + lineH);
            }
        }

        /**
         * Divide un texto en multiples lineas para que encaje dentro de un ancho maximo que definimos
         *
         * @param text     El texto a dividir en lineas
         * @param fm       la metricas para calcular el ancho
         * @param maxWidth el maximo ancho para cada linea
         * @return List<String> de la lineas de texto ajustadas
         */
        private List<String> wrapText(String text, FontMetrics fm, int maxWidth) {
            List<String> lines = new ArrayList<>();
            StringBuilder line = new StringBuilder();
            for (String word : text.split(" ")) {
                String testLine = line.length() == 0 ? word : line + " " + word;
                if (fm.stringWidth(testLine) > maxWidth) {
                    lines.add(line.toString());
                    line = new StringBuilder(word);
                } else {
                    line = new StringBuilder(testLine);
                }
            }
            if (line.length() > 0) {
                lines.add(line.toString());
            }
            return lines;
        }
    }


    /**
     * Panel para que, antes del combate, cada entrenador elija cuál de sus 6 Pokémon activos va a usar.
     */
    private class PokemonSelectionPanel extends JPanel {
        private int selected = 0;
        private final PoobKemonCombat parent;
        private final List<String> teamKeys;
        private final Image[] minis;

        private static final Point[] SLOT_POS = {
                new Point(80, 120),
                new Point(370, 82),
                new Point(370, 182),
                new Point(370, 282),
                new Point(370, 382),
                new Point(370, 481)
        };

        private static final int ICON_SIZE_CLOSED = 82;
        private static final int ICON_SIZE_OPENED = 85;

        private final Image bgImage;
        private final Image closeImage;
        private final Image openImage;

        /**
         * Constructor de PokemonSelectionPanel
         *
         * @param parent   referencia a la ventana de combate
         * @param teamKeys el equipo de 6 pokemones de cada entrenador
         */
        public PokemonSelectionPanel(PoobKemonCombat parent, List<String> teamKeys) {
            this.parent = parent;
            this.teamKeys = teamKeys;
            this.minis = new Image[teamKeys.size()];

            for (int i = 0; i < teamKeys.size(); i++) {
                String key = teamKeys.get(i);
                String res = parent.fight.getResourceName(key);
                if (!res.isEmpty()) {
                    String miniName = "mini" + res;
                    URL url = getClass().getResource("/pokemonPictures/" + miniName);
                    if (url != null) {
                        minis[i] = new ImageIcon(url).getImage();
                    }
                }
            }

            setOpaque(false);
            setFocusable(true);
            setPreferredSize(new Dimension(930, 673));


            URL bgUrl = getClass().getResource("/pokemonPictures/PoobKemonSeleccion2.png");
            bgImage = (bgUrl != null) ? new ImageIcon(bgUrl).getImage() : null;

            URL closeUrl = getClass().getResource("/pokemonPictures/PokebolaClose.png");
            closeImage = (closeUrl != null)
                    ? new ImageIcon(closeUrl).getImage().getScaledInstance(ICON_SIZE_CLOSED, ICON_SIZE_CLOSED, Image.SCALE_SMOOTH)
                    : null;

            URL openUrl = getClass().getResource("/pokemonPictures/PokebolaOpen.png");
            openImage = (openUrl != null)
                    ? new ImageIcon(openUrl).getImage().getScaledInstance(ICON_SIZE_OPENED, ICON_SIZE_OPENED, Image.SCALE_SMOOTH)
                    : null;

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
         * depende del usario al mover las flechas si la pokebola abre o cierra
         *
         * @param g graficador
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (bgImage != null) {
                g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            }

            for (int i = 0; i < teamKeys.size(); i++) {
                Point slot = SLOT_POS[i];
                Image ball = (i == selected) ? openImage : closeImage;
                if (ball != null) {
                    int w = (i == selected) ? ICON_SIZE_OPENED : ICON_SIZE_CLOSED;
                    int h = w;
                    g.drawImage(ball,
                            slot.x - w / 2 - 10,
                            slot.y - h / 2 - 3,
                            w, h, this);
                }

                Image m = minis[i];
                if (m != null) {
                    int miniSize = 80;
                    int mx = slot.x - miniSize / 2;
                    int my = slot.y - miniSize / 2;
                    g.drawImage(m, mx, my, miniSize, miniSize, this);
                }
            }
        }


        /**
         * Para moverse entre los pokemones diponibles
         * @param e el evento como darle enter a un pokemon para elegirlo
         */
        private void processKey(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_DOWN:
                    selected = (selected + 1) % teamKeys.size();
                    break;
                case KeyEvent.VK_UP:
                    selected = (selected - 1 + teamKeys.size()) % teamKeys.size();
                    break;

                case KeyEvent.VK_ENTER:
                    String key = teamKeys.get(selected);

                    if (parent.state == State.ITEM_SELECT) {
                        parent.fight.switchActivePokemon(key);
                        parent.applyPendingItemOnPokemon();
                        return;
                    }

                    if (parent.fight.isCurrentTrainerPokemonWeakened(key)) {
                        if (parent.fight.getItemCount("REVIVIR") > 0) {
                            parent.fight.useItem("REVIVIR");
                            List<String> log = parent.fight.drainLog();
                            parent.displayLog(log);
                            exitPokemonSelect();
                        }
                        return;
                    }

                    parent.fight.switchActivePokemon(key);
                    exitPokemonSelect();
                    return;

                case KeyEvent.VK_ESCAPE:
                    if (!parent.inBattleSelect) {
                        parent.cancelPokemonSelect();
                    }
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
        private final String[] items = { "Pocion", "Superpocion", "Hiperpocion", "Revivir" };
        private int seleccion = 0;
        private JTextArea description;
        private JLabel itemImageLabel;
        private JLabel cursor;
        private boolean showingOptions = false;
        private int opcionSelected = 0;
        private JPanel panelItems, panelDesc, panelOptions;
        private JLabel[] labelsItems, labelsOptions;
        private JLabel cursorOptions;
        private String[] panelOptionsOpts;

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
            labelsItems = new JLabel[items.length];
            for (int i = 0; i < items.length; i++) {
                int count = parent.fight.getItemCount(items[i]);
                labelsItems[i] = new JLabel(items[i] + " x" + count);
                labelsItems[i].setFont(f);
                labelsItems[i].setBounds(25, 20 + i * 90, 380, 40);
                panelItems.add(labelsItems[i]);
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
        public void hideOptions() {
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
        public boolean isShowingOptions() {
            return showingOptions;
        }

        /**
         * Si se da escape sale de la vetentda
         * @param e de oprimir esc
         */
        public void processKey(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                if (showingOptions) {
                    hideOptions();
                } else {
                    parent.cancelBackpack();
                }
                return;
            }

            boolean limitReached = parent.fight.getUsedItemCount() >= 2;

            if (!showingOptions) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && limitReached) {
                    showDescription();
                    return;
                }
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_DOWN:
                        seleccion = (seleccion + 1) % items.length;
                        break;
                    case KeyEvent.VK_UP:
                        seleccion = (seleccion - 1 + items.length) % items.length;
                        break;
                    case KeyEvent.VK_ENTER:
                        showOptions();
                        return;
                }
                updateCursor();
            } else {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_DOWN:
                        opcionSelected = (opcionSelected + 1) % labelsOptions.length;
                        updateCursorOptions();
                        break;
                    case KeyEvent.VK_UP:
                        opcionSelected =
                                (opcionSelected - 1 + labelsOptions.length) % labelsOptions.length;
                        updateCursorOptions();
                        break;
                    case KeyEvent.VK_ENTER:
                        executeOption(opcionSelected);
                        return;
                }
            }
        }



        /**
         * Actualiza el cursor
         */
        private void updateCursor() {

            for (int i = 0; i < items.length; i++) {
                int cnt = parent.fight.getItemCount(items[i]);
                labelsItems[i].setText(items[i] + " x" + cnt);
            }
            cursor.setVisible(true);
            cursor.setLocation(0, 20 + seleccion * 90);
            showDescription();
            repaint();
        }

        /**
         * Muestra la descripcion en un panel de abajo a la izquierda
         */
        private void showDescription() {
            description.setFont(new Font("Press Start 2P", Font.PLAIN, 14));

            boolean limitReached = parent.fight.getUsedItemCount() >= 2;
            if (limitReached) {
                description.setText("Ya has usado 2 items. No puedes usar más en esta batalla.");
                itemImageLabel.setIcon(null);
                return;
            }

            String it = items[seleccion];
            int available = parent.fight.getItemCount(it);

            if (available == 0) {
                description.setText("No te quedan " + it + " para usar.");
                itemImageLabel.setIcon(null);
                return;
            }

            description.setText("");
            itemImageLabel.setIcon(null);

            switch (it) {
                case "Pocion":
                    description.setText("""
                    Cura 20 PS de tu Pokémon.

                    Es una poción básica que 
                    puedes usar en combate.
                    """);
                    setItemImage("/pokemonPictures/Pocion.png");
                    break;

                case "Superpocion":
                    description.setText("""
                    Cura 50 PS de tu Pokémon.

                    No sirve si está debilitado.
                    """);
                    setItemImage("/pokemonPictures/SuperPocion.png");
                    break;

                case "Hiperpocion":
                    description.setText("""
                    Cura 200 PS de tu Pokémon.

                    No se puede usar en un 
                    Pokémon con 0 PS.
                    """);
                    setItemImage("/pokemonPictures/HiperPocion.png");
                    break;

                case "Revivir":
                    int currHp = parent.fight.getPokemonCurrentHp(parent.currentTurn);
                    setItemImage("/pokemonPictures/Revivir.png");
                    if (currHp > 0) {
                        description.setText(
                                "Sólo puedes usar Revivir si tu Pokémon está debilitado  (PS = 0)."
                        );
                    } else {
                        description.setText("""
                    Revive a un Pokémon con
                    la mitad de sus PS.

                    Solo funciona si está KO.
                    """);
                    }
                    break;
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
        private void showOptions() {
            if (panelOptions != null) remove(panelOptions);
            showingOptions = true;

            panelOptions = new JPanel(null) {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Image bg = loadImage("/pokemonPictures/Usos.png");
                    if (bg != null) g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
                }
            };
            panelOptions.setBounds(690, 500, 240, 140);
            panelOptions.setOpaque(false);
            add(panelOptions);

            showDescription();
            boolean canStillUse = parent.fight.getUsedItemCount() < 2;
            panelOptionsOpts = canStillUse
                    ? new String[]{ "USAR", "SALIR" }
                    : new String[]{ "SALIR" };

            Font f = new Font("Press Start 2P", Font.PLAIN, 14);
            labelsOptions = new JLabel[panelOptionsOpts.length];
            for (int i = 0; i < panelOptionsOpts.length; i++) {
                labelsOptions[i] = new JLabel(panelOptionsOpts[i]);
                labelsOptions[i].setFont(f);
                labelsOptions[i].setBounds(50, 30 + i * 40, 150, 25);
                panelOptions.add(labelsOptions[i]);
            }

            opcionSelected = 0;
            cursorOptions = new JLabel("▶");
            cursorOptions.setFont(f);
            cursorOptions.setBounds(35, 30 , 30, 25);
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
            String choice = panelOptionsOpts[op];
            if ("USAR".equals(choice)) {
                int used = parent.fight.getUsedItemCount();
                int avail = parent.fight.getItemCount(items[seleccion]);

                if (used >= 2) {
                    description.setText("Ya has usado 2 items. No puedes usar más en esta batalla.");
                    itemImageLabel.setIcon(null);
                    return;
                }
                if (avail <= 0) {
                    description.setText("No te quedan " + items[seleccion] + " para usar.");
                    itemImageLabel.setIcon(null);
                    return;
                }

                parent.enterItemSelect(items[seleccion]);
            } else {
                hideOptions();
            }
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


    /**
     *  Proporciona funcionalidad para mostrar una imagen de fondo en un panel de Swing
     */
    private static class BackgroundPanel extends JPanel {
        private Image background;

        /**
         * Contructor de BackgroundPanel
         * @param path es una ruta relativa como nueva imagen al fondo
         */
        public BackgroundPanel(String path) {
            load(path);
        }

        /**
         * Cambia la imagen de fondo por una nueva
         * @param path  es una ruta relativa como nueva imagen al fondo
         */
        public void setBackgroundImage(String path) {
            load(path);
        }

        /**
         * Establece directamente la imagen de fondo
         * @param img la nueva imagen que se usaara como fondo
         */
        public void setCustomImage(Image img) {
            this.background = img;
            repaint();
        }

        /**
         * Carga una imagen desde el recurso especificado y la establece como fondo
         * @param path la ruta de la imagen como para pokemonPictures
         */
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

        /**
         * Dibuja la imagen de fonfo y la escala
         * @param g grafica
         */
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

        fight.selectPokemon("GENGAR");
        fight.selectPokemon("GARDEVOIR");
        fight.selectPokemon("RAICHU");
        fight.selectPokemon("DUSCLOPS");
        fight.selectPokemon("METAGROSS");
        fight.selectPokemon("TYRANITAR");

        fight.nextTurn();

        fight.selectPokemon("DRAGONITE");
        fight.selectPokemon("CHARIZARD");
        fight.selectPokemon("ALAKAZAM");
        fight.selectPokemon("ABSOL");
        fight.selectPokemon("VENUSAUR");
        fight.selectPokemon("TOGETIC");

        fight.resetTurnToInitial();

        SwingUtilities.invokeLater(() -> {
            PoobKemonCombat window = new PoobKemonCombat(fight);
            window.setVisible(true);
        });
    }
}