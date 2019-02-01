package merkletree;

import utils.StringUtil;

public class Node {
	private Node left, right;
	private String data;
	private String hash;
	private boolean isLeaf;
	
	
	public Node() {
		this.left = null;
		this.right = null;
		this.isLeaf = true;
		this.data = "";
		hash = StringUtil.hmacDigest(data, "\000");
	}
	
	
	public Node(String data) {
		this.left = null;
		this.right = null;
		this.isLeaf = true;
		this.data = data;
		this.hash = StringUtil.hmacDigest(data, "\000");
	}
	
	public Node(Node left, Node right) {
		this.left = left;
		this.right = right;
		this.data = "";
		this.hash = StringUtil.hmacDigest(left.getHash() + right.getHash(), "\001");
		this.isLeaf = false;
	}


	public Node getLeft() {
		return left;
	}


	public void setLeft(Node left) {
		this.left = left;
	}


	public Node getRight() {
		return right;
	}


	public void setRight(Node right) {
		this.right = right;
	}


	public String getData() {
		return data;
	}


	public void setData(String data) {
		this.data = data;
	}


	public String getHash() {
		return hash;
	}


	public void setHash(String hash) {
		this.hash = hash;
	}


	public boolean isLeaf() {
		return isLeaf;
	}


	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}
	
	public String getUniqueId() {
		String name = super.toString();
		name = name.replaceAll("[.,@]", "");
		return name;
	}
	
	
}
