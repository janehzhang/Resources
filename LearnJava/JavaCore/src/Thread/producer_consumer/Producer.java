package Thread.producer_consumer;

// 生产者 
public class Producer implements Runnable {  
    //简单的模拟，这里一个生产容器，设置成final类型的话不允许再次赋值  
    private final Container<Bread> container;  
      
    //生产者线程监听器  
    private final Object producerMonitor;  
      
    //消费者线程监听器  
    private final Object consumerMonitor;  
      
    public Producer(Object producerMonitor,Object consumerMonitor,Container<Bread> container){  
        this.producerMonitor = producerMonitor;  
        this.consumerMonitor = consumerMonitor;  
        this.container = container;  
    }  
    
    @Override  
    public void run() {  
        while(true){  
            produce();  
        }  
    }  
  
    public void produce(){  
        //这里为了形象，模拟几个制作面包的步骤  
        step1();  
        Bread bread = step2();  
        //如果发现容器已经满了,生产者要停  
        if(container.isFull()){  
            //唤醒消费者  
            synchronized(consumerMonitor){  
                  
                if(container.isFull()){  
                    consumerMonitor.notify();  
                }  
            }  
            //生产者挂起,两把锁的问题  
            synchronized(producerMonitor){  
                try {  
                    if(container.isFull()){  
                        System.out.println("生产者挂起...");  
                        producerMonitor.wait();                          
                    }  
                } catch (InterruptedException e) {  
                    e.printStackTrace();  
                }  
            }  
        }else{  
            //容器中还有容量,把面包放到容器内,这里可能会有丢失  
            boolean result = container.add(bread);  
            System.out.println("produce bread:"+result);  
        }  
    }  
      
    public void step1(){}  
      
    public Bread step2(){  
        return new Bread();  
    }  
}  

/*
 * 先回答我上面的两把锁的问题，对于生产者和消费者的唤醒和挂起操作分别用了两个监控器
	原因很简单，就像我在生产者消费者模型图里面看到的生产者和消费者均可以是一个或者多个。
	如果这里用一个对象控制，在多个生产者和多个消费者的时候发现：本来要唤醒生产者会把消费者也唤醒，就会比较混乱。
            再看第二个问题是：为什么在三个地方都需要做判满操作？这里看生产者那张图，仔细的看这一段代码，这里的操作并非原子操作。
            图中标识的1处判满操作后，到了第3处后并不意味着容器还是满的。如果只是在1处作了校验，那么很有可能会出现这样的情况：在2处把消费者唤醒后，等运行到了第3处发现这时候的容器可能已经空了！这样就不需要把生产者进行挂起。而如果没有这段校验逻辑很有可能就会出现生产者和消费者同时挂起的情况，这里可以细细体会一下，或者写代码试验一下。
            这里再说一下wait()和notify()/notifyAll()。这两个操作是一对。这里要注意的第一个问题是对于线程进行wait()和notify()操作都要锁住持有对象。图中都可以看出代码中对两个monitor都加了锁，不加锁的话就会抛出java.lang.IllegalMonitorStateException异常。另外一个问题很容易的一个惯性思维就是线程挂起不就是this.wait()，这样可以吗？答案是可以，你只要锁住自己就OK。但是问题来了，谁来唤醒它？这也是为什么用一个公用对象进行唤醒和挂起线程操作，wait()和notify()属于Object类，任何对象都可以进行这个操作。
*/










