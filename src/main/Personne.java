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
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public PrivateKey getPrivateKey(){
		return privateKey;
	}
	
	public PublicKey getPublicKey(){
		return publicKey;
	}
	
	/* //returns balance and stores the UTXO's owned by this wallet in this.UTXOs
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
		}*/
		/*//Generates and returns a new transaction from this wallet.
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
		}*/
		
		public Transaction createEvent(String name, 
				String description,
				long begin,
				long end,
				long end_subscription,
				String location,
				int min_participants,
				int max_participants){
			
			
			if(name==null){
				System.out.println("Name of the event null");
				return null;
			}
			
			if(description==null){
				System.out.println("Description of the event null");
				return null;
			}
			
			if(begin<0){
				System.out.println("Date of the beginning of the event is null");
				return null;
			}
			
			if(end<0){
				System.out.println("Date of the end of the event is null");
				return null;
			}
			
			if(end_subscription<0){
				System.out.println("Date of the end of the subscription is null");
				return null;
			}
			
			if(min_participants<0){
				System.out.println("Minimum number of participants must be positive");
				return null;
			}
			
			if(max_participants<0){
				System.out.println("Maximum number of participants must be positive");
				return null;
			}
			
			if(min_participants>max_participants){
				System.out.println("Minimum number of participants must be smaller than the maximum number of participants");
				return null;
			}
			
			if(end_subscription<new Date().getTime()){
				System.out.println("Date of subcription is alrealdy passed");
				return null;
			}
			
			if(end<new Date().getTime()){
				System.out.println("Date of the end of the event is alrealdy passed");
				return null;
			}
			
			if(begin>end){
				System.out.println("Date of the beginning of the event is after the date of end");
				return null;
			}
			
			if(location==null){
				System.out.println("Location cannot be null");
				return null;
			}
			
			
	    //create array list of inputs
			//ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();

			Transaction newTransaction = new Transaction(publicKey ,
					name, description, begin, end, end_subscription,
					location, min_participants, max_participants);
			newTransaction.generateSignature(privateKey);
			
			/*for(TransactionInput input: inputs){
				UTXOs.remove(input.getTransactionOutputId());
			}*/
			return newTransaction;

		}
		
		public boolean isMaxParticipantsReached() {
			int total = 0;	
			int max_participants=0;
	        for (Map.Entry<String, TransactionOutput> item: BlockChain.getUTXOs().entrySet()){
	        	TransactionOutput UTXO = item.getValue();
	       
	            if(UTXO.isMine(publicKey)) { //if output belongs to me ( if coins belong to me )
	            	UTXOs.put(UTXO.getId(),UTXO); //add it to our list of unspent transactions.
	            	if(UTXO.isTypeCreation()){
	            		max_participants = UTXO.getMaxCapacity();
	            	}
	            	else{
	            		total ++ ; 
	            	}
	            }
	        } 
	        
	        if(max_participants<=total){
	        	return true;
	        }
			return false;
		}
		
		public Transaction registerParticipant(PublicKey  _recipient, String id_event){
			
			if(isMaxParticipantsReached()){
				System.out.println("Max participants reached: you cannot subscribe to this event");
				return null;
			}
			
			ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
		    
			boolean idEventFound = false;
			for (Map.Entry<String, TransactionOutput> item: UTXOs.entrySet()){
				
				TransactionOutput UTXO = item.getValue();
				if(UTXO.isTypeCreation()){
					if(UTXO.getId_Event()==id_event){
						idEventFound = true;
					}
					
				}
				
				if(UTXO.getParticipant()==_recipient){
					System.out.println("You are already registered");
					return null;
				}
				
				
			}
			
			if(idEventFound){
				System.out.println("Wrong id_event");
				return null;
			}
			
			Transaction newTransaction = new Transaction(publicKey,  _recipient, id_event, inputs);
			newTransaction.generateSignature(privateKey);
			
		
			return newTransaction;

			
		}
}