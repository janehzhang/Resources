package sort.mergeSort;
import java.util.Comparator;


public class Greater implements Comparator<Comparable>{

	public int compare(Comparable x,Comparable y){
		return x.compareTo(y);
	}
}
