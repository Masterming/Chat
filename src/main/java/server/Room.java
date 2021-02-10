package server;

import java.util.concurrent.CopyOnWriteArrayList;

public class Room {

    private int id;
    private CopyOnWriteArrayList<ClientHandler> users;
    private String name;
    private boolean editable;
    private boolean visible;
    private static int id_count = 0;

    public Room(String name, boolean editable, boolean visible) {
        this.id = id_count;
        this.setName(name);
        users = new CopyOnWriteArrayList<>();
        this.visible = visible;
        this.editable = editable;
        id_count++;
    }

    public boolean isVisible() {
        return visible;
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

        name = name.replaceAll("[^(A-z )]", "");
        this.name = name;
    }

    public int size() {
        return users.size();
    }

    public CopyOnWriteArrayList<ClientHandler> getUsers() {
        return users;
    }

    public void addUser(ClientHandler c) {
        users.add(c);
    }

    public void removeUser(ClientHandler c) {
        users.remove(c);
    }

    public String toString() {
        return (name + "[" + users.size() + "]");
    }

    @Override
    public boolean equals(Object o) {
        // self check
        if (this == o)
            return true;
        // null check
        if (o == null)
            return false;
        // type check and cast
        if (getClass() != o.getClass())
            return false;
        Room f = (Room) o;
        // field comparison
        return f.name.equals(this.name);

    }

}
