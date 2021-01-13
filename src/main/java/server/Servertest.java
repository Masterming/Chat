package server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.*;

/**
 * @author blechner
 */
public class Servertest {

    private final static Logger LOGGER = Logger.getLogger(Servertest.class.getName());

    public static void main(String[] args) {
        // Custom logging
        try {
            ExtendedLogger.disableConsole();
            ExtendedLogger.enableHtml();
            ExtendedLogger.setDebugLevel(Level.INFO); // SEVERE or INFO
        } catch (IOException ex) {
            LOGGER.warning(ex.getLocalizedMessage());
        }

        Server server = new Server(4999);
        server.run();

        ServerGUI test = new ServerGUI();
        ArrayList<Room> rooms = new ArrayList<Room>();

        

        test.setRooms(Server.getRooms());
        test.setUsers(Server.getClients());
        //test.setInformationRoom();

        System.out.println("Press enter to exit ...");
        try {
            System.in.read();
        } catch (IOException e) {
        }
        System.out.println("Shutting down server. Please wait ...");
        server.stop();
    }
}
