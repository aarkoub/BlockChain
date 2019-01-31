package main;


import java.security.Security;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;

import utils.StringUtil;

public class BlockChain {
	
	public static ArrayList<Block> blockchain = new ArrayList<Block>();
	public static HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>();
	
	public static int difficulty = 3;
	public static Person amel, lingchun;
	
	public static Transaction genesisTransaction;

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {	
		//add our blocks to the blockchain ArrayList:
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); //Setup Bouncey castle as a Security Provider
		
		//Create wallets:
		amel = new Person();
		lingchun = new Person();
		
//		Personne first_person = new Personne();
		String name = "opera";
		String description = "opera_garnier";
		String location = "paris";
		long begin = Date.UTC(2019-1900, 1, 21, 6, 0, 0);
		long end = Date.UTC(2019-1900, 1, 24, 8, 8, 9);
		long end_subscription = Date.UTC(2019-1900, 1, 19, 22, 50, 8);
		int min_capacity = 5;
		int max_capacity = 8;
		
		//create genesis transaction
		genesisTransaction = new Transaction(amel.getPublicKey(),
				name, description, begin, end, end_subscription, location,
				min_capacity, max_capacity);
		genesisTransaction.generateSignature(amel.getPrivateKey());	 //manually sign the genesis transaction	
		genesisTransaction.setTransactionId("0"); //manually set the transaction id
		genesisTransaction.getOutputs().add(new TransactionOutput(genesisTransaction.getRecipient(),genesisTransaction.getTransactionId(),
				genesisTransaction.getName(), genesisTransaction.getDescription(), genesisTransaction.getBegin().getTime(),
				genesisTransaction.getEnd().getTime(), genesisTransaction.getEnd_subcription().getTime(), genesisTransaction.getLocation(), 
				genesisTransaction.getMin_capacity(), genesisTransaction.getMax_capacity()
				)); //manually add the Transactions Output
		UTXOs.put(genesisTransaction.getOutputs().get(0).getId(), genesisTransaction.getOutputs().get(0)); //its important to store our first transaction in the UTXOs list.
		
		System.out.println("Creating and Mining Genesis block... ");
		Block genesis = new Block("0");
		genesis.addTransaction(genesisTransaction);
		addBlock(genesis);
		System.out.println();
		
		
		Block block1 = new Block(genesis.getHash());
		System.out.println("Transaction added ? "+block1.addTransaction(amel.createEvent(name, description, begin, end, end_subscription, location,
min_capacity, max_capacity)));
		addBlock(block1);
		System.out.println();
		
		Block block2 = new Block(block1.getHash());
		block2.addTransaction(amel.registerParticipant(lingchun.getPublicKey(), block1.getTransactions().get(0).getTransactionId() ));
		addBlock(block2);
		System.out.println();
		
		//test : not to add the transaction cause participant alrealdy registered
		Block block3 = new Block(block2.getHash());
		block3.addTransaction(amel.registerParticipant(lingchun.getPublicKey(),block1.getTransactions().get(0).getTransactionId()));
		addBlock(block3);
		System.out.println();
		
		/*//testing
		Block block1 = new Block(genesis.getHash());
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("\nWalletA is Attempting to send funds (40) to WalletB...");
		block1.addTransaction(walletA.sendFunds(walletB.getPublicKey(), 40f));
		addBlock(block1);
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("WalletB's balance is: " + walletB.getBalance());
		
		Block block2 = new Block(block1.getHash());
		System.out.println("\nWalletA Attempting to send more funds (1000) than it has...");
		block2.addTransaction(walletA.sendFunds(walletB.getPublicKey(), 1000f));
		addBlock(block2);
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("WalletB's balance is: " + walletB.getBalance());
		
		Block block3 = new Block(block2.getHash());
		System.out.println("\nWalletB is Attempting to send funds (20) to WalletA...");
		block3.addTransaction(walletB.sendFunds( walletA.getPublicKey(), 20));
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("WalletB's balance is: " + walletB.getBalance());*/
		
		isChainValid();
		System.out.println(StringUtil.getJson(blockchain));
	}
	
	public static Boolean isChainValid() {
		Block currentBlock; 
		Block previousBlock;
		String hashTarget = new String(new char[difficulty]).replace('\0', '0');
		HashMap<String,TransactionOutput> tempUTXOs = new HashMap<String,TransactionOutput>(); //a temporary working list of unspent transactions at a given block state.
		tempUTXOs.put(genesisTransaction.getOutputs().get(0).getId(), genesisTransaction.getOutputs().get(0));
		
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
			//check if hash is solved
			if(!currentBlock.getHash().substring( 0, difficulty).equals(hashTarget)) {
				System.out.println("#This block hasn't been mined");
				return false;
			}
			
			//loop thru blockchains transactions:
			TransactionOutput tempOutput;
			for(int t=0; t <currentBlock.getTransactions().size(); t++) {
				Transaction currentTransaction = currentBlock.getTransactions().get(t);
				
				if(!currentTransaction.verifySignature()) {
					System.out.println("#Signature on Transaction(" + t + ") is Invalid");
					return false; 
				}
				/*if(currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()) {
					System.out.println("#Inputs are note equal to outputs on Transaction(" + t + ")");
					return false; 
				}*/
				
				for(TransactionInput input: currentTransaction.getInputs()) {	
					tempOutput = tempUTXOs.get(input.getTransactionOutputId());
					
					if(tempOutput == null) {
						System.out.println("#Referenced input on Transaction(" + t + ") is Missing");
						return false;
					}
					
					if(input.getUTXO().getName() != tempOutput.getName()) {
						System.out.println("#Referenced input Transaction(" + t + ") name is Invalid");
						return false;
					}
					if(input.getUTXO().getDescription() != tempOutput.getDescription()) {
						System.out.println("#Referenced input Transaction(" + t + ") description is Invalid");
						return false;
					}
					if(input.getUTXO().getBegin().getTime() != tempOutput.getBegin().getTime()) {
						System.out.println("#Referenced input Transaction(" + t + ") time is Invalid");
						return false;
					}
					if(input.getUTXO().getEnd().getTime() != tempOutput.getEnd().getTime()) {
						System.out.println("#Referenced input Transaction(" + t + ") end is Invalid");
						return false;
					}
					if(input.getUTXO().getEnd_subcription().getTime() != tempOutput.getEnd_subcription().getTime()) {
						System.out.println("#Referenced input Transaction(" + t + ") end_subcription is Invalid");
						return false;
					}
					if(input.getUTXO().getLocation() != tempOutput.getLocation()) {
						System.out.println("#Referenced input Transaction(" + t + ") location is Invalid");
						return false;
					}
					if(input.getUTXO().getMinCapacity() != tempOutput.getMinCapacity()) {
						System.out.println("#Referenced input Transaction(" + t + ") min_capacity is Invalid");
						return false;
					}
					
					if(input.getUTXO().getMaxCapacity() != tempOutput.getMaxCapacity()) {
						System.out.println("#Referenced input Transaction(" + t + ") max_capacity is Invalid");
						return false;
					}
					
					tempUTXOs.remove(input.getTransactionOutputId());
				}
				
				for(TransactionOutput output: currentTransaction.getOutputs()) {
					tempUTXOs.put(output.getId(), output);
				}
				if(currentTransaction.getOutputs().get(0).isTypeCreation()){
					if( currentTransaction.getOutputs().get(0).getReciepient() != currentTransaction.getRecipient()) {
						
						System.out.println("#Transaction(" + t + ") output reciepient is not who it should be");
						return false;
					}
				}
				else{
					if( currentTransaction.getOutputs().get(0).getParticipant() != currentTransaction.getRecipient()) {
						
						System.out.println("#Transaction(" + t + ") output participant is not who it should be");
						return false;
					}
				}
			}
		}
		
		System.out.println("Blockchain is valid");
		return true;
	}
	
	public static void addBlock(Block newBlock) {
		newBlock.mineBlock(difficulty);
		for(Block b : blockchain) {
			if(b.getHash().equals(newBlock.getPreviousHash())){
				newBlock.setLevel(b.getLevel()+1);
			}
		}
		blockchain.add(newBlock);
	}
	
	public static HashMap<String, TransactionOutput> getUTXOs(){
		return UTXOs;
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
}
