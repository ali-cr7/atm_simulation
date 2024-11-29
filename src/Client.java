import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 3000)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            System.out.println(reader.readLine()); // Welcome message

            while (true) {
                System.out.println("Enter command: ");
                String command = scanner.nextLine();
                writer.println(command);

                String response = reader.readLine();
                System.out.println("Server: " + response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
