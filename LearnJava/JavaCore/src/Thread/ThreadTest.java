package Thread;

//设计4个线程,其中两个线程每次对j增加1，另外两个线程对j每次减少1。写出程序。

//学怎么开线程，学怎么玩内部类

public class ThreadTest 
{ 
	int j;
	public static void main(String[] args) {
		ThreadTest tt = new ThreadTest();
		Inc increse = tt.new Inc();
		Dec decrese = tt.new Dec();
		for(int i = 0; i < 2; i++){
			Thread t1 = new Thread(increse);
			t1.start();
			t1 = new Thread(decrese);
			t1.start();
		}
		
	}
	
	private synchronized void inc(){
		j++;
		System.out.println(Thread.currentThread().getName()+" inc j:"+j);
	}
	private synchronized void dec(){
		j--;
		System.out.println(Thread.currentThread().getName()+" dec j:"+j);
	}
	
	class Inc implements Runnable{
		public void run() {
			for(int i = 0; i < 50; i++){
				inc();
			}
		}
	}
	
	class Dec implements Runnable{

		public void run() {
			for(int i = 0; i < 50; i++){
				dec();
			}
		}
	}
	
} 
