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
    private static ServerView view;

    private static SQLSocket sql;

    public static SQLSocket getSql() {
        return sql;
    }

    public ServerController(int port) {
        this.port = port;

        // Make an ArrayList to hold all user objects
        users = Collections.synchronizedList(new ArrayList<>(128));
        rooms = Collections.synchronizedMap(new HashMap<Integer, Room>(10));
        rooms.put(0, new Room("Lobby", false, true));
        view = new ServerView();
        updateView();

        // gui.setInformationRoom();

        running = true;
        i = 0;

        sql = new SQLSocket();
    }

    public void run() {
        try {
            server = new ServerSocket(port, 100);
            displayMessage(Level.INFO, "Server started. Waiting for users...");
            new Thread(() -> AcceptUsersAsync()).start();
        } catch (IOException e) {
            displayMessage(Level.SEVERE, "Failed to start the sever: " + e.getMessage());
        }
    }

    private void AcceptUsersAsync() {
        try {
            Socket socket;
            BufferedReader input;
            PrintWriter output;

            while (running) {
                if (i == 128) {
                    displayMessage(Level.WARNING, "Maximum number of users reached.");
                    break;
                }
                i++;

                // Connect with user
                socket = server.accept();
                // displayMessage(Level.INFO, "Connected to " +
                // socket.getInetAddress().getHostName() +
                // "(i=" + i + ")");

                // Setup streams with user
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
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
            if (u.getLogged()) {
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
                displayMessage(Level.INFO, "Server closed");
                System.exit(0);
            } catch (Exception e) {
                System.exit(1);
            }

        }
    }

    public static void updateView() {

        view.setRooms(rooms);
        view.setUsers(getUsers());

        ArrayList<String> tmpList_r = new ArrayList<>();
        for (Room r : rooms.values()) {
            if (r.isVisible()) {
                tmpList_r.add(r.getName());
            }
        }

        for (ClientHandler c : getUsers()) {
            ArrayList<String> tmpList_u = new ArrayList<>();
            for (ClientHandler d : getRoom(c.roomID).getUsers()) {
                tmpList_u.add(d.getName());

            }
            c.updateR(tmpList_r);
            c.updateU(tmpList_u);
        }

    }

    public static void changeRoom(String r_name, ClientHandler user) {
        rooms.get(user.roomID).removeUser(user);

        for (Room r : rooms.values()) {
            if (r_name.equals(r.getName())) {
                // System.out.println(user.roomID);
                user.roomID = r.getId();
                // System.out.println(user.roomID);
            }
        }

        rooms.get(user.roomID).addUser(user);
        updateView();
    }

    public static void changeRoom(Room r, ClientHandler user) {
        rooms.get(user.roomID).removeUser(user);
        user.roomID = r.getId();
        rooms.get(user.roomID).addUser(user);
        updateView();
    }

    public static Room getRoom(int roomID) {
        return rooms.get(roomID);
    }

    public static Map<Integer, Room> getRooms() {
        return rooms;
    }

    public static void addroom(Room new_room) {
        if (!rooms.containsValue(new_room)) {
            rooms.put(new_room.getId(), new_room);
            displayMessage(Level.INFO, "[System]: Raum \"" + new_room.getName() + "\" erstellt");
            updateView();
        }
    }

    public static void deleteRoom(int room_id) {
        if (rooms.get(room_id).isEditable()) {
            for (ClientHandler c : rooms.get(room_id).getUsers()) {
                changeRoom("Lobby", c);
            }
            rooms.remove(room_id);
            displayMessage(Level.INFO, "[System]: Raum" + room_id + " entfernt");
            updateView();
        }
    }

    public static void editRoom(int id, String name) {
        if (getRoom(id).isEditable()) {
            getRoom(id).setName(name);
            updateView();
            displayMessage(Level.INFO, "[System]: Raum " + id + " umbenannt zu: " + name);
        } else {
            displayMessage(Level.WARNING, "[System]: Raum nicht editierbar");
        }
    }

    public static void displayMessage(Level lvl, String msg) {
        LOGGER.log(lvl, msg);
        view.addMessage(msg);
    }

    public static void warnUser(String msg, ClientHandler c) {
        displayMessage(Level.WARNING, "[System]: Benutzerverwarnung an " + c.getName() + ": \"" + msg + "\"");
        c.write(new Message(MsgCode.WARNING, msg));
    }

    public static void kickUser(ClientHandler c) {
        displayMessage(Level.WARNING, "[System]: Benutzerkick für: " + c.getName());

        leaveRoom(c);
        c.write(new Message(MsgCode.KICK, ""));
        updateView();
    }

    public static void banUser(ClientHandler c) {
        displayMessage(Level.WARNING, "[System]: Benutzerbann für: " + c.getName());
        leaveRoom(c);
        sql.ban(c.getName());
        c.write(new Message(MsgCode.BAN, ""));
        updateView();
    }

    public static void leaveRoom(ClientHandler c) {
        rooms.get(c.roomID).removeUser(c);
    }

    public static void createPrivate(ClientHandler c, String d) {
        Room room_tmp = new Room("Whisper", true, false);
        rooms.put(room_tmp.getId(), room_tmp);
        for (ClientHandler tmp : getUsers()) {
            if (tmp.getName().equals(d)) {
                changeRoom(room_tmp, c);
                c.write(new Message(MsgCode.MESSAGE, "[System]: private connection to \""+tmp.getName()+"\""));
                changeRoom(room_tmp, tmp);
                tmp.write(new Message(MsgCode.MESSAGE, "[System]: private connection to \""+c.getName()+"\""));
                displayMessage(Level.INFO, "[System]: privater raum erstellt");
                break;
            }
        }
        
    }
}
