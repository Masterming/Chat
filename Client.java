import java.io.*;
import java.net.*;

public class Client {

    private Socket connection;
    private BufferedReader input;
    private PrintWriter output;

    public void run() {
        try {
            connection = new Socket("localhost", 4999);

            InputStreamReader in = new InputStreamReader(connection.getInputStream());
            input = new BufferedReader(in);
            output = new PrintWriter(connection.getOutputStream());
            System.out.println("1");
            chat();

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void chat() {
        System.out.println("2");
        try {
            System.out.println("2.1");

            BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("2.2");

            while (true) {
                System.out.println("2.3");

                String msg_in;
                if (input.ready() && (msg_in = input.readLine()) != null) {
                    System.out.println("server: " + msg_in);
                }
                System.out.println("2.4");

                String msg_out;
                if (buffer.ready() && (msg_out = buffer.readLine()) != null) {
                    output.write(msg_out);
                    output.flush();
                }
                System.out.println("3");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
