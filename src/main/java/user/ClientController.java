package user;

import java.io.*;
import java.net.*;
import java.util.logging.Level;

import parser.Message;
import parser.Type;

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
        System.out.println(logged);
        if (logged) {
            gui = new ClientView();
            Thread t = new Thread(handler);
            t.start();
            
        }
        return logged;

    }

    public static String getClientname(){
        return handler.getName();
    }
	public static void send(String message) {
        Message msg = new Message(Type.MESSAGE, message);
        handler.write(msg);
	}

	public static void displayMessage(String m) {
        gui.addMessage(m);
	}

}
