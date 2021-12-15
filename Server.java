import java.io.*;
import java.net.*;

public class Server {
    ServerSocket server;
    Socket socket;

    // To read data
    BufferedReader br;

    // To write data
    PrintWriter out;

    // Constructor
    public Server() {
        try {
            server = new ServerSocket(7777);
            System.out.println("Server is ready to connect...");
            System.out.println("Waiting...");

            // To accept the request to establish the connection
            socket = server.accept();

            // To get data from socket
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // To print data to socket
            out = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();

        } catch (Exception e) {
            e.printStackTrace();
        }
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
                        System.out.println("Client left the chat");
                        socket.close();
                        break;
                    }

                    // To print result of the chat
                    System.out.println("Client : " + msg);

                }
            } catch (Exception e) {
               System.out.println("connection closed");
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

                    if(content.equals("Exit"))
                    {
                        socket.close();
                        break;
                    }

                }
            } catch (Exception e) {
                System.out.println("Connection closed");
            }

        };

        new Thread(r2).start();

    }

    public static void main(String[] args) {

        System.out.println("Server is going to start...");

        new Server();
    }
}