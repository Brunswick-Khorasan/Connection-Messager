package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.PrintStream;
import java.net.SocketException;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ClientGUI extends JFrame {
	private static final long serialVersionUID = -8332467936477512807L;
	private ConnectionClient connector;
	private JTextArea log;
	public static void main(String[] args) {
		new ClientGUI();
	}
	public ClientGUI() {
		connector = new ConnectionClient(this);
		setTitle("Messager Client");
		JTextField input = new JTextField(50);
		log = new JTextArea(40,50);
		log.setEditable(false);
		input.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					new PrintStream(connector.getSocket().getOutputStream()).println(input.getText());
					input.setText("");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		setLayout(new BoxLayout(this.getContentPane(),BoxLayout.PAGE_AXIS));
		add(log);
		add(input);
		connector.start("localhost");
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				try {
					new PrintStream(connector.getSocket().getOutputStream()).println("" + Constants.COMMANDCHAR + Constants.CommandCodes.DISCONNECT);
					connector.getSocket().close();
				} catch (SocketException e) {
				} catch (IOException e) {
					e.printStackTrace();
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
				System.exit(0);
			}
		});
		pack();
		setVisible(true);
	}
	public void addToLog(String message) {
		log.setText(log.getText() + "\n"+message);
	}
}
