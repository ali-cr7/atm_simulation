import java.io.*;
import java.net.Socket;
import java.util.Base64;

public class ServerHandler implements Runnable {
    private final Socket clientSocket;

    public ServerHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (
                InputStream input = clientSocket.getInputStream();
                OutputStream output = clientSocket.getOutputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                PrintWriter writer = new PrintWriter(output, true)
        ) {
            writer.println("Welcome to the ATM server!");

            String command;
            while ((command = reader.readLine()) != null) {
                String[] parts = command.split(" ");
                String action = parts[0];

                switch (action) {
                    case "LOGIN":
                        handleLogin(parts, writer);
                        break;
                    case "DEPOSIT":
                        handleDeposit(parts, writer);
                        break;
                    case "WITHDRAW":
                        handleWithdraw(parts, writer);
                        break;
                    case "BALANCE":
                        handleBalance(parts, writer);
                        break;
                    default:
                        writer.println("Invalid command!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleLogin(String[] parts, PrintWriter writer) {
        String username = parts[1];
        String password = parts[2];
        boolean success = DatabaseUtils.authenticateUser(username, password);

        if (success) {
            writer.println("Login successful!");
        } else {
            writer.println("Invalid credentials!");
        }
    }

    private void handleDeposit(String[] parts, PrintWriter writer) {
        String username = parts[1];
        double amount = Double.parseDouble(parts[2]);
        DatabaseUtils.updateBalance(username, amount);
        writer.println("Deposit successful!");
    }

    private void handleWithdraw(String[] parts, PrintWriter writer) {
        String username = parts[1];
        double amount = Double.parseDouble(parts[2]);

        if (DatabaseUtils.withdrawBalance(username, amount)) {
            writer.println("Withdrawal successful!");
        } else {
            writer.println("Insufficient funds!");
        }
    }

    private void handleBalance(String[] parts, PrintWriter writer) {
        String username = parts[1];
        double balance = DatabaseUtils.getBalance(username);
        writer.println("Your balance is: " + balance);
    }
}
