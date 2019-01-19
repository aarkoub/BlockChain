package test;

import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

import main.Block;
import main.Transaction;

public class TestMerkle {
	
	@Test
	public void testMerkle(){
		
		byte[] root = TestUtils.readFile("tests/test_crypto/merkle/empty/root");
		byte[] leaf_0 = TestUtils.readFile("tests/test_crypto/merkle/empty/leaf_0");
		
		Block block = new Block(Hex.toHexString(root));
		//block.addTransaction(new Transaction(sender, reciepient, id_event, inputs))
	}
	
}
