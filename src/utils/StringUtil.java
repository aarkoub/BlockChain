package utils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.ECPrivateKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Formatter;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.spec.ECNamedCurveSpec;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPObjectFactory;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPUtil;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPKeyConverter;
import org.bouncycastle.util.encoders.Hex;

import com.google.gson.GsonBuilder;

import main.Transaction;

public class StringUtil {
	
	//Applies Sha256 to a string and returns the result. 
	public static String applySha256(String input){		
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");	        
			//Applies sha256 to our input, 
			byte[] hash = digest.digest(input.getBytes("UTF-8"));	        
			StringBuffer hexString = new StringBuffer(); // This will contain hash as hexidecimal
			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if(hex.length() == 1) hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	//Applies ECDSA Signature and returns the result ( as bytes ).
	public static byte[] applyECDSASig(PrivateKey privateKey, String input) {
		Signature dsa;
		byte[] output = new byte[0];
		try {
			dsa = Signature.getInstance("SHA256withECDSA", "BC");
			dsa.initSign(privateKey);
			byte[] strByte = input.getBytes();
			dsa.update(strByte);
			byte[] realSig = dsa.sign();
			output = realSig;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return output;
	}
	
	//Convert string to hex
	public static String toHex(byte[] bytes) {
		Formatter formatter = new Formatter();
		for (byte b : bytes) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		
		return result;
	}
		
	//Verifies a String signature 
	public static boolean verifyECDSASig(PublicKey publicKey, String data, byte[] signature) {
		try {
			Signature ecdsaVerify = Signature.getInstance("SHA256withECDSA", "BC");
			ecdsaVerify.initVerify(publicKey);
			ecdsaVerify.update(data.getBytes());
			return ecdsaVerify.verify(signature);
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	//Short hand helper to turn Object into a json string
	public static String getJson(Object o) {
		return new GsonBuilder().setPrettyPrinting().create().toJson(o);
	}
	
	//Returns difficulty string target, to compare to hash. eg difficulty of 5 will return "00000"  
	public static String getDificultyString(int difficulty) {
		return new String(new char[difficulty]).replace('\0', '0');
	}
	
	public static String getStringFromKey(Key key) {
		return Base64.getEncoder().encodeToString(key.getEncoded());
	}
	
	public static String getMerkleRoot(ArrayList<Transaction> transactions) {
		int count = transactions.size();
		
		List<String> previousTreeLayer = new ArrayList<String>();
		for(Transaction transaction : transactions) {
			previousTreeLayer.add(transaction.getTransactionId());
		}
		List<String> treeLayer = previousTreeLayer;
		
		while(count > 1) {
			treeLayer = new ArrayList<String>();
			for(int i=1; i < previousTreeLayer.size(); i+=2) {
				treeLayer.add(applySha256(previousTreeLayer.get(i-1) + previousTreeLayer.get(i)));
			}
			count = treeLayer.size();
			previousTreeLayer = treeLayer;
		}
		
		String merkleRoot = (treeLayer.size() == 1) ? treeLayer.get(0) : "";
		return merkleRoot;
	}
	
	 public static byte[] hmacDigest(byte[] msg, byte[] keyString) {
		   		    
		    String algo = "HmacSHA256";
		    
		    
		    try {
		      SecretKeySpec key = new SecretKeySpec(keyString, algo);
		      Mac mac = Mac.getInstance(algo);
		      mac.init(key);

		      byte[] bytes = mac.doFinal(msg);

		      return bytes;
		    } catch (InvalidKeyException e) {
		    } catch (NoSuchAlgorithmException e) {
		    }
		    return null;
		  }
	 
	 public static PublicKey getPublicKey(byte[] publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException, IOException, PGPException{
		 java.security.Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			
	        KeyFactory fact = KeyFactory.getInstance("ECDSA", "BC");
	
	        ECParameterSpec spec = ECNamedCurveTable.getParameterSpec("secp256r1");
	        ECCurve curve = spec.getCurve();

	        ECPublicKeySpec pubKey =
	            new ECPublicKeySpec(
	                                curve.decodePoint(publicKey),
	                                spec);

	        return fact.generatePublic(pubKey);
	 }
	 
	 public static PrivateKey getPrivateKey(byte[] secret_key_hex) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException, UnsupportedEncodingException{
		
		 java.security.Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			
	        KeyFactory fact = KeyFactory.getInstance("ECDSA", "BC");
	
	        ECParameterSpec spec = ECNamedCurveTable.getParameterSpec("secp256r1");

	        org.bouncycastle.jce.spec.ECPrivateKeySpec priKey =
		            new org.bouncycastle.jce.spec.ECPrivateKeySpec(new BigInteger(new String(secret_key_hex, "UTF-8"), 16),
		                                 spec);
		 
		 return fact.generatePrivate(priKey);
		
	 }
	 

	 public static int bigint_encoding_size(byte high_byte) {
        if (high_byte >= 0) {
            return 32;
        } else {
            return 33;
        }
    }

    public static byte[] asn1_encode(byte [] sign) {
        int len_r = bigint_encoding_size(sign[0]);
        int len_s = bigint_encoding_size(sign[0]);
        int len = 6 + len_r + len_s;
        byte [] res = new byte[len];
        res[0] = 0x30;
        res[1] = (byte) (len - 2); // less than 127
        res[2] = 0x02;
        res[3] = (byte) len_r;
        res[4 + len_r] = 0x02;
        res[5 + len_r] = (byte) len_s;
        if (len_r == 33) {
            System.arraycopy(sign, 0, res, 5, 32);
            res[4] = 0x00;
        } else {
            System.arraycopy(sign, 0, res, 4, 32);
        }
        if (len_s == 33) {
            System.arraycopy(sign, 32, res, 7 + len_r, 32);
            res[6 + len_r] = 0x00;
        } else {
            System.arraycopy(sign, 32, res, 6 + len_r, 32);
        }
        return res;
    }

    public static byte[] asn1_decode(byte [] sign) {
        byte [] res = new byte[64];
        int len_r = sign[3];
        if(len_r == 32) {
            System.arraycopy(sign, 4, res, 0, 32);
        } else {
            System.arraycopy(sign, 5, res, 0, 32);
        }
        if(sign[len_r + 5] == 32) {
            System.arraycopy(sign, len_r + 6, res, 32, 32);
        } else {
            System.arraycopy(sign, len_r + 7, res, 32, 32);
        }
        return res;
    }
}