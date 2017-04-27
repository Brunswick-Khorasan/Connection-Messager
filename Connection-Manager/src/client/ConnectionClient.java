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
	public static final int PORTNUM = 3576;
	private static Socket connect;

	public static void start(String host) {
		try {
			connect = new Socket(host, PORTNUM);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try (PrintWriter out = new PrintWriter(connect.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(connect.getInputStream()));) {
			String output;
			try {
				while ((output = in.readLine()) != null) {
					System.out.println(output);
				}
			} catch (SocketException e) {
				System.out.println("Connection Ended");
			} catch (IOException e) {
				e.printStackTrace();
			}
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

	public static Socket getSocket() {
		return connect;
	}
}
