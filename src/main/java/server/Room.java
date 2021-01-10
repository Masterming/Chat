package server;

import java.util.ArrayList;

public class Room {

    private int id;
    private ArrayList<ClientHandler> users;
    private String name;
    private boolean editable;

    public Room(int id, String name, boolean editable) {
        this.id = id;;
        this.setName(name);
        users = new ArrayList<>();
        this.editable = editable;
    }

    public int getId() {
        return id;
    }

    public boolean isEditable() {
        return editable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        
        name = name.replaceAll("[^(A-z)]", "");
        this.name = name;
    }

    public int getUsercount() {
        return users.size();
    }

    public void addUser(ClientHandler c) {
        users.add(c);
    }

    public void removeUser(ClientHandler c) {
        users.remove(c);
    }
}
