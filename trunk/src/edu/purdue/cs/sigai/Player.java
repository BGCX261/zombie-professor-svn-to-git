package edu.purdue.cs.sigai;

public class Player extends Sprite {
	
	final static int UP = 0;
	final static int DOWN = 1;
	final static int LEFT = 2;
	final static int RIGHT = 3;
	
	private int dir;
	
	public Player(int row, int col, TileBoard board)
	{
		super(row,col,board);
	}
	
	public void update()
	{
		Tile newTile = null;
		
		switch(dir) {
		case UP:
			newTile = tile.up();
			break;
			
		case LEFT:
			newTile = tile.left();
			break;
			
		case DOWN:
			newTile = tile.down();
			break;
			
		case RIGHT:
			newTile = tile.right();
			break;
		}
		
		if (newTile != null && !newTile.isWall() && !newTile.hasZombie())
			tile = newTile;
	}

	public void setDir(int dir) {
		this.dir = dir;
	}
}
