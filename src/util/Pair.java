package util;
/**
 * A wrapper class for priority queue elements
 * 
 * @author americachambers
 *
 */
public class Pair<P, E> {
	public P priority;
	public E element;
	
	public Pair(P p, E e) {
		priority = p;
		element = e;
	}
}
