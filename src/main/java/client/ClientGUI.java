package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientGUI implements ActionListener {

    private final static Logger LOGGER = Logger.getLogger(ClientGUI.class.getName());

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
    private JTabbedPane statusAuswahl;
    private JList<String> users;
    private JList<String> rooms;
    private JButton join;
    private JPanel roomWbutton;

    public ClientGUI() {

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
        statusAuswahl = new JTabbedPane();
        rooms = new JList<>();
        users = new JList<>();
        join = new JButton("Beitreten");
        roomWbutton = new JPanel(new BorderLayout());

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
        JScrollPane scrollu = new JScrollPane(users);
        JScrollPane scrollr = new JScrollPane(rooms);
        roomWbutton.add(scrollr, BorderLayout.CENTER);
        roomWbutton.add(join, BorderLayout.SOUTH);
        statusAuswahl.setForeground(Color.BLACK);
        statusAuswahl.addTab("Benutzer", scrollu);
        statusAuswahl.addTab("Räume", roomWbutton);
        status.setLayout(new BorderLayout());
        status.add(statusAuswahl, BorderLayout.CENTER);
        join.addActionListener(this);

        // Information
        information.setLayout(new GridLayout(2, 2));
        information.add(server);
        information.add(port);
        information.add(name);

        // Panel-Überschriften
        chatPanel.setBorder(BorderFactory.createTitledBorder("Chat"));
        status.setBorder(BorderFactory.createTitledBorder("Status"));
        information.setBorder(BorderFactory.createTitledBorder("Information"));

        // Panel anordnen
        controlPanel.setLayout(new BorderLayout());
        controlPanel.add(status, BorderLayout.CENTER);
        controlPanel.add(information, BorderLayout.NORTH);
        panel.add(chatPanel, BorderLayout.CENTER);
        panel.add(controlPanel, BorderLayout.EAST);

        // hübsch aussehen
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Client");
        frame.pack();
        frame.setVisible(true);
    }

    // Nachricht ins Chatfenster schreiben
    public void addMessage(String m) {
        chat.append("\n" + m);
    }

    // Raumliste aktualisieren
    public void setRooms(String[] r) {
        rooms.setListData(r);
    }

    // Nutzerliste aktualisieren
    public void setUsers(String[] u) {
        users.setListData(u);
    }

    // Nutzernamen anzeigen
    public void setUserInformation(String userName) {
        name.setText("Angemeldet als: " + userName);
    }

    // Serverinformationen anzeigen
    public void setServerInformation(int port, String ip) {
        server.setText("Server IP: " + ip);
        this.port.setText("Port: " + port);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "send":
                String message = text.getText();
                text.setText("");
                addMessage(message);
                // TODO: send message
                break;

            case "join":
                if (rooms.getSelectedIndex() == -1) {
                    addMessage("Bitte wähle erst einen Raum aus.");
                } else {
                    int room = rooms.getSelectedIndex();
                    LOGGER.log(Level.INFO, "Entered room" + room);
                    // TODO: join room
                }
                break;

            default:
                break;
        }
    }
}
