package server;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

public class ServerView implements ActionListener, ListSelectionListener {

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
    private JPanel task;
    private TextArea chat;
    private JTabbedPane statusSelection;
    private JList<ClientHandler> users;
    private JList<Room> rooms;
    private JButton ok;
    private JComboBox<String> choice;
    private JPanel taskSelection;
    private JPanel newRoom;
    private JLabel rName;
    private JTextField roomName;
    private JCheckBox editable;
    private JButton okNewRoom;
    private JPanel deleteRoom;
    private JLabel deleteRLabel;
    private JButton deleteRButton;
    private JPanel editRoom;
    private JLabel editRLabel;
    private JPanel editName;
    private JLabel editRNameLabel;
    private JTextField editRNameText;
    private JButton editRButton;
    private JPanel warnUser;
    private JLabel warnLabel;
    private JTextField warnMessage;
    private JButton warnButton;
    private JPanel kickUser;
    private JLabel kickLabel;
    private JButton kickButton;
    private JPanel banUser;
    private JLabel banLabel;
    private JButton banButton;
    private JPanel taskSouth;

    public ServerView() {

        frame = new JFrame();
        panel = new JPanel(new BorderLayout());
        chatPanel = new JPanel(new BorderLayout());
        controlPanel = new JPanel(new BorderLayout());
        status = new JPanel();
        information = new JPanel(new GridLayout(2, 2));
        info1 = new JLabel();
        info2 = new JLabel();
        info3 = new JLabel();
        info4 = new JLabel();
        task = new JPanel(new BorderLayout());
        chat = new TextArea();
        statusSelection = new JTabbedPane();
        rooms = new JList<>();
        users = new JList<>();
        ok = new JButton("OK");
        taskSelection = new JPanel(new BorderLayout());
        newRoom = new JPanel(new GridLayout(2, 2));
        rName = new JLabel("Raumname:");
        roomName = new JTextField();
        editable = new JCheckBox("Editierbar");
        okNewRoom = new JButton("Raum erstellen");
        deleteRoom = new JPanel(new GridLayout(0, 1));
        deleteRLabel = new JLabel("Wähle den Raum aus der Liste aus.");
        deleteRButton = new JButton("Raum löschen");
        editRoom = new JPanel(new BorderLayout());
        editRLabel = new JLabel("Wähle den Raum aus der Liste aus.");
        editName = new JPanel(new GridLayout(0, 2));
        editRNameLabel = new JLabel("Neuer Name:");
        editRNameText = new JTextField();
        editRButton = new JButton("Speichern");
        warnUser = new JPanel(new GridLayout(0, 1));
        warnLabel = new JLabel("Wähle einen Nutzer aus und füge eine Nachricht hinzu.");
        warnMessage = new JTextField();
        warnButton = new JButton("Warnen");
        kickUser = new JPanel(new GridLayout(0, 1));
        kickLabel = new JLabel("Wähle einen Nutzer aus der Liste aus.");
        kickButton = new JButton("Kicken");
        banUser = new JPanel(new GridLayout(0, 1));
        banLabel = new JLabel("Wähle einen Nutzer aus der Liste aus.");
        banButton = new JButton("Bannen");
        taskSouth = new JPanel(new FlowLayout());

        // Chatfenster
        chat.setEditable(false);
        chatPanel.add(chat, BorderLayout.CENTER);

        // Status
        users.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        users.addListSelectionListener(this);
        rooms.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        rooms.addListSelectionListener(this);
        JScrollPane scrollu = new JScrollPane(users);
        JScrollPane scrollr = new JScrollPane(rooms);
        statusSelection.setForeground(Color.BLACK);
        statusSelection.addTab("Benutzer", scrollu);
        statusSelection.addTab("Räume", scrollr);
        status.setLayout(new BorderLayout());
        status.add(statusSelection, BorderLayout.CENTER);

        // Informationen
        information.add(info1);
        information.add(info2);
        information.add(info3);
        information.add(info4);

        // Aufgaben
        String auswahlListe[] = { "Raum erstellen", "Raum löschen", "Raum bearbeiten", "Benutzer verwarnen",
                "Benutzer kicken", "Benutzer bannen" };
        choice = new JComboBox<>(auswahlListe);
        ok.addActionListener(this);
        taskSelection.add(choice, BorderLayout.CENTER);
        taskSelection.add(ok, BorderLayout.EAST);
        task.add(taskSelection, BorderLayout.CENTER);
        task.add(taskSouth, BorderLayout.SOUTH);
        taskSouth.add(newRoom);
        taskSouth.add(deleteRoom);
        taskSouth.add(editRoom);
        taskSouth.add(warnUser);
        taskSouth.add(kickUser);
        taskSouth.add(banUser);

        // Raum erstellen
        okNewRoom.addActionListener(this);
        newRoom.add(rName);
        newRoom.add(roomName);
        newRoom.add(editable);
        newRoom.add(okNewRoom);
        newRoom.setVisible(false);

        // Raum löschen
        deleteRoom.add(deleteRLabel);
        deleteRoom.add(deleteRButton);
        deleteRButton.setForeground(Color.RED);
        deleteRButton.addActionListener(this);
        deleteRoom.setVisible(false);

        // Raum editieren
        editRoom.add(editRLabel, BorderLayout.NORTH);
        editName.add(editRNameLabel);
        editName.add(editRNameText);
        editRoom.add(editName, BorderLayout.CENTER);
        editRoom.add(editRButton, BorderLayout.SOUTH);
        editRButton.addActionListener(this);
        editRoom.setVisible(false);

        // Nutzer warnen
        warnUser.add(warnLabel);
        warnUser.add(warnMessage);
        warnUser.add(warnButton);
        warnButton.addActionListener(this);
        warnButton.setForeground(Color.RED);
        warnUser.setVisible(false);

        // Nutzer kicken
        kickUser.add(kickLabel);
        kickUser.add(kickButton);
        kickButton.addActionListener(this);
        kickButton.setForeground(Color.RED);
        kickUser.setVisible(false);

        // Nutzer bannen
        banUser.add(banLabel);
        banUser.add(banButton);
        banButton.addActionListener(this);
        banButton.setForeground(Color.RED);
        banUser.setVisible(false);

        // Panel-Überschriften
        chatPanel.setBorder(BorderFactory.createTitledBorder("Chat"));
        status.setBorder(BorderFactory.createTitledBorder("Status"));
        information.setBorder(BorderFactory.createTitledBorder("Information"));
        task.setBorder(BorderFactory.createTitledBorder("Aufgaben"));

        // Panel anordnen
        controlPanel.add(status, BorderLayout.CENTER);
        controlPanel.add(information, BorderLayout.NORTH);
        controlPanel.add(task, BorderLayout.SOUTH);
        panel.add(chatPanel, BorderLayout.CENTER);
        panel.add(controlPanel, BorderLayout.EAST);

        // hübsch aussehen
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(frame, "Beenden?", "Exit", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
        frame.setTitle("Server");
        frame.pack();
        frame.setVisible(true);
        frame.setMinimumSize(new Dimension(800, 350));

    }

    public void addMessage(String m) {
        chat.append(m + "\n");
    }

    public void setRooms(Map<Integer, Room> r) {
        rooms.setListData(r.values().toArray(new Room[0]));
    }

    public void setUsers(List<ClientHandler> c) {

        users.setListData(c.toArray(new ClientHandler[0]));
    }

    public void setInformationRoom(Room room) {
        info1.setText("ID: " + room.getId());
        info2.setText("Name: " + room.getName());
        info3.setText("Mitglieder: " + room.size());
        info4.setText("Editierbar: " + room.isEditable());
    }

    public void setInformationUser(int id, String name) {
        info1.setText("ID: " + id);
        info2.setText("Name: " + name);
        info3.setText("");
        info4.setText("");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ok) {
            newRoom.setVisible(false);
            deleteRoom.setVisible(false);
            editRoom.setVisible(false);
            warnUser.setVisible(false);
            kickUser.setVisible(false);
            banUser.setVisible(false);
            switch (choice.getSelectedIndex()) {
                case 0:
                    newRoom.setVisible(true);
                    break;
                case 1:
                    deleteRoom.setVisible(true);
                    statusSelection.setSelectedIndex(1);
                    break;
                case 2:
                    editRoom.setVisible(true);
                    statusSelection.setSelectedIndex(1);
                    break;
                case 3:
                    warnUser.setVisible(true);
                    statusSelection.setSelectedIndex(0);
                    break;
                case 4:
                    kickUser.setVisible(true);
                    statusSelection.setSelectedIndex(0);
                    break;
                case 5:
                    banUser.setVisible(true);
                    statusSelection.setSelectedIndex(0);
                    break;
                default:
                    break;
            }
            frame.pack();
        } else if (e.getSource() == okNewRoom) {
            if (roomName.getText().equals("")) {
                addMessage("Raumname eintragen!");
            } else {
                String name = roomName.getText();
                Boolean edit = editable.isSelected();
                newRoom.setVisible(false);

                ServerController.addroom(new Room(name, edit, true));

            }
        } else if (e.getSource() == deleteRButton) {
            if (rooms.getSelectedIndex() == -1) {
                addMessage("Wähle einen Raum aus!");
            } else {
                Room room = rooms.getSelectedValue();
                deleteRoom.setVisible(false);

                ServerController.deleteRoom(room.getId());
            }

        } else if (e.getSource() == editRButton) {
            if (rooms.getSelectedIndex() == -1) {
                addMessage("Wähle einen Raum aus!");
            } else {
                if (editRNameText.getText().equals("")) {
                    addMessage("Neuen Raumnamen eintragen!");
                } else {
                    String name = editRNameText.getText();
                    Room room = rooms.getSelectedValue();
                    editRoom.setVisible(false);

                    ServerController.editRoom(room.getId(), name);
                }
            }
        } else if (e.getSource() == warnButton) {
            if (users.getSelectedIndex() == -1) {
                addMessage("Wähle einen Nutzer aus!");
            } else {
                if (warnMessage.getText().equals("")) {
                    addMessage("Füge eine Nachricht hinzu!");
                } else {
                    String message = warnMessage.getText();
                    ClientHandler user = users.getSelectedValue();
                    warnUser.setVisible(false);

                    ServerController.warnUser(message, user);

                }
            }
        } else if (e.getSource() == kickButton) {
            if (users.getSelectedIndex() == -1) {
                addMessage("Wähle einen Nutzer aus!");
            } else {
                ClientHandler user = users.getSelectedValue();
                kickUser.setVisible(false);

                ServerController.kickUser(user);

            }
        } else if (e.getSource() == banButton) {
            if (users.getSelectedIndex() == -1) {
                addMessage("Wähle einen Nutzer aus!");
            } else {
                ClientHandler user = users.getSelectedValue();
                banUser.setVisible(false);

                ServerController.banUser(user);
            }
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
    }
}
