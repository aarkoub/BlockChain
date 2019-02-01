package test;

import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.util.encoders.Base64;
import org.junit.Test;

import utils.StringUtil;

public class TestHMAC {
	
    @Test
    public void test_valid() throws UnsupportedEncodingException {
		byte[] data = TestUtils.readFile("tests/test_crypto/hmac/valid/data");
		byte[] secret = TestUtils.readFile("tests/test_crypto/hmac/valid/secret");
    	byte[] hmac_hex = TestUtils.readFile("tests/test_crypto/hmac/valid/hmac_hex");
    	
    	String dataString = new String(data, "UTF-8");
    	String keyString = new String(secret, "UTF-8");
    	String hmac_res = StringUtil.hmacDigest(dataString, keyString);
    
		assertTrue(hmac_res.equals(new String(hmac_hex, "UTF-8")));

    }
    
    @Test
    public void test_valid_2() throws UnsupportedEncodingException{
    	
    	byte[] data = TestUtils.readFile("tests/test_crypto/hmac/valid_2/data");
		byte[] secret = TestUtils.readFile("tests/test_crypto/hmac/valid_2/secret");
    	byte[] hmac_hex = TestUtils.readFile("tests/test_crypto/hmac/valid_2/hmac_hex");
    	
    	String dataString = new String(data, "UTF-8");
    	String keyString = new String(secret, "UTF-8");
    	String hmac_res = StringUtil.hmacDigest(dataString, keyString);
  
    	System.out.println(hmac_res);
    	System.out.println(new String(hmac_hex, "UTF-8"));
	    assertTrue(hmac_res.equals(new String(hmac_hex, "UTF-8")));
    	
    }
    
    @Test
    public void test_invalid() throws UnsupportedEncodingException {

    		byte[] data = TestUtils.readFile("tests/test_crypto/hmac/invalid/data");
    		byte[] secret = TestUtils.readFile("tests/test_crypto/hmac/invalid/secret");
        	byte[] hmac_hex = TestUtils.readFile("tests/test_crypto/hmac/invalid/hmac_hex");
        	
        	String dataString = new String(data, "UTF-8");
        	String keyString = new String(secret, "UTF-8");
        	String hmac_res = StringUtil.hmacDigest(dataString, keyString);
			assertTrue(!hmac_res.equals(new String(hmac_hex, "UTF-8")));

    }	
    


    
}
