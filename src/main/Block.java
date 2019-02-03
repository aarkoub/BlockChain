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
	private int nonce;
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
				Integer.toString(nonce) + 
				merkleRoot
				);
		return calculatedhash;
	}
	
	//Increases nonce value until hash target is reached.
	public void mineBlock(int difficulty) {
		String target = StringUtil.getDificultyString(difficulty); 
		while(!hash.substring( 0, difficulty).equals(target)) {
			nonce ++;
			hash = calculateHash();
		}
		System.out.println("Block Mined!!! : " + hash);
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