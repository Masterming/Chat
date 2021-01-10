package client;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.TileObserver;
import java.util.List;

public class ClientGUI implements ActionListener {

    private JFrame frame;
    private JPanel panel;
    private JPanel chatPanel;
    private JPanel controlPanel;
    private JPanel status;
    private JPanel information;
    private JLabel server;
    private JLabel name;
    private TextArea chat;
    private JPanel message;
    private JTextField text;
    private JButton send;
    private JTabbedPane statusAuswahl;
    private JList users;
    private JList rooms;

    public ClientGUI(){
        
        frame = new JFrame();
        panel = new JPanel(new BorderLayout());
        chatPanel = new JPanel(new BorderLayout());
        controlPanel = new JPanel();
        status = new JPanel();
        information = new JPanel();
        server = new JLabel();
        name = new JLabel();
        chat = new TextArea();
        text = new JTextField();
        send = new JButton("Senden");
        message = new JPanel();
        statusAuswahl = new JTabbedPane();
        rooms = new JList();
        users = new JList();

        //Chatfenster
        chat.setEditable(false);
        message.setLayout(new BorderLayout());
        message.add(text, BorderLayout.CENTER);
        message.add(send, BorderLayout.EAST);
        chatPanel.add(chat, BorderLayout.CENTER);
        chatPanel.add(message, BorderLayout.SOUTH);

        //Status
        statusAuswahl.setForeground(Color.BLACK);
        statusAuswahl.addTab("Benutzer", users);
        statusAuswahl.addTab("Räume", rooms);
        status.setLayout(new BorderLayout());
        status.add(statusAuswahl, BorderLayout.CENTER);

        //Information
        information.setLayout(new BoxLayout(information, BoxLayout.Y_AXIS));
        information.add(server);
        information.add(name);

        //Panel-Überschriften
        chatPanel.setBorder(BorderFactory.createTitledBorder("Chat"));
        status.setBorder(BorderFactory.createTitledBorder("Status"));
        information.setBorder(BorderFactory.createTitledBorder("Information"));

        //Panel anordnen
        controlPanel.setLayout(new GridLayout(0,1));
        controlPanel.add(status);
        controlPanel.add(information);
        panel.add(chatPanel, BorderLayout.CENTER);
        panel.add(controlPanel, BorderLayout.EAST);

        //hübsch aussehen
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Client");
        frame.pack();
        frame.setVisible(true);
    }

    public void addMessage(String m) {
        chat.append(m);
    }

    public void setRooms(String[] r) {
        rooms.setListData(r);
    }

    public void setUsers(String[] u) {
        users.setListData(u);
    }

    public void setUserInformation(String userName) {
        name.setText("Angemeldet als:" + userName);
    }

    public void setServerInformation(int port, String ip) {
        server.setText("Server IP: " + ip + " , Port: " + port);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == send) {

        }
    }
}

