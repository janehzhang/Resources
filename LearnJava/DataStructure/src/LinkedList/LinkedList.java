package LinkedList;


public class LinkedList<E>{

	Node<E> top = null;
	
	private boolean isEmpty(){
		return top == null;
	}
	
	private void push(E data){
		Node<E> newNode = new Node<E>(data);
		newNode.next = top.next;
		top.next = newNode;
	}
	
	private E pop(){
		if(isEmpty()){
			return null;
		}
		E data = top.next.getData();
		top.next = top.next.next;
		return data;
	}
	
	private E peek(){
		if(isEmpty()){
			return null;
		}
		return top.next.getData();
	}
	
	public static void main(String[] args) {
		
		
	}

}
