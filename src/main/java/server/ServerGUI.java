package server;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.image.TileObserver;
import java.util.List;

public class ServerGUI {

    private JFrame frame;
    private JPanel panel;
    private JPanel chatPanel;
    private JPanel controlPanel;
    private JPanel status;
    private JPanel information;
    private JLabel info1;
    private JLabel info2;
    private JLabel info3;
    private JLabel info4;
    private JPanel aufgabe;
    private TextArea chat;
    private JTabbedPane statusAuswahl;
    private JList users;
    private JList rooms;
    private JTextField text;
    private JButton ok;
    private JComboBox wahl;
    private JPanel aufgabeAuswahl;

    public ServerGUI() {

        frame = new JFrame();
        panel = new JPanel(new BorderLayout());
        chatPanel = new JPanel(new BorderLayout());
        controlPanel = new JPanel();
        status = new JPanel();
        information = new JPanel(new GridLayout(2,2));
        info1 = new JLabel();
        info2 = new JLabel();
        info3 = new JLabel();
        info4 = new JLabel();
        aufgabe = new JPanel(new GridLayout(0,1));
        chat = new TextArea();
        statusAuswahl = new JTabbedPane();
        rooms = new JList();
        users = new JList();
        text = new JTextField();
        ok = new JButton("OK");
        wahl = new JComboBox();
        aufgabeAuswahl = new JPanel(new BorderLayout());

        //Chatfenster
        chat.setEditable(false);
        chatPanel.add(chat, BorderLayout.CENTER);

        //Status
        statusAuswahl.setForeground(Color.BLACK);
        statusAuswahl.addTab("Benutzer", users);
        statusAuswahl.addTab("Räume", rooms);
        status.setLayout(new BorderLayout());
        status.add(statusAuswahl, BorderLayout.CENTER);

        //Informationen
        information.add(info1);
        information.add(info2);
        information.add(info3);
        information.add(info4);

        //Aufgaben
        aufgabeAuswahl.add(wahl, BorderLayout.CENTER);
        aufgabeAuswahl.add(ok, BorderLayout.EAST);
        aufgabe.add(text);
        aufgabe.add(aufgabeAuswahl);

        //Panel-Überschriften
        chatPanel.setBorder(BorderFactory.createTitledBorder("Chat"));
        status.setBorder(BorderFactory.createTitledBorder("Status"));
        information.setBorder(BorderFactory.createTitledBorder("Information"));
        aufgabe.setBorder(BorderFactory.createTitledBorder("Aufgaben"));

        //Panel anordnen
        controlPanel.setLayout(new GridLayout(0,1));
        controlPanel.add(status);
        controlPanel.add(information);
        controlPanel.add(aufgabe);
        panel.add(chatPanel, BorderLayout.CENTER);
        panel.add(controlPanel, BorderLayout.EAST);

        //hübsch aussehen
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Server");
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

    public void setInformationRoom(int id, String name, int mitglieder, boolean edit) {
        info1.setText("ID: " + id);
        info2.setText("Name: " + name);
        info3.setText("Mitglieder: " + mitglieder);
        if(edit){ info4.setText("Editierbar: ja"); }
        else{ info4.setText("Editierbar: nein"); }
    }

}
