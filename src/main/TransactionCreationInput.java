package main;

public class TransactionCreationInput {
	
	private String transactionOutputId; //Reference to TransactionOutputs -> transactionId
	private TransactionCreationOutput UTXO; //Contains the Unspent transaction output
	
	public TransactionCreationInput(String transactionOutputId) {
		this.transactionOutputId = transactionOutputId;
	}
	
	public String getTransactionOutputId(){
		return transactionOutputId;
	}
	
	public TransactionCreationOutput getUTXO(){
		return UTXO;
	}
	
	public void setUTXO(TransactionCreationOutput utxo){
		this.UTXO = utxo;
	}
	
}
