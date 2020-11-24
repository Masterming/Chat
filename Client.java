import java.io.*;
import java.net.*;
import java.util.concurrent.*;

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
            write("Hello server!");
            // scheduleWrite();
            new Thread(() -> recieve()).start();
            new Thread(() -> consoleWrite()).start();
        } catch (IOException e) {
            System.out.println("Connection closed");
            // e.printStackTrace();
            running = false;
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
        System.out.println("Sent: " + msg);
        output.print(msg);
        output.flush();
    }

    private void scheduleWrite() {
        final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> write("Message"), 0, 5, TimeUnit.SECONDS);
    }

    private void recieve() {
        // blocking
        running = true;
        char[] buffer = new char[1024];
        int count = 0;

        while (running) {
            try {
                count = input.read(buffer, 0, 1024);
                System.out.println("Server: " + new String(buffer, 0, count));
            } catch (IOException e) {
                System.out.println("Connection closed");
                // e.printStackTrace();
                running = false;
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
                System.out.println("Connection closed");
                // e.printStackTrace();
                running = false;
            }
        }
    }
}
