package network;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class PeerListener extends Thread {
	private Socket peer;
	
	public PeerListener(Socket peer) {
		this.peer = peer;
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				InputStream is = peer.getInputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
