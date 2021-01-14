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
    private static List<UserHandler> users;
    private static Map<Integer, Room> rooms;
    private static int i;
    private static ServerGUI gui;

    private static SQLSocket sql;

    public static SQLSocket getSql() {
        return sql;
    }

    public Server(int port) {
        this.port = port;

        // Make an ArrayList to hold all user objects
        users = Collections.synchronizedList(new ArrayList<>(128));
        rooms = Collections.synchronizedMap(new HashMap<Integer, Room>(10));
        rooms.put(0, new Room("Eingangshalle", false));
        gui = new ServerGUI();
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
                UserHandler c = new UserHandler(socket, input, output, i, "User" + i);

                users.add(c);
                Thread t = new Thread(c);
                t.start();
            }
        } catch (IOException e) {
            stop();
        }

    }

    // Getter
    public static List<UserHandler> getUsers() {
        List<UserHandler> loggedUsers = new ArrayList<>();
        for (UserHandler u : users) {
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

    public static void changeRoom(int destination_id, UserHandler user) {
        rooms.get(user.roomID).removeUser(user);
        user.roomID = destination_id;
        rooms.get(user.roomID).addUser(user);
        updategui();
    }

	public static Room getRoom(int roomID) {
		return rooms.get(roomID);
	}

    public static Map<Integer, Room> getRooms() {
        return rooms;
    }

    public static void addroom(Room new_room) {
        rooms.put(new_room.getId(), new_room);
        updategui();
    }

    public static void deleteRoom(int room_id) {
        if (rooms.get(room_id).isEditable()) {
            for (UserHandler c : rooms.get(room_id).getUsers()) {
                changeRoom(0, c);
            }
            rooms.remove(room_id);
            updategui();
        }
    }

    public static void editRoom(int id, String name) {
        if (rooms.get(id).isEditable()) {
            rooms.get(id).setName(name);
            updategui();
            LOGGER.log(Level.INFO, "Raum editiert");
        } else {
            LOGGER.log(Level.WARNING, "Raum nicht editierbar");
        }
    }
}
