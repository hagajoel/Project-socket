package listeners;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;

import com.formdev.flatlaf.ui.FlatFileChooserUI;

public class ChooseFileHandler implements MouseListener{
    File[] toSend;
    JLabel fileNameLbl;
    String yourPath = "";
    public ChooseFileHandler(JLabel fileNameLbl,File[] fileToSend) {
        setFileNameLbl(fileNameLbl);
        setToSend(fileToSend);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose a file");
        fileChooser.setMultiSelectionEnabled(true);
        // fileChooser.setCurrentDirectory(new File(yourPath));
        if (fileChooser.showOpenDialog(null)==fileChooser.APPROVE_OPTION) {
            toSend[0]= fileChooser.getSelectedFile();
            fileNameLbl.setText(toSend[0].getName());
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }
    public File[] getToSend() {
        return toSend;
    }

    public void setToSend(File[] toSend) {
        this.toSend = toSend;
    }

    public void setFileNameLbl(JLabel fileNameLbl) {
        this.fileNameLbl = fileNameLbl;
    }
}
