import java.io.*;
import java.net.*;

public class Server {

    private ServerSocket ss;
    private int portNumber;
    private Socket connection;
    private BufferedReader input;
    private PrintWriter output;

    public Server() {
        portNumber = 4999;
    }

    public void run() {
        try {
            ss = new ServerSocket(portNumber, 100);

            while (true) {
                try {
                    connect();
                    setupStreams();
                    chat();
                } catch (EOFException eofException) {
                    System.out.println("\n Server ended the connection! ");
                } finally {
                    close();
                }
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void connect() throws IOException {
        System.out.println("Waiting for someone to connect... \n");
        connection = ss.accept();
        System.out.println("Now connected to " + connection.getInetAddress().getHostName());
    }

    private void setupStreams() throws IOException {
        InputStreamReader in = new InputStreamReader(connection.getInputStream());
        input = new BufferedReader(in);
        output = new PrintWriter(connection.getOutputStream());
        output.flush();
    }

    private void chat() {

        while (true) {
            try {
                String msg = input.readLine();
                
                if(msg.length() > 0){
                    System.out.println(msg);  //TODO output.write
                }
            } 
            catch (IOException e) {

            }
        }
    }

    private void close() throws IOException {
        System.out.println("\n Closing connections... \n");
        input.close();
        output.close();
        connection.close();
    }
}
