package stack_queue;

import java.util.LinkedList;


//数组实现队列
public class MyQueue1<E>{

	private LinkedList<E> list = new LinkedList<E>();
	private int size=0;
	public synchronized void put(E data){
		list.addLast(data);
		size++;
	}
	public synchronized E pop(){
		size--;
		return list.removeFirst();
	}
	public synchronized boolean empty(){
		return size==0;
	}
	public synchronized int size(){
		return size;
	}
	
	
	public static void main(String[] args){
		MyQueue1<Integer> q = new MyQueue1<Integer>();
		q.put(1);
		q.put(2);
		q.put(3);
		System.out.println("长度："+q.size());
		System.out.println("对列首元素："+q.pop());
		System.out.println("对列首元素："+q.pop());
		
		
	}
}
