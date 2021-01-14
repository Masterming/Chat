package user;

import java.io.*;
import java.net.*;

import com.google.gson.Gson;

import parser.*;

/**
 * @author blechner
 */
public class ClientController {
    private final Gson parser;
    private ClientView gui;
    private LoginView login_gui;
    private boolean logged;
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private boolean running;

    private final String ip;
    private final int port;

    public ClientController(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.parser = new Gson();
    }

    public void run() {
        try {
            connect();
            setup();
            new Thread(() -> recieve()).start();
            new Thread(() -> consoleWrite()).start();
            login_gui = new LoginView();
            if (logged) {
                gui = new ClientView();
            }
        } catch (IOException e) {
            close();
        }
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

    private void connect() throws IOException {
        socket = new Socket(InetAddress.getByName(ip), port);
        System.out.println("Connected to: " + ip + ":" + port);
    }

    private void setup() throws IOException {
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        output.flush();
    }

    private void recieve() {
        running = true;

        while (running) {
            try {
                Message msg = read();
                System.out.println(msg.content);
            } catch (IOException e) {
                close();
            }
        }
    }

    private void consoleWrite() {
        BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));

        while (running) {
            String msg;
            try {
                if ((msg = buffer.readLine()) != null)
                    write(new Message(Type.MESSAGE, msg));
            } catch (IOException e) {
                close();
            }
        }
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
}
