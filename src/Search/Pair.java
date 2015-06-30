/**
 * 
 */
package Search;

/**
 * @author Vany
 *
 */
public class Pair<T1 extends Comparable<T1>, T2 extends Comparable<T2>> implements 
	Comparable<Pair<T1, T2>> {
	public T1 first;
	public T2 second;
	public Pair(T1 first, T2 second){
		this.first = first;
		this.second = second;
	}
	
	@Override
	public int compareTo(Pair<T1,T2> o){
		return -first.compareTo(o.first);
	}
}
