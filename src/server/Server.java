package server;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.*;

public class Server {
    ArrayList<ReceiveFile> listfiles = new ArrayList<>();
    
    public Server() {
        ReceiveClient receiveClient = new ReceiveClient(listfiles);
        ServerSocket serverSocket = new ServerSocket(1234);
        JFrame frame = receiveClient.getFrame();
        JPanel panel = (JPanel)frame.getComponent(0);
        while(true) {
            try {
            Socket socket = serverSocket.accept();
            DataInputStream input = new DataInputStream(socket.getInputStream());
            int filenameLength = input.readInt();
            if(filenameLength > 0) {
                byte[] filenameBytes = new byte[filenameLength];
                input.readFully(filenameBytes,0,filenameBytes.length);
                String fileName = new String(filenameBytes);

                int fileContentLength = input.readInt();

                if (fileContentLength > 0) {
                    byte[] fileContentBytes = new byte[fileContentLength];
                    input.readFully(fileContentBytes,0,fileContentLength);

                    if (getFileExtension(fileName).equalsIgnoreCase("txt")) {
                        
                    }

                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}

    private String getFileExtension(String fileName) {
        int i = fileName.lastIndexOf('.');
        if (i>0) {
            return fileName.substring(i+1);
        }
        else {
            return "Not found extension";
        }
    }

}
