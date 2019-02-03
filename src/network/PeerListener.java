package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import org.json.JSONObject;

import utils.NetworkUtil;

public class PeerListener extends Thread {
	private Socket peer;
	private P2PNetwork network;
	
	public PeerListener(Socket peer, P2PNetwork network) {
		this.peer = peer;
		this.network = network;
	}
	
	@Override
	public void run() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(peer.getInputStream()));
			String bindata = "";
			while(true) {
				bindata = br.readLine();
				int size = Integer.parseInt(bindata.substring(0, 32), 2);
				String data = NetworkUtil.binarytoString(bindata.substring(32, 32+size));
				String signature = bindata.substring(32+size, bindata.length());
				JSONObject json = new JSONObject(data);

			}
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		
	}
}
