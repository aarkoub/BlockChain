package main;


import java.security.PublicKey;
import java.security.Security;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public static Map<String, List<Transaction>> database = new HashMap<>();
	public static Map<String,Transaction> databaseEvents = new HashMap<>();

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
		if(!verifyTransaction(t)){
			System.out.println("Invalid transaction");
			return;
		}
		
		t.generateSignature(amel.getPrivateKey());
		List<Transaction> transactions = new ArrayList<>();
		transactions.add(t);
		Block block1 = new Block(genesis.getHash(), transactions);
		addBlock(block1);
		
		Transaction t2 = lingchun.registerParticipant(block1.getTransactions().get(0).getTransactionId() );
		if(!verifyTransaction(t2)){
			System.out.println("Invalid transaction");
			return;
		}
		List<Transaction> transactions_blocks2 = new ArrayList<>();
		transactions_blocks2.add(t2);
		Block block2 = new Block(block1.getHash(), transactions_blocks2);
		addBlock(block2);
		
		Transaction t3 = lingchun.registerParticipant(block1.getTransactions().get(0).getTransactionId() );
		if(!verifyTransaction(t3)){
			System.out.println("Invalid transaction");
			return;
		}
		List<Transaction> transactions_blocks3 = new ArrayList<>();
		transactions_blocks3.add(t3);
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
			return new Transaction(subscriber, id_event);
		}
	}
	
	public static boolean verifyTransaction(Transaction transaction) {

		if(transaction == null) {
			System.out.println("Transaction is null.");
			return false;}		
			
		if((transaction.processTransaction() != true)) {
				System.out.println("Transaction failed to process. Discarded.");
				return false;
			
		}

		if(transaction.isTypeCreation()){
			
			if(transaction.getName()==null){
				System.out.println("Name of the event null");
				return false;
			}
			
			if(transaction.getDescription()==null){
				System.out.println("Description of the event null");
				return false;
			}
			
			if(transaction.getBegin().getTime()<0){
				System.out.println("Date of the beginning of the event is null");
				return false;
			}
			
			if(transaction.getEnd().getTime()<0){
				System.out.println("Date of the end of the event is null");
				return false;
			}
			
			if(transaction.getEnd_subscription().getTime()<0){
				System.out.println("Date of the end of the subscription is null");
				return false;
			}
			
			if(transaction.getMin_capacity()<0){
				System.out.println("Minimum number of participants must be positive");
				return false;
			}
			
			if(transaction.getMax_capacity()<0){
				System.out.println("Maximum number of participants must be positive");
				return false;
			}
			
			if(transaction.getMin_capacity()>transaction.getMax_capacity()){
				System.out.println("Minimum number of participants must be smaller than the maximum number of participants");
				return false;
			}
			
			if(transaction.getEnd_subscription().getTime()<new Date().getTime()){
				System.out.println("Date of subcription is alrealdy passed");
				return false;
			}
			
			if(transaction.getEnd().getTime()<new Date().getTime()){
				System.out.println("Date of the end of the event is alrealdy passed");
				return false;
			}
			
			if(transaction.getBegin().getTime()>transaction.getEnd().getTime()){
				System.out.println("Date of the beginning of the event is after the date of end");
				return false;
			}
			
			if(transaction.getLocation()==null){
				System.out.println("Location cannot be null");
				return false;
			}
			
			database.put(transaction.getTransactionId(), new ArrayList<>());
			databaseEvents.put(transaction.getTransactionId(), transaction);
		}
		else{
			
						
			if(database.get(transaction.getIdEventSubsciption())==null){
				System.out.println("Wrong id_event");
				return false;
			}
			
			if(databaseEvents.get(transaction.getIdEventSubsciption()).getMax_capacity()==database.get(transaction.getIdEventSubsciption()).size()){
				System.out.println("Max participants reached: you cannot subscribe to this event");
				return false;
			}
			
			
			for(Transaction t : database.get(transaction.getIdEventSubsciption())){
				
				if(!t.isTypeCreation()){
							
					if(t.getSubscriber().getEncoded().equals(transaction.getSubscriber())){
						System.out.println("You are already registered");
						return false;
					}
				}
				
				
			}
			
			
			database.get(transaction.getIdEventSubsciption()).add(transaction);
		}

		return true;
	}
	
}
