package server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.*;
import com.google.gson.Gson;

import parser.*;

/**
 * @author blechner
 */
public class ClientHandler implements Runnable {

    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private final Gson parser;

    private final int id;
    private String name;
    public int roomID;
    private boolean logged;

    private boolean running;

    public ClientHandler(Socket s, BufferedReader in, PrintWriter out, int id, String name) {
        this.socket = s;
        this.input = in;
        this.output = out;
        this.id = id;
        this.name = name;
        this.roomID = 0;
        this.running = true;
        this.logged = false;
        this.parser = new Gson();
    }

    @Override
    public void run() {
        receive();
    }

    public void write(Message msg) {
        String json = parser.toJson(msg);
        // System.out.println(json);
        System.out.println(msg);

        output.println(json);
        output.flush();
    }

    private Message read() throws IOException {
        String json = input.readLine();
        // System.out.println(json);
        Message msg = parser.fromJson(json, Message.class);
        System.out.println(msg);

        return msg;
    }

    private void receive() {
        // blocking
        running = true;

        while (running) {
            try {
                Message msg = read();
                switch (msg.type) {
                    case LOGIN_NAME: // Set user
                        String username = msg.content;
                        username = username.toLowerCase();
                        name = username.replaceAll("[^(a-z 0-9)]", "");
                        break;

                    case LOGIN_PW: // Try to login active user
                        String password = msg.content;
                        password = password.replaceAll("[^(A-z 0-9)]", "");
                        if (login(password)) {
                            logged = true;
                            ServerController.getRoom(roomID).addUser(this);
                            ServerController.updateView();
                            sendToRoom(new Message(MsgCode.MESSAGE, "[System]: " + name + " connected"));
                            ServerController.displayMessage(Level.INFO, "[System]: " + name + " connected");
                        }
                        break;

                    case MESSAGE: // Send message in room
                        if (!logged)
                            break;
                        ServerController.displayMessage(Level.INFO, "[" + name + "]: " + msg.content);
                        sendToRoom(new Message(MsgCode.MESSAGE, "[" + name + "]: " + msg.content));
                        break;

                    case GET_ACTIVE:
                        if (!logged)
                            break;
                        List<String> active = new ArrayList<>();
                        for (ClientHandler handler : ServerController.getUsers()) {
                            // Only show users, who are connected and logged in
                            if (handler.id != this.id && handler.logged) {
                                active.add(handler.name);
                            }
                        }
                        write(new Message(MsgCode.GET_ACTIVE, active.toString()));
                        break;

                    case CHANGE_ROOM:
                        ServerController.changeRoom(msg.content, this);
                        sendToRoom(new Message(MsgCode.MESSAGE, "[System]: " + name + " connected"));
                        ServerController.displayMessage(Level.INFO,
                                "[System]: " + name + " connected to " + msg.content);
                        break;

                    case LOGOUT:
                        ServerController.displayMessage(Level.INFO, "[System]: " + name + " disconnected");
                        close();
                        return;
                    
                    case WHISPER:
                        ServerController.createPrivate(this, msg.content);
                        break;
                        

                    default:
                        break;
                }
            } catch (IOException | NullPointerException e) {
                ServerController.displayMessage(Level.SEVERE, e.getMessage());
                close();
            }
        }
        close();
    }

    public boolean login(String password) {
        switch (checkPassword(name, password)) {
            case 0: // logged in
                write(new Message(MsgCode.LOGIN_SUCCESS, "0"));
                return true;
            case 1: // banned
                write(new Message(MsgCode.LOGIN_FAILED, "2"));
                close();
                return false;
            case 2: // pw wrong
                write(new Message(MsgCode.LOGIN_FAILED, "3"));
                return false;
            case 3: // registered
                write(new Message(MsgCode.LOGIN_SUCCESS, "1"));
                return true;
            case 4:
                write(new Message(MsgCode.LOGIN_FAILED, "4"));
            default:
                write(new Message(MsgCode.LOGIN_FAILED, "ERROR"));
                return false;
        }
    }

    public int checkPassword(String username, String password) {
        boolean name_found = false;

        // check if username exists
        for (ClientHandler c : ServerController.getUsers()) {
            if (username.equals(c.name))
                return 4;
        }
        for (String un : ServerController.getSql().getAllUser()) {
            if (username.equals(un)) {
                name_found = true;
                break;
            }
        }

        // register new user
        if (!name_found) {
            ServerController.getSql().register(username, password);
            return 3;
        }

        // log in existing user
        return ServerController.getSql().login(username, password);
    }

    private void sendToRoom(Message msg) {
        for (ClientHandler handler : ServerController.getRoom(roomID).getUsers()) {
            // Only show to users, who are connected and logged in the same room
            if (handler.id != this.id && handler.logged) {
                handler.write(msg);
            }
        }
    }

    public void close() {
        if (running) {
            logged = false;
            ServerController.leaveRoom(this);
            running = false;
            try {

                ServerController.updateView();
                input.close();
                output.close();
                socket.close();
            } catch (IOException e) {
                ServerController.displayMessage(Level.SEVERE, e.getMessage());
            }
            ServerController.displayMessage(Level.INFO, "Connection closed");
        }
    }

    public boolean getLogged() {
        return logged;
    }

    public String toString() {
        return ("[" + ServerController.getRoom(roomID).getName() + "] " + name);
    }

    public String getName() {
        return name;
    }

    public void updateR(ArrayList<String> rooms) {
        String tmp_r = parser.toJson(rooms);
        write(new Message(MsgCode.UPDATE_ROOMS, tmp_r));
    }

    public void updateU(ArrayList<String> users) {
        String tmp_u = parser.toJson(users);
        write(new Message(MsgCode.UPDATE_USERS, tmp_u));
    }
}
