package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

import utils.StringUtil;

public class TestHMAC {
	
    @Test
    public void test_valid() throws UnsupportedEncodingException {
		byte[] data = TestUtils.readFile("tests/test_crypto/hmac/valid/data");
		byte[] secret = TestUtils.readFile("tests/test_crypto/hmac/valid/secret");
    	byte[] hmac_hex = TestUtils.readFile("tests/test_crypto/hmac/valid/hmac_hex");

    	byte[] hmac_res = StringUtil.hmacDigest(data, secret);
    
    	assertEquals(new String(hmac_hex), StringUtil.toHex(hmac_res));

    }
    
    @Test
    public void test_valid_2() throws UnsupportedEncodingException{
    	
    	byte[] data = TestUtils.readFile("tests/test_crypto/hmac/valid_2/data");
		byte[] secret = TestUtils.readFile("tests/test_crypto/hmac/valid_2/secret");
    	byte[] hmac_hex = TestUtils.readFile("tests/test_crypto/hmac/valid_2/hmac_hex");

    	byte[] hmac_res = StringUtil.hmacDigest(data, secret);

    	
    	assertEquals(new String(hmac_hex), StringUtil.toHex(hmac_res));

    	
    }
    
   @Test
    public void test_invalid() throws UnsupportedEncodingException {

    		byte[] data = TestUtils.readFile("tests/test_crypto/hmac/invalid/data");
    		byte[] secret = TestUtils.readFile("tests/test_crypto/hmac/invalid/secret");
        	byte[] hmac_hex = TestUtils.readFile("tests/test_crypto/hmac/invalid/hmac_hex");
        	
        	byte[] hmac_res = StringUtil.hmacDigest(data, secret);
        	assertNotEquals(new String(hmac_hex), StringUtil.toHex(hmac_res));

    }	
    


    
}
