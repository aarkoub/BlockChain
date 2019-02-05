package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

import utils.StringUtil;

public class TestSECP256R1 {
	
	@Test
    public void test_valid() {
    	try {
    		
    		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    		byte[] data = TestUtils.readFile("tests/test_crypto/SECP256R1/data");
    		byte[] secret_key = TestUtils.readFile("tests/test_crypto/SECP256R1/secret_key_hex");
        	byte[] public_key_uncompressed =  TestUtils.readFile("tests/test_crypto/SECP256R1/public_key_uncompressed");
        	byte[] valid_signature = TestUtils.readFile("tests/test_crypto/SECP256R1/valid_signature");
  
			PrivateKey privateKey = StringUtil.getPrivateKey(secret_key);
			PublicKey publicKey = StringUtil.getPublicKey(public_key_uncompressed);
			
			byte[] signature = StringUtil.applyECDSASig(privateKey, new String(data, "UTF-8"));

	        byte sigBytes[] = StringUtil.asn1_encode(valid_signature);
	        
	       assertTrue(StringUtil.verifyECDSASig(publicKey, new String(data, "UTF-8"), sigBytes));
        
		} catch ( NoSuchProviderException | IOException | NoSuchAlgorithmException | InvalidKeySpecException | PGPException e ) {
			e.printStackTrace();
		}
    }
	
	@Test
    public void test_valid2(){
		try {
			java.security.Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	
	        KeyFactory fact = KeyFactory.getInstance("ECDSA", "BC");
	
	        ECParameterSpec spec = ECNamedCurveTable.getParameterSpec("secp256r1");
	        ECCurve curve = spec.getCurve();
	
	        // Imorter des clefs signature
	        org.bouncycastle.jce.spec.ECPrivateKeySpec priKey =
	            new org.bouncycastle.jce.spec.ECPrivateKeySpec(new BigInteger("661296e095da29349f26ca9e7eac10d1056729cbe9f85f9af92380ce496f8415", 16),
	                                 spec);
	
	        ECPublicKeySpec pubKey =
	            new ECPublicKeySpec(
	                                curve.decodePoint(Hex.decode("029e15edf9abdbf2bbcbedad647c881ca6d0a068552f8dc459c4fef3439254103e")),
	                                spec);
	        PrivateKey          sKey = fact.generatePrivate(priKey);
	        PublicKey           vKey = fact.generatePublic(pubKey);
	
	        Signature sig = Signature.getInstance("SHA256withECDSA", "BC");
	
	        byte msg[] = Hex.decode("736f6d652064617461");
	
	        // Imorter une signature raw
	        byte rawSig[] = Hex.decode("5264964a82b0175b38abf71f396ac0cc5e883845051b5401f98e4448ca4f71ab272d97bd1880e0cdb2021e9444c5ac34c3a93f9d5d93c57394e39ee9a6205bdb");
	        byte sigBytes[] = StringUtil.asn1_encode(rawSig);
	
	        // Vérifier une signature
	        sig.initVerify(vKey);
	        sig.update(msg);
	        if (!sig.verify(sigBytes))
	        {
	            System.out.println("Fail");
	        } else {
	            System.out.println("Good");
	        }
	
	        // Générer une signature
	        sig.initSign(sKey);
	        sig.update(msg);
	        byte[] signed = sig.sign();
	        byte raw_signed[] = StringUtil.asn1_decode(signed);
	        System.out.println("sign " + new String (Hex.encode(raw_signed)));
	
	        // Revérifier une signature
	        sig.initVerify(vKey);
	        sig.update(msg);
	        if (!sig.verify(signed))
	        {
	            System.out.println("Fail 2");
	        } else {
	            System.out.println("Good 2");
	        }
	
	        System.out.println("That's all falks");
		}catch (InvalidKeySpecException| NoSuchAlgorithmException| NoSuchProviderException| InvalidKeyException| SignatureException e) {
			e.printStackTrace();
		}
	}

}
