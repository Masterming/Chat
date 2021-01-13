package client;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author blechner
 */
public class Client {

    private final static Logger LOGGER = Logger.getLogger(Client.class.getName());

    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private boolean running;

    private final String ip;
    private final int port;

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void run() {
        try {
            connect();
            setup();
            new Thread(() -> recieve()).start();
            new Thread(() -> consoleWrite()).start();
        } catch (IOException e) {
            close();
        }
    }

    private void connect() throws IOException {
        socket = new Socket(InetAddress.getByName(ip), port);
        System.out.println("Connected to: " + ip + ":" + port);
        LOGGER.log(Level.INFO, "Connected to: " + ip + ":" + port);
    }

    private void setup() throws IOException {
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        output.flush();
        LOGGER.log(Level.INFO, "Stream Setup complete.");
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
        } catch (IOException e) {
            System.exit(1);
        }
    }
}
