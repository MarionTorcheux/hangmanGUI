package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class Hangman extends JFrame implements ActionListener {

    private static final String[] list = {"pomme", "natation", "poney", "dauphin", "ordinateur", "chocolat"};
    private static final Random r = new Random();
    public static String guessWord = list[r.nextInt(list.length)];
    private static String asterisk = new String(new char[guessWord.length()]).replace("\0", "*");
    private static int trials = 0;
    private static final int maxTrials = 8;

    private final JTextField userInputField;
    private final JTextArea hangmanOutput;

    private static JLabel hangmanImage;




    public Hangman() {

        setTitle("Hangman Game");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        hangmanImage = new JLabel();
        ImageIcon initialImage = new ImageIcon("src/main/resources/images/1.png");
        hangmanImage.setIcon(initialImage);



        userInputField = new JTextField();
        JButton guessButton = new JButton("Guess");
        guessButton.addActionListener(this);

        hangmanOutput = new JTextArea();
        hangmanOutput.setEditable(false);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(hangmanOutput)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(userInputField)
                                .addComponent(guessButton)
                        )
                        .addComponent(hangmanImage)
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addComponent(hangmanOutput)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(userInputField)
                                .addComponent(guessButton)
                        )
                        .addComponent(hangmanImage)
        );

        render();
    }

    public void updateHangmanImage(int imageNumber) {
        // Mettez à jour l'image en fonction du numéro passé en paramètre
        String imagePath = "src/main/resources/images/" + imageNumber + ".png";
        ImageIcon newImageIcon = new ImageIcon(imagePath);
        hangmanImage.setIcon(newImageIcon);
    }


    public static void main(String[] args) {

            Hangman hangman = new Hangman();
            hangman.setVisible(true);
            hangman.updateHangmanImage(trials + 1);

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String guess = userInputField.getText().toLowerCase();
        if (!guess.isEmpty() && guess.matches("[a-z]")) {
            hang(guess);
            userInputField.setText("");
            render();
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez entrer une lettre valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void render() {
        hangmanOutput.setText(asterisk + "\n");
        switch (trials) {
            case 6:
                hangmanOutput.append("Essaie encore ! Plus que 1 essai");
                break;
            case 7:
                hangmanOutput.append("GAME OVER!\nPERDU ! Le mot était " + guessWord);
                updateHangmanImage(8);
                startNewGame();
                break;
            default:
                hangmanOutput.append("Essaie encore plus que " + (maxTrials - trials) + " essais");
        }

        if (asterisk.equals(guessWord)) {
            hangmanOutput.setText( "GAGNÉ ! Bravo ! Le mot était " + guessWord + "\n");
            startNewGame();
        }

        updateHangmanImage(trials + 1);
    }

    private void hang(String guess) {
        StringBuilder newAsterisk = new StringBuilder();
        for (int i = 0; i < guessWord.length(); i++) {
            if (guessWord.charAt(i) == guess.charAt(0)) {
                newAsterisk.append(guess.charAt(0));
            } else if (asterisk.charAt(i) != '*') {
                newAsterisk.append(guessWord.charAt(i));
            } else {
                newAsterisk.append("*");
            }
        }

        if (asterisk.contentEquals(newAsterisk)) {
            trials++;
        } else {
            asterisk = newAsterisk.toString();
        }
    }

    private void startNewGame() {
        int dialogResult = JOptionPane.showConfirmDialog(null, "Voulez-vous relancer une partie ?", "Relancer une partie", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            guessWord = list[r.nextInt(list.length)];
            asterisk = new String(new char[guessWord.length()]).replace("\0", "*");
            trials = 0;
            render();
        } else {
            System.exit(0);
        }

    }
}
