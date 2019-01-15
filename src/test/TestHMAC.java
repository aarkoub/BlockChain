package test;

import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.Test;

import utils.StringUtil;

public class TestHMAC {
	
    @Test
    public void test_0() {
    	try {
    		byte[] input = TestUtils.readFile("tests/test_crypto/hex/test_0/data");
        	byte[] output = TestUtils.readFile("tests/test_crypto/hex/test_0/hex");
			assertTrue(StringUtil.applyHex(input).equals(new String(output, "UTF-8")));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    }
    
    @Test
    public void test_1() {
    	try {
    		byte[] input = TestUtils.readFile("tests/test_crypto/hex/test_1/data");
        	byte[] output = TestUtils.readFile("tests/test_crypto/hex/test_1/hex");
			assertTrue(StringUtil.applyHex(input).equals(new String(output, "UTF-8")));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    }
    
    @Test
    public void test_2() {
    	try {
    		byte[] input = TestUtils.readFile("tests/test_crypto/hex/test_2/data");
        	byte[] output = TestUtils.readFile("tests/test_crypto/hex/test_2/hex");
			assertTrue(StringUtil.applyHex(input).equals(new String(output, "UTF-8")));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    }
    
    
}
