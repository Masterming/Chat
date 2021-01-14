package server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.*;

import parser.*;

/**
 * @author blechner
 */
public class ServerController {

    private final static Logger LOGGER = Logger.getLogger(ServerController.class.getName());

    private ServerSocket server;

    private final int port;
    private boolean running;
    private static List<ClientHandler> users;
    private static Map<Integer, Room> rooms;
    private static int i;
    private static ServerView gui;

    private static SQLSocket sql;

    public static SQLSocket getSql() {
        return sql;
    }

    public ServerController(int port) {
        this.port = port;

        // Make an ArrayList to hold all user objects
        users = Collections.synchronizedList(new ArrayList<>(128));
        rooms = Collections.synchronizedMap(new HashMap<Integer, Room>(10));
        rooms.put(0, new Room("Eingangshalle", false));
        gui = new ServerView();
        updategui();

        // gui.setInformationRoom();

        running = true;
        i = 0;

        sql = new SQLSocket();
    }

    public void run() {
        try {
            server = new ServerSocket(port, 100);
            LOGGER.log(Level.INFO, "Server started. Waiting for users...");
            new Thread(() -> AcceptUsersAsync()).start();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to start the sever: " + e.getMessage());
        }
    }

    private void AcceptUsersAsync() {
        try {
            Socket socket;
            BufferedReader input;
            PrintWriter output;

            while (running) {
                if (i == 128) {
                    LOGGER.log(Level.WARNING, "Maximum number of users reached.");
                    break;
                }
                i++;

                // Connect with user
                socket = server.accept();
                // LOGGER.log(Level.INFO, "Connected to " +
                // socket.getInetAddress().getHostName() +
                // "(i=" + i + ")");

                // Setup streams with user
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                output.flush();

                // Create a new handler object for handling this request.
                ClientHandler c = new ClientHandler(socket, input, output, i, "User" + i);

                users.add(c);
                Thread t = new Thread(c);
                t.start();
            }
        } catch (IOException e) {
            stop();
        }

    }

    // Getter
    public static List<ClientHandler> getUsers() {
        List<ClientHandler> loggedUsers = new ArrayList<>();
        for (ClientHandler u : users) {
            if (u.getlogged()) {
                loggedUsers.add(u);
            }
        }
        return loggedUsers;
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

    public static void updategui() {
        gui.setRooms(rooms);
        gui.setUsers(users);
    }

    public static void changeRoom(int destination_id, ClientHandler user) {
        getRoom(user.roomID).removeUser(user);
        user.roomID = destination_id;
        getRoom(user.roomID).addUser(user);
        updategui();
    }

	public static Room getRoom(int roomID) {
		return getRoom(roomID);
	}

    public static Map<Integer, Room> getRooms() {
        return rooms;
    }

    public static void addroom(Room new_room) {
        rooms.put(new_room.getId(), new_room);
        updategui();
    }

    public static void deleteRoom(int room_id) {
        if (getRoom(room_id).isEditable()) {
            for (ClientHandler c : getRoom(room_id).getUsers()) {
                changeRoom(0, c);
            }
            rooms.remove(room_id);
            updategui();
        }
    }

    public static void editRoom(int id, String name) {
        if (getRoom(id).isEditable()) {
            getRoom(id).setName(name);
            updategui();
            LOGGER.log(Level.INFO, "Raum editiert");
        } else {
            LOGGER.log(Level.WARNING, "Raum nicht editierbar");
        }
    }
    public static void displayMessage(String msg){
        gui.addMessage(msg);
    }
}
