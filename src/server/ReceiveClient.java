package server;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.util.*;
public class ReceiveClient {
    JFrame frame;

	public ReceiveClient(ArrayList<ReceiveFile> files) {
        frame = new JFrame();
        frame.setTitle("Fast Transfer - v.0.1");
        frame.setResizable(false);

		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
        frame.setLocationRelativeTo(null);

		
		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 414, 250);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("FastTranfer");
		lblNewLabel.setBounds(144, 0, 130, 25);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblNewLabel);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 37, 394, 138);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panel.add(scrollPane);

		JPanel filePanel = new JPanel();
		filePanel.setLayout(new BoxLayout(filePanel, BoxLayout.Y_AXIS));
		scrollPane.add(filePanel);

		JLabel fileName = new JLabel();
		fileName.setFont(new Font("Tahoma", Font.BOLD, 20));
		fileName.setBorder(new EmptyBorder(10,0,10,0));
		
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setForeground(new Color(0, 128, 64));
		progressBar.setStringPainted(true);
		progressBar.setValue(35);
		progressBar.setBounds(10, 186, 394, 14);
		panel.add(progressBar);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setIcon(null);
		cancelButton.setBackground(new Color(220, 20, 60));
		cancelButton.setBounds(10, 211, 89, 23);
		panel.add(cancelButton);

        frame.setVisible(true);

    }
	public JFrame getFrame() {
		return frame;
	}
	public void setFrame(JFrame frame) {
		this.frame = frame;
	}
}
