package utils;

import java.util.ArrayList;
import java.util.List;


public class MerkleTreeUtil {
	public static String getMerkleRoot(List<String> data) {
		int count = data.size();
		if(count == 1) return StringUtil.hmacDigest(data.get(0), new String("\000"));
		List<String> previousTreeLayer = new ArrayList<String>();
		for(String transaction : data) {
			previousTreeLayer.add(StringUtil.hmacDigest(transaction, new String("\000")));
		}
		List<String> treeLayer = previousTreeLayer;
		
		while(count > 1) {
			treeLayer = new ArrayList<String>();
			for(int i=1; i < previousTreeLayer.size(); i+=2) {
				treeLayer.add(StringUtil.hmacDigest(previousTreeLayer.get(i-1) + previousTreeLayer.get(i), new String("\001")));
			}
			count = treeLayer.size();
			previousTreeLayer = treeLayer;
		}
		
		String merkleRoot = (treeLayer.size() == 1) ? treeLayer.get(0) : "";
		return merkleRoot;
	}
}
