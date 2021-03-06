package test;

import static org.junit.Assert.assertTrue;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import merkletree.MerkleTree;

public class TestMerkle {
	
	@Test
	public void leaf_0(){
		byte[] root = TestUtils.readFile("tests/test_crypto/merkle/leaf_0/root");
		byte[] data = TestUtils.readFile("tests/test_crypto/merkle/leaf_0/data_00");
		List<String> datastring = new ArrayList<>();
		datastring.add(new String(data, StandardCharsets.UTF_8));
		String data_res = MerkleTree.getMerkleTreeRoot(datastring);
		assertTrue(data_res.equals(new String(root, StandardCharsets.UTF_8)));
	}
	
	@Test
	public void leaf_1(){
		byte[] root = TestUtils.readFile("tests/test_crypto/merkle/leaf_1/root");
		byte[] data = TestUtils.readFile("tests/test_crypto/merkle/leaf_1/data_00");
		List<String> datastring = new ArrayList<>();
		datastring.add(new String(data, StandardCharsets.UTF_8));
		String data_res = MerkleTree.getMerkleTreeRoot(datastring);
		assertTrue(data_res.equals(new String(root, StandardCharsets.UTF_8)));
	}
	
	@Test
	public void leaf_2(){
		byte[] root = TestUtils.readFile("tests/test_crypto/merkle/leaf_2/root");
		byte[] data = TestUtils.readFile("tests/test_crypto/merkle/leaf_2/data_00");
		List<String> datastring = new ArrayList<>();
		datastring.add(new String(data, StandardCharsets.UTF_8));
		String data_res = MerkleTree.getMerkleTreeRoot(datastring);
		assertTrue(data_res.equals(new String(root, StandardCharsets.UTF_8)));
	}
	
	@Test
	public void leaf_3(){
		byte[] root = TestUtils.readFile("tests/test_crypto/merkle/leaf_3/root");
		byte[] data = TestUtils.readFile("tests/test_crypto/merkle/leaf_3/data_00");
		List<String> datastring = new ArrayList<>();
		datastring.add(new String(data, StandardCharsets.UTF_8));
		String data_res = MerkleTree.getMerkleTreeRoot(datastring);
		assertTrue(data_res.equals(new String(root, StandardCharsets.UTF_8)));
	}
	
	
	@Test
	public void node_0(){
		byte[] root = TestUtils.readFile("tests/test_crypto/merkle/node_0/root");
		byte[] data1 = TestUtils.readFile("tests/test_crypto/merkle/node_0/data_00");
		byte[] data2 = TestUtils.readFile("tests/test_crypto/merkle/node_0/data_01");
		List<String> datastring = new ArrayList<>();
		datastring.add(new String(data1, StandardCharsets.UTF_8));
		datastring.add(new String(data2, StandardCharsets.UTF_8));
		String data_res = MerkleTree.getMerkleTreeRoot(datastring);
		assertTrue(data_res.equals(new String(root, StandardCharsets.UTF_8)));
	}
}
