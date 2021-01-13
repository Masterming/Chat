package server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.*;

/**
 * @author blechner
 */
public class Server {

    private final static Logger LOGGER = Logger.getLogger(Server.class.getName());

    private ServerSocket server;

    private final int port;
    private boolean running;
    private static List<ClientHandler> clients;
    private static Map<Integer, Room> rooms;
    private static int i;

    private static SQLSocket sql;

    public static SQLSocket getSql() {
        return sql;
    }

    public Server(int port) {
        this.port = port;

        // Make an ArrayList to hold all client objects
        clients = Collections.synchronizedList(new ArrayList<>(128));
        rooms = Collections.synchronizedMap(new HashMap<Integer, Room>(10));
        rooms.put(0, new Room( "Eingangshalle", false));
        running = true;
        i = 0;

        sql = new SQLSocket();
    }

    public void run() {
        try {
            server = new ServerSocket(port, 100);
            LOGGER.log(Level.INFO, "Server started. Waiting for clients...");
            new Thread(() -> AcceptClientsAsync()).start();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to start the sever: " + e.getMessage());
        }
    }

    private void AcceptClientsAsync() {
        try {
            Socket socket;
            BufferedReader input;
            PrintWriter output;

            while (running) {
                if (i == 128) {
                    LOGGER.log(Level.WARNING, "Maximum number of clients reached.");
                    break;
                }
                i++;

                // Connect with client
                socket = server.accept();
                // LOGGER.log(Level.INFO, "Connected to " +
                // socket.getInetAddress().getHostName() +
                // "(i=" + i + ")");

                // Setup streams with client
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                output.flush();

                // Create a new handler object for handling this request.
                ClientHandler c = new ClientHandler(socket, input, output, i, "Client" + i);

                clients.add(c);
                rooms.get(0).addUser(c);
                Thread t = new Thread(c);
                t.start();
            }
        } catch (IOException e) {
            stop();
        }

    }

    // Getter
    public static List<ClientHandler> getClients() {
        return clients;
    }

    public void stop() {
        if (running) {
            running = false;
            try {
                server.close();
                LOGGER.log(Level.INFO, "Server closed");
                System.exit(0);
            } catch (Exception e) {
                System.exit(1);
            }

        }
    }

    public static List<ClientHandler> getClientsInRoom(int id) {
        // TODO: get Room and clients
        return null;
    }

    public static void changeRoom(int destination_id, ClientHandler user) {
        rooms.get(user.roomID).removeUser(user);
        user.roomID = destination_id;
        rooms.get(user.roomID).addUser(user);
    }

    public static Map<Integer, Room> getRooms() {
        return rooms;
    }

    public static void addroom(Room new_room) {
        rooms.put(new_room.getId(), new_room);
    }

    public static void deleteRoom(int room_id) {
        if (rooms.get(room_id).geteditable()) {
            for (ClientHandler c : rooms.get(room_id).getUsers()) {
                changeRoom(0, c);
            }
            rooms.remove(room_id);
        }
    }
}
