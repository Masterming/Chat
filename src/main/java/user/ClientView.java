package user;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ClientView implements ActionListener {

    private JFrame frame;
    private JPanel panel;
    private JPanel chatPanel;
    private JPanel controlPanel;
    private JPanel status;
    private JPanel information;
    private JLabel server;
    private JLabel port;
    private JLabel name;
    private TextArea chat;
    private JPanel message;
    private JTextField text;
    private JButton send;
    private JTabbedPane statusSelection;
    private JList<String> users;
    private JList<String> rooms;
    private JButton join;
    private JButton pchat;
    private JPanel roomButton;
    private JPanel userButton;

    public ClientView() {

        frame = new JFrame();
        panel = new JPanel(new BorderLayout());
        chatPanel = new JPanel(new BorderLayout());
        controlPanel = new JPanel();
        status = new JPanel();
        information = new JPanel();
        server = new JLabel();
        port = new JLabel();
        name = new JLabel();
        chat = new TextArea();
        text = new JTextField();
        send = new JButton("Senden");
        message = new JPanel();
        statusSelection = new JTabbedPane();
        rooms = new JList<>();
        users = new JList<>();
        join = new JButton("Beitreten");
        pchat = new JButton("Privaten Chat starten");
        roomButton = new JPanel(new BorderLayout());
        userButton = new JPanel(new BorderLayout());

        // Chatfenster
        chat.setEditable(false);
        send.addActionListener(this);
        message.setLayout(new BorderLayout());
        message.add(text, BorderLayout.CENTER);
        message.add(send, BorderLayout.EAST);
        chatPanel.add(chat, BorderLayout.CENTER);
        chatPanel.add(message, BorderLayout.SOUTH);

        // Status
        users.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        rooms.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollUser = new JScrollPane(users);
        JScrollPane scrollRoom = new JScrollPane(rooms);
        roomButton.add(scrollRoom, BorderLayout.CENTER);
        roomButton.add(join, BorderLayout.SOUTH);
        userButton.add(scrollUser, BorderLayout.CENTER);
        userButton.add(pchat, BorderLayout.SOUTH);
        statusSelection.setForeground(Color.BLACK);
        statusSelection.addTab("Benutzer", userButton);
        statusSelection.addTab("Räume", roomButton);
        status.setLayout(new BorderLayout());
        status.add(statusSelection, BorderLayout.CENTER);
        join.addActionListener(this);
        pchat.addActionListener(this);

        // Information
        information.setLayout(new GridLayout(2, 2));
        information.add(server);
        information.add(port);
        information.add(name);

        // Panel-Caption
        chatPanel.setBorder(BorderFactory.createTitledBorder("Chat"));
        status.setBorder(BorderFactory.createTitledBorder("Status"));
        information.setBorder(BorderFactory.createTitledBorder("Information"));

        // Panel-Arrangement
        controlPanel.setLayout(new BorderLayout());
        controlPanel.add(status, BorderLayout.CENTER);
        controlPanel.add(information, BorderLayout.NORTH);
        panel.add(chatPanel, BorderLayout.CENTER);
        panel.add(controlPanel, BorderLayout.EAST);

        // Design
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(frame, "Beenden?", "Exit", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    ClientController.logout();
                    System.exit(0);
                }
            }
        });
        frame.setTitle("User");
        frame.pack();
        frame.setVisible(true);
    }

    // display message to central window
    public void addMessage(String m) {
        chat.append(m + "\n");
    }

    // updater list of rooms
    public void setRooms(ArrayList<String> r) {
        rooms.setListData(r.toArray(new String[0]));
    }

    // updater list of users
    public void setUsers(ArrayList<String> u) {
        users.setListData(u.toArray(new String[0]));
    }

    // display name of user
    public void setUserInformation(String userName) {
        name.setText("Angemeldet als: " + userName);
    }

    // display server ip
    public void setServerInformation(int port, String ip) {
        server.setText("Server IP: " + ip);
        this.port.setText("Port: " + port);
    }

    // show alert
    public void popUp(String msg) {
        JOptionPane.showMessageDialog(frame, msg, "Verwarnung", JOptionPane.WARNING_MESSAGE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Senden":
                String message = text.getText();
                text.setText("");
                ClientController.send(message);
                addMessage("[" + ClientController.getName() + "]: " + message);

                break;

            case "Beitreten":
                if (rooms.getSelectedIndex() == -1) {
                    addMessage("Bitte wähle erst einen Raum aus.");
                } else {
                    String room = rooms.getSelectedValue();
                    ClientController.changeRoom(room);
                }
                break;
            case "Privaten Chat starten":
                if (users.getSelectedIndex() == -1) {
                    addMessage("Bitte wähle erst einen Nutzer aus.");    
                } 
                else {
                    String user = users.getSelectedValue();
                    ClientController.startWhisper(user);

                }
                break;

            default:
                break;
        }
    }
}
