package user;

import java.util.ArrayList;

import parser.Message;
import parser.MsgCode;

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
        Message msg = new Message(MsgCode.MESSAGE, message);
        handler.write(msg);
	}

	public static void displayMessage(String m) {
        gui.addMessage(m);
	}
    public static void updateRooms(ArrayList<String> tmp){
        gui.setRooms(tmp);
    }
    public static void updateUsers(ArrayList<String> tmp){
        gui.setUsers(tmp);
    }

	public static void changeRoom(String room) {
        gui.addMessage("[System]: "+ room +" beigetreten");
        handler.write(new Message(MsgCode.CHANGE_ROOM, room));

    }
    public static void warnUser(String msg){
        gui.popUp(msg);
    }

	public static void logout() {
        handler.close();
	}
}
