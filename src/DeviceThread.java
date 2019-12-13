import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class DeviceThread implements Runnable {
    private Socket deviceSocket;

    public DeviceThread(Socket deviceSocketSocket) {
        this.deviceSocket = deviceSocket;
    }

    public void run(){
        try {
            BufferedReader reader= new BufferedReader(new InputStreamReader(deviceSocket.getInputStream()));

            PrintWriter writer = new PrintWriter(deviceSocket.getOutputStream(),true);

            ChatServer.registerClient(writer);

            String name;
            do {
                writer.println("Please enter a name");
                name = reader.readLine();
            } while(!ChatServer.addUser(name));

            writer.println(name + " has been registered successfully.");
            writer.println("Type a message (type \"exit\" to exit)");

            boolean done = false;

            while(!done) {
                String message = reader.readLine();
                //debug
                System.out.println("client message: " + message);

                if (message.equals("exit")) {
                    done = true;
                    ChatServer.removeUser(name);
                } else {
                    ChatServer.broadcastMessage(name, message);
                }
            }
        }
        catch(IOException ex) {
            System.out.println("Error reading/writing from/to file: " + ex.getMessage());
        }
    }
}
