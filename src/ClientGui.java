import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.UnknownHostException;

import javax.swing.*;

@SuppressWarnings("serial")
public class ClientGui extends JFrame {

    private JTextField typeMsg;
    private JTextArea msgList;
    private JScrollPane scrollPane;
    private String name;

    private BufferedReader reader;
    private PrintWriter writer;

    public ClientGui() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setTitle("My Chatroom Client");

        buildGui();
        createServerConnection();
        registerEventHandlers();
    }

    private void buildGui() {
        this.setLayout(new BorderLayout());

        typeMsg = new JTextField(50); //80 is the #of char
        this.getContentPane().add(typeMsg, BorderLayout.NORTH);

        msgList = new JTextArea(20,50); //20 char 80 columns
        scrollPane = new JScrollPane(msgList);
        this.getContentPane().add(scrollPane, BorderLayout.CENTER);
        msgList.setEditable(false); //dont allow user to edit text
        this.pack();
    }

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

    private void registerEventHandlers() {
        typeMsg.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                writer.println(typeMsg.getText());

                typeMsg.setText("");
            }
        });
    }

    public void processingLoop() {
        //listen to server
        while (true) {
            try {
                String serverMsg = reader.readLine();

                if (serverMsg.equals("Please enter a name")) {
                    name = JOptionPane.showInputDialog("Enter a name");
                    writer.println(name);
                } else {
                    msgList.append(serverMsg + "\n");
                }
            }
            catch (IOException e) {
                System.out.println("Error receiving server message: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        ClientGui gui = new ClientGui();
        gui.processingLoop();
    }
}
