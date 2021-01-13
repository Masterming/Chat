package client;

/**
 * @author blechner
 */
public class Clienttest {

    public static void main(String[] args) {
        Client c = new Client("127.0.0.1", 4999);
        c.run();

        ClientGUI test2 = new ClientGUI();

        // neue Nachricht
        test2.addMessage("test123");
        for (int i = 0; i < 30; i++) {
            test2.addMessage("" + i + "\n");
        }

        test2.setServerInformation(4999, "testIP");
        test2.setUserInformation("Tom");

        String[] rooms = new String[3];

        for (int i = 0; i < 3; i++) {
            rooms[i] = "Raum " + i;
        }
        test2.setRooms(rooms);

        String[] user = new String[3];

        for (int i = 0; i < 3; i++) {
            user[i] = "Nutzer " + i;
        }
        test2.setUsers(user);
    }
}
