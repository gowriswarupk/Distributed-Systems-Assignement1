import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.*;

public class ClientHandler extends JFrame {

	//import required modules 
	
    private static final long serialVersionUID = 1L;
    private JTextField textField = new JTextField();
    private JButton newbutton;

   
    
    public ClientHandler() {
    	
    	//creating JFrame panel for Login Window
        JFrame loginframe = new JFrame();
        loginframe.setPreferredSize(new Dimension(500, 300)); //setting hard-coded dimensions
        loginframe.setTitle("Login");
        JLabel label = new JLabel("Enter the Student ID: ");
        
        JButton login = new JButton("Login");

        JPanel loginpanel = new JPanel(new GridLayout(4, 35));
        loginpanel.add(label, BorderLayout.WEST);;
        loginpanel.add(textField);
        loginframe.add(loginpanel);
        loginframe.add(login, BorderLayout.SOUTH);
        loginframe.setVisible(true);
        loginframe.pack();

        login.addActionListener(new ActionListener() { // Once login button is clicked, ActionListener kicks in

            public void actionPerformed(ActionEvent e) {
            	
                String studentID = textField.getText(); //collecting entered studentID from text field
                try {
                	
                    Connection conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/Assign1","root", ""); //When LAMPP services are running, this establishes connection between DB and servers. 
                    																										//UserName "root" and nil password for configuration purposes outlines in Assignment structure

                    PreparedStatement q = (PreparedStatement) conn.prepareStatement("Select * from students where STUD_ID=?"); //SQL Query q is prepared for carrying out DB querying for checking studentID validity.

                    
                    q.setString(1, studentID);
                    ResultSet rs = q.executeQuery();
                    
                    if (rs.next()) { 
                    	
                        String studentfname = rs.getString("FNAME");//collecting first name from returned rs to display personalized messages
                        studentfname = studentfname.substring(1, studentfname.length() - 1); //Since name is returned with [] surrounded, this removes that character(s). 

                        // Login Success Message
                        JOptionPane.showMessageDialog(newbutton, "Welcome, " + studentfname + "! You have successfully logged in.");
                    	
                        // Assignment requirements: Updating Total Requests TOT_REQsaved
                        q.executeUpdate("UPDATE students SET TOT_REQ=TOT_REQ+1 WHERE STUD_ID='" + studentID + "'");
                       
                        //Starting client server
                        new Client();
                
                    } else {
                        
                    	//If STUD_ID doesn't have the user id saved, Login Fail message displayed.
                        JOptionPane.showMessageDialog(newbutton, "Sorry, there are no user credentials saved with the ID '" + studentID + "'");
                    }
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }
            }
        });
    }
}