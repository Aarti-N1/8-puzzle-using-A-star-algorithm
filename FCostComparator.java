/* 
 * Aarti Nimhan - 801098198
 * 
 * 
 * This is a comparator created which is used in the Priority Queue to decide the ordering based on the fCost. 
 * The priority queue is ordered in ascending order of fcost.
 */
import java.util.Comparator;

public class FCostComparator implements Comparator<Node> {

	@Override
	public int compare(Node o1, Node o2) {
		if(o1.getfCost()< o2.getfCost())
			return -1;
		if(o1.getfCost() > o2.getfCost())
			return 1;
		return 0;
	}

}
