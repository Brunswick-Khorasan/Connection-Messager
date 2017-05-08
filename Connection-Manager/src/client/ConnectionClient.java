package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * The client for the messenger application
 * 
 * @author Morgan
 *
 */
public class ConnectionClient {
	private Socket connect;
	private ClientGUI gui;
	
	public ConnectionClient(ClientGUI gui) {
		this.gui=gui;
	}
	public void start(String host) {
		new Thread() {
			public void run() {
				try { // Try to connect to server
					connect = new Socket(host, Constants.PORTNUM);
					gui.addToLog("Socket connected to server "+host+" on port: "+Constants.PORTNUM);
				} catch (ConnectException c) {
					gui.addToLog("Server not open.");
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try (PrintWriter out = new PrintWriter(connect.getOutputStream(), true);
						BufferedReader in = new BufferedReader(new InputStreamReader(connect.getInputStream()));) {
					new Thread() { // Server Listener
						public void run() {
							while (true) {
								try {
									gui.addToLog(in.readLine());
								} catch (SocketException e) {
									gui.addToLog("Socket Closed");
								} catch (IOException e) {e.printStackTrace();}
							}
						}
					}.run();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ConnectException e) {
					System.err.println("The Server is not connected");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
	}

	public Socket getSocket() {
		return connect;
	}
}
