package BiTree.init;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;


//构造树
//遍历，  非递归不会
//E peek() 查看堆栈顶部的对象，但不从堆栈中移除它。 

public class MyBiTree{
	
	private static BiTreeNode root;

	public MyBiTree(){
		root = null;
	}
	
	public MyBiTree(BiTreeNode root){
		this.root = root;
	}

	public BiTreeNode init(int[] data){
		for(int i = 0; i < data.length; i++){
			insert(data[i]);
		}
		return root;
	}
	

	public void insert(int data){
		
		BiTreeNode newNode = new BiTreeNode(data);
		if(root == null){
			root = newNode;
		}else{
			BiTreeNode curr = root;
			BiTreeNode parent;
			while(true){
				parent = curr;
				if(curr.getData() > data){
					curr = curr.lChild;
					if(curr == null){
						parent.lChild = newNode;
						return;
				    }
			    }else{
			    	curr = curr.rChild;
					if(curr == null){
						parent.rChild = newNode;
						return;
				    }
			    }
			}
		}

	}

	//后序遍历:1 3 5 6 4 7 7 9 8 2
	public void postOrder(BiTreeNode root){
		if(root != null){
			postOrder(root.lChild);
			postOrder(root.rChild);
			System.out.print(root.getData() + " ");
		}
	}
	
	/*
	 * 借助栈实现非递归遍历，二叉树的非递归遍历在面试中经常考察，非常重要。
	 * 后序遍历：当用栈来存储节点，必须分清返回根节点时，是左子树返回的，还是右子树返回的，
	 * 所以要一个辅助指针right指向最近访问过的节点。
	 */
	//非递归后序遍历:1 3 5 6 4 7 7 9 8 2
	public void postOrder2(BiTreeNode root){
		
		BiTreeNode parent = root, right = null;
		
		Stack<BiTreeNode> stack = new Stack<BiTreeNode>();
		
		while(parent!=null || !(stack.isEmpty())){
			if(parent != null){			
				stack.push(parent);
				parent = parent.lChild;		//走到最左边
			}else{
				parent = stack.peek();
				if(parent.rChild != null && parent.rChild != right){   //有右孩子且未被访问过
					parent = parent.rChild;		//转向右
					stack.push(parent);			
					parent = parent.lChild;   //再走到最左边
				}else{
					parent = stack.peek();
					stack.pop();		//将节点弹出
					System.out.print(parent.getData()+" ");
					right = parent;			//记录最近访问的节点
					parent = null; 
				}
			}//else
		}//while
	}

    //中序遍历:1 2 3 4 5 6 7 7 8 9
	public void inOrder(BiTreeNode root){
		if(root != null){
			inOrder(root.lChild);
			System.out.print(root.getData() + " ");
			inOrder(root.rChild);
		}
	}
	
	//非递归中序遍历
	public void inOrder2(BiTreeNode root){
		BiTreeNode p = root;
		Stack s = new Stack<BiTreeNode>();
		while(p != null || !s.empty()){
			while(p != null){
				s.push(p);
				p = p.lChild;
			}
			if(!s.empty()){
				p = (BiTreeNode) s.peek();
				System.out.print(p.getData() + " ");
				s.pop();
				p = p.rChild;
			}
		}
	}

	
	
	//先序遍历:2 1 8 7 4 3 6 5 7 9  
	public void preOrder(BiTreeNode root){
		if(root != null){
			System.out.print(root.getData() + " ");
			preOrder(root.lChild);
			preOrder(root.rChild);
		}
	}

	public void preOrder2(BiTreeNode root){
		
		BiTreeNode p = root;
		Stack s = new Stack<BiTreeNode>();
		while(p != null || !s.empty()){
			while(p != null){
				System.out.print(p.getData() + " ");
				s.push(p);
				p = p.lChild;
			}
			if(!s.empty()){
				p = (BiTreeNode) s.peek();
				s.pop();
				p = p.rChild;
				
			}
		}
	}
	
	//呵呵，要出发笔试题目
	//层序遍历，借助队列 :2 1 8 7 9 4 7 3 6 5 
	public void layerTranverse(BiTreeNode root){
		Queue<BiTreeNode> queue = new LinkedList<BiTreeNode>();
		if(root == null){
			return;
		}
		queue.add(root);
		while(!queue.isEmpty()){
			BiTreeNode node = queue.poll();
			System.out.print(node.getData() + " ");
			if(node.lChild != null){
				queue.add(node.lChild);
			}
			if(node.rChild != null){
				queue.add(node.rChild);
			}
		}
		
	}

	

	public static void main(String[] args){
		
		MyBiTree myBiTree = new MyBiTree();
		int[] a = {2,8,7,4,9,3,1,6,7,5};
		myBiTree.init(a);
		System.out.print("后序遍历:");
		myBiTree.postOrder2(root);  //非递归
		
		System.out.print("\n中序遍历:");
		myBiTree.inOrder2(root);
		
		System.out.print("\n先序遍历:");
		myBiTree.preOrder2(root);
		
		
		System.out.print("\n层序遍历:");
		myBiTree.layerTranverse(root);

	}

	
}
