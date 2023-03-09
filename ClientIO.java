package OneToOneMessageApp;
import java.io.*;
import java.net.*;
public class ClientIO {

    Socket socket;
    BufferedReader br;
    PrintWriter out;

    public ClientIO(){
        try {

            //creating the socket
            socket = new Socket("127.0.0.1", 9999);
            System.out.println("Connection done with server....");

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
                        System.out.println("Server connection is terminated.");
                        socket.close();
                        break;
                    }
                    System.out.println("Server - "+message);

                }
            }
            catch (Exception e){
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
                    }

                }
            }
            catch (Exception e){
                System.out.println(e.getMessage());;
            }

        };

        Thread writerThread = new Thread(writer);
        writerThread.start();
    }

    public static void main(String[] args) {
        //calling the constructor
        new ClientIO();
    }
}
