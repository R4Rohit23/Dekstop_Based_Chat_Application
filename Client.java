import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.BorderLayout;
import java.awt.Font;
import java.io.*;

public class Client extends JFrame {

    Socket socket;
    BufferedReader br;
    PrintWriter out;

    // Declare components using swing

    // JLabel is use to display the one line text or image or both
    private JLabel heading = new JLabel("Client Area");

    // JTextArea is use to display specified text or simple chat b/w client and
    // server
    private JTextArea messageArea = new JTextArea();

    // JTextField is where text will be shown means background
    private JTextField message = new JTextField();

    // For font
    private Font font = new Font("Roman", Font.BOLD, 20);

    public Client() {
        try {
 
            System.out.println("Sending request to server");
            socket = new Socket("127.0.0.1", 7777);
            System.out.println("Connection done");

            // To get data from socket
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // To print data to socket
            out = new PrintWriter(socket.getOutputStream());

            createGUI();
            handleEvent();

            startReading();
            // startWriting();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleEvent() {

        message.addKeyListener(new KeyListener()
        {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
               
                // if user press enter 
                if(e.getKeyCode() == 10)
                {
                    String contentToSend = message.getText();
                    messageArea.append("Me : " + contentToSend + "\n");
                    out.println(contentToSend);
                    out.flush();
                    message.setText("");
                    message.requestFocus();
                }
                
            }

        });
        
    }

    // Function for GUI
    private void createGUI() {
        // To set the title
        this.setTitle("Chat Application");

        // to set the size in (width, height) format
        this.setSize(600, 600);

        // sets the location of the window. If null then it will be in center
        this.setLocationRelativeTo(null);

        // How to exit the window
        // EXIT_ON_CLOSE = program will be terminated when closed
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setVisible(true);

        // setting fonts
        heading.setFont(font);
        message.setFont(font);
        messageArea.setFont(font);

        // setting frame's layout
        this.setLayout(new BorderLayout());

        // Now we are adding our components to frame

        // heading will be on the top
        this.add(heading, BorderLayout.NORTH);

        // adding scroll bar
        JScrollPane jScrollPane = new JScrollPane(messageArea);

        this.add(jScrollPane, BorderLayout.CENTER);

        this.add(message, BorderLayout.SOUTH);

        // heading will be in center
        heading.setHorizontalAlignment(SwingConstants.CENTER);

        // Like it sets the margin around the heading
        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // message area should not be editable
        messageArea.setEditable(false);

        // to give the logo
        ImageIcon imageIcon = new ImageIcon(new ImageIcon("Chat Logo.png").getImage().getScaledInstance(60, 60, Image.SCALE_DEFAULT));

        heading.setIcon(imageIcon);

        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
    }

    public void startReading() {
        // thread - read karega
        Runnable r1 = () -> {

            System.out.println("Reader Started");

            try {

                while (!socket.isClosed()) {

                    String msg = br.readLine();

                    // To exit the chat
                    if (msg.equals("Exit")) {
                        System.out.println("Server Terminated");
                        JOptionPane.showMessageDialog(this, "Server Terminated");
                        message.setEnabled(false);
                        socket.close();
                        break;
                    }

                    // To print result of the chat
                    // System.out.println("Server : " + msg);
                    messageArea.append("Server : " + msg + "\n");

                }
            } catch (Exception e) {
                System.out.println("Connection is closed");
            }
        };

        new Thread(r1).start();

    }

    public void startWriting() {
        Runnable r2 = () -> {

            System.out.println("Writer started");

            try {

                while (!socket.isClosed()) {

                    // To get the byte code data from the buffer
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));

                    String content = br1.readLine();

                    out.println(content);

                    out.flush();

                    if (content.equals("Exit")) {
                        socket.close();
                        break;
                    }

                }
            } catch (Exception e) {
                System.out.println("Connection is closed");
            }

        };

        new Thread(r2).start();

    }

    public static void main(String[] args) {

        System.out.println("This is Client");
        new Client();
    }

}
