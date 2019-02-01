package test;



import static org.junit.Assert.*;


import org.junit.Test;

import utils.StringUtil;

public class TestCryptoHex {
	
    @Test
    public void test_0() {
    	byte[] input = TestUtils.readFile("tests/test_crypto/hex/test_0/data");
        byte[] output = TestUtils.readFile("tests/test_crypto/hex/test_0/hex");
		assertTrue(StringUtil.toHex(input).equals(new String(output)));
	
    }
    
    @Test
    public void test_1() {
    	byte[] input = TestUtils.readFile("tests/test_crypto/hex/test_1/data");
        byte[] output = TestUtils.readFile("tests/test_crypto/hex/test_1/hex");
		assertTrue(StringUtil.toHex(input).equals(new String(output)));
    }
    
    @Test
    public void test_2() {
    	byte[] input = TestUtils.readFile("tests/test_crypto/hex/test_2/data");
        byte[] output = TestUtils.readFile("tests/test_crypto/hex/test_2/hex");
		assertTrue(StringUtil.toHex(input).equals(new String(output)));

    }

    
}
