package user;

import java.io.*;
import java.net.*;

import com.google.gson.Gson;

import parser.*;

public class Handler implements Runnable {

    private final Gson parser;
    private boolean logged;
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private boolean running;
    private String name;

    private final String ip;
    private final int port;

    public Handler(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.parser = new Gson();
        try {
            setup();
        } catch (IOException e) {

        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private void setup() throws IOException {
        socket = new Socket(InetAddress.getByName(ip), port);
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        output.flush();
    }
    public void run(){
        recieve();
    }

    private void recieve() {
        running = true;

        while (running) {
            try {
                Message msg = read();

                switch (msg.type) {
                    case LOGIN:
                        break;
                    case ERROR:
                        break;
                    case MESSAGE:
                        ClientController.displayMessage(msg.content);
                        break;
                }
                System.out.println(msg.content);
            } catch (IOException e) {
                close();
            }
        }
    }

    private Message read() throws IOException {
        char[] buffer = new char[1024];
        int count = input.read(buffer, 0, 1024);
        String json = new String(buffer, 0, count);
        Message msg = parser.fromJson(json, Message.class);
        System.out.println(msg);

        return msg;
    }

    public void write(Message msg) {
        String json = parser.toJson(msg);
        System.out.println(msg);
        output.print(json);
        output.flush();
    }

    public void close() {
        // System.out.println("\n The server disconnected...");
        running = false;
        try {
            input.close();
            output.close();
            socket.close();
            System.exit(0);
        } catch (IOException | NullPointerException e) {
            System.exit(1);
        }
    }

    public boolean login(String name, String pw) {
        write(new Message(Type.LOGIN_NAME, name));
        setName(name);
        write(new Message(Type.LOGIN_PW, pw));
        try {
            Message msg = read();

            switch (msg.type) {
                case LOGIN_SUCCESS: // weird stuff happened
                    logged = true;
                    break;
                case LOGIN_FAILED:
                    if (msg.content.equals("3")) {
                        close();
                    }
                    logged = false;
                    break;

                default:
                    logged = false;

            }
        } catch (IOException e) {

        }
        return logged;
    }
}