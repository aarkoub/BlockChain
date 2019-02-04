package merkletree;

import java.util.ArrayList;
import java.util.List;

import utils.StringUtil;



public class MerkleTree {

	public static Node getMerkleTree(List<String> data) {
		
		if(data.size()==0) return new Node();
		
		if(data.size() == 1) return new Node(data.get(0));
		
		int count = data.size();
		
		List<Node> previousTreeLayer = new ArrayList<>();
		for(String transaction : data) {
			previousTreeLayer.add(new Node(transaction));
		}
		List<Node> treeLayer = previousTreeLayer;
		
		while(count > 1) {
			treeLayer = new ArrayList<>();
			for(int i=1; i < previousTreeLayer.size(); i+=2) {
				treeLayer.add(new Node(previousTreeLayer.get(i-1), previousTreeLayer.get(i)));
			}
			count = treeLayer.size();
			previousTreeLayer = treeLayer;
		}
		
		return treeLayer.get(0);
		
	}
	
	public static String getMerkleTreeRoot(List<String> data) {
		return getMerkleTree(data).getHash();
	}
	
	public static void makeGraph(Node stree) {
		StringBuilder sb = new StringBuilder();
		sb.append("digraph {\n  edge [arrowhead = none, arrowtail=none];\n");
		sb.append(makeGraphAux(stree));
		sb.append("}");
		System.out.println(sb.toString());
	}
	
	private static String makeGraphAux(Node stree) {
		if(stree.isLeaf()) {
			return stree.getUniqueId() + " [label=\""+ stree.getData()+"  "+ stree.getHash().substring(0, 8) +"\", shape=circle];\n";
		}
		StringBuilder sb = new StringBuilder();
		sb.append(stree.getUniqueId() + " [label=\""+ stree.getHash().substring(0, 8) +"\", shape=circle];\n");
		if(stree.getLeft() != null) {
			sb.append(stree.getUniqueId() + " -> " + stree.getLeft().getUniqueId() + ";\n");
			sb.append(makeGraphAux(stree.getLeft()));
		}
		if(stree.getRight() != null) {
			sb.append(stree.getUniqueId() + " -> " + stree.getRight().getUniqueId() + ";\n");
			sb.append(makeGraphAux(stree.getRight()));
		}

		return sb.toString();
		
	}
	
	
	
	public static void main(String[] args) {
		List<String> data = new ArrayList<>();
		data.add("1111");
		data.add("2222");
		data.add("3333");
		data.add("4444");
		data.add("5555");
		data.add("6666");
		data.add("7777");
		data.add("8888");
		Node n = getMerkleTree(data);
		makeGraph(n);
		System.out.println(StringUtil.toHex(StringUtil.hmacDigest("1111".getBytes(), "\000".getBytes())));
	}
}
