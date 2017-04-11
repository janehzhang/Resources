package stack_queue;

//找出栈elem中最小的一个元素

public class getMinElement{

	public MyStack2 <Integer> elem;
	public MyStack2 <Integer> min;
	
	public getMinElement(){
		elem = new MyStack2<Integer>();			
		min = new MyStack2<Integer>();		  //用来放最小元素的栈
	}
	
	public void push(int data){			//入栈时，如果进入elem的元素比min栈顶元素小，就要也入到min中
		elem.push(data);
		if(min.isEmpty()){
			min.push(data);
		}else if(min.peek()>data){
			min.push(data);
		}
	}
	
	public int pop(){				//出栈时，elem出来的如果是最小元素，那么这个最小元素一定也在min的栈顶，也出来
		int topData = elem.peek();
		elem.pop();
		if(topData==this.min()){
			min.pop();
		}
		return topData;
	}
	
	public int min(){
		if(min.isEmpty()){
			return Integer.MAX_VALUE;
		}else{
			return min.peek();
		}
	}
	
}
