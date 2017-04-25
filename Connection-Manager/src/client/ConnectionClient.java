package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * The client for the messenger application
 * @author Morgan
 *
 */
public class ConnectionClient {
	public static final int PORTNUM = 3576;
	private static Socket connect;
	public static void start() {
		try (
				Socket c = new Socket("localhost", PORTNUM);
				PrintWriter out =
						new PrintWriter(c.getOutputStream(), true);
				BufferedReader in =
						new BufferedReader(
								new InputStreamReader(c.getInputStream()));
				BufferedReader stdIn =
						new BufferedReader(
								new InputStreamReader(System.in))) {
			connect = c;
			Thread getConsoleMessage = new Thread() {
				public void run() {
					while (true) {
					try {
						System.out.println(in.readLine());
					} catch (IOException e) {}
					}
				}
			};
			getConsoleMessage.start();
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
