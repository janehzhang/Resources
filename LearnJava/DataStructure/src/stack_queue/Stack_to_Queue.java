package stack_queue;

//用两个栈实现对列！！！！！！！！！！！
public class Stack_to_Queue<E>{

	private MyStack2<E> s1 = new MyStack2<E>();
	private MyStack2<E> s2 = new MyStack2<E>();
	
	public synchronized void put(E data){
		s1.push(data);
	}
	
	public synchronized E pop(){
		if(s2.isEmpty()){
			while(!s1.isEmpty()){
				s2.push(s1.pop());
			}
		}
		return s2.pop();
	}
	
	public boolean isEmpty(){
		return s1.isEmpty()&&s2.isEmpty();
	}
	
	public static void main(String[] args){
		Stack_to_Queue<Integer> s = new Stack_to_Queue<Integer>();
		s.put(1);
		s.put(2);
		s.put(3);
		System.out.println("队首元素是："+s.pop());
		System.out.println("队首元素是："+s.pop());
	}
}
