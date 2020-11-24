import java.io.*;
import java.net.*;

public class Server {

    private ServerSocket server;

    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private boolean running;

    private int port;

    public Server(int port) {
        this.port = port;
    }

    public void run() {
        try {
            server = new ServerSocket(port, 100);

            while (true) {
                try {
                    connect();
                    setup();
                    write("Hello User!");
                    recieve();
                } catch (EOFException e) {
                    System.out.println("Connection closed");
                } finally {
                    close();
                }
            }
        } catch (IOException e) {
            System.out.println("Connection closed");
            // e.printStackTrace();
            running = false;
        }
    }

    private void connect() throws IOException {
        System.out.println("Waiting for someone to connect...");
        socket = server.accept();
        System.out.println("Now connected to " + socket.getInetAddress().getHostName());
    }

    private void setup() throws IOException {
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        output.flush();
        System.out.println("Setup complete\n");
    }

    private void write(String msg) {
        System.out.println("Sent: " + msg);
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
                System.out.println("Client: " + new String(buffer, 0, count));
            } catch (IOException e) {
                System.out.println("Connection closed");
                // e.printStackTrace();
                running = false;
            }
        }
    }

    private void close() throws IOException {
        System.out.println("\n Closing connections... \n");
        input.close();
        output.close();
        socket.close();
    }
}
