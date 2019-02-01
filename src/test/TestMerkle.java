package test;

import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import merkletree.MerkleTree;
import utils.MerkleTreeUtil;
import utils.StringUtil;

public class TestMerkle {
	
	@Test
	public void leaf_0(){
		byte[] root_hex = TestUtils.readFile("tests/test_crypto/merkle/leaf_0/root_hex");
		byte[] data = TestUtils.readFile("tests/test_crypto/merkle/leaf_0/data_00");
		List<String> datastring = new ArrayList<>();
		datastring.add(new String(data, StandardCharsets.UTF_8));
		String data_res = MerkleTree.getMerkleTreeRoot(datastring);
		assertTrue(data_res.equals(new String(root_hex, StandardCharsets.UTF_8)));
	}
	
	@Test
	public void leaf_1(){
		byte[] root_hex = TestUtils.readFile("tests/test_crypto/merkle/leaf_1/root_hex");
		byte[] data = TestUtils.readFile("tests/test_crypto/merkle/leaf_1/data_00");
		List<String> datastring = new ArrayList<>();
		datastring.add(new String(data, StandardCharsets.UTF_8));
		String data_res = MerkleTree.getMerkleTreeRoot(datastring);
		assertTrue(data_res.equals(new String(root_hex, StandardCharsets.UTF_8)));
	}
	
	@Test
	public void leaf_2(){
		byte[] root_hex = TestUtils.readFile("tests/test_crypto/merkle/leaf_2/root_hex");
		byte[] data = TestUtils.readFile("tests/test_crypto/merkle/leaf_2/data_00");
		List<String> datastring = new ArrayList<>();
		datastring.add(new String(data, StandardCharsets.UTF_8));
		String data_res = MerkleTree.getMerkleTreeRoot(datastring);
		assertTrue(data_res.equals(new String(root_hex, StandardCharsets.UTF_8)));
	}
	
	@Test
	public void leaf_3(){
		byte[] root_hex = TestUtils.readFile("tests/test_crypto/merkle/leaf_3/root_hex");
		byte[] data = TestUtils.readFile("tests/test_crypto/merkle/leaf_3/data_00");
		List<String> datastring = new ArrayList<>();
		datastring.add(new String(data, StandardCharsets.UTF_8));
		String data_res = MerkleTree.getMerkleTreeRoot(datastring);
		assertTrue(data_res.equals(new String(root_hex, StandardCharsets.UTF_8)));
	}
	
	
	@Test
	public void node_0(){
		byte[] root_hex = TestUtils.readFile("tests/test_crypto/merkle/node_0/root_hex");
		byte[] data1 = TestUtils.readFile("tests/test_crypto/merkle/node_0/data_00");
		byte[] data2 = TestUtils.readFile("tests/test_crypto/merkle/node_0/data_01");
		List<String> datastring = new ArrayList<>();
		datastring.add(new String(data1, StandardCharsets.UTF_8));
		datastring.add(new String(data2, StandardCharsets.UTF_8));
		String data_res = MerkleTree.getMerkleTreeRoot(datastring);
		assertTrue(data_res.equals(new String(root_hex, StandardCharsets.UTF_8)));
	}
}
