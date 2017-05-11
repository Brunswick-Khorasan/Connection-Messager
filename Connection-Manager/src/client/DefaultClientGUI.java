package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class DefaultClientGUI extends JFrame implements ClientGUI {
	private static final long serialVersionUID = -8332467936477512807L;
	private ConnectionClient connector;
	private JTextArea log;
	public static void main(String[] args) {
		new DefaultClientGUI();
	}
	public DefaultClientGUI() {
		connector = new ConnectionClient(this);
		setTitle("Messager Client");
		JTextField input = new JTextField(50);
		JMenuBar menu = new JMenuBar();
		JMenu options = new JMenu("Options");
		JMenuItem getFullLog = new JMenuItem("Display full server log");
		getFullLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					new PrintStream(connector.getSocket().getOutputStream()).println("" + Constants.COMMANDCHAR + Constants.CommandCodes.DOWNLOADLOG);
					String sLog = new BufferedReader(new InputStreamReader(connector.getSocket().getInputStream())).readLine();
					sLog = sLog.replace('§', '\n');
					log.setText(sLog);
				} catch (Exception x) {
					x.printStackTrace();
				}
			}
		});
		options.add(getFullLog);
		menu.add(options);
		setJMenuBar(menu);
		log = new JTextArea(40,50);
		log.setEditable(false);
		JScrollPane scrollbar = new JScrollPane(log);
		scrollbar.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		input.addActionListener(new ActionListener() {
			boolean nameEntered = false;
			public void actionPerformed(ActionEvent arg0) {
				try {
					String toSpeak = input.getText();
					if (!nameEntered) {
						nameEntered = true;
						setTitle(getTitle() + " - "+toSpeak);
					}
					new PrintStream(connector.getSocket().getOutputStream()).println(toSpeak);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (NullPointerException e) {
					addToLog("Host unknown or server not running");
				} finally {
					input.setText("");
				}
			}
		});
		setLayout(new BoxLayout(this.getContentPane(),BoxLayout.PAGE_AXIS));
		add(scrollbar);
		add(input);
		connector.start(JOptionPane.showInputDialog(this, "Enter a host"));
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				try {
					new PrintStream(connector.getSocket().getOutputStream()).println("" + Constants.COMMANDCHAR + Constants.CommandCodes.DISCONNECT);
					connector.getSocket().close();
				} catch (Exception e) {
				} finally {
					System.exit(0);
				}
			}
		});
		pack();
		setVisible(true);
	}
	public void addToLog(String message) {
		log.setText(log.getText() + "\n"+message);
	}
}
