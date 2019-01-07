package main;

import java.security.PublicKey;
import java.util.Date;

import utils.StringUtil;

public class TransactionCreationOutput {
	
	private String id;
	private PublicKey reciepient; //also known as the new owner of these coins.
	private Date begin, end, end_subcription;
	private String name, description;
	private String parentTransactionId; //the id of the transaction this output was created in
	private String location;
	private int max_capacity, min_capacity;
	
	//Constructor
	public TransactionCreationOutput(PublicKey reciepient, String parentTransactionId,
			String name, String description,
			long begin, long end, long end_subcription,
			String location, int min_capacity, int max_capacity) {
		this.reciepient = reciepient;
		this.name = name;
		this.description = description;
		this.begin = new Date(begin);
		this.end = new Date(end);
		this.end_subcription = new Date(end_subcription);
		this.parentTransactionId = parentTransactionId;
		this.min_capacity = min_capacity;
		this.max_capacity = max_capacity;
		this.location = location;
		this.id = StringUtil.applySha256(StringUtil.getStringFromKey(reciepient)+
				name + description + begin + end + end_subcription + location+
				min_capacity + min_capacity
		+parentTransactionId);
	}
	
	//Check if coin belongs to you
	public boolean isMine(PublicKey publicKey) {
		return (publicKey == reciepient);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public PublicKey getReciepient() {
		return reciepient;
	}

	public void setReciepient(PublicKey reciepient) {
		this.reciepient = reciepient;
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

	public String getParentTransactionId() {
		return parentTransactionId;
	}

	public void setParentTransactionId(String parentTransactionId) {
		this.parentTransactionId = parentTransactionId;
	}
	
	public String getLocation(){
		return location;
	}
	
	public void setLocation(String location){
		this.location = location;
	}
	
	public int getMaxCapacity(){
		return max_capacity;
	}
	
	public void setMaxCapacity(int max_capacity){
		this.max_capacity = max_capacity;
	}
	
	public int getMinCapacity(){
		return min_capacity;
	}
	
	public void setMinCapacity(int min_capacity){
		this.min_capacity = min_capacity;
	}
}
