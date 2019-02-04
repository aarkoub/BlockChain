package utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class MerkleTreeUtil {
	public static String getMerkleRoot(List<String> data) {
		int count = data.size();
		if(count == 1) {
			try {
				return new String(StringUtil.hmacDigest(data.get(0).getBytes(), "\000".getBytes()), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		List<String> previousTreeLayer = new ArrayList<String>();
		for(String transaction : data) {
			
			
			
			try {
				previousTreeLayer.add(new String(StringUtil.hmacDigest(transaction.getBytes(), new String("\000").getBytes()), 
						"UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		List<String> treeLayer = previousTreeLayer;
		
		while(count > 1) {
			treeLayer = new ArrayList<String>();
			for(int i=1; i < previousTreeLayer.size(); i+=2) {
				try {
					treeLayer.add(new String(StringUtil.hmacDigest( (previousTreeLayer.get(i-1) + previousTreeLayer.get(i)).getBytes(),
							new String("\001").getBytes()), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			count = treeLayer.size();
			previousTreeLayer = treeLayer;
		}
		
		String merkleRoot = (treeLayer.size() == 1) ? treeLayer.get(0) : "";
		return merkleRoot;
	}
}
