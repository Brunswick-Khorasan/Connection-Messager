package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.PrintStream;
import java.net.SocketException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class ClientGUI extends JFrame {
	private static final long serialVersionUID = -8332467936477512807L;
	private ConnectionClient connector;
	public static void main(String[] args) {
		new ClientGUI();
	}
	public ClientGUI() {
		connector = new ConnectionClient();
		Thread client = new Thread() {
			public void run() {
				connector.start("localhost");
			}
		};
		client.start();
		setTitle("Messager Client");
		JTextField input = new JTextField(50);
		input.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					new PrintStream(connector.getSocket().getOutputStream()).println(input.getText());
					input.setText("");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		add(input);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				try {
					new PrintStream(connector.getSocket().getOutputStream()).println("" + Constants.COMMANDCHAR + Constants.CommandCodes.DISCONNECT);
					connector.getSocket().close();
				} catch (SocketException e) {
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
