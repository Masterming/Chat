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
public class UserHandler implements Runnable {

    private final static Logger LOGGER = Logger.getLogger(UserHandler.class.getName());

    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private final Gson parser;

    private final int id;
    private String name;
    public int roomID;
    private boolean logged;

    private boolean running;

    public UserHandler(Socket s, BufferedReader in, PrintWriter out, int id, String name) {
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
        recieve();
    }

    private void recieve() {
        // blocking
        running = true;

        while (running) {
            try {
                Message msg = read();
                switch (msg.type) {
                    case LOGIN_NAME: // Set user
                        String username = msg.content;
                        username = username.toLowerCase();
                        name = username.replaceAll("[^(a-z)]", "");
                        break;

                    case LOGIN_PW: // Try to login active user
                        String password = msg.content;
                        password = password.replaceAll("[^(A-z)]", "");
                        if (login(password)) {
                            logged = true;
                            Server.getRoom(roomID).addUser(this);
                            Server.updategui();
                            sendToRoom(new Message(Type.MESSAGE, "[System]: " + name + " connected\n"));
                            LOGGER.log(Level.INFO, "[System]: " + name + " connected\n");
                        }
                        break;

                    case MESSAGE: // Send message in room
                        if (!logged)
                            break;
                        LOGGER.log(Level.INFO, "User " + id + ": " + msg.content);
                        sendToRoom(new Message(Type.MESSAGE, "[" + name + "]: " + msg.content));
                        break;

                    case GET_ACTIVE:
                        if (!logged)
                            break;
                        List<String> active = new ArrayList<>();
                        for (UserHandler handler : Server.getUsers()) {
                            // Only show users, who are connected and logged in
                            if (handler.id != this.id && handler.logged) {
                                active.add(handler.name);
                            }
                        }
                        write(new Message(Type.GET_ACTIVE, active.toString()));
                        break;

                    case CHANGE_ROOM:
                        Server.changeRoom(Integer.parseInt(msg.content), this);
                        sendToRoom(new Message(Type.MESSAGE, "[System]: " + name + " connected\n"));
                        break;

                    case DISCONNECT:
                        // Server.
                        close();
                        return;

                    default:
                        break;
                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, e.getMessage());
                close();
            }
        }
        close();
    }

    private void write(Message msg) {
        String json = parser.toJson(msg);
        output.print(json);
        output.flush();
    }

    private Message read() throws IOException {
        char[] buffer = new char[1024];
        int count = input.read(buffer, 0, 1024);
        String json = new String(buffer, 0, count);
        Message msg = parser.fromJson(json, Message.class);
        return msg;
    }

    public boolean login(String password) {
        switch (checkPassword(name, password)) {
            case -1: // weird stuff happened
                write(new Message(Type.ERROR, "ERROR"));
                return false;
            case 0: // logged in
                write(new Message(Type.LOGIN, "0"));
                return true;
            case 1: // banned
                write(new Message(Type.LOGIN, "1"));
                close();
                return false;
            case 2: // pw wrong
                write(new Message(Type.LOGIN, "2"));
                return false;
            case 3: // registered
                write(new Message(Type.LOGIN, "3"));
                return true;
            default:
                write(new Message(Type.ERROR, "ERROR"));
                return false;
        }
    }

    public int checkPassword(String username, String password) {
        boolean name_found = false;

        // check if username exists
        for (String un : Server.getSql().getAllUser()) {
            if (username.equals(un)) {
                name_found = true;
                break;
            }
        }

        // register new user
        if (!name_found) {
            Server.getSql().register(username, password);
            return 3;
        }

        // log in existing user
        return Server.getSql().login(username, password);
    }

    private void sendToRoom(Message msg) {
        for (UserHandler handler : Server.getRoom(roomID).getUsers()) {
            // Only show to users, who are connected and logged in the same room
            if (handler.id != this.id && handler.logged) {
                handler.write(msg);
            }
        }
    }

    public void close() {
        if (running) {
            running = false;
            try {
                input.close();
                output.close();
                socket.close();
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, e.getMessage());
            }
            LOGGER.log(Level.INFO, "Connection closed");
        }
    }

    public boolean getlogged() {
        return logged;
    }

    public String toString() {
        return ("[" + Server.getRoom(roomID).getName() + "] " + name);
    }
}
