package presentation;

import domain.*;

import java.util.List;

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
    private final boolean survivalMode;

    /**
     * Constructor de PoobKemonModality
     * @param fight representa la preparacion para la batalla
     */
    public PoobKemonModality(PoobKemonFight fight, boolean survivalMode) {
        this.fight = fight;
        this.survivalMode = survivalMode;
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
            if (survivalMode) {
                try {
                    SurvivalPoobKemonFight survFight = new SurvivalPoobKemonFight(
                            fight.getTrainer1(),
                            fight.getTrainer2(),
                            fight.getInitialPickerIndex() + 1
                    );
                    PoobKemonCombat combatWindow = new PoobKemonCombat(survFight);
                    combatWindow.setVisible(true);

                } catch (POOBKemonException ex) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Error al iniciar Modo Supervivencia:\n" + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                    new StartPoobKemon().setVisible(true);
                }
            } else {
                new PoobKemonSelector(fight);
            }
        });

        //PvM
        JButton pvmButton = createTransparentButton(345, 259, 270, 75);
        pvmButton.addActionListener(e -> {
            String[] options = {
                    "Defensive Trainer",
                    "Attacking Trainer",
                    "Changing Trainer",
                    "Expert Trainer"
            };
            String[] keys = { "DEFENSIVE", "ATTACKING", "CHANGING", "EXPERT" };

            int choice = JOptionPane.showOptionDialog(
                    this,
                    "Elige el tipo de entrenador IA:",
                    "Seleccionar IA",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if (choice >= 0) {
                BattleStrategy strat;
                switch (choice) {
                    case 0:  strat = new DefensiveStrategy(); break;
                    case 1:  strat = new AttackingStrategy(); break;
                    case 2:  strat = new ChangingStrategy();   break;
                    case 3:  strat = new ExpertStrategy();     break;
                    default: strat = new DefensiveStrategy();  break;
                }

                AITrainer ai = new AITrainer(
                        options[choice],
                        fight.getTrainer2().getColorTeam(),
                        strat,
                        keys[choice]
                );

                if (fight.getCurrentTrainer().isAI()) {
                    fight.nextTurn();
                }

                fight.addTrainer(ai, 2);
                new PoobKemonSelector(fight).setVisible(true);
                dispose();
            }
        });

        //MvM
        JButton mvmButton = createTransparentButton(345, 420, 280, 72);
        mvmButton.addActionListener(e -> {
            String[] options = {
                    "Defensive Trainer",
                    "Attacking Trainer",
                    "Changing Trainer",
                    "Expert Trainer"
            };

            String[] keys = { "DEFENSIVE", "ATTACKING", "CHANGING", "EXPERT" };

            int choice1 = JOptionPane.showOptionDialog(
                    this,
                    "Elige el tipo de entrenador IA para el Jugador 1:",
                    "Seleccionar IA 1",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
            );
            if (choice1 < 0) return;

            int choice2 = JOptionPane.showOptionDialog(
                    this,
                    "Elige el tipo de entrenador IA para el Jugador 2:",
                    "Seleccionar IA 2",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if (choice2 < 0) return;
            BattleStrategy strat1, strat2;
            switch (choice1) {
                case 0: strat1 = new DefensiveStrategy(); break;
                case 1: strat1 = new AttackingStrategy(); break;
                case 2: strat1 = new ChangingStrategy(); break;
                case 3: strat1 = new ExpertStrategy(); break;
                default: strat1 = new DefensiveStrategy(); break;
            }
            switch (choice2) {
                case 0: strat2 = new DefensiveStrategy(); break;
                case 1: strat2 = new AttackingStrategy(); break;
                case 2: strat2 = new ChangingStrategy(); break;
                case 3: strat2 = new ExpertStrategy(); break;
                default: strat2 = new DefensiveStrategy(); break;
            }

            AITrainer ia1 = new AITrainer(
                    "IA 1 " + options[choice1],
                    fight.getTrainer1().getColorTeam(),
                    strat1,
                    keys[choice1]
            );
            AITrainer ia2 = new AITrainer(
                    "IA 2 " + options[choice2],
                    fight.getTrainer2().getColorTeam(),
                    strat2,
                    keys[choice2]
            );

            fight.addTrainer(ia1, 1);
            fight.addTrainer(ia2, 2);


            List<String> available = fight.getPokemonKeys();
            List<String> team1Keys = strat1.selectTeam(available);
            for (String key : team1Keys) {
                fight.selectPokemon(key);
            }
            for (String key : strat2.selectTeam(available)) {
                fight.nextTurn();
                fight.selectPokemon(key);
            }

            fight.resetTurnToInitial();
            dispose();
            PoobKemonCombat combat = new PoobKemonCombat(fight);
            combat.setVisible(true);
        });

        if (survivalMode) {
            pvmButton.setEnabled(false);
            mvmButton.setEnabled(false);
            pvmButton.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            mvmButton.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

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