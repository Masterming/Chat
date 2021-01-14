package user;

import javax.swing.*;



import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginView implements ActionListener {

    private JFrame frame;
    private JPanel panel;
    private JLabel nameLabel;
    private JLabel passwortLabel;
    private JTextField nameText;
    private JPasswordField passwortText;
    private JButton login;

    public LoginView() {

        frame = new JFrame();
        panel = new JPanel(new GridLayout(3, 2));
        nameLabel = new JLabel("Nutzername: ");
        passwortLabel = new JLabel("Passwort: ");
        nameText = new JTextField();
        passwortText = new JPasswordField();
        login = new JButton("Login");

        panel.add(nameLabel);
        panel.add(nameText);
        panel.add(passwortLabel);
        panel.add(passwortText);
        panel.add(login);

        login.addActionListener(this);

        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Login");
        frame.pack();
        frame.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == login) {

            String name = nameText.getText();
            char[] passw = passwortText.getPassword();
            String password = new String(passw);
            
            if (ClientController.login(name, password)){
                System.out.println("success");
                frame.dispose();
            }
        }
    }
}