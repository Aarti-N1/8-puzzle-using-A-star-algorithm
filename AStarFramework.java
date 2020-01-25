
/*
 * Aarti Nimhan - 801098198
 * 
 * 
 * This class is a framework class which takes input, validates the input, does the necessary processing of the input so that the input can be initialized as required. 
 * That is converting the input of numbers into Nodes.
 * 
 */
import java.util.ArrayList;
import java.util.Scanner;

public class AStarFramework {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AStarFramework aFramework = new AStarFramework();
		AStarSearchAlgo aStar = new AStarSearchAlgo();
		Node initialStateNode, goalStateNode;
		System.out.println(
				" Enter the Initial State (row wise) and seperate each input by enter for the 8-puzzle problem:  ");
		initialStateNode = aFramework.initializingNode();
		System.out.println(
				" Enter the Goal State (row wise) and seperate each input by enter for the 8-puzzle problem:  ");
		goalStateNode = aFramework.initializingNode();
		if (validation(initialStateNode.getState()) && validation(goalStateNode.getState())) {	//If input entered is valid we proceed to processing.
			aStar.aStarProcess(initialStateNode, goalStateNode);
		} else {
			return;
		}
	}

	/**
	 * Takes a node state as input and returns true if it is a valid input and false if the input contains repeated numbers.
	 * @param nodeState is a two dimensional integer array which represents the state of the puzzle board. 
	 * @return a boolean true if input is valid and false if it is invalid.
	 */
	private static boolean validation(int[][] nodeState) {
		ArrayList<Integer> validInput = new ArrayList<Integer>();
		for (int x = 0; x < 9; x++) {
			validInput.add(x);
		}
		for (int i = 0; i < nodeState.length; i++) {
			for (int j = 0; j < nodeState.length; j++) {
				if (validInput.contains(nodeState[i][j])) {
					validInput.remove((Integer) nodeState[i][j]);
				} else {
					System.out.println("Entered input does not contain unique numbers from 0 through 8. ");
					return false;
				}
			}
		}

		return true;

	}

	/**
	 * Takes input of integers
	 * @return a Node created form the input of numbers
	 */
	private Node initializingNode() {
		int[][] state = new int[3][3]; // As 8 puzzle will always be a 3X3 matrix
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		for (int i = 0; i < state.length; i++) {
			for (int j = 0; j < state.length; j++) {
				state[i][j] = scanner.nextInt();
			}
		}
		Node createdState = new Node();
		createdState.setState(state);
		return createdState;
	}

}
