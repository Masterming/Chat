package user;

import java.io.*;
import java.net.*;

/**
 * @author blechner
 */
public class ClientController {
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

    }

    public void run() {
        try {
            connect();
            setup();
            new Thread(() -> recieve()).start();
            new Thread(() -> consoleWrite()).start();
            login_gui = new LoginView();
            if (logged){
                gui = new ClientView();
            }
        } catch (IOException e) {
            close();
        }
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

    private void write(String msg) {
        output.print(msg);
        output.flush();
        // System.out.println(msg);
    }

    private void recieve() {
        running = true;

        while (running) {
            char[] buffer = new char[1024];
            try {
                input.read(buffer, 0, 1024); // blocking
                System.out.print(String.valueOf(buffer));
                // System.out.flush();
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
                    write(msg);
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
