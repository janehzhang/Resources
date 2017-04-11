package LinkedList;


public class Node<E>{
	private E data;
	public Node<E> next;

	public Node(E data){		
		this.setData(data);
		next = null;
	}

	public void setData(E data) {
		this.data = data;
	}

	public E getData() {
		return data;
	}
}
