package main;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import merkletree.MerkleTree;
import utils.StringUtil;

public class Block {
	
	private String hash;
	private String previousHash; 
	private String merkleRoot;
	private List<Transaction> transactions = new ArrayList<Transaction>(); 
	private long timeStamp;
	private int level;	

	public Block(String previousHash, List<Transaction> transactions ) {
		this.previousHash = previousHash;
		this.timeStamp = new Date().getTime();
		this.transactions = transactions;
		this.level = 0;
		this.hash = calculateHash(); 
		List<String> hashes = new ArrayList<>();
		if(transactions!=null){
			for(Transaction t : transactions){
				hashes.add(t.getTransactionId());
			}
		}
		else{
			hashes.add("");
		}
		
		merkleRoot = MerkleTree.getMerkleTreeRoot(hashes);
		hash = calculateHash();
		
	
	}

	public String calculateHash() {
		String calculatedhash = StringUtil.applySha256( 
				previousHash +
				Long.toString(timeStamp) +
				merkleRoot
				);
		return calculatedhash;
	}

	public String getHash() {
		return hash;
	}

	
	public String getPreviousHash() {
		return previousHash;
	}
	

	public String getMerkleRoot() {
		return merkleRoot;
	}

	

	public List<Transaction> getTransactions() {
		return transactions;
	}


	public long getTimeStamp() {
		return timeStamp;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}