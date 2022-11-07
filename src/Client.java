import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client extends JFrame {
	//declarations
    
	// IO streams
    private DataOutputStream toServer;
    private DataInputStream fromServer;
    
    //visual contents
    private JTextArea jta = new JTextArea(); //Text area to display contents
    private JTextField jtf = new JTextField(); //accepts radius input

    private JButton run = new JButton("Run");
    private JButton exit = new JButton("Exit"); //GO and exit buttons

    


    private Socket socket;

    public Client() {
        // Panel p to hold the label and text field
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());
        p.add(new JLabel("Enter radius"), BorderLayout.WEST);
        p.add(jtf, BorderLayout.CENTER);
        jtf.setHorizontalAlignment(JTextField.RIGHT);

        setLayout(new BorderLayout());
        add(p, BorderLayout.NORTH);
        add(new JScrollPane(jta), BorderLayout.CENTER);
        add(exit, BorderLayout.SOUTH);
        p.add(run, BorderLayout.EAST);

        run.addActionListener(new Listener()); // Register listener

        setTitle("Client");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true); // To ensure proper workflow, this is required to be included as "true"

        try {
            socket = new Socket("localhost", 8000); // Create a socket connection for the server

            fromServer = new DataInputStream(socket.getInputStream()); // Create an input stream to receive data from the server

            toServer = new DataOutputStream(socket.getOutputStream()); // Create an output stream to send data to the server
        }
        catch (IOException ex) {
            jta.append(ex.toString() + '\n');
        }


        exit.addActionListener(e -> {
            try {
                socket.close();
                jta.append("----End Session----");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private class Listener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // Get the radius from the text field
                double radius = Double.parseDouble(jtf.getText().trim());

                // Send the radius to the server
                toServer.writeDouble(radius);
                toServer.flush();

                // Get area from the server
                double area = fromServer.readDouble();

                // Display to the text area
                jta.append("Radius is " + radius + "\n");
                jta.append("Area received from the server is "
                        + area + '\n');
            }
            catch (IOException ex) {
                System.err.println(ex);
            }
        }
    }
}