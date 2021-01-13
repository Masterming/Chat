package server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.*;

/**
 * @author blechner
 */
public class ClientHandler implements Runnable {

    private final static Logger LOGGER = Logger.getLogger(ClientHandler.class.getName());

    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    private final int id;
    private String name;
    private int roomID;

    private boolean running;
    private boolean logged;

    public ClientHandler(Socket s, BufferedReader in, PrintWriter out, int id, String name) {
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
            printActive();
            printNew();
            recieve();
        }
        this.close();

    }
    // recieve message and share it with all remaining clients

    private void recieve() {
        // blocking
        running = true;
        char[] buffer = new char[1024];
        int count;

        while (running) {
            try {
                count = input.read(buffer, 0, 1024);
                String s = new String(buffer, 0, count);
                LOGGER.log(Level.INFO, "Client " + id + ": " + s);

                for (ClientHandler handler : Server.getClientsInRoom(roomID)) {
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

            if (checkPassword(username, password)) {
                name = username;
                logged = true;
            } else {
                write("Wrong password.\n");
                login();
            }

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
            close();
        }
    }

    public boolean checkPassword(String username, String password) {
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
            this.write("registered as: " + username + "\n");
            return true;
        }

        // log in existing user
        return Server.getSql().login(username, password);
    }

    private void printActive() {
        List<String> active = new ArrayList<>();
        for (ClientHandler handler : Server.getClients()) {
            // Only show users, who are connected and logged in
            if (handler.id != this.id && handler.logged) {
                active.add(handler.name);
            }
        }
        write("[System]: Currently online: " + active.toString() + "\n");
        // write("-------------------------------------------------------------\n\n");
    }

    private void printNew() {
        for (ClientHandler handler : Server.getClients()) {
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
}
