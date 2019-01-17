package test;

import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

import utils.StringUtil;

public class TestHMAC {
	
    @Test
    public void test_valid() {
    	try {
    		byte[] data = TestUtils.readFile("tests/test_crypto/hmac/valid/data");
    		byte[] secret = TestUtils.readFile("tests/test_crypto/hmac/valid/secret");
        	byte[] hmac_hex = TestUtils.readFile("tests/test_crypto/hmac/valid/hmac_hex");
        	
        	String dataString = new String(data, "UTF-8");
        	String keyString = new String(secret, "UTF-8");
        	String hmac_res = StringUtil.hmacDigest(dataString, keyString);
        
			assertTrue(hmac_res.equals(new String(hmac_hex, "UTF-8")));
		} catch (UnsupportedEncodingException e) {
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
