package ui;

import javax.swing.*;

import client.SendClient;
import server.ReceiveClient;
import server.Server;

import java.awt.*;
import java.awt.event.*;

public class StartFrame {
    JFrame frame;
    public StartFrame() {
        frame = new JFrame();

        frame.setTitle("Fast Transfer - v.0.1");
        frame.setResizable(false);
        
		frame.setBounds(100, 100, 430, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
        frame.setLocationRelativeTo(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 46, 194, 204);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("Send File");
		lblNewLabel_1.setBounds(71, 11, 100, 14);
		panel.add(lblNewLabel_1);
		
		JButton sendClientBtn = new JButton("Send File");
		sendClientBtn.setBackground(new Color(0, 128, 64));
		sendClientBtn.setBounds(10, 88, 174, 23);
		sendClientBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				frame.dispose();
				new SendClient();
			}

		});
		panel.add(sendClientBtn);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(214, 46, 194, 204);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JLabel receiveClient = new JLabel("Receive File");
		receiveClient.setHorizontalAlignment(SwingConstants.CENTER);
		receiveClient.setBounds(62, 11, 71, 14);
		panel_1.add(receiveClient);
		
		JButton receiveClientBtn = new JButton("Receive File");
		receiveClientBtn.setBackground(UIManager.getColor("FormattedTextField.selectionBackground"));
		receiveClientBtn.setBounds(10, 89, 174, 23);
		receiveClientBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				frame.dispose();
				new Server();
			}

		});
		panel_1.add(receiveClientBtn);
		
		JLabel lblNewLabel = new JLabel("FastTransfer");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNewLabel.setBounds(141, 11, 145, 24);
		frame.getContentPane().add(lblNewLabel);
        frame.setVisible(true);
    }
}
