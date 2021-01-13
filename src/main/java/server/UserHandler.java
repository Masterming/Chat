package server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.*;

/**
 * @author blechner
 */
public class UserHandler implements Runnable {

    private final static Logger LOGGER = Logger.getLogger(UserHandler.class.getName());

    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private final int id;
    private String name;
    public int roomID;
    private boolean running;
    private boolean logged;

    public UserHandler(Socket s, BufferedReader in, PrintWriter out, int id, String name) {
        this.socket = s;
        this.input = in;
        this.output = out;
        this.id = id;
        this.name = name;
        this.roomID = 0;
    }

    @Override
    public void run() {
        running = true;
        logged = false;

        login();
        if (logged) {
            Server.getRooms().get(roomID).addUser(this);
            Server.updategui();
            printActive();
            printNew();
            recieve();
        }
        this.close();

    }
    // recieve message and share it with all remaining users

    private void recieve() {
        // blocking
        running = true;
        char[] buffer = new char[1024];
        int count;

        while (running) {
            try {
                count = input.read(buffer, 0, 1024);
                String s = new String(buffer, 0, count);
                LOGGER.log(Level.INFO, "User " + id + ": " + s);

                for (UserHandler handler : Server.getUsersInRoom(roomID)) {
                    if (handler.id != this.id) {
                        handler.write("[" + name + "]: " + s + "\n");
                    }
                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, e.getMessage());
                close();
            }
        }
        close();
    }

    private void write(String msg) {
        output.print(msg);
        output.flush();
    }

    public void login() {
        String password;
        String username;

        char[] buffer = new char[1024];
        int count;

        try {
            write("Enter name: ");
            count = input.read(buffer, 0, 1024);
            username = new String(buffer, 0, count);
            username = username.toLowerCase();
            username = username.replaceAll("[^(a-z)]", "");

            write("Enter password: ");
            count = input.read(buffer, 0, 1024);
            password = new String(buffer, 0, count);
            password = password.replaceAll("[^(A-z)]", "");

            switch (checkPassword(username, password)) {
                case -1: // weird stuff happened
                    write("something went wrong on our side, please try again later\n");
                    login();
                    break;
                case 0: // logged in
                    this.write("login as: " + username + " successful\n");
                    name = username;
                    logged = true;
                    break;
                case 1: // banned
                    write("You are banned.\n");
                    close();
                    break;
                case 2: // pw wrong
                    write("Wrong password.\n");
                    login();
                    break;
                case 3: // registered
                    this.write("registered as: " + username + "\n");
                    name = username;
                    logged = true;
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
            close();
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

    private void printActive() {
        List<String> active = new ArrayList<>();
        for (UserHandler handler : Server.getUsers()) {
            // Only show users, who are connected and logged in
            if (handler.id != this.id && handler.logged) {
                active.add(handler.name);
            }
        }
        write("[System]: Currently online: " + active.toString() + "\n");
        // write("-------------------------------------------------------------\n\n");
    }

    private void printNew() {
        for (UserHandler handler : Server.getUsers()) {
            // Only show to users, who are connected and logged in
            if (handler.id != this.id && handler.logged) {
                handler.write("[System]: " + name + " connected\n");
                LOGGER.log(Level.INFO, "[System]: " + name + " connected\n");
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
    public boolean getlogged(){
        return logged;
    }

    public String toString() {
        return ("[" + Server.getRooms().get(roomID).getName() + "] " + name);
    }
}
