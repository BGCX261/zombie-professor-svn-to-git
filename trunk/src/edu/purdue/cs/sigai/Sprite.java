package edu.purdue.cs.sigai;

abstract public class Sprite
{
	// Protected to allow children full control over the sprite's motion
	protected final TileBoard board;
	protected Tile tile;
	
	public Sprite(int row, int col, TileBoard board)
	{
		this.board = board;
		this.tile = board.getBoard()[row][col];
	}
	
	public float getRow() { return tile.getRow(); }
	public float getCol() { return tile.getCol(); }
	public Tile getTile() { return tile; }
	
	abstract public void update();  // Update the position and characteristics of the sprite
}