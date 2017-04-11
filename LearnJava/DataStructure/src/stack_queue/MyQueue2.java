package stack_queue;

//链表实现队列
class QNode<E>{
	E data;
	QNode<E> next = null;
	public QNode(E data){
		this.data =data;
	}
}

public class MyQueue2<E> {
	
	private QNode<E> head;
	private QNode<E> tail;
	
	public boolean isEmpty(){
		return head==tail;
	}
	public void put(E data){
		QNode<E> newNode = new QNode<E>(data);
		if(head==null&&tail==null){
			head = tail = newNode;
		}else{
			tail.next = newNode;
			tail = newNode;
		}
		
	}
	public E pop(){
		if(this.isEmpty()){
			return null;
		}
		E data = head.data;
		head = head.next;
		return data;
	}
	public int size(){
		QNode temp = head;
		int n=0;
		while(temp!=null){
			temp = temp.next;
			n++;
		}
		return n;
	}
	
	public static void main(String[] args){
		MyQueue2<Integer> q = new MyQueue2<Integer>();
		q.put(1);
		q.put(2);
		q.put(3);
		System.out.println("长度："+q.size());
		System.out.println("对列首元素："+q.pop());
		System.out.println("对列首元素："+q.pop());
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
}
