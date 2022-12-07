package listeners;
import java.awt.event.*;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.Socket;
import javax.swing.*;
public class ClientSendFileHanlder implements ActionListener{
    File[] toSend;
    JLabel label;
    public ClientSendFileHanlder(JLabel chooseLbl ,File[] fileToSend) {
        setToSend(fileToSend);    
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        System.out.println(toSend[0].getName());
        if (toSend[0].equals(null)) {
            label.setText("Choose file first");
        }
        else{
            try {
                FileInputStream input = new FileInputStream(toSend[0].getAbsolutePath());
                Socket socket = new Socket("localhost",1234);

                DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                String fileName = toSend[0].getName();
                byte[] nameFileByte = fileName.getBytes();
                byte[] contentBytes = new byte[(int) toSend[0].length()];
                input.read(contentBytes);

                output.writeInt(nameFileByte.length);
                output.write(nameFileByte);

                output.writeInt(contentBytes.length);
                output.write(contentBytes);

            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        }
    }

    public void setToSend(File[] toSend) {
        this.toSend = toSend;

    }
    
}
