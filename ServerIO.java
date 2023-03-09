package OneToOneMessageApp;
import java.io.*;
import java.net.*;
public class ServerIO {

    ServerSocket serverSocket;
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    public ServerIO(){
        try{
            //creating socket
            serverSocket = new ServerSocket(9999);
            System.out.println("I am ready to accept request....");
            System.out.println("Waiting......");
            //ready to accept
            socket = serverSocket.accept();

            //creating the input and output pipes
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

            startReading();
            startWriting();
        }
        catch (Exception e){
            e.printStackTrace();
        }



    }
    private void startReading(){

        //creating thread which accept the message
        Runnable reader = () -> {

            try{
                while (!socket.isClosed()){
                    String message = br.readLine();

                    if(message.equals("exit")){
                        System.out.println("Client connection is terminated.");
                        socket.close();
                        break;
                    }
                    System.out.println("Client - "+message);

                }
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }


        };

        Thread readerThread = new Thread(reader);
        readerThread.start();
    }

    private void startWriting(){

        //creating thread which use to read the message from console
        Runnable writer = () -> {
            try{
                while(!socket.isClosed()){
                    BufferedReader bufferedReaderInput = new BufferedReader(new InputStreamReader(System.in));
                    String message = bufferedReaderInput.readLine();
                    out.println(message);
                    out.flush();

                    if(message.equals("exit")){
                        socket.close();
                        break;
                    }

                }
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }

        };

        Thread writerThread = new Thread(writer);
        writerThread.start();
    }
    public static void main(String[] args) {

        //calling the constructor
        new ServerIO();

    }
}
