package user;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PrivateChatGUI implements ActionListener {
    private JFrame frame;
    private JPanel chatPanel;
    private JPanel message;
    private JTextField text;
    private JButton send;
    private TextArea chat;
    private JButton leave;

    public PrivateChatGUI() {

        frame = new JFrame();
        chatPanel = new JPanel(new BorderLayout());
        chat = new TextArea();
        text = new JTextField();
        send = new JButton("Senden");
        message = new JPanel();
        leave = new JButton("privaten Chat beenden");

        // Chatfenster
        chat.setEditable(false);
        send.addActionListener(this);
        leave.addActionListener(this);
        message.setLayout(new BorderLayout());
        message.add(text, BorderLayout.CENTER);
        message.add(send, BorderLayout.EAST);
        message.add(leave, BorderLayout.SOUTH);
        chatPanel.add(chat, BorderLayout.CENTER);
        chatPanel.add(message, BorderLayout.SOUTH);

        // hübsch machen
        chatPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(chatPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Privatchat");
        frame.pack();
        frame.setVisible(true);
    }

    //Nachricht ins Chatfenster schreiben
    public void addMessage(String m) {
        chat.append("\n" + m);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == send) {
            String message = text.getText();
            text.setText("");
            addMessage(message);

            //message rausschicken

        } else if(e.getSource() == leave) {
            frame.dispose();

            //Verbindung trennen

        }
    }
}
