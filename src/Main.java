import java.awt.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {
    	
    	//Prints text to terminal console to show session start and update
    	System.out.println("----Session Started----"+ '\n');
        System.out.println("Distributed Systems: Assignment 1- Multithreaded Client/Server Application");

        Thread thread = new Thread(() -> new Server());

        thread.start();


        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ClientHandler frame = new ClientHandler();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
}