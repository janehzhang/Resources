package BiTree;

import BiTree.init.BiTreeNode;
import BiTree.init.MyBiTree;

//2016.9.25 不会  
//2016.9.29 不会

//树节点间最大距离（边的熟数量最多）

/*
 * 两种情况:1.从左最深穿过根节点到右最深节点; 2.路径不穿过根节点，而是在某个子树的最大路径。
 * 所以需要一个节点同时传回两个信息：字树的最大深度，子树的最大距离
 */
class Result{			//定义返回结果
	int nMaxDistance;	//最大距离
	int nMaxDepth;		//最大深度
	
	public Result(){
		
	}
	public Result(int dis, int de){
		this.nMaxDistance = dis;
		this.nMaxDepth = de;
	}
	
	public String toString(){
		return "nMaxDepth:" +this.nMaxDepth + " nMaxDistance:" + this.nMaxDistance;
	}
}

public class 树节点间最大距离{
	
	private static Result MaxDistance(BiTreeNode root) {
		
		
		return null;
		
	}
	
	/*
	private static Result MaxDistance(BiTreeNode root) {
		
		if(root == null){
			Result result = new Result(0,-1);
			return result;
		}
		
		Result ld = MaxDistance(root.lChild);
		Result rd = MaxDistance(root.rChild);
		
		Result result = new Result();
		result.nMaxDepth = max(ld.nMaxDepth + 1, rd.nMaxDepth + 1);
		result.nMaxDistance = max( max(ld.nMaxDistance, rd.nMaxDistance), 
								   ld.nMaxDepth + rd.nMaxDepth + 2
								 );
		return result;
	}
	*/
	
	
	private static int max(int x, int y) {
		return x > y ? x : y;
	}
	
	public static void main(String[] args) {
		MyBiTree biTree = new MyBiTree();
		int[] a = {2,8,7,4,9,3,1,6,7,5};
		BiTreeNode root = biTree.init(a);
		System.out.println(MaxDistance(root));
	}
	
}
