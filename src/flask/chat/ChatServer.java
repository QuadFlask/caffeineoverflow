package flask.chat;

import java.io.IOException;
import java.net.Socket;

public class ChatServer {
	private String ip;
	private int port;
	private Socket socket;

	public ChatServer(String ip, int port) {
		this.ip = ip;
		this.port = port;

		try {
			socket = new Socket(ip, port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}


