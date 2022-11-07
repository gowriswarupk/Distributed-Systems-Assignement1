import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;



 //Server class
 //Starts the server and GUI.

public class Server extends JFrame {
    // Text area for displaying contents
    JTextArea jta;

    public Server() {
        // Place text area on the frame

        jta = new JTextArea();

        setLayout(new BorderLayout());
        add(new JScrollPane(jta), BorderLayout.CENTER);
        setTitle("Server");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true); // It is necessary to show the frame here!


        try {
            // Create a server socket
            ServerSocket serverSocket = new ServerSocket(8000);
            jta.append("----Session Started----"+ '\n');
            jta.append("Server started at " + new Date() + '\n');
            
            
            // Find the client's host name, and IP address
            InetAddress inetAddress = InetAddress.getLocalHost();
            jta.append("Host name is " + inetAddress.getHostName() + '\n');
            jta.append("IP Address is " + inetAddress.getHostAddress() + '\n');

            Socket s1 = null;
            while (true) {
                s1 = serverSocket.accept();
                ThreadClass tc = new ThreadClass(s1);
                tc.start();

            }
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

     // Runs a thread
    private class ThreadClass extends Thread {
        //The socket the client is connected through
        Socket socket;

        //The input and output streams to the client
        private DataInputStream inputFromClient;
        private DataOutputStream outputToClient;

        public ThreadClass(Socket socket) throws IOException {

            // Declare & Initialize i/o streams
            this.socket = socket;

            // Create an input stream to receive data from the server
            inputFromClient = new DataInputStream(socket.getInputStream());

            // Create an output stream to send data to the server
            outputToClient = new DataOutputStream(socket.getOutputStream());
        }


        //The method that runs when the thread starts
        public void run(){
            try{
                while (true) {
                    InetAddress inetAddress = InetAddress.getLocalHost();
                	System.out.println("----Request Generated----"+ '\n');
                    System.out.println("current logged in user : " + inetAddress.getHostName() + "@" + inetAddress.getHostAddress()); //displays console output for Host IP address and UserName to keep logs.

                    double radius = inputFromClient.readDouble();
                    double area = radius * radius * Math.PI;


                    // Send area back to the client
                    outputToClient.writeDouble(area);
                    outputToClient.flush();


                    jta.append("Radius received from client: " + radius + '\n');
                    jta.append("Area found: " + area + '\n');
                }

            } catch (Exception e) {
                System.err.println(e + " on " + socket);
            }
        }
    }
}





