package edu.purdue.cs.sigai;

import edu.purdue.cs.sigai.heap.*;
import java.util.*;

import android.widget.Toast;

public class TileBoard {
	public static final int tileWidth = 10;
	
	private Tile[][] board;
	
	private Player player;
	private Zombie zombie1;
	private Zombie zombie2;
	
	private int numZombs;
	
	
	
	public TileBoard(int numRows, int numCols, int numZombs) {
		this.numZombs = numZombs;
		
		HashSet<Tile> possNext = new HashSet<Tile>();
		board = new Tile[numRows][numCols];
		
		for (int row = 0; row < numRows; row++)
			for (int col = 0; col < numCols; col++) 
					board[row][col] = new Tile(row, col, true, this);
		
		possNext.add(board[numRows/2][numCols/2]);
		
		while (!possNext.isEmpty()) {
			int stop = (int)(Math.random() * possNext.size());
			Iterator<Tile> iter = possNext.iterator();
			Tile next = iter.next();
			for (int i = 0; i < stop && iter.hasNext(); i++)
				next = iter.next();
			
			next.setWall(false);
			possNext.remove(next);
			next.setFinal(true);
			
			for (int i = 0; i < next.getAdj().size(); i++) {
				Tile t = next.getAdj().get(i);
				if (possNext.contains(t)) {
					possNext.remove(t);
					t.setFinal(true);
				}
				else if (!t.isEdge() && !t.isFinal())
					possNext.add(t);
			}
		}
		
		// Put the player in the upper-left most node available.
		leave:
		for (int row = 1; row < board.length-1; row++)
			for (int col = 1; col < board[row].length-1; col++)
				if (!board[row][col].isWall()) {
					player = new Player(1,col, this);
					break leave;
				}
		
		// Put a zombie in the upper-right most node available.
		if (numZombs > 0) {
			leave:
			for (int row = 1; row < board.length-1; row++)
				for (int col = board[row].length-2; col >= 1; col--)
					if (!board[row][col].isWall()) {
						zombie1 = new Zombie(row, col, this);
						break leave;
					}
		}
		
		if (numZombs > 1) {
			leave:
			for (int row = board.length-2; row >=1; row--)
				for (int col = 1; col < board[row].length-1; col++)
					if (!board[row][col].isWall()) {
						zombie2 = new Zombie(row, col, this);
						break leave;
					}
		}
		
		// Create an exit
		for (int col = board[board.length-2].length-2; col >= 1; col--)
			if (!board[board.length-2][col].isWall()) {
				board[board.length-1][col].setWall(false);
				board[board.length-1][col].setWin(true);
				break;
			}
		
		// Set some random tiles to be in the maze to create some loops.
		for (int i = 0; i < numCols*numRows/10; i++) {
			int col = (int)(Math.random()*numCols), row = (int)(Math.random()*numRows);
			if (col == 0) col++;
			if (row == 0) row++;
			if (row == numRows-1) row--;
			if (col == numCols-1) col--;
			board[row][col].setWall(false);
		}
		
		dijk(player.getTile());
		
		// Set any unreachable tiles to be walls
		for (int row = 0; row < board.length; row++)
			for (int col = 0; col < board[row].length; col++)
				if (board[row][col].getWeight() == Tile.INF)
					board[row][col].setWall(true);
	}

	/**
	 * @return the board
	 */
	public Tile[][] getBoard() {
		return board;
	}

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * @return the zombie
	 */
	public Zombie getZombie1() {
		return zombie1;
	}
	
	public Zombie getZombie2() {
		return zombie2;
	}
	
	public int getNumZombs() {
		return numZombs;
	}

	public void setNumZombs(int numZombs) {
		this.numZombs = numZombs;
	}

	// Update the board
	public void update() {
		player.update();
		zombie1.update();
		zombie2.update();
		
		
	}
	
	public void dijk(Tile source)
	{
		boolean pathExists = true;  // Whether a path might exist
		Heap<Tile> prior = new Heap<Tile>();  // The priority queue
		Tile cur;  // The current node

		// Initialization
		for (int i = 0; i < board.length; i++)
			for (int j = 0; j < board[i].length; j++) {
				board[i][j].setWeight(Tile.INF);
				board[i][j].setFinal(false);
			}

		cur = source;
		cur.setWeight(0);
		cur.setFinal(true);
		
		
		// Initialize the priority queue  ( this is what separates Dijkstra from A* )
		for (int i = 0; i < board.length; i++)
			for (int j = 0; j < board[i].length; j++) 
				if (!board[i][j].isWall())
					board[i][j].setHeap(prior.insert(board[i][j]));



		// Algorithm
		while (pathExists) {  // Stops if all remaining lengths are infinite
			
			for (Iterator<Tile> i = cur.getAdj().iterator(); i.hasNext();) {
				Tile to = i.next();
				int newWeight;
				
				if (to == null || to.isFinal()) continue;  // Only consider what's not final and actually in the maze
	
				newWeight = cur.getWeight() + 1;
	
				// If the new path is better than the old one
				if (newWeight < to.getWeight() || to.getWeight() == Tile.INF) {
					to.setWeight(newWeight);
					to.setPrev(cur);
	
					prior.decreaseKey(to.getHeap());
				}
			}
			
			// Finalize the next smallest node (the new cur)
			cur = prior.deleteMin();
			cur.setHeap(null);
			cur.setFinal(true);

			// Check if there's any more nodes through which to reach the goal
			if (prior.getSize() == 0 || cur.getWeight() == Tile.INF)
				pathExists = false;
		}

	}
}
