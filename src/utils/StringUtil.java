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
import java.security.Security;
import java.security.Signature;
import java.security.spec.ECPrivateKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Formatter;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.EphemeralKeyPair;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.spec.ECNamedCurveSpec;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPObjectFactory;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPUtil;
import org.bouncycastle.openpgp.operator.jcajce.JcaKeyFingerprintCalculator;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPKeyConverter;

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
			dsa = Signature.getInstance("ECDSA", "BC");
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
	public static String applyHex(byte[] bytes) {
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
			Signature ecdsaVerify = Signature.getInstance("SHA256withECDSA",  new BouncyCastleProvider());
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
	
	 public static String hmacDigest(String msg, String keyString) {
		    String digest = null;
		    
		    String algo = "HmacSHA256";
		    
		    
		    try {
		      SecretKeySpec key = new SecretKeySpec((keyString).getBytes("UTF-8"), algo);
		      Mac mac = Mac.getInstance(algo);
		      mac.init(key);

		      byte[] bytes = mac.doFinal(msg.getBytes("UTF-8"));

		      StringBuffer hash = new StringBuffer();
		      for (int i = 0; i < bytes.length; i++) {
		        String hex = Integer.toHexString(0xFF & bytes[i]);
		        if (hex.length() == 1) {
		          hash.append('0');
		        }
		        hash.append(hex);
		      }
		      digest = hash.toString();
		    } catch (UnsupportedEncodingException e) {
		    } catch (InvalidKeyException e) {
		    } catch (NoSuchAlgorithmException e) {
		    }
		    return digest;
		  }
	 
	 public static PublicKey getPublicKey(byte[] publicKeyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException, IOException, PGPException{
         InputStream pgpIn = PGPUtil.getDecoderStream(new ByteArrayInputStream(publicKeyBytes));
         PGPObjectFactory pgpFact = new PGPObjectFactory(pgpIn);
         PGPPublicKeyRing pgpSecRing = (PGPPublicKeyRing) pgpFact.nextObject();
         PGPPublicKey publicKey = pgpSecRing.getPublicKey();
         JcaPGPKeyConverter converter = new JcaPGPKeyConverter();
         Provider bcProvider = new BouncyCastleProvider();
         converter.setProvider(bcProvider);
         return converter.getPublicKey(publicKey);
	 }
	 
	 public static PrivateKey getPrivateKey(byte[] secret_key) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException{
		
		
        ECNamedCurveParameterSpec spec = ECNamedCurveTable.getParameterSpec("secp256r1");
     	KeyFactory key_factory = KeyFactory.getInstance("ECDSA", "BC");
     	ECNamedCurveSpec parameters = new ECNamedCurveSpec("secp256r1", spec.getCurve(), spec.getG(),
     			spec.getN());
     	ECPrivateKeySpec privateKeySpec = new ECPrivateKeySpec(new BigInteger(secret_key), parameters);
			return key_factory.generatePrivate(privateKeySpec);
	 }
	 
	 public static PublicKey getPublicKeyFromPrivateKey(PrivateKey privateKey){
		 KeyFactory keyFactory;
		try {
			keyFactory = KeyFactory.getInstance("ECDSA", "BC");
			 ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp256r1");

			    ECPoint Q = ecSpec.getG().multiply(((org.bouncycastle.jce.interfaces.ECPrivateKey) privateKey).getD());

			    ECPublicKeySpec pubSpec = new ECPublicKeySpec(Q, ecSpec);
			    PublicKey publicKeyGenerated = keyFactory.generatePublic(pubSpec);
			    return publicKeyGenerated;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   return null;
	 }
	 
}