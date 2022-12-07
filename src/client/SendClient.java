package client;
import javax.swing.*;

import listeners.ChooseFileHandler;
import listeners.ClientSendFileHanlder;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
public class SendClient {
    JFrame frame;
	File[] file = new File[1];

	public SendClient() {
        frame = new JFrame();
        frame.setTitle("Fast Transfer - v.0.1");
        frame.setResizable(false);
        
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
        frame.setLocationRelativeTo(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 414, 239);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("FastTransfer");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNewLabel.setBounds(138, 0, 129, 25);
		panel.add(lblNewLabel);
		
		JButton btnNewButton = new JButton("Send");
		btnNewButton.setBackground(new Color(0, 128, 64));
		btnNewButton.setBounds(315, 205, 89, 23);
		panel.add(btnNewButton);
		
		JLabel chooseFileLbl = new JLabel("Choose a file");
		JPanel chooseFilePanel = new JPanel();
		chooseFilePanel.setBackground(new Color(48, 48, 48));
		chooseFilePanel.setBounds(10, 27, 394, 167);
		
		chooseFileLbl.setForeground(new Color(255, 255, 255));
		chooseFileLbl.setFont(new Font("Tahoma", Font.BOLD, 15));
		chooseFileLbl.setHorizontalAlignment(SwingConstants.CENTER);
		chooseFileLbl.setBounds(10, 71, 374, 14);
		chooseFilePanel.add(chooseFileLbl);
		chooseFilePanel.addMouseListener(new ChooseFileHandler(chooseFileLbl,file));
		btnNewButton.addActionListener(new ClientSendFileHanlder(chooseFileLbl,file));

		panel.add(chooseFilePanel);
		chooseFilePanel.setLayout(null);
		
        frame.setVisible(true);
    }
	
	public File[] getFile() {
		return file;
	}

	public void setFile(File[] file) {
		this.file = file;
	}
}
