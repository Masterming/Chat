import java.io.*;
import java.net.*;

public class Server {
    public void run() {

        ServerSocket ss = new ServerSocket(4999);
        while (true) {
            try {
                
                Socket s = ss.accept();

                System.out.println("client connected");

                InputStreamReader in = new InputStreamReader(s.getInputStream());
                BufferedReader bf = new BufferedReader(in);

                String str = bf.readLine();
                System.out.println(("client: " + str));

                PrintWriter pr = new PrintWriter(s.getOutputStream());
                pr.println("yes");
                pr.flush();

            } catch (IOException e) {
                // TODO: handle exception
            }
            finally{

            }

        }
    }

    public static void main(String[] args) throws IOException {

    }
}
