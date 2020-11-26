package client;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

/**
 * @author blechner
 */
public class Client {
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private boolean running;

    private String ip;
    private int port;

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void run() {
        try {
            connect();
            setup();
            // write("Hello server!");
            // scheduleWrite();
            new Thread(() -> recieve()).start();
            new Thread(() -> consoleWrite()).start();
        } catch (IOException e) {
            // e.printStackTrace();
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
        System.out.println("Setup complete\n");
    }

    private void write(String msg) {
        // System.out.println("Sent: " + msg);
        output.print(msg);
        output.flush();
    }

    private void recieve() {
        // blocking
        running = true;
        char[] buffer = new char[1024];
        int count = 0;

        while (running) {
            try {
                count = input.read(buffer, 0, 1024);
                System.out.println(new String(buffer, 0, count));
            } catch (IOException e) {
                // e.printStackTrace();
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
                // e.printStackTrace();
                close();
            }
        }
    }

    public void close() {
        System.out.println("\n Closing connections...");
        running = false;
        try {
            input.close();
            output.close();
            socket.close();
            System.exit(0);
        } catch (IOException e) {
        }
    }
}
