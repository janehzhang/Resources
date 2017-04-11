package sort.mergeSort;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author 寰愬瓙鐝� */

import java.util.Comparator;

public class Less implements Comparator<Comparable>{
    public int compare(Comparable x, Comparable y){
        return y.compareTo(x);
    }
}
