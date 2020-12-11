package client;

/**
 * @author blechner
 */
public class Clienttest {

    public static void main(String[] args) {
        Client c = new Client("127.0.0.1", 4999);
        c.run();
    }
}
