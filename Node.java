/*
 * Aarti Nimhan - 801098198
 * 
 * 
 * This class is a node class which consists of the unique state of the puzzle.
 * It contains a parentId which is the Id given to the state from which the current state is generated with only one tile move.
 * Each node is given an Id which is used for logically connecting nodes to keep a track of the tree and for back tracking the path of the solution.
 * Along with the state the Node also contains fields like gcost => the actual cost from root till current node, hcost => the heuristic cost of the
 *  current state that is the estimated distance from the goal and fcost => which is the addition of gcost and hcost.
 * The fcost is used for deciding which Node to expand next.
 */

public class Node {
	private int[][] state;
	private int parentId;
	private int id;
	private int fCost;
	private int gCost;
	private int hCost;

	public int[][] getState() {
		return state;
	}

	public void setState(int[][] state) {
		this.state = state;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getfCost() {
		return fCost;
	}

	public void setfCost(int fCost) {
		this.fCost = fCost;
	}

	public int getgCost() {
		return gCost;
	}

	public void setgCost(int gCost) {
		this.gCost = gCost;
	}

	public int gethCost() {
		return hCost;
	}

	public void sethCost(int hCost) {
		this.hCost = hCost;
	}

}
