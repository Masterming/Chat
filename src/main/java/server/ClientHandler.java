/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.*;
import java.net.*;
import java.util.*;

import jdk.nashorn.internal.runtime.UserAccessorProperty;
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
    private int id;
    private String name;
    
    private boolean running;

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

        // write("Hello User!");
        login();
        recieve();
        System.out.println("Connection closed");
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

                for (ClientHandler handler : Server.getClients())  
                { 
                    // if the recipient is found, write on its 
                    // output stream 
                    if (handler.id != this.id)  
                    { 
                        handler.write("[" + name + "]: " + s);
                    } 
                } 
            } catch (IOException e) {
                // e.printStackTrace();
            }
        }
        close();
    }

    private void write(String msg) {
        System.out.println("Sent: " + msg);
        output.print(msg);
        output.flush();
    }

    public void login() {
        String password;
        String username;
        
        char[] buffer = new char[1024];
        int count = 0;

        this.write("Enter name: ");
        count = input.read(buffer, 0, 1024);
        username = new String(buffer, 0, count);

        this.write("Enter password: ");
        count = input.read(buffer, 0, 1024);
        password = new String(buffer, 0, count);


        if(checkPassword(username, password)) {
            this.write("Your are logged in.");
        } else {
            this.write("Wrong password.");
            this.close();
        }
    }

    public boolean checkPassword(String username, String password){
        if(SQLSocket.login(username, password)){
            return true;
        } else {
            for(String name : SQLSocket.getUsers()) {
                if(username == name) {
                    return false;
                } else {
                    SQLSocket.register(username, password);
                    return true;
                }
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
