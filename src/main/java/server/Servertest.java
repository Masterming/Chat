package server;

import java.io.IOException;

/**
 * @author blechner
 */
public class Servertest {

    public static void main(String[] args) {
        Server server = new Server(4999);
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
