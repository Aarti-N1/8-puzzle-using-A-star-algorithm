
/* 
 * Aarti Nimhan - 801098198
 * 
 * 
 * This class contains the logic for solving the 8 puzzle problem.
 * 
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Stack;

public class AStarSearchAlgo {

	// Constants for type of heuristic
	private static String MANHATTAN_DISTANCE_HEURISTIC = "Manhattan Distance Heuristic";
	private static String MISPLACED_TILES_HEURISTIC = "Misplaced Tiles Heuristic";

	public String[] heuristicTypes = { MANHATTAN_DISTANCE_HEURISTIC, MISPLACED_TILES_HEURISTIC };
	public PriorityQueue<Node> priorityQueue;
	public int numberOfNodesExpanded;
	public int numberOfNodesGenerated;
	public int stateId;
	public HashMap<Integer, Node> exploredNodeSet;

	/**
	 * This method implements the A* algorithm it takes the initial node and goal node as input and moves one tile at a time to reach the goal state. 
	 * It uses Manhattan Distance and Misplaced tiles heuristic to reach the goal state. It displays the solution path.
	 * @param initialStateNode Node representing the initial state
	 * @param goalStateNode Node representing the goal state 
	 */
	public void aStarProcess(Node initialStateNode, Node goalStateNode) {

		// For each heuristic
		for (int heuristic = 0; heuristic < heuristicTypes.length; heuristic++) {
			System.out.println("--------- A* for " + heuristicTypes[heuristic]+ "-----------");
			numberOfNodesExpanded = 0;
			numberOfNodesGenerated = 1;
			stateId = 1;
			priorityQueue = new PriorityQueue<Node>(100, new FCostComparator());
			exploredNodeSet = new HashMap<Integer, Node>();
			initialStateNode.setgCost(0);
			initialStateNode.setId(stateId++);
			initialStateNode.setParentId(0);
			// Update initial state based on heuristic type
			calculateHeuristics(initialStateNode, goalStateNode, heuristicTypes[heuristic]);

			priorityQueue.add(initialStateNode);
			while (!priorityQueue.isEmpty()) {
				// extract a node to be expanded
				Node dequeueNode = priorityQueue.poll();
				exploredNodeSet.put(dequeueNode.getId(), dequeueNode);
				numberOfNodesExpanded++;
				// Check if goal State is reached. The goal state will always have a hcost of zero.
				if (dequeueNode.gethCost() == 0) {
					// if the goal is reached we need to backtrack the goal state to find the solution path. Creating a stack with the solution path nodes.
					Stack<Node> backtrackSolutionStack = new Stack<Node>();
					backtrackSolutionStack.push(dequeueNode);
					Node currentInPathNode = dequeueNode;
					int parentId = currentInPathNode.getParentId();
					// Finding solution path by backtracking using parent id.
					while (parentId != 0) {
						backtrackSolutionStack.push(exploredNodeSet.get(parentId));
						currentInPathNode = exploredNodeSet.get(parentId);
						parentId = currentInPathNode.getParentId();
					}
					// Print Solution path using nodes from the backtrack solution stack.
					System.out.println("------------ Printing Solution Path --------------");
					int pathCost = backtrackSolutionStack.size() - 1;
					printSolutionPathNode(backtrackSolutionStack);
					System.out.println();
					System.out.println("Nodes generated: " + numberOfNodesGenerated);
					System.out.println("Nodes explored: " + exploredNodeSet.size());
					System.out.println("Path Cost: " + pathCost);
					System.out.println();
					break;

				}
				//Finding the free tile location
				String indexOfFreeTile = findingFreeTileLocation(dequeueNode);
				String[] temp = indexOfFreeTile.split(",");
				int rowOfFreeTile = Integer.parseInt(temp[0]);
				int columnOfFreeTile = Integer.parseInt(temp[1]);

				// Calculating possible child states based on the free tile location
				List<String> possiblePositionsForExpansion = calculatePossiblePositionsForExpansion(dequeueNode,
						rowOfFreeTile, columnOfFreeTile);

				// For each Child
				for (int i = 0; i < possiblePositionsForExpansion.size(); i++) {
					// Child node created & generated count increased
					Node childStateNode = createChildNode(dequeueNode, rowOfFreeTile, columnOfFreeTile,
							possiblePositionsForExpansion.get(i));
					childStateNode.setId(stateId++);
					numberOfNodesGenerated++;
					boolean isChildInExploredSet = isChildInExploredSet(exploredNodeSet, childStateNode);
					boolean isChildInPriorityQueue = isChildInPriorityQueue(priorityQueue, childStateNode);
					// Checking if child is explored set or Priority Queue
					if (!isChildInExploredSet && !isChildInPriorityQueue) {
						// if yes do nothing if no calculate heuristic function for child
						calculateHeuristics(childStateNode, goalStateNode, heuristicTypes[heuristic]);
						// insert child in queue
						priorityQueue.add(childStateNode);
					} 
				}
			}
		}
	}

	/**
	 * This method updates the heuristic value for the appropriate type of heuristic. It sets these values in the node of the currentStateNode which is taken as input. 
	 * 
	 * @param currentStateNode is the node representing the current state of the board.
	 * @param goalStateNode is the node representing the expected goal state of the board.
	 * @param heuristicType is the type of heuristic that needs to be calculated.
	 */
	private void calculateHeuristics(Node currentStateNode, Node goalStateNode, String heuristicType) {
		if (heuristicType.equalsIgnoreCase(MANHATTAN_DISTANCE_HEURISTIC)) {
			int manhattanDistanceCost = manhattanDistanceHeuristic(currentStateNode, goalStateNode);
			currentStateNode.sethCost(manhattanDistanceCost);
			currentStateNode.setfCost(currentStateNode.getgCost() + currentStateNode.gethCost());

		}
		if (heuristicType.equalsIgnoreCase(MISPLACED_TILES_HEURISTIC)) {
			int misplacedTilesCost = misplacedTilesHeuristic(currentStateNode, goalStateNode);
			currentStateNode.sethCost(misplacedTilesCost);
			currentStateNode.setfCost(currentStateNode.getgCost() + currentStateNode.gethCost());

		}
	}

	/**
	 * This method calculates the Manhattan Distance Heuristic. It counts the minimum number of moves required for each tile to reach its goal state position. 
	 * @param currentStateNode Node representing the current state of the board.
	 * @param goalStateNode Node representing the expected goal state 
	 * @return calculated heuristic cost 
	 */
	private int manhattanDistanceHeuristic(Node currentStateNode, Node goalStateNode) {
		int[][] currentState = currentStateNode.getState();
		int[][] goalState = goalStateNode.getState();
		int hcost = 0;
		int i = 0, j = 0, l = 0, m = 0;
		for (int a = 1; a <= 8; a++) {
			outerloop: for (i = 0; i < currentState.length; i++)
				for (j = 0; j < currentState.length; j++)
					if (currentState[i][j] == a)
						break outerloop;
			outerloop1: for (l = 0; l < goalState.length; l++)
				for (m = 0; m < goalState.length; m++)
					if (goalState[l][m] == a)
						break outerloop1;
			hcost = hcost + (Math.abs(i - l) + Math.abs(j - m));
		}
		return hcost;
	}

	/**
	 * This method calculates the Misplaced Tiles Heuristic. It counts the number of tiles that are placed differently from their counterparts in the goal state. 
	 * @param currentStateNode  Node representing the current state of the board.
	 * @param goalStateNode Node representing the expected goal state
	 * @return calculated heuristic cost
	 */
	private int misplacedTilesHeuristic(Node currentStateNode, Node goalStateNode) {
		int[][] currentState = currentStateNode.getState();
		int[][] goalState = goalStateNode.getState();
		int hcost = 0;
		for (int i = 0; i < currentState.length; i++)
			for (int j = 0; j < currentState.length; j++) {
				if (currentState[i][j] != goalState[i][j] && currentState[i][j] > 0) {
					hcost++;
				}
			}
		return hcost;
	}

	/**
	 * This method finds the location of the free tile that is zero.  
	 * @param currentStateNode Node representing the current state of the board.
	 * @return a String which is a comma separated concatenation of the indexes of the free tile. 
	 */
	private String findingFreeTileLocation(Node currentStateNode) {
		int[][] currentState = currentStateNode.getState();
		int i = 0, j = 0;
		outerloop: for (i = 0; i < currentState.length; i++) {
			for (j = 0; j < currentState.length; j++) {
				if (currentState[i][j] == 0)
					break outerloop;
			}
		}
		String indexOfFreeTile = Integer.toString(i) + "," + Integer.toString(j);
		return indexOfFreeTile;
	}

	/**
	 * This method computes the valid possible positions the free tile can move to next.
	 * @param currentStateNode Node representing the current state of the board.
	 * @param rowOfFreeTile Row index of the free tile 
	 * @param columnOfFreeTile Column index of the free tile.
	 * @return a list of Strings representing comma separated indexes of valid possible free tile positions.  
	 */
	private List<String> calculatePossiblePositionsForExpansion(Node currentStateNode, int rowOfFreeTile,
			int columnOfFreeTile) {
		int[][] currentState = currentStateNode.getState();
		List<String> possiblePositionsForExpansion = new ArrayList<String>();
		// DOWN
		if ((rowOfFreeTile + 1) < currentState.length)
			possiblePositionsForExpansion
					.add(Integer.toString(rowOfFreeTile + 1) + "," + Integer.toString(columnOfFreeTile));
		// UP
		if ((rowOfFreeTile - 1) >= 0)
			possiblePositionsForExpansion
					.add(Integer.toString(rowOfFreeTile - 1) + "," + Integer.toString(columnOfFreeTile));
		// RIGHT
		if ((columnOfFreeTile + 1) < currentState.length)
			possiblePositionsForExpansion
					.add(Integer.toString(rowOfFreeTile) + "," + Integer.toString(columnOfFreeTile + 1));
		// LEFT
		if ((columnOfFreeTile - 1) >= 0)
			possiblePositionsForExpansion
					.add(Integer.toString(rowOfFreeTile) + "," + Integer.toString(columnOfFreeTile - 1));
		return possiblePositionsForExpansion;
	}

	/**
	 * This method creates a child Node for a given possible position of free tile. 
	 * @param dequeueNode  Node representing the parent state of the board from which the child is to be created.
	 * @param rowOfFreeTile Row index of the free tile 
	 * @param columnOfFreeTile Column index of the free tile.
	 * @param possiblePositionsForExpansion a String representing comma separated indexes of the next valid possible free tile positions.
	 * @return Node representing the child state created by moving one tile from parent state.
	 */
	private Node createChildNode(Node dequeueNode, int rowOfFreeTile, int columnOfFreeTile,
			String possiblePositionsForExpansion) {
		// copying state of parent
		Node childNode = new Node();
		int[][] childNodeState = new int[3][3];
		// For each Child clone the current state and replace changed tiles
		copyState(dequeueNode.getState(), childNodeState); // Copy state source to destination
		String[] temp = possiblePositionsForExpansion.split(",");
		int newRow = Integer.parseInt(temp[0]);
		int newColumn = Integer.parseInt(temp[1]);
		// move empty tile to new position
		int swapNum = childNodeState[rowOfFreeTile][columnOfFreeTile];
		childNodeState[rowOfFreeTile][columnOfFreeTile] = childNodeState[newRow][newColumn];
		childNodeState[newRow][newColumn] = swapNum;
		childNode.setState(childNodeState);
		childNode.setgCost(dequeueNode.getgCost() + 1);
		childNode.setParentId(dequeueNode.getId());
		return childNode;
	}

	/** This is a utility method to copy one state to another
	 * @param sourceNodeState state to be copied.
	 * @param destinationNodeState copied state.
	 */
	private void copyState(int[][] sourceNodeState, int[][] destinationNodeState) {
		for (int i = 0; i < sourceNodeState.length; i++)
			for (int j = 0; j < sourceNodeState.length; j++)
				destinationNodeState[i][j] = sourceNodeState[i][j];

	}

	/**
	 * This method checks if the child node created is present in the explored set.
	 * @param exploredNodeSet Map containing all the nodes that have been explored already.
	 * @param childStateNode Node representing the child state which is to be checked in the explored set.
	 * @return true if the child is in explored set or false if the child is not in the explored set.
	 */
	private boolean isChildInExploredSet(HashMap<Integer, Node> exploredNodeSet, Node childStateNode) {
		for (Map.Entry<Integer, Node> entry : exploredNodeSet.entrySet()) {
			Node exploredNode = (Node) entry.getValue();
			if (compareNodesForEquality(childStateNode, exploredNode))
				return true;
		}
		return false;
	}

	/** This method compares two nodes for equality.
	 * @param first
	 * @param second
	 * @return true if nodes are equal and false otherwise
	 */
	private boolean compareNodesForEquality(Node first, Node second) {
		int[][] firstState = first.getState();
		int[][] secondState = second.getState();
		for (int i = 0; i < firstState.length; i++)
			for (int j = 0; j < firstState.length; j++)
				if (firstState[i][j] != secondState[i][j])
					return false;
		return true;

	}

	/** This method checks if the child node created is present in the priority queue
	 * @param priorityQueue Priority queue
	 * @param childStateNode Node representing the child state which is to be checked in the priority queue.
	 * @return true if the child is in priority queue or false if the child is not in the priority queue.
	 */
	private boolean isChildInPriorityQueue(PriorityQueue<Node> priorityQueue, Node childStateNode) {
		List<Node> pqList = new ArrayList<Node>(priorityQueue);
		for (int i = 0; i < pqList.size(); i++) {
			if (compareNodesForEquality(pqList.get(i), childStateNode))
				return true;
		}
		return false;
	}

	/**This method prints the solution path from initial state to goal state.
	 * @param backtrackSolutionStack contains the list of nodes that are on the solution path.
	 */
	private void printSolutionPathNode(Stack<Node> backtrackSolutionStack) {
		Node currentPoppedNode;
		int[][] currentPoppedNodeState;
		while (!backtrackSolutionStack.isEmpty()) {
			currentPoppedNode = backtrackSolutionStack.pop();
			currentPoppedNodeState = currentPoppedNode.getState();
			// Print matrix
			System.out.println("g(n)= " + currentPoppedNode.getgCost() + "\t h(n)= " + currentPoppedNode.gethCost()
					+ "\t f(n)= " + currentPoppedNode.getfCost());
			for (int i = 0; i < currentPoppedNodeState.length; i++) {
				for (int j = 0; j < currentPoppedNodeState.length; j++)
					System.out.print("\t" + currentPoppedNodeState[i][j]);
				System.out.println();
			}
			if(!backtrackSolutionStack.isEmpty()) {
				System.out.println();
				System.out.println("Next state: ");
			}
		}
	}
}
