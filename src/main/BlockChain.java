package main;


import java.security.PublicKey;
import java.security.Security;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import merkletree.MerkleTree;
import utils.StringUtil;

public class BlockChain {
	
	public static ArrayList<Block> blockchain = new ArrayList<Block>();
	public static Person amel = new Person();
	public static Person lingchun = new Person();

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
		
		Block genesis = new Block("0", null);
		addBlock(genesis);
			
		Transaction t  = amel.createEvent(name, description, begin, end, end_subscription, location,
				min_capacity, max_capacity);
		t.generateSignature(amel.getPrivateKey());
		List<Transaction> transactions = new ArrayList<>();
		transactions.add(t);
		Block block1 = new Block(genesis.getHash(), transactions);
		addBlock(block1);
		
		List<Transaction> transactions_blocks2 = new ArrayList<>();
		transactions_blocks2.add(amel.registerParticipant(lingchun.getPublicKey(), block1.getTransactions().get(0).getTransactionId() ));
		Block block2 = new Block(block1.getHash(), transactions_blocks2);
		addBlock(block2);
		
		List<Transaction> transactions_blocks3 = new ArrayList<>();
		transactions_blocks3.add(amel.registerParticipant(lingchun.getPublicKey(), block1.getTransactions().get(0).getTransactionId() ));
		//test : not to add the transaction cause participant alrealdy registered
		Block block3 = new Block(block2.getHash(), transactions_blocks3);
		addBlock(block3);
		System.out.println();
		
		isChainValid();
		System.out.println(StringUtil.getJson(blockchain));
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
		JsonElement mJson =  new JsonParser().parse(json);
		JsonObject jsonObject = mJson.getAsJsonObject();
		
		String transactionId = jsonObject.get("transactionId").getAsString(); 
		PublicKey subscriber = new Gson().fromJson(jsonObject.get("subscriber"), PublicKey.class); 
		PublicKey creator = new Gson().fromJson(jsonObject.get("creator"), PublicKey.class); 
		String name = jsonObject.get("name").getAsString(); 
		String description = jsonObject.get("description").getAsString(); 
		Date begin = new Gson().fromJson(jsonObject.get("begin"), Date.class); 
		Date end = new Gson().fromJson(jsonObject.get("end"), Date.class); 
		Date end_subscription = new Gson().fromJson(jsonObject.get("end_subscription"), Date.class); 
		String location = jsonObject.get("location").getAsString(); 
		int min_capacity = jsonObject.get("min_capacity").getAsInt(); 
		int max_capacity = jsonObject.get("max_capacity").getAsInt(); 
		JsonArray signature_json = jsonObject.get("signature").getAsJsonArray();
		byte[] signature = new byte[signature_json.size()];
		for (int i = 0; i < signature_json.size(); i++) {
			signature[i]=signature_json.get(i).getAsByte();
		}
		String id_event = jsonObject.get("id_event").getAsString(); 
		boolean isTypeCreation = jsonObject.get("isTypeCreation").getAsBoolean(); 
		Date date_creation_transaction = new Gson().fromJson(jsonObject.get("date_creation_transaction"), Date.class);
		
		if(isTypeCreation) {
			return new Transaction(creator, name, description, begin.getTime(), end.getTime(),
					end_subscription.getTime(), location, min_capacity, max_capacity);
		}
		else {
			return new Transaction(creator, subscriber, id_event);
		}
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
