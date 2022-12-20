package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {
    int port = 8008;

    int length = 6;
    Socket socket;

    public WebServer(){
        
        try {
            ServerSocket serversocket = new ServerSocket(port, length);
            System.out.println("Starting web server at port: "+port);
            System.out.println("http://localhost:"+port+" is ready");

            while (true) {
                socket = serversocket.accept();
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String request = "";
                String clientRequest = "";

                while ((clientRequest = reader.readLine()) != null) {
                    if (request.equals("")) {
                        request = clientRequest;
                    }
                    if (clientRequest.equals("")) {
                        break;
                    }
                }
                if (request != null && !request.equals("")) {
                    new HttpUtils(request,socket).start();
                }
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println(e);
        }
        finally{
            System.out.println("Server has been shutdown");
        }

    }

}
