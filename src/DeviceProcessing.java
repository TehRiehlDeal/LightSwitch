import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.UnknownHostException;




public class DeviceProcessing {
    private static BufferedReader reader;
    private static PrintWriter writer;

    public DeviceProcessing(){
        createServerConnection();
    }

    private void createServerConnection() {

        try {
            Socket socket = new Socket("127.0.0.1", 9001);

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch(UnknownHostException ex) {
            System.out.println("Unknown host: " + ex.getLocalizedMessage());
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
                    System.out.println("Flipping Switch!");
                } else {
                    System.out.println(serverMsg);
                }
            }
            catch (IOException e) {
                System.out.println("Error receiving server message: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        DeviceProcessing con = new DeviceProcessing();
        con.processingLoop();
    }
}
