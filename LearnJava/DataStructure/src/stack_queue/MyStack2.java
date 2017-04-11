package stack_queue;

//链表实现的栈
class Node<E>{
	Node<E> next= null;
	E data;
	public Node(E data){
		this.data = data;
	}
}

public class MyStack2<E>{

	Node<E> top = null;
	
	public boolean isEmpty(){
		return top==null;
	}
	
	public int length(){
		int count=1;
		while(top.next!=null){
			top = top.next;
			count++;
		}
		return count;
	}
	
	public void push(E data){
		Node<E> newNode = new Node<E>(data);
		newNode.next = top;
		top = newNode;
	}
	public E pop(){
		if(this.isEmpty()){
			return null;
		}
		E data = top.data;
		top = top.next;
		return data;
	}
	public E peek(){
		if(isEmpty()){
			return null;
		}
		return top.data;
	}
	
	
	public static void main(String[] args){
		
		MyStack2<Integer> stack = new MyStack2<Integer>();
		stack.push(1);
		stack.push(2);
		stack.push(3);
		System.out.println("栈元素个数："+stack.length());
		System.out.println("栈顶元素："+stack.pop());
		System.out.println("栈顶元素："+stack.pop());
		
	}
	
}
