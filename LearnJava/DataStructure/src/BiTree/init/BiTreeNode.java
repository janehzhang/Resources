package BiTree.init;


public class BiTreeNode{
	private int data;
	public BiTreeNode lChild;
	public BiTreeNode rChild;

	public BiTreeNode(int data){		
		this.setData(data);
		lChild = null;
		rChild = null;
	}

	public void setData(int data) {
		this.data = data;
	}

	public int getData() {
		return data;
	}
}
