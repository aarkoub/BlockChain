package main;


import java.security.Security;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;

import merkletree.MerkleTree;
import utils.StringUtil;

public class BlockChain {
	
	public static ArrayList<Block> blockchain = new ArrayList<Block>();
	public static Person amel = new Person();

	public static void main(String[] args) {	
		//add our blocks to the blockchain ArrayList:
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); //Setup Bouncey castle as a Security Provider

		String name = "opera";
		String description = "opera_garnier";
		String location = "paris";
		long begin = Date.UTC(2019-1900, 1, 21, 6, 0, 0);
		long end = Date.UTC(2019-1900, 1, 24, 8, 8, 9);
		long end_subscription = Date.UTC(2019-1900, 1, 19, 22, 50, 8);
		int min_capacity = 5;
		int max_capacity = 8;
		
		Block genesis = new Block("", null);
		addBlock(genesis);
			
		Transaction t  = amel.createEvent(name, description, begin, end, end_subscription, location,
				min_capacity, max_capacity);
		t.generateSignature(amel.getPrivateKey());
		List<Transaction> transactions = new ArrayList<>();
		transactions.add(t);
		Block block1 = new Block(genesis.getHash(), transactions);
		addBlock(block1);
		System.out.println();
		
		/*Block block2 = new Block(block1.getHash());
		block2.addTransaction(amel.registerParticipant(lingchun.getPublicKey(), block1.getTransactions().get(0).getTransactionId() ));
		addBlock(block2);
		System.out.println();
		
		//test : not to add the transaction cause participant alrealdy registered
		Block block3 = new Block(block2.getHash());
		block3.addTransaction(amel.registerParticipant(lingchun.getPublicKey(),block1.getTransactions().get(0).getTransactionId()));
		addBlock(block3);
		System.out.println();*/
		
		
		isChainValid();
		//System.out.println(StringUtil.getJson(blockchain));
	}
	
	public static Boolean isChainValid() {
		
		Block currentBlock; 
		Block previousBlock;
				
		//loop through blockchain to check hashes:
		for(int i=1; i < blockchain.size(); i++) {
			
			currentBlock = blockchain.get(i);
			previousBlock = blockchain.get(i-1);
			//compare registered hash and calculated hash:
			if(!currentBlock.getHash().equals(currentBlock.calculateHash()) ){
				System.out.println("#Current Hashes not equal");
				return false;
			}
			//compare previous hash and registered previous hash
			if(!previousBlock.getHash().equals(currentBlock.getPreviousHash()) ) {
				System.out.println("#Previous Hashes not equal");
				return false;
			}
			
			for(int t=0; t <currentBlock.getTransactions().size(); t++) {
				Transaction currentTransaction = currentBlock.getTransactions().get(t);
				
				if(!currentTransaction.verifySignature()) {
					System.out.println("#Signature on Transaction(" + t + ") is Invalid");
					return false; 
				}
	
			}
			
			List<String> data = new ArrayList<>();
			
			for(Transaction t : currentBlock.getTransactions()){
				data.add(t.getTransactionId());
			}
			
			if(!MerkleTree.getMerkleTreeRoot(data).equals(currentBlock.getMerkleRoot())){
				System.out.println("Wrong merkle tree root");
				return false;
			}
			
			
		}
		
		System.out.println("Blockchain is valid");
		return true;
	}
	
	public static void addBlock(Block newBlock) {
		
		for(Block b : blockchain) {
			if(b.getHash().equals(newBlock.getPreviousHash())){
				newBlock.setLevel(b.getLevel()+1);
			}
		}
		blockchain.add(newBlock);
	}
	

	public static Transaction verifyTransaction(String json) {
		try {
			Object obj = new JSONParser().parse(json);
			JSONObject jo = (JSONObject) obj; 
			
			String transactionId = (String) jo.get("transactionId"); 
	        
			Object sender = jo.get("sender"); 
	        JSONObject sender_json = (JSONObject) sender;
	        
	        Object recipient = jo.get("recipient"); 
	        JSONObject recipient_json = (JSONObject) recipient;
	        
	        String type_transaction = (String) jo.get("type_transaction");
	        
	        String min_capacity = (String) jo.get("min_capacity"); 
	        
	        String max_capacity = (String) jo.get("max_capacity");
	        
	        Object signature = jo.get("signature"); 
	        JSONArray signature_json = (JSONArray) signature;
	        
	        String id_event = (String) jo.get("id_event");
	        
	        String isTypeCreation = (String) jo.get("isTypeCreation");
	        
	        Object inputs = jo.get("inputs"); 
	        JSONArray inputs_json = (JSONArray) inputs;
	        
	        Object outputs = jo.get("outputs"); 
	        JSONArray outputs_json = (JSONArray) outputs;
	        
	        
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public boolean verifyTransaction(Transaction transaction) {
		//process transaction and check if valid, unless block is genesis block then ignore.
		if(transaction == null) {
			System.out.println("Transaction is null.");
			return false;}		
			
		if((transaction.processTransaction() != true)) {
				System.out.println("Transaction failed to process. Discarded.");
				return false;
			
		}

		return true;
	}
	
}
