package stack_queue;

import java.util.Arrays;


//数组实现的栈
public class MyStack1<E> {
	
	Object[] stack = null;
	int size;
	
	public MyStack1(){
		stack = new Object[10];
	}
	
	public boolean isEmpty(){
		return size==0;
	}
	
	public E peek(){
		if(isEmpty()){
			return null;
		}
		return (E) stack[size-1];
	}
	
	public E pop(){
		E e = peek();
		stack[size-1] = null;
		size--;
		return e;
	}
	
	public E push(E item){
		ensureCapacity(size+1);
		stack[size++] = item;
		return item;
		
	}

	private void ensureCapacity(int size) {
		int len = stack.length;
		if(size>len){
			int newLen = 10;
			stack = Arrays.copyOf(stack, newLen);
		}
	}
	
	public static void main(String[] args){
		
		MyStack1<Integer> stack = new MyStack1<Integer>();
		stack.push(1);
		stack.push(2);
		System.out.println("栈元素个数："+stack.size);
		System.out.println("栈顶元素："+stack.pop());
		System.out.println("栈顶元素："+stack.pop());
		
	}
	
}
