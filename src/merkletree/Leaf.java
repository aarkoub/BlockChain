package merkletree;

public class Leaf extends Node {

	public Leaf(byte[] data) {
		super(null, null);
		setHashData(data);
	}

}
