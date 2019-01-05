package main;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Personne {
	private PrivateKey privateKey;
	private PublicKey publicKey;
	public HashMap<String,TransactionOutput> UTXOs = 
			new HashMap<String,TransactionOutput>(); //only UTXOs owned by this wallet.
	
	public Personne(){
		generateKeyPair();	
	}
	
	public void generateKeyPair() {
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
			// Initialize the key generator and generate a KeyPair
			keyGen.initialize(ecSpec, random);   //256 bytes provides an acceptable security level
	        	KeyPair keyPair = keyGen.generateKeyPair();
	        	// Set the public and private keys from the keyPair
	        	privateKey = keyPair.getPrivate();
	        	publicKey = keyPair.getPublic();
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public PrivateKey getPrivateKey(){
		return privateKey;
	}
	
	public PublicKey getPublicKey(){
		return publicKey;
	}
	
	 //returns balance and stores the UTXO's owned by this wallet in this.UTXOs
		public float getBalance() {
			float total = 0;	
	        for (Map.Entry<String, TransactionOutput> item: NoobChain.getUTXOs().entrySet()){
	        	TransactionOutput UTXO = item.getValue();
	            if(UTXO.isMine(publicKey)) { //if output belongs to me ( if coins belong to me )
	            	UTXOs.put(UTXO.getId(),UTXO); //add it to our list of unspent transactions.
	            	total += UTXO.getValue() ; 
	            }
	        }  
			return total;
		}
		//Generates and returns a new transaction from this wallet.
		public Transaction sendFunds(PublicKey _recipient,float value ) {
			if(getBalance() < value) { //gather balance and check funds.
				System.out.println("#Not Enough funds to send transaction. Transaction Discarded.");
				return null;
			}
	    //create array list of inputs
			ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
	    
			float total = 0;
			for (Map.Entry<String, TransactionOutput> item: UTXOs.entrySet()){
				TransactionOutput UTXO = item.getValue();
				total += UTXO.getValue();
				inputs.add(new TransactionInput(UTXO.getId()));
				if(total > value) break;
			}
			
			Transaction newTransaction = new Transaction(publicKey, _recipient , value, inputs);
			newTransaction.generateSignature(privateKey);
			
			for(TransactionInput input: inputs){
				UTXOs.remove(input.getTransactionOutputId());
			}
			return newTransaction;
		}
		
		public boolean createEvent(PublicKey _recipient, String name, 
				String description,
				Date begin,
				Date end,
				Date end_subscription,
				String location,
				int min_participants,
				int max_participants){
			
			
			if(name==null){
				System.out.println("Name of the event null");
				return false;
			}
			
			if(description==null){
				System.out.println("Description of the event null");
				return false;
			}
			
			if(begin==null){
				System.out.println("Date of the beginning of the event is null");
				return false;
			}
			
			if(end==null){
				System.out.println("Date of the end of the event is null");
				return false;
			}
			
			if(end_subscription==null){
				System.out.println("Date of the end of the subscription is null");
				return false;
			}
			
			if(min_participants<0){
				System.out.println("Minimum number of participants must be positive");
				return false;
			}
			
			if(max_participants<0){
				System.out.println("Maximum number of participants must be positive");
				return false;
			}
			
			if(min_participants>max_participants){
				System.out.println("Minimum number of participants must be smaller than the maximum number of participants");
				return false;
			}
			
			if(end_subscription.before(new Date())){
				System.out.println("Date of subcription is alrealdy passed");
				return false;
			}
			
			if(end.before(new Date())){
				System.out.println("Date of the end of the event is alrealdy passed");
				return false;
			}
			
			return true;
		}
}