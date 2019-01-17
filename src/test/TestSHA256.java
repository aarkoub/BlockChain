package test;

import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

import utils.StringUtil;

public class TestSHA256 {
	
    @Test
    public void test_hex_data() {
    	try {
    		byte[] input = TestUtils.readFile("tests/test_crypto/sha256/data");
        	byte[] output = TestUtils.readFile("tests/test_crypto/sha256/data_hex");
			assertTrue(StringUtil.applyHex(input).equals(new String(output, "UTF-8")));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    }
    
    @Test
    public void test_sha_hex() {
    	try {
    		byte[] input = TestUtils.readFile("tests/test_crypto/sha256/sha256");
        	byte[] output = TestUtils.readFile("tests/test_crypto/sha256/sha256_hex");
			assertTrue(StringUtil.applyHex(input).equals(new String(output, "UTF-8")));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    }
    
    @Test
    public void test_sha(){
    	
    	try {
			String data = new String(TestUtils.readFile("tests/test_crypto/sha256/data"), "UTF-8");
			String sha256_hex = new String(TestUtils.readFile("tests/test_crypto/sha256/sha256_hex"), "UTF-8");
			
			String sha256_res = StringUtil.applySha256(data);
			assertTrue(sha256_hex.equals(sha256_res));
			
    	} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
}
