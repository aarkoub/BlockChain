package test;

import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import org.bouncycastle.asn1.eac.ECDSAPublicKey;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

import utils.StringUtil;

public class TestSECP256R1 {
	
	@Test
    public void test_valid() {
    	try {
    		byte[] data = TestUtils.readFile("tests/test_crypto/SECP256R1/data");
    		byte[] secret_key = TestUtils.readFile("tests/test_crypto/SECP256R1/secret_key");
        	byte[] hmac_hex = TestUtils.readFile("tests/test_crypto/hmac/valid/hmac_hex");
        	byte[] public_key_compressed =  TestUtils.readFile("tests/test_crypto/SECP256R1/public_key_uncompressed_hex");
        	byte[] valid_signature = TestUtils.readFile("tests/test_crypto/SECP256R1/valid_signature");
        	
        	String dataString = new String(data, "UTF-8");
        	String keyString = new String(secret_key, "UTF-8");
        	String publicKeyString = new String(public_key_compressed, "UTF-8");
        	System.out.println(publicKeyString);
        	X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Hex.decode(public_key_compressed));
        	try {
				KeyFactory keyFactory = KeyFactory.getInstance("EC");
				try {
					PublicKey pubKey = keyFactory.generatePublic(keySpec);
				} catch (InvalidKeySpecException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        
        	
        	
        	//System.out.println(StringUtil.verifyECDSASig(pubKey, dataString, valid_signature));
        	
        
		} catch (UnsupportedEncodingException /*| InvalidKeySpecException | NoSuchAlgorithmException */e) {
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
