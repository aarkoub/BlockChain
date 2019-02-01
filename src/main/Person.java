package main;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Person {
	private PrivateKey privateKey;
	private PublicKey publicKey;
	
	private List<Transaction> transactions = new ArrayList<>();
	
	public Person(){
		generateKeyPair();	
	}
	
	public void generateKeyPair() {
		try {
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256r1");
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
			
			Transaction newTransaction = new Transaction(publicKey ,
					name, description, begin, end, end_subscription,
					location, min_participants, max_participants);
			
			newTransaction.generateSignature(privateKey);
			
			transactions.add(newTransaction);
			
			return newTransaction;

		}
		
		public boolean isMaxParticipantsReached(String id) {
			int total = 0;	
			int max_participants=0;
	       
			for(Transaction t : transactions){
				if(!t.isTypeCreation()){
					total ++ ;
				}
				else{
					if(t.getTransactionId().equals(id)){
						max_participants = t.getMax_capacity();
					}
				}
			}
	        
	        if(max_participants>total){
	        	return true;
	        }
			return false;
		}
		
		public Transaction registerParticipant(PublicKey _recipient, String id_event){
		
			boolean idEventFound = false;
			
			for(Transaction t : transactions){
				
				
				if(t.isTypeCreation()){
					if(t.getTransactionId().equals(id_event)){
						idEventFound = true;
						break;
					}
				}
			}
			
			if(idEventFound){
				System.out.println("Wrong id_event");
				return null;
			}
			
			if(isMaxParticipantsReached(id_event)){
				System.out.println("Max participants reached: you cannot subscribe to this event");
				return null;
			}
			
			
			for(Transaction t : transactions){
				
				
							
				if(t.getSubscriber().getEncoded().equals(_recipient)){
					System.out.println("You are already registered");
					return null;
				}
				
				
			}
			
			Transaction newTransaction = new Transaction(_recipient, id_event);
			newTransaction.generateSignature(privateKey);
			
			transactions.add(newTransaction);
		
			return newTransaction;

			
		}
}