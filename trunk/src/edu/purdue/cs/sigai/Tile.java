package edu.purdue.cs.sigai;

import edu.purdue.cs.sigai.heap.*;
import java.util.*;

public class Tile implements Comparable<Tile> {
	final public static int INF = -1;
	
	// Tile members
	final private int row, col;
	final private TileBoard board;
	private boolean isWall;  // Zero if it's floor, One if it's wall
	private boolean isWin;  //  True if it's a win tile
	private boolean isZombie;  //Zero if free, 1 if zombie
	
	// Dijkstra helper members
	private int weight;
	private boolean finalized;
	private HeapNode<Tile> heap;
	private Tile prev;
	
	public Tile(int row, int col, boolean isWall, TileBoard board)
	{
		this.row = row;
		this.col = col;
		this.isWall = isWall;
		this.board = board;
		
		// Dijk Defaults
		this.weight = INF;
		this.finalized = false;
		this.heap = null;
	}
	
	public Tile right()
	{
		return (col+1 < board.getBoard()[row].length) ? board.getBoard()[row][col+1] : null;
	}
	public Tile left()
	{
		return (col-1 >= 0) ? board.getBoard()[row][col-1] : null;
	}
	public Tile up()
	{
		return (row-1 >= 0) ? board.getBoard()[row-1][col] : null;
	}
	public Tile down()
	{
		return (row+1 < board.getBoard().length) ? board.getBoard()[row+1][col] : null;
	}
	
	// Comparable
	public int compareTo(Tile t)
	{
		if (weight == INF)
			return 1;
		if (t.weight == INF)
			return -1;
		return weight-t.weight;
	}
	
	// Accessors
	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}
	
	public boolean isWall() {
		return isWall;
	}
	
	public boolean isEdge() {
		 return row == 0 || col == 0 || row == board.getBoard().length-1 || col == board.getBoard()[0].length - 1;
	}
	
	public boolean isWin() {
		return isWin;
	}


	
	public boolean hasZombie() {
		return isZombie;
	}

	public void setZombie(boolean isOccupied) {
		this.isZombie = isOccupied;
	}

	// Map mutators
	public void setWall(boolean isWall) {
		this.isWall = isWall;
	}
	
	// Dijk Accessors
	public int getWeight()
	{
		return weight;
	}
	
	public boolean isFinal()
	{
		return finalized;
	}
	
	public HeapNode<Tile> getHeap()
	{
		return heap;
	}
	
	public ArrayList<Tile> getAdj()
	{
		ArrayList<Tile> ret = new ArrayList<Tile>();
		ret.add(up());
		ret.add(right());
		ret.add(left());
		ret.add(down());
		return ret;
	}
	
	public Tile getPrev()
	{
		return prev;
	}
	
	public void setWin(boolean isWin) {
		this.isWin = isWin;
	}
	
	
	// Dijk Mutators
	public void setWeight(final int weight)
	{
		this.weight = weight;
	}
	
	public void setFinal(boolean finalized)
	{
		this.finalized = finalized;
	}
	
	public void setHeap(HeapNode<Tile> heap) {
		this.heap = heap;
	}
	
	public void setPrev(Tile prev)
	{
		this.prev = prev;
	}
}
