package user;

import java.io.*;
import java.net.*;

/**
 * @author blechner
 */
public class ClientController {

    private static ClientView gui;
    private static LoginView login_gui;
    private static boolean logged;
    private static boolean running;
    private static Handler handler;

    public ClientController(String ip, int port) {

        handler = new Handler(ip, port);
        login_gui = new LoginView();
            
    }

    public static boolean login(String name, String pw) {
        logged = handler.login(name, pw);
        if (logged) {
            gui = new ClientView();
        }
        return logged;

    }

}
