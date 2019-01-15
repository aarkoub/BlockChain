package test;

import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.Test;

import utils.StringUtil;

public class TestSHA256 {
	
    @Test
    public void test_0() {
    	try {
    		byte[] input = TestUtils.readFile("tests/test_crypto/sha256/data");
        	byte[] output = TestUtils.readFile("tests/test_crypto/sha256/data_hex");
			assertTrue(StringUtil.applyHex(input).equals(new String(output, "UTF-8")));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    }
    
    @Test
    public void test_1() {
    	try {
    		byte[] input = TestUtils.readFile("tests/test_crypto/sha256/sha256");
        	byte[] output = TestUtils.readFile("tests/test_crypto/sha256/sha256_hex");
			assertTrue(StringUtil.applyHex(input).equals(new String(output, "UTF-8")));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    }
}
