package main;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
			
		
		Transaction newTransaction = new Transaction(publicKey ,
				name, description, begin, end, end_subscription,
				location, min_participants, max_participants);
		
		newTransaction.generateSignature(privateKey);
		
		transactions.add(newTransaction);
		
		return newTransaction;

	}
	
	
	public Transaction registerParticipant(String id_event){
			
		Transaction newTransaction = new Transaction(publicKey, id_event);
		newTransaction.generateSignature(privateKey);
		
		transactions.add(newTransaction);
	
		return newTransaction;

		
	}
}