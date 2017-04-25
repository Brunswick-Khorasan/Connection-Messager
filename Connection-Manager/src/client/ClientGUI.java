package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.PrintStream;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class ClientGUI extends JFrame {
	private static final long serialVersionUID = -8332467936477512807L;
	
	public static void main(String[] args) {
		new ClientGUI();
	}
	public ClientGUI() {
		ConnectionClient.start();
		setTitle("Messager Client");
		JTextField input = new JTextField(50);
		input.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					new PrintStream(ConnectionClient.getSocket().getOutputStream()).print(input.getText());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		add(new JLabel("Weyhoo"));
		add(input);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent arg0) {
				try {
					ConnectionClient.getSocket().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.exit(0);
			}
		});
		pack();
		setVisible(true);
	}
	
}
