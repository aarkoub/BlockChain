package test;

import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.spec.ECPrivateKeySpec;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveGenParameterSpec;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.spec.ECNamedCurveSpec;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

import utils.StringUtil;

public class TestSECP256R1 {
	
	@Test
    public void test_valid() {
    	try {
    		byte[] data = TestUtils.readFile("tests/test_crypto/SECP256R1/data");
    		byte[] secret_key = TestUtils.readFile("tests/test_crypto/SECP256R1/secret_key_hex");
        	byte[] hmac_hex = TestUtils.readFile("tests/test_crypto/hmac/valid/hmac_hex");
        	byte[] public_key_compressed =  TestUtils.readFile("tests/test_crypto/SECP256R1/public_key_uncompressed");
        	byte[] valid_signature = TestUtils.readFile("tests/test_crypto/SECP256R1/valid_signature_hex");
        	
        	
        	String dataString = new String(data);
        	String keyString = new String(secret_key);
        	String publicKeyString = new String(public_key_compressed);
        	
        	//System.out.println(keyString);
        	//System.out.println(publicKeyString);
        	
        	     
			PrivateKey privateKey = StringUtil.getPrivateKey(secret_key);
        	byte[] donnees_signees = StringUtil.applyECDSASig(privateKey, dataString);
        	System.out.println(Hex.toHexString(donnees_signees));
        	System.out.println(new String(valid_signature));
        	PublicKey publicKey = StringUtil.getPublicKey(public_key_compressed);
        	
        	String signTostring = DatatypeConverter.printBase64Binary(valid_signature);
        	signTostring = URLEncoder.encode(signTostring, "UTF-8");
        	
        	System.out.println(StringUtil.verifyECDSASig(publicKey, dataString, signTostring.getBytes()));
        		
        	
      
        
		} catch (NoSuchAlgorithmException | InvalidKeySpecException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    }
    
    @Test
    public void test_invalid() {
    	try {
    		byte[] data = TestUtils.readFile("tests/test_crypto/hmac/invalid/data");
    		byte[] secret = TestUtils.readFile("tests/test_crypto/hmac/invalid/secret");
        	byte[] hmac_hex = TestUtils.readFile("tests/test_crypto/hmac/invalid/hmac_hex");
        	
        	String dataString = new String(data, "UTF-8");
        	String keyString = new String(secret, "UTF-8");
        	String hmac_res = StringUtil.hmacDigest(dataString, keyString);
        
			assertTrue(!hmac_res.equals(new String(hmac_hex, "UTF-8")));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    }

}
