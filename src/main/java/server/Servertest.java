package server;

import java.io.IOException;

/**
 * @author blechner
 */
public class Servertest {

    public static void main(String[] args) {
        Server server = new Server(4999);
        server.run();

        ServerGUI test = new ServerGUI();
        String[] rooms = new String[3];

        for (int i = 0; i < 3; i++) {
            rooms[i] = "Raum " + i;
        }

        test.setRooms(rooms);

        String[] user = new String[3];

        for (int i = 0; i < 3; i++) {
            user[i] = "Nutzer " + i;
        }

        test.setUsers(user);
        test.setInformationRoom(3, "Testraum", 45, true);
        
        System.out.println("Press enter to exit ...");
        try {
            System.in.read();
        } catch (IOException e) {
        }
        System.out.println("Shutting down server. Please wait ...");
        server.stop();
    }
}
