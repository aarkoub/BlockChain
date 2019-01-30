package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class P2PNetwork {
	private int port;
	private ServerSocket server;
	private List<Socket> peers;
	private List<PeerListener> peerslistener;
	private ServerListener serverlistener;
	
	public P2PNetwork(int port) throws IOException {
		this.port = port;
		this.peers = new ArrayList<>();
		this.server = new ServerSocket(this.port);
		this.peerslistener = new ArrayList<>();
		this.serverlistener = new ServerListener(server, this);
		this.serverlistener.start();
	}
	
	public synchronized void addPeer(Socket s) {
		peers.add(s);
	}
	
	
}
