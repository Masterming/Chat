/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import server.SQLSocket;

/**
 *
 * @author blech
 */
public class ClientHandler implements Runnable {

    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public boolean getlogged() {
        return logged;
    }
    private int id;
    private String name;

    private boolean running;
    private boolean logged;

    public ClientHandler(Socket s, BufferedReader in, PrintWriter out, int id, String name) {
        this.socket = s;
        this.input = in;
        this.output = out;
        this.id = id;
        this.name = name;
    }

    @Override
    public void run() {
        running = true;
        logged = false;

        // write("Hello User!");
        login();
        if (logged) {
            write("[System]: Currently online: "+onlineUsers().toString());
            newUserOnline();
            recieve();
        }
        System.out.println("Connection closed");
        this.close();

    }
    // recieve message and share it with all remaining clients

    private void recieve() {
        // blocking
        running = true;
        char[] buffer = new char[1024];
        int count = 0;

        while (running) {
            try {
                count = input.read(buffer, 0, 1024);
                String s = new String(buffer, 0, count);
                //System.out.println("Client " + id + ": " + s);

                for (ClientHandler handler : Server.getClients()) {
                    // if the recipient is found, write on its 
                    // output stream 
                    if (handler.id != this.id) {
                        handler.write("[" + name + "]: " + s);
                    }
                }
            } catch (IOException e) {
                // e.printStackTrace();
            }
        }
        close();
    }
    
    private void newUserOnline(){
        
        for (ClientHandler handler : Server.getClients()) {
                    // if the recipient is found, write on its 
                    // output stream 
                    if (handler.id != this.id) {
                        handler.write("[System]:New User online: " + name);
                    }
                }
    }
    
    private void write(String msg) {
        System.out.println("Sent: " + msg);
        output.print(msg);
        output.flush();
    }

    public void login() {
        String password;
        String username;
        
        char[] buffer = new char[64];
        int count = 0;

        this.write("Enter name: ");
        try {
            count = input.read(buffer, 0, 64);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        username = new String(buffer, 0, count);
        username = username.toLowerCase();
        
        this.write("Enter password: ");
        try {
            count = input.read(buffer, 0, 64);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        password = new String(buffer, 0, count);
        
        if (checkPassword(username, password)) {
            this.write("Your are logged in.\n");
            this.name = username;
            logged = true;
        } else {
            this.write("Wrong password.\n");
            login();
        }
    }
    private List onlineUsers(){
        
        List <String> online = new ArrayList<String>();
        for (ClientHandler handler : Server.getClients()) {
             
            if (handler.id != this.id && handler.getlogged()) {
                online.add(handler.name);
            }
        }
        return online;
    }

    public boolean checkPassword(String username, String password) {

        boolean name_found = false;
        if (Server.getSql().login(username, password)) {
            return true;
        } else {
            for (String name : Server.getSql().getUsers()) {
                if (username.equals(name)) {
                    name_found = true;
                }
            }
            if (!name_found) {
                Server.getSql().register(username, password);
                this.write("registered as: " + username+"\n");
                return true;
            } else {
                return false;

            }
        }

    }

    public void close() {
        System.out.println("\n Closing connections... \n");
        running = false;
        try {
            input.close();
            output.close();
            socket.close();
        } catch (IOException e) {
        }
    }
}
