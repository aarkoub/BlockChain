package merkletree;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

import javax.crypto.spec.SecretKeySpec;

import merkletree.hmac.HmacSha1Signature;

public class Node {
	
	private Node filsGauche, filsDroit;
	private byte[] hashData;
	private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
	private static String KEY="FindAGoodKey";
	
	
	public Node(Node filsGauche, Node filsDroit){
		
		if(filsGauche!=null && filsDroit!=null){
		
			this.filsGauche = filsGauche;
			this.filsDroit = filsDroit;
			String data = "1"+filsGauche.getHashData()+filsDroit.getHashData();
			setHashData(data);
			
		}
		
	}
	
	public byte[] getHashDataBytes(){
		return hashData;
	}
	
	public String getHashData(){
		return String.valueOf(hashData);
	}
	
	public void setHashData(byte[] data){
		try {
			hashData = HmacSha1Signature.calculateRFC2104HMAC(String.valueOf(data), KEY);
		} catch (InvalidKeyException | SignatureException | NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setHashData(String data){
		try {
			hashData = HmacSha1Signature.calculateRFC2104HMAC(data, KEY);
		} catch (InvalidKeyException | SignatureException | NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
