package main;

import java.security.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import utils.StringUtil;

public class Transaction {
	
	private String transactionId; // this is also the hash of the transaction.
	private PublicKey sender; // senders address/public key.
	private PublicKey reciepient; // Recipients address/public key.
	private String type_transaction ;
	private String name, description;
	private Date begin, end, end_subcription;
	private String location;
	private int min_capacity, max_capacity;
	private byte[] signature; // this is to prevent anybody else from spending funds in our wallet.
	private String id_event;
	private boolean isTypeCreation;
	private List<TransactionInput> inputs = new ArrayList<>();
	private List<TransactionOutput> outputs = new ArrayList<>();
	
	
	
	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public PublicKey getSender() {
		return sender;
	}

	public void setSender(PublicKey sender) {
		this.sender = sender;
	}

	public PublicKey getReciepient() {
		return reciepient;
	}

	public void setReciepient(PublicKey reciepient) {
		this.reciepient = reciepient;
	}

	public byte[] getSignature() {
		return signature;
	}

	public void setSignature(byte[] signature) {
		this.signature = signature;
	}

	public List<TransactionInput> getInputs() {
		return inputs;
	}

	public void setInputs(List<TransactionInput> inputs) {
		this.inputs = inputs;
	}

	public List<TransactionOutput> getOutputs() {
		return outputs;
	}

	public void setOutputs(List<TransactionOutput> outputs) {
		this.outputs = outputs;
	}

	private static int sequence = 0; // a rough count of how many transactions have been generated. 
	
	// Constructor: 
	public Transaction(PublicKey sender,
			String name, String description, 
			long begin, long end, long end_subscription,
			String location,
			int min_capacity, int max_capacity) {
		this.sender = sender;
		this.begin = new Date(begin);
		this.end = new Date(end);
		this.end_subcription = new Date(end_subscription);
		this.name = name;
		this.description = description;
		this.min_capacity = min_capacity;
		this.max_capacity = max_capacity;
		this.location = location;
		reciepient = sender;
		type_transaction = "creation";
		isTypeCreation = true;
	}
	
	public Transaction(PublicKey sender, PublicKey reciepient, String id_event,
			List<TransactionInput> inputs) {
		this.sender = sender;
		this.reciepient = reciepient;
		type_transaction = "register";
		this.id_event = id_event;
		this.inputs = inputs;
	}
	
	
	// This Calculates the transaction hash (which will be used as its Id)
	private String calulateHash() {
		sequence++; //increase the sequence to avoid 2 identical transactions having the same hash
		return StringUtil.applySha256(
				StringUtil.getStringFromKey(sender) +
				StringUtil.getStringFromKey(reciepient) +type_transaction +
				name+description+begin+end+end_subcription+
				min_capacity+max_capacity+ sequence
				);
	}
	
	//Signs all the data we dont wish to be tampered with.
	public void generateSignature(PrivateKey privateKey) {
		String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient)+type_transaction +  name +
				description + begin + end+end_subcription+
				location+min_capacity+max_capacity;
		signature = StringUtil.applyECDSASig(privateKey,data);		
	}
	//Verifies the data we signed hasnt been tampered with
	public boolean verifiySignature() {
		String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient)+type_transaction   + name +
				description + begin + end+end_subcription+ location
				+min_capacity+max_capacity;
		System.out.println("signature "+signature);
		return StringUtil.verifyECDSASig(sender, data, signature);
	}
	
	public boolean verifySignature() {
		String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient)+type_transaction  + name +
				description + begin + end+end_subcription+
				location+min_capacity+max_capacity;
		System.out.println("signature "+signature);
		return StringUtil.verifyECDSASig(sender, data, signature);
	}	
	
	//Returns true if new transaction could be created.	
	public boolean processTransaction() {
			
			if(verifiySignature() == false) {
				System.out.println("#Transaction Signature failed to verify");
				return false;
			}
					
			//gather transaction inputs (Make sure they are unspent):
			for(TransactionInput i : inputs) {
				i.setUTXO(BlockChain.getUTXOs().get(i.getTransactionOutputId()));
			}

			
			transactionId = calulateHash();
			
			if(type_transaction=="creation"){
			
			outputs.add(new TransactionOutput( this.sender,transactionId,
					name, description, begin.getTime(), end.getTime(), end_subcription.getTime(),
					location, min_capacity, max_capacity));
			
			}
			else{
				if(type_transaction=="register"){
					
					outputs.add(new TransactionOutput(this.sender, this.reciepient, id_event,transactionId));
				}
			}
			//outputs.add(new TransactionOutput( this.sender, leftOver,transactionId)); //send the left over 'change' back to sender		
				
			//add outputs to Unspent list
			for(TransactionOutput o : outputs) {
				BlockChain.getUTXOs().put(o.getId() , o);
			}
			
			//remove transaction inputs from UTXO lists as spent:
			for(TransactionInput i : inputs) {
		
				if(i.getUTXO() == null) continue; //if Transaction can't be found skip it 
				BlockChain.getUTXOs().remove(i.getUTXO().getId());
			}
			
			return true;
		}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getBegin() {
		return begin;
	}

	public void setBegin(Date begin) {
		this.begin = begin;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public Date getEnd_subcription() {
		return end_subcription;
	}

	public void setEnd_subcription(Date end_subcription) {
		this.end_subcription = end_subcription;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getMin_capacity() {
		return min_capacity;
	}

	public void setMin_capacity(int min_capacity) {
		this.min_capacity = min_capacity;
	}

	public int getMax_capacity() {
		return max_capacity;
	}

	public void setMax_capacity(int max_capacity) {
		this.max_capacity = max_capacity;
	}
	
	public boolean isTypeCreation(){
		return isTypeCreation;
	}
		
	//returns sum of inputs(UTXOs) values
		/*public float getInputsValue() {
			float total = 0;
			for(TransactionInput i : inputs) {
				if(i.getUTXO() == null) continue; //if Transaction can't be found skip it 
				total += i.getUTXO().getValue();
			}
			return total;
		}*/

	//returns sum of outputs:
		/*public float getOutputsValue() {
			float total = 0;
			for(TransactionOutput o : outputs) {
				total += o.getValue();
			}
			return total;
		}*/
		
		
	
}
