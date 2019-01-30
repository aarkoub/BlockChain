package network;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import utils.NetworkUtil;

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
		PeerListener pl = new PeerListener(s, this);
		pl.start();
		peerslistener.add(pl);
	}
	
	public synchronized void addPeer(String host, int port) {
		try {
			Socket s = new Socket(host, port);
			addPeer(s);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public synchronized void broadcastData(String data) throws IOException {
		String send = NetworkUtil.stringToBinary(data);
		String length = Integer.toBinaryString(send.length());
		while(length.length() < 32) {
			length = "0"+length;
		}
		send = length+send;
		for(Socket s: peers) {
			BufferedWriter br = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			br.write(send);
			br.newLine();
			br.flush();
		}
	}
	
	
}
