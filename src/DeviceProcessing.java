import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.UnknownHostException;




public class DeviceProcessing {
    private BufferedReader reader;
    private PrintWriter writer;

    private void createServerConnection() {
        String serverIP = JOptionPane.showInputDialog("Enter a remote IP address");

        try {
            Socket socket = new Socket(serverIP, ChatServer.CLIENT_PORT);

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch(UnknownHostException ex) {
            System.out.println("Unknonw host: " + ex.getLocalizedMessage());
        } catch (IOException ex) {
            System.out.println("Connection error: " + ex.getMessage());
        }
    }

    public void processingLoop() {
        //listen to server
        while (true) {
            try {
                String serverMsg = reader.readLine();

                if (serverMsg.equals("Please enter a name")) {
                    writer.println("Device1");
                } else if(serverMsg.contains("Device1 on")) {
                    //Flip the relay
                }
            }
            catch (IOException e) {
                System.out.println("Error receiving server message: " + e.getMessage());
            }
        }
    }
}
