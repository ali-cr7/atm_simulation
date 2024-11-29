import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(3000)) {
            System.out.println("Server started on port 3000...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected...");
                new Thread(new ServerHandler(clientSocket)).start();
            }
        } catch (Exception e) { 
            e.printStackTrace();
        }
    }
}