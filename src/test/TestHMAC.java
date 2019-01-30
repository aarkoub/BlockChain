package test;

import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

import utils.StringUtil;

public class TestHMAC {
	
    @Test
    public void test_valid() {
		byte[] data = TestUtils.readFile("tests/test_crypto/hmac/valid/data");
		byte[] secret = TestUtils.readFile("tests/test_crypto/hmac/valid/secret");
    	byte[] hmac_hex = TestUtils.readFile("tests/test_crypto/hmac/valid/hmac_hex");
    	
    	String dataString = new String(data);
    	String keyString = new String(secret);
    	String hmac_res = StringUtil.hmacDigest(dataString, keyString);
    
		assertTrue(hmac_res.equals(new String(hmac_hex)));

    }
    
    @Test
    public void test_valid_2(){
    	
    	byte[] data = TestUtils.readFile("tests/test_crypto/hmac/valid_2/data");
		byte[] secret = TestUtils.readFile("tests/test_crypto/hmac/valid_2/secret");
    	byte[] hmac_hex = TestUtils.readFile("tests/test_crypto/hmac/valid_2/hmac_hex");
    	
    	String dataString = new String(data);
    	String keyString = new String(secret);
    	String hmac_res = StringUtil.hmacDigest(dataString, keyString);
   
    	System.out.println(StringUtil.applyHex(hmac_res.getBytes()));
	    System.out.println(new String(hmac_hex));
			
	    assertTrue(hmac_res.equals(new String(hmac_hex)));
    	
    }
    
    @Test
    public void test_invalid() {

    		byte[] data = TestUtils.readFile("tests/test_crypto/hmac/invalid/data");
    		byte[] secret = TestUtils.readFile("tests/test_crypto/hmac/invalid/secret");
        	byte[] hmac_hex = TestUtils.readFile("tests/test_crypto/hmac/invalid/hmac_hex");
        	
        	String dataString = new String(data);
        	String keyString = new String(secret);
        	String hmac_res = StringUtil.hmacDigest(dataString, keyString);
        
			assertTrue(!hmac_res.equals(new String(hmac_hex)));

    }
    
}
