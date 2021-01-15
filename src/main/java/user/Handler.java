package user;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

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

    public void run() {
        recieve();
    }

    private void recieve() {
        running = true;

        while (running) {
            try {
                Message msg = read();
                Type type = new TypeToken<ArrayList<String>>() {
                }.getType();
                switch (msg.type) {
                    case LOGIN:
                        break;
                    case ERROR:
                        break;
                    case MESSAGE:
                        ClientController.displayMessage(msg.content);
                        break;
                    case UPDATE_ROOMS:

                        ArrayList<String> tmp_r = parser.fromJson(msg.content, type);
                        ClientController.updateRooms(tmp_r);
                        break;
                    case UPDATE_USERS:

                        ArrayList<String> tmp_u = parser.fromJson(msg.content, type);
                        ClientController.updateUsers(tmp_u);
                        break;
                    case WARNING:
                        ClientController.warnUser(msg.content);
                        break;
                    case KICK:
                        close();
                        break;
                    case BAN:
                        close();
                        break;
                    default:
                        break;
                }
                System.out.println(msg.content);
            } catch (IOException | NullPointerException e) {
                close();
            }
        }
    }

    private Message read() throws IOException {
        String json = input.readLine();
        // System.out.println(json);
        Message msg = parser.fromJson(json, Message.class);
        System.out.println(msg);
        return msg;
    }

    public void write(Message msg) {
        String json = parser.toJson(msg);
        System.out.println(msg);
        //System.out.println(json);
        output.println(json);
        output.flush();
    }

    public void close() {
        // System.out.println("\n The server disconnected...");
        logged =false;
        write(new Message(MsgCode.LOGOUT, ""));
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
        write(new Message(MsgCode.LOGIN_NAME, name));
        setName(name);
        write(new Message(MsgCode.LOGIN_PW, pw));
        try {
            Message msg = read();

            switch (msg.type) {
                case LOGIN_SUCCESS: // weird stuff happened
                    logged = true;
                    break;
                case LOGIN_FAILED:
                    if (msg.content.equals("2")) {
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