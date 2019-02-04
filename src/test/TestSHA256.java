package test;

import static org.junit.Assert.assertTrue;


import org.junit.Test;

import utils.StringUtil;

public class TestSHA256 {
	
    @Test
    public void test_hex_data() {
    	byte[] input = TestUtils.readFile("tests/test_crypto/sha256/data");
        byte[] output = TestUtils.readFile("tests/test_crypto/sha256/data_hex");
		assertTrue(StringUtil.toHex(input).equals(new String(output)));
    }
    
    @Test
    public void test_sha_hex() {
    	byte[] input = TestUtils.readFile("tests/test_crypto/sha256/sha256");
        byte[] output = TestUtils.readFile("tests/test_crypto/sha256/sha256_hex");
		assertTrue(StringUtil.toHex(input).equals(new String(output)));
    }
    
    @Test
    public void test_sha(){

		String data = new String(TestUtils.readFile("tests/test_crypto/sha256/data"));
		String sha256_hex = new String(TestUtils.readFile("tests/test_crypto/sha256/sha256_hex"));
		String sha256_res = StringUtil.applySha256(data);
		assertTrue(sha256_hex.equals(sha256_res));
    }
    
}
