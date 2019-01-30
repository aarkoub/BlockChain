package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListener extends Thread {
	private ServerSocket server;
	private P2PNetwork network;
	
	public ServerListener(ServerSocket server, P2PNetwork network) {
		this.server = server;
		this.network = network;
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				Socket s = server.accept();
				network.addPeer(s);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
}
