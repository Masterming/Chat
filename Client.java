import java.io.*;
import java.net.*;

public class Client {
    public void run() {
        try {
            Socket s = new Socket("localhost", 4999);

            PrintWriter pr = new PrintWriter(s.getOutputStream());
            pr.println("is it working?");
            pr.flush();

            InputStreamReader in = new InputStreamReader(s.getInputStream());
            BufferedReader bf = new BufferedReader(in);

            String str = bf.readLine();
            System.out.println("server: " + str);

            s.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
