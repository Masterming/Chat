/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;
import java.io.*;
import java.net.*;
import java.util.*;
/**
 *
 * @author blech
 */
public class Server {
    
     private ServerSocket server;

    private int port;
    private static List<ClientHandler> clients;
    private static int i;
    private boolean running;

    private static SQLSocket sql;

    public static SQLSocket getSql() {
        return sql;
    }
    
    public Server(int port) {
        this.port = port;

        // Make an ArrayList to hold all client objects
        clients = Collections.synchronizedList(new ArrayList<ClientHandler>(128));
        running = true;
        i = 0;
        
        sql = new SQLSocket();
    }

    public void run() {
        try {
            Socket socket;
            BufferedReader input;
            PrintWriter output;
            server = new ServerSocket(port, 100);
            System.out.println("Server started. Waiting for clients...");

            while (running) {
                if(i == 128){
                    System.out.println("Maximum number of clients reached.");
                    break;
                }
                i++;

                // Connect with client
                socket = server.accept();
                System.out.println("Connected to " + socket.getInetAddress().getHostName() + "(i=" + i + ")");

                // Setup streams with client
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                output.flush();

                // Create a new handler object for handling this request. 
                ClientHandler handler = new ClientHandler(socket, input, output, i, "Client:"+i); // todo : client + i ==> name
                clients.add(handler);
                Thread t = new Thread(handler);
                t.start();
            }
        } catch (IOException e) {
            System.out.println("Connection closed");
            e.printStackTrace();
        }
    }

    // Getter
    public static List<ClientHandler> getClients(){
        return clients;
    }

    public void stop(){
        running = false;
        try {
            server.close();
        } catch (IOException e) {
        }
    }
    
}
