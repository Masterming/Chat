package server;

import java.io.IOException;
import java.util.logging.*;

/**
 * @author blechner
 */
public class ServerTest {

    private final static Logger LOGGER = Logger.getLogger(ServerTest.class.getName());

    public static void main(String[] args) {
        // Custom logging
        try {
            ExtendedLogger.disableConsole();
            ExtendedLogger.enableHtml();
            ExtendedLogger.setDebugLevel(Level.INFO); // SEVERE or INFO
        } catch (IOException ex) {
            LOGGER.warning(ex.getLocalizedMessage());
        }

        ServerController server = new ServerController(4999);
        server.run();

        System.out.println("Press enter to exit ...");
        try {
            System.in.read();
        } catch (IOException e) {
        }
        System.out.println("Shutting down server. Please wait ...");
        server.stop();
    }
}
