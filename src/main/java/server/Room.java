package server;

import java.util.ArrayList;

public class Room {

    private int id;
    private ArrayList<ClientHandler> users;
    private String name;
    private boolean editable;
    private static int id_count=0;

    public Room(String name, boolean editable) {
        this.id = id_count;
        this.setName(name);
        users = new ArrayList<>();
        this.editable = editable;
        id_count++;
    }

    public boolean geteditable(){
        return editable;
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

    public ArrayList<ClientHandler> getUsers(){
        return users;
    }

    public void addUser(ClientHandler c) {
        users.add(c);
    }

    public void removeUser(ClientHandler c) {
        users.remove(c);
    }
    public String toString(){
        return (name +"["+users.size()+"]");
    }
}
