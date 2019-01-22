package test;

import static org.junit.Assert.assertTrue;

import java.security.Security;

import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

import main.Block;
import main.Personne;
import main.Transaction;
import main.TransactionInput;

public class TestMerkle {
	
	@Test
	public void testMerkleEmpty(){
		
		byte[] root = TestUtils.readFile("tests/test_crypto/merkle/empty/root");
		byte[] root_hex = TestUtils.readFile("tests/test_crypto/merkle/empty/root_hex");
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		Personne alice = new Personne();
		Personne bob = new Personne();
		
		
		String root_hex_string = new String(root_hex);
		
		Block block = new Block("0");
	
		
		block.addTransaction(new Transaction(alice.getPublicKey(), bob.getPublicKey(),
				"0", null));
		
		block.mineBlock(0);
		assertTrue(block.getMerkleRoot().equals(root_hex_string));
	}
	
	@Test
	public void leaf_1(){
		byte[] root = TestUtils.readFile("tests/test_crypto/merkle/empty/root");
		byte[] root_hex = TestUtils.readFile("tests/test_crypto/merkle/empty/root_hex");
		byte[] data = TestUtils.readFile("tests/test_crypto/merkle/empty/data_00");
		byte[] data_hex = TestUtils.readFile("tests/test_crypto/merkle/empty/data_00");
		
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		Personne alice = new Personne();
		Personne bob = new Personne();
		
		
		Block block = new Block("0");
	
		
		block.addTransaction(new Transaction(alice.getPublicKey(), bob.getPublicKey(),
				"0", null));
		
		
	}
	
}
