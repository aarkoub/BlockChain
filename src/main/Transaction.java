package main;

import java.security.*;
import java.util.Date;

import utils.StringUtil;

public class Transaction {
	
	private String transactionId; // this is also the hash of the transaction.
	private PublicKey subscriber, creator; // senders address/public key.
	private String name, description;
	private Date begin, end, end_subscription;
	private String location;
	private int min_capacity, max_capacity;
	private byte[] signature; // this is to prevent anybody else from spending funds in our wallet.
	private String id_event;
	private boolean isTypeCreation;
	private Date date_creation_transaction ;

	


	
	// Constructor: 
	public Transaction(PublicKey creator,
			String name, String description, 
			long begin, long end, long end_subscription,
			String location,
			int min_capacity, int max_capacity) {
		this.creator = creator;
		this.begin = new Date(begin);
		this.end = new Date(end);
		this.end_subscription = new Date(end_subscription);
		this.name = name;
		this.description = description;
		this.min_capacity = min_capacity;
		this.max_capacity = max_capacity;
		this.location = location;
		isTypeCreation = true;
		
		date_creation_transaction = new Date();

		
	}
	
	public Transaction(PublicKey subscriber, String id_event) {
		this.subscriber = subscriber;
		this.id_event = id_event;
	}
	
	
	// This Calculates the transaction hash (which will be used as its Id)
	private String calulateHash() {
		
		if(isTypeCreation){
			return StringUtil.applySha256(
					StringUtil.getStringFromKey(creator) +
					name+description+begin+end+end_subscription+
					min_capacity+max_capacity+ date_creation_transaction.getTime()
					);
			
		}
		else{
			return StringUtil.applySha256(
					StringUtil.getStringFromKey(subscriber) + id_event);
			
		}
		
		
	}
	
	//Signs all the data we dont wish to be tampered with.
	public void generateSignature(PrivateKey privateKey) {
		
		String data;
		
		if(isTypeCreation){
			
			data = StringUtil.getStringFromKey(creator) +  name +
					description + begin + end+end_subscription+
					location+min_capacity+max_capacity;
			
		}
		else{
			
			data = StringUtil.getStringFromKey(subscriber)+id_event;
			
			
		}
		
		signature = StringUtil.applyECDSASig(privateKey,data);
		processTransaction();
			
	}
	//Verifies the data we signed hasnt been tampered with
	public boolean verifySignature() {
		
		String data;
		
		if(isTypeCreation){
			
			data = StringUtil.getStringFromKey(creator)  + name +
					description + begin + end+end_subscription+
					location+min_capacity+max_capacity;
			return StringUtil.verifyECDSASig(creator, data, signature);
		}
		else {
			data = StringUtil.getStringFromKey(subscriber)+id_event;
			return StringUtil.verifyECDSASig(subscriber, data, signature);
		}
//		System.out.println("signature "+signature);
		
	}	
	
	//Returns true if new transaction could be created.	
	public boolean processTransaction() {
		
			if(verifySignature() == false) {
				System.out.println("#Transaction Signature failed to verify");
				return false;
			}
					
			transactionId = calulateHash();
			
						
			return true;
		}

	public String getName() {
		return name;
	}


	public String getDescription() {
		return description;
	}



	public Date getBegin() {
		return begin;
	}


	public Date getEnd() {
		return end;
	}



	public Date getEnd_subscription() {
		return end_subscription;
	}



	public String getLocation() {
		return location;
	}



	public int getMin_capacity() {
		return min_capacity;
	}



	public int getMax_capacity() {
		return max_capacity;
	}

	
	public boolean isTypeCreation(){
		return isTypeCreation;
	}
	
	
	public String getTransactionId() {
		return transactionId;
	}


	public PublicKey getCreator() {
		return creator;
	}
	
	public PublicKey getSubscriber() {
		return subscriber;
	}


	public byte[] getSignature() {
		return signature;
	}
	
	public String getIdEventSubsciption(){
		return id_event;
	}

	
}
