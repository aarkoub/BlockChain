package network.test;

import java.io.IOException;

import org.json.JSONObject;

import network.P2PNetwork;

public class TestNetwork {
	public static void main(String[] args) throws IOException {

		int port = 10027;
		P2PNetwork net1 = new P2PNetwork(port+1);
		P2PNetwork net2 = new P2PNetwork(port+2);
		
		net2.addPeer("localhost", port+1);
		JSONObject json = new JSONObject();
		json.accumulate("property", "value");
		net2.broadcastData(json.toString());
		
		
	}
}
