import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer
{
    public static final int CLIENT_PORT = 9000;
    public static final int DEVICE_PORT = 9001;

    private static List<String> users = new ArrayList<String>();
    private static List<PrintWriter> clients = new ArrayList<PrintWriter>();

    private static List<String> devices = new ArrayList<String>();

    public static void main(String[] args) {
        while(true) {
            try(ServerSocket server = new ServerSocket(CLIENT_PORT)) {
                Socket client = server.accept();
                InetAddress IpAddress = client.getInetAddress();
                System.out.println("Client connected from " + IpAddress);
                new Thread(new ClientThread(client)).start();

                /**System.out.println("The server is running...");

                Socket client = server.accept();

                InetAddress IpAddress = client.getInetAddress();
                System.out.println("Client connected from " + IpAddress);

                Runnable thread = new ClientThread(client);

                new Thread(thread).start();
                //thread.start();*/
            } catch (IOException ex) {
                System.out.println("Error reader/writing from/to file: " + ex.getMessage());
            }
            try(ServerSocket server = new ServerSocket(DEVICE_PORT)) {

            } catch (IOException ex) {
                System.out.println("Error reader/writing from/to file: " + ex.getMessage());
            }
        }
    }


    // ******   Registering users    ******** //

    private static boolean isUser(String name) {
        synchronized(users) {
            if(name == null || name.equals("")) {
                throw new IllegalStateException("User name cannot be empty");
            }
            return users.contains(name);
        }
    }

    public static boolean addUser(String name) {
        synchronized (users) {
            if(isUser(name)) {
                return false;
            }
            users.add(name);
            return true;
        }
    }

    public static void removeUser(String name){
        synchronized (users){
            users.remove(name);
            System.out.println(name + " has disconnected");
            broadcastMessage(name, "has disconnected.");
        }
    }

    // ******* Register Client Output Streams ********* //

    public static void registerClient(PrintWriter client) {
        synchronized (clients) {
            clients.add(client);
        }
    }

    public static void broadcastMessage(String name, String message) {
        synchronized (clients) {
            for (PrintWriter client : clients) {
                client.println(name + ": " + message);
            }
        }
    }
}
